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
import tiane.org.ssm.dao.BizCountryMapper;
import tiane.org.ssm.dao.BizCredentialsMapper;
import tiane.org.ssm.dao.BizOrdersR1Mapper;
import tiane.org.ssm.dao.BizOrdersR1Mapper;
import tiane.org.ssm.entity.*;
import tiane.org.ssm.service.*;
import tiane.org.ssm.util.DoubleUtil;
import tiane.org.ssm.vo.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 国内订单服务层
 * @author dhl
 */
@Service
public class BizOrdersR1ServiceImpl extends ServiceImpl<BizOrdersR1Mapper, BizOrdersR1> implements BizOrdersR1Service {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BizOrdersR1Mapper bizOrdersR1Mapper;

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

    @Autowired
    private BizCountryMapper countryMapper;


    @Override
    public IPage<BizOrdersR1> queryPages(Page page, Map<String, Object> map) {
        QueryWrapper<BizOrdersR1> wrapper = new QueryWrapper<>();
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

        List<BizOrdersR1> bizOrdersR1IPage = bizOrdersR1Mapper.selectMyPage(map);
        page.setRecords(bizOrdersR1IPage);
        return page;
    }

    @Override
    public List<BizOrdersR1> getListByCode(String orderCode) {
        QueryWrapper<BizOrdersR1> wrapper = new QueryWrapper<BizOrdersR1>();
        wrapper.eq("order_id",orderCode);
        return this.list(wrapper);
    }

    /**
     * 查询订单详情
     * @param orderId
     * @return
     */
    @Override
    public BizOrderR1DetailVO getOrderDetail(String orderId) {
        BizOrderR1DetailVO vo = new BizOrderR1DetailVO();
        BizOrdersR1 ordersR1 = this.getById(orderId);
        BizGoods product = productsService.getById(ordersR1.getProductId());
        BizCrm crm = bizCrmService.getCrmByCardNoByR1(ordersR1);
        BizStock stock = bizStockService.getById(product.getInvId());

        QueryWrapper<BizCredentials> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("number",ordersR1.getPassport());
        List<BizCredentials> credentials = bizCredentialsMapper.selectList(queryWrapper);
        vo.setExpiry(credentials.get(0).getExpiry());
        vo.setCardType(credentials.get(0).getType());
        vo.setCabin(product.getCabin());

        vo.setStock(stock);

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
        vo.setOrder(ordersR1);

        // 计算剩余支付时间
        Integer pay = -1;
        if("待付款".equals(ordersR1.getOrderStatus()) && "待出票".equals(ordersR1.getTicketStatus())){
            Date createdTime = ordersR1.getCreatedTime();
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
            for (BizPasserVO passer:passers){
                // 根据证件号或身份证号查找乘机人
//                String passerId = bizCrmService.getIdByCardOrIdCard(passer);
                String creatorId = vo.getCreator();
                BizAgency agency = bizAgencyService.getById(creatorId);

                BizOrdersR1 order = new BizOrdersR1();
                order.setOrderDate(new Date());
                order.setOrderType("出票");
                order.setOrderStatus("待付款");
                order.setPassengerType(passer.getType());
                order.setPassport(passer.getCardNumber());
                order.setContact(passer.getPhoneNo());
                order.setFlightDate(simpleDateFormat.parse(products.getStartDate()));
                order.setCreatedTime(new Date());
                order.setPassenger(passer.getName());

                String nation = passer.getNationality();
                QueryWrapper<BizCountry> countryQuery = new QueryWrapper<>();
                countryQuery.eq("name",nation);
                List<BizCountry> countries = countryMapper.selectList(countryQuery);
                order.setNationality(countries.get(0).getNwcc());

                order.setPassportExpiry(new SimpleDateFormat("yyyy-MM-dd").parse(passer.getExpiry()));
                order.setGender(passer.getGender());
                order.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse(passer.getBirthDate()));

                order.setTicketStatus("待出票");
                // 销售价格 代理费 进账金额 票款   民航发展基金婴儿不收费
                if(passer.getType().equals("ADT")){
                    order.setSalePrice(DoubleUtil.add(goods.getSalePrice(),stock.getCadf())); // 票款 + 民航发展基金
                    order.setIncomeAmount(DoubleUtil.sub(order.getSalePrice(),goods.getAgencyFees()));//票款 + 民航发展基金 - 代理费
                    order.setAgentFee(goods.getAgencyFees()); // 代理费
                    order.setTicketPar(goods.getSalePrice());// 票款 = 商品价格
                    order.setTax(stock.getCadf());
                    order.setInboundTax(stock.getInboundCadf());
                    order.setOutboundTax(stock.getOutboundCadf());
                }else if(passer.getType().equals("CHD")){
                    order.setSalePrice(DoubleUtil.add(DoubleUtil.mul(stock.getPrice(),0.5),stock.getCadf())); // 票款 + 民航发展基金
                    order.setIncomeAmount(DoubleUtil.sub(order.getSalePrice(),goods.getAgencyFees()));//票款 + 民航发展基金 - 代理费
                    order.setAgentFee(goods.getAgencyFees()); // 代理费
                    order.setTicketPar(DoubleUtil.mul(goods.getSalePrice(),0.5));// 票款 = 商品价格
                    order.setTax(stock.getCadf());
                    order.setInboundTax(stock.getInboundCadf());
                    order.setOutboundTax(stock.getOutboundCadf());
                }else{
                    order.setSalePrice(DoubleUtil.add(DoubleUtil.mul(stock.getPrice(),0.1),0.0)); // 票款 + 民航发展基金
                    order.setIncomeAmount(DoubleUtil.sub(order.getSalePrice(),goods.getAgencyFees()));//票款 + 民航发展基金 - 代理费
                    order.setAgentFee(goods.getAgencyFees()); // 代理费
                    order.setTicketPar(DoubleUtil.mul(goods.getSalePrice(),0.1));// 票款 = 商品价格
                    order.setTax(0.00);
                    order.setInboundTax(0.00);
                    order.setOutboundTax(0.00);
                }
                order.setFlightNo(products.getFlightNo());
                order.setCreatedBy(agency.getName());
                order.setSalesChannel("线下代理");
                order.setSalesUnit(agency.getCompany());

                // 根据name查crcc
                List<BizCity> bizCities = bizCityService.getCrccByName(products.getDeparture());
                List<BizCity> bizCities2 = bizCityService.getCrccByName(products.getArrival());
                String s1 = bizCities.size() > 0 ? bizCities.get(0).getCrcc() : "";
                String s2 = bizCities2.size() > 0 ? bizCities2.get(0).getCrcc() : "";
                order.setFlightSegment(s1 + "-" + s2);
                order.setProductId(productId);

                Date date = new Date();
                order.setOrderId(timeStr);
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
            QueryWrapper<BizOrdersR1> wrapper = new QueryWrapper<>();
            wrapper.eq("order_id",orderCode);
            wrapper.eq("id",vo.getOrderId());
            wrapper.eq("order_status","已付款");
            List<BizOrdersR1> bizOrdersList = this.list(wrapper);
            if(bizOrdersList == null || bizOrdersList.size() == 0){
                return false;
            }
            // 筛选最新的记录
            BizOrdersR1 bizOrders = null;
            Date maxDate = null;
            for(BizOrdersR1 temp:bizOrdersList){
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
                QueryWrapper<BizOrdersR1> wrapper2 = new QueryWrapper<>();
                wrapper2.eq("order_id",orderCode2);
//                wrapper2.eq("id",vo.getOrderId());
                wrapper2.eq("order_status","已付款");
                wrapper2.eq("passenger_type","INF");
                List<BizOrdersR1> bizOrdersList2 = this.list(wrapper2);
                if(bizOrdersList2 == null || bizOrdersList2.size() == 0){
                    return true;
                }
                // 筛选最新的记录
                BizOrdersR1 bizOrders2 = null;
                Date maxDate2 = null;
                for(BizOrdersR1 temp:bizOrdersList2){
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
    private boolean makeTrnOperate(BizOrdersR1 bizOrders,String remarks) {
        BizOrdersR1 bizOrdersR1 = new BizOrdersR1();
        Date date = new Date();
        String dateString = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);

        bizOrdersR1.setCreatedBy(bizOrders.getCreatedBy());
        bizOrdersR1.setSalesUnit(bizOrders.getSalesUnit());
        bizOrdersR1.setPassport(bizOrders.getPassport());
        bizOrdersR1.setContact(bizOrders.getContact());
        bizOrdersR1.setFlightDate(bizOrders.getFlightDate());
        bizOrdersR1.setPassengerType(bizOrders.getPassengerType());
        bizOrdersR1.setTicketNo(bizOrders.getTicketNo());
        bizOrdersR1.setPnr(bizOrders.getPnr());

        bizOrdersR1.setPassenger(bizOrders.getPassenger());
        bizOrdersR1.setFlightNo(bizOrders.getFlightNo());
        bizOrdersR1.setFlightSegment(bizOrders.getFlightSegment());
        bizOrdersR1.setFlightItinerary(bizOrders.getFlightItinerary());
        bizOrdersR1.setProductId(bizOrders.getProductId());
        bizOrdersR1.setOrderDate(date);
        bizOrdersR1.setTicketNo(bizOrders.getTicketNo());

        bizOrdersR1.setTicketPar(bizOrders.getTicketPar());
        bizOrdersR1.setOutboundTax(bizOrders.getOutboundTax());
        bizOrdersR1.setInboundTax(bizOrders.getInboundTax());
        bizOrdersR1.setAgentFee(bizOrders.getAgentFee());
        bizOrdersR1.setTax(bizOrders.getTax());
        // 销售价格 和 进账价格（负数）
        bizOrdersR1.setSalePrice(bizOrders.getSalePrice());
        bizOrdersR1.setIncomeAmount(DoubleUtil.sub(0.00,bizOrders.getIncomeAmount()));

        bizOrdersR1.setNationality(bizOrders.getNationality());
        bizOrdersR1.setPassportExpiry(bizOrders.getPassportExpiry());
        bizOrdersR1.setGender(bizOrders.getGender());
        bizOrdersR1.setBirthDate(bizOrders.getBirthDate());

        bizOrdersR1.setId(dateString);
        bizOrdersR1.setOrderId(bizOrders.getOrderId());
        bizOrdersR1.setOrderType("退票");
        bizOrdersR1.setOrderStatus("待退款");
        bizOrdersR1.setTicketStatus("待退票");
        bizOrdersR1.setSalesChannel("线下代理");
        bizOrdersR1.setRemarks(remarks);
        bizOrdersR1.setCreatedTime(date);
        return this.saveOrUpdate(bizOrdersR1);
    }

    /**
     * 改签 更新订单
     * @param vo 产品vo
     * @return
     */
    @Override

    public boolean updateTicket(BizRtnTicketVO vo) {
        try{
            QueryWrapper<BizOrdersR1> wrapper = new QueryWrapper<>();
            wrapper.eq("order_id",vo.getOrderCode());
            wrapper.eq("id",vo.getOrderId());
            wrapper.eq("order_status","已付款");
            List<BizOrdersR1> bizOrdersList = this.list(wrapper);

            if(bizOrdersList == null || bizOrdersList.size() == 0){
                return false;
            }

            BizOrdersR1 bizOrders = null;
            Date maxDate = null;
            for(BizOrdersR1 temp:bizOrdersList){
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
                QueryWrapper<BizOrdersR1> wrapper2 = new QueryWrapper<>();
                wrapper2.eq("order_id",orderCode2);
//                wrapper2.eq("id",vo.getOrderId());
                wrapper2.eq("order_status","已付款");
                wrapper2.eq("passenger_type","INF");
                List<BizOrdersR1> bizOrdersList2 = this.list(wrapper2);
                if(bizOrdersList2 == null || bizOrdersList2.size() == 0){
                    return true;
                }
                // 筛选最新的记录
                BizOrdersR1 bizOrders2 = null;
                Date maxDate2 = null;
                for(BizOrdersR1 temp:bizOrdersList2){
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
    private boolean makeUpdateOperate(BizRtnTicketVO vo,BizOrdersR1 bizOrders) {
        Date date1 = new Date();
        String dateString = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date1);
        BizOrdersR1 bizOrdersR1_rtn = new BizOrdersR1();
        bizOrdersR1_rtn.setCreatedBy(vo.getUpdater());
        bizOrdersR1_rtn.setCreatedTime(date1);
        bizOrdersR1_rtn.setPassport(bizOrders.getPassport());
        bizOrdersR1_rtn.setContact(bizOrders.getContact());
        bizOrdersR1_rtn.setFlightDate(bizOrders.getFlightDate());
        bizOrdersR1_rtn.setPassenger(bizOrders.getPassenger());


        bizOrdersR1_rtn.setIncomeAmount(DoubleUtil.sub(0.00,bizOrders.getIncomeAmount()));
        bizOrdersR1_rtn.setSalePrice(bizOrders.getSalePrice());
        bizOrdersR1_rtn.setAgentFee(bizOrders.getAgentFee());
        bizOrdersR1_rtn.setTax(bizOrders.getTax());
        bizOrdersR1_rtn.setInboundTax(bizOrders.getInboundTax());
        bizOrdersR1_rtn.setOutboundTax(bizOrders.getOutboundTax());
        bizOrdersR1_rtn.setTicketPar(bizOrders.getTicketPar());

        bizOrdersR1_rtn.setFlightItinerary(bizOrders.getFlightItinerary());
        bizOrdersR1_rtn.setFlightNo(bizOrders.getFlightNo());
        bizOrdersR1_rtn.setFlightSegment(bizOrders.getFlightSegment());
        bizOrdersR1_rtn.setProductId(bizOrders.getProductId());
        bizOrdersR1_rtn.setId(dateString);
        bizOrdersR1_rtn.setOrderId(bizOrders.getOrderId());
        bizOrdersR1_rtn.setOrderDate(date1);
        bizOrdersR1_rtn.setPnr(bizOrders.getPnr());
        bizOrdersR1_rtn.setTicketNo(bizOrders.getTicketNo());

        bizOrdersR1_rtn.setNationality(bizOrders.getNationality());
        bizOrdersR1_rtn.setPassportExpiry(bizOrders.getPassportExpiry());
        bizOrdersR1_rtn.setGender(bizOrders.getGender());
        bizOrdersR1_rtn.setBirthDate(bizOrders.getBirthDate());

        bizOrdersR1_rtn.setSalesUnit(bizOrders.getSalesUnit());
        bizOrdersR1_rtn.setPassengerType(bizOrders.getPassengerType());
        bizOrdersR1_rtn.setSalesChannel("线下代理");
        bizOrdersR1_rtn.setTicketStatus("待退票");
        bizOrdersR1_rtn.setOrderType("退票");
        bizOrdersR1_rtn.setOrderStatus("待退款");
        this.saveOrUpdate(bizOrdersR1_rtn);


        Date date2 = new Date();
        date2.setSeconds(date2.getSeconds()+1);// 区分创建时间
        String dateString2 = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date2);
        BizOrdersR1 bizOrdersR1 = new BizOrdersR1();
        bizOrdersR1.setCreatedBy(bizOrders.getCreatedBy());
        bizOrdersR1.setPassport(bizOrders.getPassport());
        bizOrdersR1.setContact(bizOrders.getContact());
        bizOrdersR1.setCreatedTime(date2);
        bizOrdersR1.setPassenger(bizOrders.getPassenger());

        String productId = vo.getProductId();
        BizGoods newGoods = bizGoodsService.getById(productId);
        BizStock stock = bizStockService.getById(newGoods.getInvId());
        BizSchedule newSchedule = bizScheduleService.getById(newGoods.getInvsId());

        // 根据name查crcc
        List<BizCity> bizCities = bizCityService.getCrccByName(stock.getSrcPoint());
        List<BizCity> bizCities2 = bizCityService.getCrccByName(stock.getDstPoint());
        String s1 = bizCities.size() > 0 ? bizCities.get(0).getCrcc() : "";
        String s2 = bizCities2.size() > 0 ? bizCities2.get(0).getCrcc() : "";
        bizOrdersR1.setFlightSegment(s1 + "-" + s2);
        bizOrdersR1.setFlightNo(stock.getFlightNo());
        bizOrdersR1.setFlightDate(newSchedule.getFlightDate());

        // 销售价格 和 进账价格（负数）
        if(bizOrders.getPassengerType().equals("ADT")){
            bizOrdersR1.setSalePrice(DoubleUtil.add(newGoods.getSalePrice(),stock.getCadf())); // 票款 + 民航发展基金
            bizOrdersR1.setIncomeAmount(DoubleUtil.sub(bizOrdersR1.getSalePrice(),newGoods.getAgencyFees()));//票款 + 民航发展基金 - 代理费
            bizOrdersR1.setAgentFee(newGoods.getAgencyFees()); // 代理费
            bizOrdersR1.setTicketPar(newGoods.getSalePrice());// 票款 = 商品价格
            bizOrdersR1.setTax(stock.getCadf());
            bizOrdersR1.setInboundTax(stock.getInboundCadf());
            bizOrdersR1.setOutboundTax(stock.getOutboundCadf());
        }else if(bizOrders.getPassengerType().equals("CHD")){
            bizOrdersR1.setSalePrice(DoubleUtil.add(DoubleUtil.mul(stock.getPrice(),0.5),stock.getCadf())); // 票款 + 民航发展基金
            bizOrdersR1.setIncomeAmount(DoubleUtil.sub(bizOrdersR1.getSalePrice(),newGoods.getAgencyFees()));//票款 + 民航发展基金 - 代理费
            bizOrdersR1.setAgentFee(newGoods.getAgencyFees()); // 代理费
            bizOrdersR1.setTicketPar(DoubleUtil.mul(newGoods.getSalePrice(),0.5));// 票款 = 商品价格
            bizOrdersR1.setTax(stock.getCadf());
            bizOrdersR1.setInboundTax(stock.getInboundCadf());
            bizOrdersR1.setOutboundTax(stock.getOutboundCadf());
        }else{
            bizOrdersR1.setSalePrice(DoubleUtil.add(DoubleUtil.mul(stock.getPrice(),0.1),0.0)); // 票款 + 民航发展基金
            bizOrdersR1.setIncomeAmount(DoubleUtil.sub(bizOrdersR1.getSalePrice(),newGoods.getAgencyFees()));//票款 + 民航发展基金 - 代理费
            bizOrdersR1.setAgentFee(newGoods.getAgencyFees()); // 代理费
            bizOrdersR1.setTicketPar(DoubleUtil.mul(newGoods.getSalePrice(),0.1));// 票款 = 商品价格
            bizOrdersR1.setTax(0.00);
            bizOrdersR1.setInboundTax(0.00);
            bizOrdersR1.setOutboundTax(0.00);
        }

        bizOrdersR1.setNationality(bizOrders.getNationality());
        bizOrdersR1.setPassportExpiry(bizOrders.getPassportExpiry());
        bizOrdersR1.setGender(bizOrders.getGender());
        bizOrdersR1.setBirthDate(bizOrders.getBirthDate());

        bizOrdersR1.setSalesChannel("线下代理");
        bizOrdersR1.setProductId(vo.getProductId());
        bizOrdersR1.setId(dateString2);
        bizOrdersR1.setOrderId(bizOrders.getOrderId());
        bizOrdersR1.setOrderDate(date2);

        bizOrdersR1.setSalesUnit(bizOrders.getSalesUnit());
        bizOrdersR1.setPassengerType(bizOrders.getPassengerType());
        bizOrdersR1.setTicketStatus("待改签");
        bizOrdersR1.setOrderType("改期");
        bizOrdersR1.setOrderStatus("待付款");
        return this.saveOrUpdate(bizOrdersR1);
    }
}
