package tiane.org.ssm.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiane.org.ssm.dao.BizCredentialsMapper;
import tiane.org.ssm.dao.BizOrdersR2Mapper;
import tiane.org.ssm.entity.*;
import tiane.org.ssm.service.*;
import tiane.org.ssm.util.DoubleUtil;
import tiane.org.ssm.vo.*;
import tiane.org.ssm.vo.xc.GetCoopRefundConfirmItem;
import tiane.org.ssm.vo.xc.OpenApiQueryExchangeItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 国内订单服务层
 * @author dhl
 */
@Service("bizOrdersR2Service")
public class BizOrdersR2ServiceImpl extends ServiceImpl<BizOrdersR2Mapper, BizOrdersR2> implements BizOrdersR2Service {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BizOrdersR2Mapper bizOrdersR2Mapper;

    @Autowired
    private BizCrmService bizCrmService;

    @Autowired
    private BizGoodsService productsService;

    @Autowired
    private BizCredentialsMapper bizCredentialsMapper;

    @Autowired
    private BizStockService bizStockService;

    @Autowired
    private BizScheduleService bizScheduleService;

    @Autowired
    private BizCityService bizCityService;

    @Autowired
    private BizAgencyService bizAgencyService;

    @Autowired
    private BizGoodsService bizGoodsService;


    @Override
    public IPage<BizOrdersR2> queryPages(Page page, Map<String, Object> map) {
        QueryWrapper<BizOrdersR2> wrapper = new QueryWrapper<>();
        String srcPoint = (String)map.get("srcPoint");
        String dstPoint = (String)map.get("dstPoint");

        if(srcPoint != null && srcPoint.length() > 0 && dstPoint != null && dstPoint.length() > 0){
            List<BizCity> src = bizCityService.getCrccByName(srcPoint);
            List<BizCity> dst = bizCityService.getCrccByName(dstPoint);

            if(src != null && src.size() > 0){
                srcPoint = src.get(0).getCrcc();
            }else{
                return null;
            }

            if(dst != null && dst.size() > 0){
                dstPoint = dst.get(0).getCrcc();
            }else{
                return null;
            }
            String fligetSegment = srcPoint +"-"+ dstPoint;
            map.put("fligetSegment",fligetSegment);
        }

        map.put("page",page);

        List<BizOrdersR2> bizOrdersR2IPage = bizOrdersR2Mapper.selectMyPage(map);
        page.setRecords(bizOrdersR2IPage);
        return page;
    }

    @Override
    public List<BizOrdersR2> getListByCode(String orderCode) {
        QueryWrapper<BizOrdersR2> wrapper = new QueryWrapper<BizOrdersR2>();
        wrapper.eq("order_id",orderCode);
        return this.list(wrapper);
    }

    /**
     * 查询订单详情
     * @param orderId
     * @return
     */
    @Override
    public BizOrderR2DetailVO getOrderDetail(String orderId) {
        BizOrderR2DetailVO vo = new BizOrderR2DetailVO();
        BizOrdersR2 ordersR2 = this.getById(orderId);
        BizGoods product = productsService.getById(ordersR2.getProductId());
        BizCrm crm = bizCrmService.getCrmByCardNo(ordersR2);
        BizStock stock = bizStockService.getById(product.getInvId());

        QueryWrapper<BizCredentials> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("number",ordersR2.getCredential());
        List<BizCredentials> credentials = bizCredentialsMapper.selectList(queryWrapper);
        vo.setExpiry(credentials.get(0).getExpiry());
        vo.setCardType(credentials.get(0).getType());

        vo.setCabin(product.getCabin());
        vo.setArrivalTime(stock.getArrivalTime());
        vo.setDepartureTime(stock.getDepartureTime());
        vo.setFlightType(stock.getFlightType());
        vo.setSrcAirport(stock.getSrcAirport());
        vo.setDstAirport(stock.getDstAirport());

        vo.setSrcCity(stock.getSrcPoint());
        vo.setDstCity(stock.getDstPoint());
        vo.setAirline(stock.getAirline());
        vo.setModelPlane(stock.getModelPlane());

        String invsId = product.getInvsId();
        BizSchedule schedule = bizScheduleService.getById(invsId);
        Date flightDate = schedule.getFlightDate();
        SimpleDateFormat formatss = new SimpleDateFormat("yyyy-MM-dd");
        vo.setFlightDate(formatss.format(flightDate));

        // 飞行天数
        Integer tripDays = stock.getTripDays();
        flightDate.setDate(Math.addExact(flightDate.getDate(),tripDays));
        vo.setFlightEndDate(formatss.format(flightDate));

        vo.setPasser(crm);
        vo.setOrder(ordersR2);

        // 计算剩余支付时间
        Integer pay = -1;
        if("待付款".equals(ordersR2.getOrderStatus()) && "待出票".equals(ordersR2.getTicketStatus())){
            Date createdTime = ordersR2.getCreatedTime();
            Date nowTime = new Date();
            Date date = DateUtils.addMinutes(createdTime, product.getPaymentTimeout());
            if(nowTime.before(date)){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String s = format.format(createdTime);
                Integer currMin = Integer.parseInt(s.substring(14, 16));
                Integer currHour = Integer.parseInt(s.substring(11, 13));
                String nows = format.format(new Date());
                Integer nowsMin = Integer.parseInt(nows.substring(14, 16));
                Integer nowsHour = Integer.parseInt(nows.substring(11, 13));
                if(Math.subtractExact(nowsHour,currHour) == 0){
                    pay = Math.subtractExact(nowsMin,currMin);
                    pay = Math.subtractExact(product.getPaymentTimeout(),pay);
                }else if (Math.subtractExact(nowsHour,currHour) == 1 || nowsHour < currHour && currHour == 23){
                    pay = Math.addExact(Math.subtractExact(60,currMin),nowsMin);
                    pay = Math.subtractExact(product.getPaymentTimeout(),pay);
                }else {
                    pay = -1;
                }
            }
        }
        vo.setPayTime(pay > 20 ? -1: pay);
        return vo;
    }

    /**
     * 携程退票
     * @param getCoopRefundConfirmItem
     */
    @Override
    public void rtnXCTicket(GetCoopRefundConfirmItem getCoopRefundConfirmItem) {
        Long orderId = getCoopRefundConfirmItem.getOrderId();
        BizOrdersR2 bizOrders = this.getById(orderId);
        if(bizOrders == null){
            LOGGER.info("rtnXCTicket:订单号不匹配!");
            return;
        }
        this.makeTrnOperate(bizOrders,getCoopRefundConfirmItem.getRefundreason());
    }

    /**
     * 携程改签
     * @param getCoopRefundConfirmItem
     * @param bizOrders
     */
    @Override
    public void updateXCTicket(OpenApiQueryExchangeItem getCoopRefundConfirmItem, BizOrdersR2 bizOrders) {

        Date date2 = new Date();
        date2.setSeconds(date2.getSeconds()+1);// 区分创建时间
        String dateString2 = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date2);
        BizOrdersR2 bizOrdersR2 = new BizOrdersR2();
        bizOrdersR2.setCreatedBy(bizOrders.getCreatedBy());
        bizOrdersR2.setCredential(bizOrders.getCredential());
        bizOrdersR2.setContact(bizOrders.getContact());
        bizOrdersR2.setCreatedTime(date2);
        bizOrdersR2.setPassenger(bizOrders.getPassenger());

//        String productId = vo.getProductId();
//        BizGoods newGoods = bizGoodsService.getById(productId);
//        BizStock stock = bizStockService.getById(newGoods.getInvId());
//        BizSchedule newSchedule = bizScheduleService.getById(newGoods.getInvsId());

        // 根据name查crcc
//        List<BizCity> bizCities = bizCityService.getCrccByName(stock.getSrcPoint());
//        List<BizCity> bizCities2 = bizCityService.getCrccByName(stock.getDstPoint());
//        String s1 = bizCities.size() > 0 ? bizCities.get(0).getCrcc() : "";
//        String s2 = bizCities2.size() > 0 ? bizCities2.get(0).getCrcc() : "";
//        bizOrdersR2.setFlightSegment(s1 + "-" + s2);
//        bizOrdersR2.setFlightNo(stock.getFlightNo());
//        bizOrdersR2.setFlightDate(newSchedule.getFlightDate());

        // TODO 线上的改期到现在也没说清楚 不知道改的是不是我们得票我们得商品 无法锁定新签的是哪个商品 需不需要操作库存
        // 销售价格 和 进账价格（负数）
//        if(bizOrders.getPassengerType().equals("ADT")){
            bizOrdersR2.setSalePrice(DoubleUtil.sub(0,bizOrders.getSalePrice())); // 票款 + 民航发展基金
            bizOrdersR2.setIncomeAmount(DoubleUtil.sub(0,bizOrders.getIncomeAmount()));//票款 + 民航发展基金 - 代理费
            bizOrdersR2.setAgentFee(0.0); // 代理费
            bizOrdersR2.setFare(0.0);// 票款 = 商品价格
            bizOrdersR2.setCadf(0.0);
            bizOrdersR2.setInboundCadf(0.0);
            bizOrdersR2.setOutboundCadf(0.0);
//        }else if(bizOrders.getPassengerType().equals("CHD")){
//            bizOrdersR2.setSalePrice(DoubleUtil.add(DoubleUtil.mul(stock.getPrice(),0.5),stock.getCadf())); // 票款 + 民航发展基金
//            bizOrdersR2.setIncomeAmount(DoubleUtil.sub(bizOrdersR2.getSalePrice(),newGoods.getAgencyFees()));//票款 + 民航发展基金 - 代理费
//            bizOrdersR2.setAgentFee(newGoods.getAgencyFees()); // 代理费
//            bizOrdersR2.setFare(DoubleUtil.mul(newGoods.getSalePrice(),0.5));// 票款 = 商品价格
//            bizOrdersR2.setCadf(stock.getCadf());
//            bizOrdersR2.setInboundCadf(stock.getInboundCadf());
//            bizOrdersR2.setOutboundCadf(stock.getOutboundCadf());
//        }else{
//            bizOrdersR2.setSalePrice(DoubleUtil.add(DoubleUtil.mul(stock.getPrice(),0.1),0.0)); // 票款 + 民航发展基金
//            bizOrdersR2.setIncomeAmount(DoubleUtil.sub(bizOrdersR2.getSalePrice(),newGoods.getAgencyFees()));//票款 + 民航发展基金 - 代理费
//            bizOrdersR2.setAgentFee(newGoods.getAgencyFees()); // 代理费
//            bizOrdersR2.setFare(DoubleUtil.mul(newGoods.getSalePrice(),0.1));// 票款 = 商品价格
//            bizOrdersR2.setCadf(0.00);
//            bizOrdersR2.setInboundCadf(0.00);
//            bizOrdersR2.setOutboundCadf(0.00);
//        }

        bizOrdersR2.setSalesChannel("线上平台");
        bizOrdersR2.setProductId(null);
        bizOrdersR2.setId(dateString2);
        bizOrdersR2.setOrderId(bizOrders.getOrderId());
        bizOrdersR2.setOrderDate(date2);

        bizOrdersR2.setSalesCompany(bizOrders.getSalesCompany());
        bizOrdersR2.setPassengerType(bizOrders.getPassengerType());
        bizOrdersR2.setTicketStatus("已改签");
        bizOrdersR2.setOrderType("改期");
        bizOrdersR2.setOrderStatus("已付款");
        this.saveOrUpdate(bizOrdersR2);
    }

    @Override
    public boolean saveOrder(BizOrderVO vo) {
        LOGGER.info(JSON.toJSONString(vo));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        BizProductsVO products = vo.getProducts();
        // 商品
        String productId = products.getProductId();
        BizGoods goods = bizGoodsService.getById(productId);

        List<BizPasserVO> passers = vo.getPasser();

        /**
         * 当前商品 没有多余的票
         */
        if(goods.getNumberSurplus() < passers.size()){
            return false;
        }
        // 库存
        String invId = goods.getInvId();
        BizStock stock = bizStockService.getById(invId);

        try{
            // 生产客户
            for(BizPasserVO passerVO:passers){
                // 证件类别和证件号 一起验证
                String cardType = passerVO.getCardType();
                String cardNumber = passerVO.getCardNumber();
                String passerName = passerVO.getName();
                if(bizCrmService.isNeedInsert(cardType,cardNumber,passerName) != null){
                    continue;
                }
                BizCrm crm = new BizCrm();
                Date date = new Date();
                crm.setId(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date));
                crm.setContact(passerVO.getPhoneNo());
                crm.setGender(passerVO.getGender());
                crm.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse(passerVO.getBirthDate()));
                crm.setType(passerVO.getType());
                crm.setNationality(passerVO.getNationality());
                crm.setName(passerVO.getName());
                bizCrmService.save(crm);

                BizCredentials bizCredentials = new BizCredentials();
                bizCredentials.setId(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date));
                bizCredentials.setCrmId(crm.getId());
                bizCredentials.setNumber(cardNumber);
                bizCredentials.setType(cardType);
                bizCredentials.setExpiry(new SimpleDateFormat("yyyy-MM-dd").parse(passerVO.getExpiry()));
                bizCredentialsMapper.insert(bizCredentials);

            }
            String timeStr = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            // 生产订单
            String creatorId = vo.getCreator();
            BizAgency agency = bizAgencyService.getById(creatorId);
            for (BizPasserVO passer:passers){
                BizOrdersR2 order = new BizOrdersR2();
                order.setOrderDate(new Date());
                order.setOrderType("出票");
                order.setOrderStatus("待付款");
                order.setPassengerType(passer.getType());
                order.setCredential(passer.getCardNumber());
                order.setContact(passer.getPhoneNo());
                order.setFlightDate(simpleDateFormat.parse(products.getStartDate()));
                order.setCreatedTime(new Date());
                order.setPassenger(passer.getName());
                order.setTicketStatus("待出票");
                // 销售价格 代理费 进账金额 票款   民航发展基金婴儿不收费
                if(passer.getType().equals("ADT")){
                    order.setSalePrice(DoubleUtil.add(goods.getSalePrice(),stock.getCadf())); // 票款 + 民航发展基金
                    order.setIncomeAmount(DoubleUtil.sub(order.getSalePrice(),goods.getAgencyFees()));//票款 + 民航发展基金 - 代理费
                    order.setAgentFee(goods.getAgencyFees()); // 代理费
                    order.setFare(goods.getSalePrice());// 票款 = 商品价格
                    order.setCadf(stock.getCadf());
                    order.setInboundCadf(stock.getInboundCadf());
                    order.setOutboundCadf(stock.getOutboundCadf());
                }else if(passer.getType().equals("CHD")){
                    order.setSalePrice(DoubleUtil.add(DoubleUtil.mul(stock.getPrice(),0.5),stock.getCadf())); // 票款 + 民航发展基金
                    order.setIncomeAmount(DoubleUtil.sub(order.getSalePrice(),goods.getAgencyFees()));//票款 + 民航发展基金 - 代理费
                    order.setAgentFee(goods.getAgencyFees()); // 代理费
                    order.setFare(DoubleUtil.mul(goods.getSalePrice(),0.5));// 票款 = 商品价格
                    order.setCadf(stock.getCadf());
                    order.setInboundCadf(stock.getInboundCadf());
                    order.setOutboundCadf(stock.getOutboundCadf());
                }else{
                    order.setSalePrice(DoubleUtil.add(DoubleUtil.mul(stock.getPrice(),0.1),0.0)); // 票款 + 民航发展基金
                    order.setIncomeAmount(DoubleUtil.sub(order.getSalePrice(),goods.getAgencyFees()));//票款 + 民航发展基金 - 代理费
                    order.setAgentFee(goods.getAgencyFees()); // 代理费
                    order.setFare(DoubleUtil.mul(goods.getSalePrice(),0.1));// 票款 = 商品价格
                    order.setCadf(0.00);
                    order.setInboundCadf(0.00);
                    order.setOutboundCadf(0.00);
                }
                order.setFlightNo(products.getFlightNo());

                if("b2b".equals(vo.getType())){
                    order.setCreatedBy(agency.getName());
                    order.setSalesChannel("线下代理");
                    order.setSalesCompany(agency.getCompany());
                    order.setOrderId(timeStr);
                }else{
                    // 携程
                    order.setCreatedBy(vo.getCreator());
                    order.setSalesChannel("线上平台");
                    order.setSalesCompany(vo.getCreator());
                    order.setOrderId(vo.getPlatformSerialNumber());
                }

                // 根据name查crcc
                List<BizCity> bizCities = bizCityService.getCrccByName(products.getDeparture());
                List<BizCity> bizCities2 = bizCityService.getCrccByName(products.getArrival());
                String s1 = bizCities.size() > 0 ? bizCities.get(0).getCrcc() : "";
                String s2 = bizCities2.size() > 0 ? bizCities2.get(0).getCrcc() : "";
                order.setFlightSegment(s1 + "-" + s2);
                order.setProductId(productId);

                Date date = new Date();
                order.setId(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date));
                this.save(order);
            }
            // 处理库存
            productsService.dealProductNumById(productId);
        }catch (Exception e){
            LOGGER.info("saveOrder:"+e.getMessage());
            return false;
        }
     return true;
    }

    /**
     * 退票
     * @param vo 退票vo
     * @return
     */
    @Override
    public boolean rtnTicket(BizRtnTicketVO vo) {
        try{
            String orderCode = vo.getOrderCode();
            String remarks = vo.getRemarks();
            QueryWrapper<BizOrdersR2> wrapper = new QueryWrapper<>();
            wrapper.eq("order_id",orderCode);
            wrapper.eq("id",vo.getOrderId());
            wrapper.eq("order_status","已付款");
            List<BizOrdersR2> bizOrdersList = this.list(wrapper);
            if(bizOrdersList == null){
                return false;
            }
            // 筛选最新的记录
            BizOrdersR2 bizOrders = null;
            Date maxDate = null;
            for(BizOrdersR2 temp:bizOrdersList){
                Date orderDate = temp.getOrderDate();
                if(maxDate == null){
                    maxDate = orderDate;
                    bizOrders = temp;
                    continue;
                }
                if(orderDate.after(maxDate)){
                    maxDate = orderDate;
                    bizOrders = temp;
                }
            }
            Boolean flag = this.makeTrnOperate(bizOrders,remarks);

            if(bizOrders.getPassengerType().equals("ADT")){
                // 退相关婴儿
                String orderCode2 = bizOrders.getOrderId();
                QueryWrapper<BizOrdersR2> wrapper2 = new QueryWrapper<>();
                wrapper2.eq("order_id",orderCode2);
//                wrapper2.eq("id",vo.getOrderId());
                wrapper2.eq("order_status","已付款");
                wrapper2.eq("passenger_type","INF");
                List<BizOrdersR2> bizOrdersList2 = this.list(wrapper2);
                if(bizOrdersList2 == null){
                    return true;
                }
                // 筛选最新的记录
                BizOrdersR2 bizOrders2 = null;
                Date maxDate2 = null;
                for(BizOrdersR2 temp:bizOrdersList2){
                    Date orderDate = temp.getOrderDate();
                    if(maxDate2 == null){
                        maxDate2 = orderDate;
                        bizOrders2 = temp;
                        continue;
                    }
                    if(orderDate.after(maxDate2)){
                        maxDate2 = orderDate;
                        bizOrders2 = temp;
                    }
                }
                flag = this.makeTrnOperate(bizOrders2,remarks);
            }

            return flag;
        }catch (Exception e){
            LOGGER.info("rtnTicket:"+e.getMessage());
            return false;
        }
    }

    /**
     * 退票相关业务
     * @param bizOrders
     * @param remarks
     * @return
     */
    private boolean makeTrnOperate(BizOrdersR2 bizOrders,String remarks) {
        BizOrdersR2 bizOrdersR2 = new BizOrdersR2();
        Date date = new Date();
        String dateString = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);

        bizOrdersR2.setCreatedBy(bizOrders.getCreatedBy());
        bizOrdersR2.setSalesCompany(bizOrders.getSalesCompany());
        bizOrdersR2.setCredential(bizOrders.getCredential());
        bizOrdersR2.setContact(bizOrders.getContact());
        bizOrdersR2.setFlightDate(bizOrders.getFlightDate());
        bizOrdersR2.setPassengerType(bizOrders.getPassengerType());
        bizOrdersR2.setTicketNo(bizOrders.getTicketNo());
        bizOrdersR2.setPnr(bizOrders.getPnr());

        bizOrdersR2.setPassenger(bizOrders.getPassenger());
        bizOrdersR2.setFlightNo(bizOrders.getFlightNo());
        bizOrdersR2.setFlightSegment(bizOrders.getFlightSegment());
        bizOrdersR2.setFlightItinerary(bizOrders.getFlightItinerary());
        bizOrdersR2.setProductId(bizOrders.getProductId());
        bizOrdersR2.setOrderDate(date);
        bizOrdersR2.setTicketNo(bizOrders.getTicketNo());

        bizOrdersR2.setCadf(bizOrders.getCadf());
        bizOrdersR2.setOutboundCadf(bizOrders.getOutboundCadf());
        bizOrdersR2.setInboundCadf(bizOrders.getInboundCadf());
        bizOrdersR2.setAgentFee(bizOrders.getAgentFee());
        bizOrdersR2.setFare(bizOrders.getFare());
        // 销售价格 和 进账价格（负数）
        bizOrdersR2.setSalePrice(bizOrders.getSalePrice());
        bizOrdersR2.setIncomeAmount(DoubleUtil.sub(0.00,bizOrders.getIncomeAmount()));

        bizOrdersR2.setId(dateString);
        bizOrdersR2.setOrderId(bizOrders.getOrderId());

        if(bizOrdersR2.getSalesChannel().equals("线上平台")){
            bizOrdersR2.setOrderType("退票");
            bizOrdersR2.setOrderStatus("已退款");
            bizOrdersR2.setTicketStatus("已退票");
            bizOrdersR2.setSalesChannel("线上平台");
        }else{
            bizOrdersR2.setOrderType("退票");
            bizOrdersR2.setOrderStatus("待退款");
            bizOrdersR2.setTicketStatus("待退票");
            bizOrdersR2.setSalesChannel("线下代理");
        }


        bizOrdersR2.setRemarks(remarks);
        bizOrdersR2.setCreatedTime(date);
        return this.saveOrUpdate(bizOrdersR2);
    }

    /**
     * 改签 更新订单
     * @param vo 产品vo
     * @return
     */
    @Override

    public boolean updateTicket(BizRtnTicketVO vo) {
        try{
            QueryWrapper<BizOrdersR2> wrapper = new QueryWrapper<>();
            wrapper.eq("order_id",vo.getOrderCode());
            wrapper.eq("id",vo.getOrderId());
            wrapper.eq("order_status","已付款");
            List<BizOrdersR2> bizOrdersList = this.list(wrapper);

            if(bizOrdersList == null || bizOrdersList.size() == 0){
                return false;
            }

            BizOrdersR2 bizOrders = null;
            Date maxDate = null;
            for(BizOrdersR2 temp:bizOrdersList){
                Date orderDate = temp.getOrderDate();
                if(maxDate == null){
                    maxDate = orderDate;
                    bizOrders = temp;
                    continue;
                }
                if(orderDate.after(maxDate)){
                    maxDate = orderDate;
                    bizOrders = temp;
                }
            }
            Boolean flag = this.makeUpdateOperate(vo,bizOrders);

            if(bizOrders.getPassengerType().equals("ADT")){
                // 退相关婴儿
                String orderCode2 = bizOrders.getOrderId();
                QueryWrapper<BizOrdersR2> wrapper2 = new QueryWrapper<>();
                wrapper2.eq("order_id",orderCode2);
//                wrapper2.eq("id",vo.getOrderId());
                wrapper2.eq("order_status","已付款");
                wrapper2.eq("passenger_type","INF");
                List<BizOrdersR2> bizOrdersList2 = this.list(wrapper2);
                if(bizOrdersList2 == null || bizOrdersList2.size() == 0){
                    return true;
                }
                // 筛选最新的记录
                BizOrdersR2 bizOrders2 = null;
                Date maxDate2 = null;
                for(BizOrdersR2 temp:bizOrdersList2){
                    Date orderDate = temp.getOrderDate();
                    if(maxDate2 == null){
                        maxDate2 = orderDate;
                        bizOrders2 = temp;
                        continue;
                    }
                    if(orderDate.after(maxDate2)){
                        maxDate2 = orderDate;
                        bizOrders2 = temp;
                    }
                }
                flag = this.makeUpdateOperate(vo,bizOrders2);
            }
            return flag;
        }catch (Exception e){
            LOGGER.info("updateTicket:"+e.getMessage());
            return false;
        }
    }

    /**
     * 改签相关操作
     * @return
     */
    private boolean makeUpdateOperate(BizRtnTicketVO vo,BizOrdersR2 bizOrders) {
        Date date1 = new Date();
        String dateString = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date1);
        BizOrdersR2 bizOrdersR2_rtn = new BizOrdersR2();
        bizOrdersR2_rtn.setCreatedBy(vo.getUpdater());
        bizOrdersR2_rtn.setCreatedTime(date1);
        bizOrdersR2_rtn.setCredential(bizOrders.getCredential());
        bizOrdersR2_rtn.setContact(bizOrders.getContact());
        bizOrdersR2_rtn.setFlightDate(bizOrders.getFlightDate());
        bizOrdersR2_rtn.setPassenger(bizOrders.getPassenger());


        bizOrdersR2_rtn.setIncomeAmount(DoubleUtil.sub(0.00,bizOrders.getIncomeAmount()));
        bizOrdersR2_rtn.setSalePrice(bizOrders.getSalePrice());
        bizOrdersR2_rtn.setAgentFee(bizOrders.getAgentFee());
        bizOrdersR2_rtn.setCadf(bizOrders.getCadf());
        bizOrdersR2_rtn.setInboundCadf(bizOrders.getInboundCadf());
        bizOrdersR2_rtn.setOutboundCadf(bizOrders.getOutboundCadf());
        bizOrdersR2_rtn.setFare(bizOrders.getFare());

        bizOrdersR2_rtn.setFlightItinerary(bizOrders.getFlightItinerary());
        bizOrdersR2_rtn.setFlightNo(bizOrders.getFlightNo());
        bizOrdersR2_rtn.setFlightSegment(bizOrders.getFlightSegment());
        bizOrdersR2_rtn.setProductId(bizOrders.getProductId());
        bizOrdersR2_rtn.setId(dateString);
        bizOrdersR2_rtn.setOrderId(bizOrders.getOrderId());
        bizOrdersR2_rtn.setOrderDate(date1);
        bizOrdersR2_rtn.setPnr(bizOrders.getPnr());
        bizOrdersR2_rtn.setTicketNo(bizOrders.getTicketNo());

        bizOrdersR2_rtn.setSalesCompany(bizOrders.getSalesCompany());
        bizOrdersR2_rtn.setPassengerType(bizOrders.getPassengerType());
        bizOrdersR2_rtn.setSalesChannel("线下代理");
        bizOrdersR2_rtn.setTicketStatus("待退票");
        bizOrdersR2_rtn.setOrderType("退票");
        bizOrdersR2_rtn.setOrderStatus("待退款");
        this.saveOrUpdate(bizOrdersR2_rtn);


        Date date2 = new Date();
        date2.setSeconds(date2.getSeconds()+1);// 区分创建时间
        String dateString2 = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date2);
        BizOrdersR2 bizOrdersR2 = new BizOrdersR2();
        bizOrdersR2.setCreatedBy(bizOrders.getCreatedBy());
        bizOrdersR2.setCredential(bizOrders.getCredential());
        bizOrdersR2.setContact(bizOrders.getContact());
        bizOrdersR2.setCreatedTime(date2);
        bizOrdersR2.setPassenger(bizOrders.getPassenger());

        String productId = vo.getProductId();
        BizGoods newGoods = bizGoodsService.getById(productId);
        BizStock stock = bizStockService.getById(newGoods.getInvId());
        BizSchedule newSchedule = bizScheduleService.getById(newGoods.getInvsId());

        // 根据name查crcc
        List<BizCity> bizCities = bizCityService.getCrccByName(stock.getSrcPoint());
        List<BizCity> bizCities2 = bizCityService.getCrccByName(stock.getDstPoint());
        String s1 = bizCities.size() > 0 ? bizCities.get(0).getCrcc() : "";
        String s2 = bizCities2.size() > 0 ? bizCities2.get(0).getCrcc() : "";
        bizOrdersR2.setFlightSegment(s1 + "-" + s2);
        bizOrdersR2.setFlightNo(stock.getFlightNo());
        bizOrdersR2.setFlightDate(newSchedule.getFlightDate());

        // 销售价格 和 进账价格（负数）
        if(bizOrders.getPassengerType().equals("ADT")){
            bizOrdersR2.setSalePrice(DoubleUtil.add(newGoods.getSalePrice(),stock.getCadf())); // 票款 + 民航发展基金
            bizOrdersR2.setIncomeAmount(DoubleUtil.sub(bizOrdersR2.getSalePrice(),newGoods.getAgencyFees()));//票款 + 民航发展基金 - 代理费
            bizOrdersR2.setAgentFee(newGoods.getAgencyFees()); // 代理费
            bizOrdersR2.setFare(newGoods.getSalePrice());// 票款 = 商品价格
            bizOrdersR2.setCadf(stock.getCadf());
            bizOrdersR2.setInboundCadf(stock.getInboundCadf());
            bizOrdersR2.setOutboundCadf(stock.getOutboundCadf());
        }else if(bizOrders.getPassengerType().equals("CHD")){
            bizOrdersR2.setSalePrice(DoubleUtil.add(DoubleUtil.mul(stock.getPrice(),0.5),stock.getCadf())); // 票款 + 民航发展基金
            bizOrdersR2.setIncomeAmount(DoubleUtil.sub(bizOrdersR2.getSalePrice(),newGoods.getAgencyFees()));//票款 + 民航发展基金 - 代理费
            bizOrdersR2.setAgentFee(newGoods.getAgencyFees()); // 代理费
            bizOrdersR2.setFare(DoubleUtil.mul(newGoods.getSalePrice(),0.5));// 票款 = 商品价格
            bizOrdersR2.setCadf(stock.getCadf());
            bizOrdersR2.setInboundCadf(stock.getInboundCadf());
            bizOrdersR2.setOutboundCadf(stock.getOutboundCadf());
        }else{
            bizOrdersR2.setSalePrice(DoubleUtil.add(DoubleUtil.mul(stock.getPrice(),0.1),0.0)); // 票款 + 民航发展基金
            bizOrdersR2.setIncomeAmount(DoubleUtil.sub(bizOrdersR2.getSalePrice(),newGoods.getAgencyFees()));//票款 + 民航发展基金 - 代理费
            bizOrdersR2.setAgentFee(newGoods.getAgencyFees()); // 代理费
            bizOrdersR2.setFare(DoubleUtil.mul(newGoods.getSalePrice(),0.1));// 票款 = 商品价格
            bizOrdersR2.setCadf(0.00);
            bizOrdersR2.setInboundCadf(0.00);
            bizOrdersR2.setOutboundCadf(0.00);
        }

        bizOrdersR2.setSalesChannel("线下代理");
        bizOrdersR2.setProductId(vo.getProductId());
        bizOrdersR2.setId(dateString2);
        bizOrdersR2.setOrderId(bizOrders.getOrderId());
        bizOrdersR2.setOrderDate(date2);

        bizOrdersR2.setSalesCompany(bizOrders.getSalesCompany());
        bizOrdersR2.setPassengerType(bizOrders.getPassengerType());
        bizOrdersR2.setTicketStatus("待改签");
        bizOrdersR2.setOrderType("改期");
        bizOrdersR2.setOrderStatus("待付款");
        return this.saveOrUpdate(bizOrdersR2);
    }
}
