package tiane.org.ssm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiane.org.ssm.dao.BizScheduleMapper;
import tiane.org.ssm.dao.BizGoodsMapper;
import tiane.org.ssm.dao.BizStockMapper;
import tiane.org.ssm.entity.*;
import tiane.org.ssm.service.BizAgencyService;
import tiane.org.ssm.service.BizCityService;
import tiane.org.ssm.service.BizGoodsService;
import tiane.org.ssm.vo.BizProductsVO;
import tiane.org.ssm.vo.xc.AirShoppingRequestVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BizGoodsServiceImpl extends ServiceImpl<BizGoodsMapper, BizGoods> implements BizGoodsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BizAgencyService agencyService;
    @Autowired
    private BizGoodsMapper bizGoodsMapper;
    @Autowired
    private BizStockMapper bizStockMapper;
    @Autowired
    private BizScheduleMapper bizScheduleMapper;
    @Autowired
    private BizCityService bizCityService;

    @Override
    public List<BizProductsVO> queryOneWayAllTicketsByDate(String userId,  String salesTarget, String departure,
                                                           String arrival, String startDate,String flightType) {
        String temp = startDate;
        // 数据权限 分销商
        BizAgency agency = agencyService.getById(userId);
        String resellerCode = agency.getParentId();
        if(resellerCode != null){
            agency = agencyService.getById(resellerCode);
        }
        resellerCode = agency.getCompany();

        // 查找库存
        QueryWrapper<BizStock> izStockQueryWrapper = new QueryWrapper<>();
        izStockQueryWrapper.eq("src_point", departure);
        izStockQueryWrapper.eq("dst_point", arrival);
        izStockQueryWrapper.eq("`status`", 0);
        izStockQueryWrapper.eq("flight_Type", flightType);
        List<BizStock> bizInventories = bizStockMapper.selectList(izStockQueryWrapper);
        if(bizInventories == null || bizInventories.size() == 0){
            return null;
        }

        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        if(startDate.length() < 8){
            startDate += "-01";
        }
//            Date  parse = sp.parse(startDate);
        bizInventories = bizInventories.stream()
                .filter(item -> item.getFinishTime().compareTo(new Date()) >= 0)
                .collect(Collectors.toList());

        List<BizProductsVO> rtnList = new ArrayList<>();
        List<BizGoods> relist = new ArrayList<>();

        for (BizStock bizStock : bizInventories) {
            String bizStockId = bizStock.getId();
//            String bizStockId = bizStock.getflightType();
            // 查找航线

            QueryWrapper<BizSchedule> scheduleQueryWrapper = new QueryWrapper<>();
            scheduleQueryWrapper.eq("ivt_id", bizStockId);
            if(temp.length() > 8){
                // 选择的日期为航班日期
                scheduleQueryWrapper.eq("flight_date", startDate);
            }

            List<BizSchedule> BizStockSchedules = bizScheduleMapper.selectList(scheduleQueryWrapper);
            if(BizStockSchedules == null || BizStockSchedules.size() == 0){
                continue;
            }
            for (BizSchedule schedule : BizStockSchedules) {
                // 差具体某天的
                List<BizGoods> products = null;
                QueryWrapper<BizGoods> productsQueryWrapper = new QueryWrapper<>();
                productsQueryWrapper.eq("inv_id", bizStockId);
                String scheduleId = schedule.getId();
                productsQueryWrapper.eq("invs_id", scheduleId);
                productsQueryWrapper.eq("status", 0);
                productsQueryWrapper.eq("sales_channel", resellerCode);
                productsQueryWrapper.eq("sales_target", salesTarget);
                if(temp.length() < 8){
                    // 要查当月的 只需要日期和最小值 包装带上出发地和目的地 方便再次查询具体日期
                    productsQueryWrapper.having("MONTH(sale_date) = {0}",startDate.substring(5,7));
                    products = bizGoodsMapper.selectList(productsQueryWrapper);
                    if(products == null || products.size() == 0){
                        continue;
                    }
                    relist.addAll(products);
                }else {
//                    productsQueryWrapper.like("sale_date", startDate);// yyyy-mm-dd
                    products = bizGoodsMapper.selectList(productsQueryWrapper);

                    if(products == null || products.size() == 0){
                        continue;
                    }

                    // 价格低的销售空了 才可以显示高价格的票
                    Double temps = 9999999.99;
                    BizGoods tempGoods = null;
                    for(BizGoods product:products){
                        if(product.getNumberSurplus() == 0){
                            continue;
                        }
                        if(product.getSalePrice() < temps ){
                            temps = product.getSalePrice();
                            tempGoods = product;
                        }
                    }
                    // 转成VO发给前端
                    rtnList = this.exchangeProductVOs(tempGoods, bizStock);
                }
            }
        }
        return rtnList.size() > 0? rtnList: this.createMonthList(relist, departure, arrival);
    }

    /**
     * 处理订单生产后的数量
     * @param productId 产品id
     */
    @Override
    public void dealProductNumById(String productId) {
        BizGoods product = this.getById(productId);
        Integer numberSurplus = product.getNumberSurplus();
        Integer saleQuantity = product.getSaleQuantity();
        product.setSaleQuantity(Math.addExact(saleQuantity,1));
        product.setNumberSurplus(Math.subtractExact(numberSurplus,1));

        this.saveOrUpdate(product);
        String invsId = product.getInvsId();
        BizSchedule bizSchedule = bizScheduleMapper.selectById(invsId);
        // 库存剩余
        Integer stockSurplus = bizSchedule.getStockSurplus();
        // 销售数量
        Integer saleQuantity1 = bizSchedule.getSaleQuantity();
        bizSchedule.setStockSurplus(Math.subtractExact(stockSurplus,1));
        bizSchedule.setSaleQuantity(Math.addExact(saleQuantity1,1));
        bizScheduleMapper.updateById(bizSchedule);
    }

    /**
     *服务携程的查询
     * @param airShoppingRequestVO
     * @return
     */
    @Override
    public List<BizGoods> queryProducts4XC(AirShoppingRequestVO airShoppingRequestVO) {
        // 到达城市 三字码
        String arriveCity = airShoppingRequestVO.getArriveCity();
        // 出发城市
        String departCity = airShoppingRequestVO.getDepartCity();
        // 类型 固定S
        String routeType = airShoppingRequestVO.getRouteType();// S

        // 默认是单程
        if(!"A".equals(routeType) && !"S".equals(routeType) ){
            return null;
        }

        // 出发日期
        Date departDate = airShoppingRequestVO.getDepartDate();

        QueryWrapper<BizCity> wrapper = new QueryWrapper<>();
        wrapper.eq("crcc",arriveCity);
        QueryWrapper<BizCity> wrapper2 = new QueryWrapper<>();
        wrapper.eq("crcc",departCity);
        BizCity one = null;
        BizCity two = null;
        try{
             one = bizCityService.getOne(wrapper);
             two = bizCityService.getOne(wrapper2);
        }catch (Exception e){
            logger.info("三字码对应不止一个城市");
        }

        List<BizGoods> list = new ArrayList<>();

        if(one == null || two == null){
            return null;
        } else{
            QueryWrapper<BizStock> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("src_point",two.getName());
            wrapper1.eq("dst_point",one.getName());
            wrapper1.eq("status",0);
            List<BizStock> bizStocks = bizStockMapper.selectList(wrapper1);
            if(bizStocks == null){
                return null;
            }else{
                for(BizStock bizStock:bizStocks){
                    String id = bizStock.getId();
                    QueryWrapper<BizSchedule> wrapper3 = new QueryWrapper<>();
                    wrapper3.eq("ivt_id",id);
                    wrapper3.eq("ivt_id",new SimpleDateFormat("yyyy-MM-dd").format(departDate));
                    wrapper3.eq("status",0);
                    List<BizSchedule> bizSchedules = bizScheduleMapper.selectList(wrapper3);
                    for(BizSchedule bizSchedule:bizSchedules){
                        String invsId = bizSchedule.getId();
                        QueryWrapper<BizGoods> wrapper4 = new QueryWrapper<>();
                        wrapper4.eq("inv_id",id);
                        wrapper4.eq("invs_id",invsId);
                        wrapper4.eq("status",0);
                        wrapper4.eq("sales_channel","携程网");
                        List<BizGoods> bizGoods = bizGoodsMapper.selectList(wrapper4);
                        list.addAll(bizGoods);
                    }
                }
            }
        }

        return list.size()==0? null: list;
    }

    /**
     * 产品日历
     * @param products 该月份下的产品
     * @param departure 出发地
     * @param arrival 目的地
     * @return
     */
    private List<BizProductsVO> createMonthList(List<BizGoods> products, String departure, String arrival) {
        List<BizProductsVO> list = new ArrayList<>();
//        LocalDate date = LocalDate.now(); //todo 不知道要返回当月组建数据还是真实数据 不知道前端处理还是后台处理
//        Month month = date.getMonth();
//        int length = month.length(date.isLeapYear());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(BizGoods product:products){
            BizProductsVO vo = new BizProductsVO();
            vo.setTotalPrice(product.getSalePrice());
            vo.setStartDate(format.format(product.getSaleDate()));
            for(BizGoods product2:products){
                if(product2.getSaleDate().equals(product.getSaleDate())){
                    double min = Double.min(product2.getSalePrice(), product.getSalePrice());
                    vo.setTotalPrice(min);
                }
            }
            Boolean flag = true;
            if(list.size() > 0){
                for(BizProductsVO vo1:list){
                    String startDate = vo1.getStartDate();
                    if(startDate.equals(vo.getStartDate())){
                        flag = false;
                        break;
                    }
                }
            }
           if(flag){
               vo.setDeparture(departure);
               vo.setArrival(arrival);
               list.add(vo);
           }
        }
        return list;
    }

    /**
     * 产品 组装成前端使用的VO
     * @param bizProduct 符合条件的产品
     * @param bizStock
     * @return
     */
    private List<BizProductsVO> exchangeProductVOs(BizGoods bizProduct, BizStock bizStock) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<BizProductsVO> list = new ArrayList<>();
        BizProductsVO vo = new BizProductsVO();
        vo.setArrival(bizStock.getDstPoint());
        vo.setDeparture(bizStock.getSrcPoint());
        vo.setTotalPrice(bizProduct.getSalePrice());
        vo.setCounts(bizProduct.getNumberSurplus());
        vo.setEndDate(dateFormat.format(bizProduct.getSaleDate()));
        vo.setEndTime(bizStock.getArrivalTime());
        vo.setMyc(bizStock.getCadf());
        vo.setProductId(bizProduct.getId());
        vo.setStartDate(dateFormat.format(bizProduct.getSaleDate()));
        vo.setStartTime(bizStock.getDepartureTime());
        vo.setTicketPar(bizProduct.getSalePrice());
        vo.setFlightNo(bizStock.getFlightNo());
        vo.setModelPlane(bizStock.getModelPlane());
        vo.setSrcAirport(bizStock.getSrcAirport());
        vo.setDstAirport(bizStock.getDstAirport());
        vo.setAirline(bizStock.getAirline());
        vo.setRefundRule(bizProduct.getRefundRule());
        vo.setBaggageAllowance(bizStock.getBaggageAllowance());
        list.add(vo);
        return list;
    }

}
