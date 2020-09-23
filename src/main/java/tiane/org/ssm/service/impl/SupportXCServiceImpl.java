package tiane.org.ssm.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiane.org.ssm.entity.BizGoods;
import tiane.org.ssm.entity.BizOrdersR2;
import tiane.org.ssm.entity.BizSchedule;
import tiane.org.ssm.entity.BizStock;
import tiane.org.ssm.service.*;
import tiane.org.ssm.util.DoubleUtil;
import tiane.org.ssm.vo.BizOrderVO;
import tiane.org.ssm.vo.BizPasserVO;
import tiane.org.ssm.vo.BizProductsVO;
import tiane.org.ssm.vo.xc.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SupportXCServiceImpl implements SupportXCService {

    @Autowired
    private BizStockService bizStockService;

    @Autowired
    private BizScheduleService bizScheduleService;

    // 商品服务
    @Autowired
    private BizGoodsService bizGoodsService;

    // 国内订单服务
    @Autowired
    private BizOrdersR2Service bizOrdersR2Service;

    @Override
    public String airShopping(ResultVO<AirShoppingRequestVO> vo) {
//        MessageHead messageHead = vo.getMessageHead();
        // 是否需要验证签名和token??? 可以通用的入口
        AirShoppingRequestVO airShoppingRequestVO = vo.getMessageBody();
        // 为携程查询商品
        List<BizGoods> list = bizGoodsService.queryProducts4XC(airShoppingRequestVO);
        // 组合商品 返回要的格式json
        AirShoppingResponseVO responseVO = new AirShoppingResponseVO();
        List<SingleSegmentOfferItem> singleSegmentOfferItems = new ArrayList<>();
        for(BizGoods bizGoods:list){
            SingleSegmentOfferItem item = this.makeSingleSegementOfferItem(bizGoods);
            singleSegmentOfferItems.add(item);
        }
        responseVO.setSingleSegmentOfferItems(singleSegmentOfferItems);
        responseVO.setMutlipleSegmentOfferItems(null);
        ResultVO<AirShoppingResponseVO> resultVO = new ResultVO<>();
        resultVO.setMessageBody(responseVO);
        resultVO.setMessageHead(new MessageHead());
        return JSON.toJSONString(resultVO);
    }

    /**
     * 组装单程vo
     * @param bizGoods
     * @return
     */
    private SingleSegmentOfferItem makeSingleSegementOfferItem(BizGoods bizGoods) {
        String invId = bizGoods.getInvId();
        String invsId = bizGoods.getInvsId();
        BizSchedule schedule = bizScheduleService.getById(invsId);
        Date flightDate = schedule.getFlightDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(flightDate);
        BizStock stock = bizStockService.getById(invId);

        SingleSegmentOfferItem item = new SingleSegmentOfferItem();
        item.setAirline(stock.getAirline());
        item.setArriveAirport(stock.getDstAirport());
        item.setArriveCity(stock.getDstPoint());
        item.setArriveTerminal("");
        try{item.setArriveTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr+" "+stock.getArrivalTime()));
            // 机型
            item.setCraftType(stock.getModelPlane());
            item.setDepartAirport(stock.getSrcAirport());
            item.setDepartCity(stock.getSrcPoint());
            item.setDepartTerminal("");
            item.setDepartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr+" "+stock.getDepartureTime()));
            item.setFlightNumber(stock.getFlightNo());
        }catch(ParseException e){
            e.getMessage();
        }
        Tax newTax = new Tax();
        newTax.setBabyTax(new BigDecimal(0.0));
        newTax.setChildTax(new BigDecimal(stock.getCadf()));
        newTax.setAdultTax(new BigDecimal(stock.getCadf()));
        item.setTax(newTax);
        item.setOilFee(null);
        item.setMeal("");
        item.setOperateFlightNumber("");
        List<FlightOfferSubClass> sublist = new ArrayList<>();
        // 可售仓位
        FlightOfferSubClass flightOfferSubClass = new FlightOfferSubClass();
        flightOfferSubClass.setOfferItemID(bizGoods.getId());
        ArrayList<Integer> cardTypes = new ArrayList<>();
        cardTypes.add(1);
        cardTypes.add(2);
        flightOfferSubClass.setAllowCardTypeList(cardTypes);
        ArrayList<Integer> passTypes = new ArrayList<>();
        passTypes.add(0);
        passTypes.add(1);
        passTypes.add(2);
        flightOfferSubClass.setAllowPassengerTypeList(passTypes);
        flightOfferSubClass.setCabin(bizGoods.getCabin());
        flightOfferSubClass.setCostPrice(new BigDecimal(DoubleUtil.add(stock.getPrice(),stock.getCadf())));
        flightOfferSubClass.setChildCostPrice(new BigDecimal(DoubleUtil.add(DoubleUtil.mul(stock.getPrice(),0.5),stock.getCadf())));
        flightOfferSubClass.setIATANumber("");
        flightOfferSubClass.setInventoryType(0);
        flightOfferSubClass.setRemark("");
        flightOfferSubClass.setPrintPrice(new BigDecimal(bizGoods.getSalePrice()));
        flightOfferSubClass.setSeats(bizGoods.getNumberSeats());
        flightOfferSubClass.setPassengerType(bizGoods.getSalesTarget());
        flightOfferSubClass.setRescheduleRemark(bizGoods.getRefundRuleDetails());
        flightOfferSubClass.setProductType(7);// 切位

        item.setSubClassList(sublist);
        item.setStopsInfo(null);
        return item;
    }

    @Override
    public String flightPrice(ResultVO<FlightPriceRequestVO> vo) {
        FlightPriceRequestVO flightPriceRequestVO = vo.getMessageBody();
        Integer adultCount = flightPriceRequestVO.getAdultCount();
        Integer childCount = flightPriceRequestVO.getChildCount();
        SingleSegmentOfferItem singleSegmentOfferItem = flightPriceRequestVO.getSingleSegmentOfferItem();
        Integer total = Math.addExact(adultCount,childCount);

        // 航班公司
        String airline = singleSegmentOfferItem.getAirline();
        // 航班号
        String flightNumber = singleSegmentOfferItem.getFlightNumber();
        // 仓位
        String cabin = singleSegmentOfferItem.getCabin();
        // 出发时间
        Date departTime = singleSegmentOfferItem.getDepartTime();

        QueryWrapper<BizStock> stockMapper = new QueryWrapper<>();
        stockMapper.eq("flight_no",flightNumber);
        stockMapper.eq("airline",airline);
        stockMapper.eq("status",0);
        List<BizStock> bizStocks = bizStockService.list(stockMapper);
        FlightPriceResponseVO responseVO = new FlightPriceResponseVO();
        responseVO.setMessage("验价失败");
        responseVO.setSeats(0);
        responseVO.setStateCode(1);
        if(bizStocks == null || bizStocks.size() == 0){
            responseVO.setMessage("航线不匹配");
//            return JSON.toJSONString(responseVO);
            ResultVO<FlightPriceResponseVO> resultVO = new ResultVO<>();
            resultVO.setMessageBody(responseVO);
            resultVO.setMessageHead(new MessageHead());
            return JSON.toJSONString(resultVO);
        }else{
            for(BizStock stock:bizStocks){
                QueryWrapper<BizSchedule> scheduleQueryWrapper = new QueryWrapper<>();
                String id = stock.getId();
                String format = new SimpleDateFormat("yyyy-MM-dd").format(departTime);
                scheduleQueryWrapper.eq("flight_date",format);
                scheduleQueryWrapper.eq("ivt_id",id);
                scheduleQueryWrapper.eq("status",0);
                List<BizSchedule> bizSchedules = bizScheduleService.list(scheduleQueryWrapper);
                if(bizSchedules == null || bizSchedules.size() == 0){
                    responseVO.setMessage("航线不匹配");
                    ResultVO<FlightPriceResponseVO> resultVO = new ResultVO<>();
                    resultVO.setMessageBody(responseVO);
                    resultVO.setMessageHead(new MessageHead());
                    return JSON.toJSONString(resultVO);
                }else {
                    for (BizSchedule bizSchedule:bizSchedules){
                        String scheduleId = bizSchedule.getId();
                        QueryWrapper<BizGoods> wrapper = new QueryWrapper<>();
                        wrapper.eq("cabin",cabin);
                        wrapper.eq("inv_id",id);
                        wrapper.eq("invs_id",scheduleId);
                        wrapper.eq("sales_channel","携程网");
                        wrapper.eq("status",0);
                        List<BizGoods> bizGoods = bizGoodsService.list(wrapper);
                        if(bizGoods == null || bizGoods.size() == 0){
                            ResultVO<FlightPriceResponseVO> resultVO = new ResultVO<>();
                            resultVO.setMessageBody(responseVO);
                            resultVO.setMessageHead(new MessageHead());
                            return JSON.toJSONString(resultVO);
                        }else{
                            for (BizGoods goods:bizGoods){
                                Integer numberSurplus = goods.getNumberSurplus();
                                if(numberSurplus < total){
                                    responseVO.setMessage("仓位不足");
                                    ResultVO<FlightPriceResponseVO> resultVO = new ResultVO<>();
                                    resultVO.setMessageBody(responseVO);
                                    resultVO.setMessageHead(new MessageHead());
                                    return JSON.toJSONString(resultVO);
                                }else{
                                    responseVO.setStateCode(0);
                                    responseVO.setSeats(numberSurplus);
                                    responseVO.setMessage("验价成功");
                                    ResultVO<FlightPriceResponseVO> resultVO = new ResultVO<>();
                                    resultVO.setMessageBody(responseVO);
                                    resultVO.setMessageHead(new MessageHead());
                                    return JSON.toJSONString(resultVO);
                                }
                            }
                        }
                    }
                }
            }
        }
        ResultVO<FlightPriceResponseVO> resultVO = new ResultVO<>();
        resultVO.setMessageBody(responseVO);
        resultVO.setMessageHead(new MessageHead());
        return JSON.toJSONString(resultVO);
    }

    @Override
    public String createOrder(ResultVO<CreateOrderRequestVO> vo) {
        CreateOrderResponse response = new CreateOrderResponse();
        CreateOrderRequestVO messageBody = vo.getMessageBody();
        String contactName = messageBody.getContactName();// 联系人
        String contactPhone = messageBody.getContactPhone();// 联系电话
        String platformSerialNumber = messageBody.getPlatformSerialNumber();// 携程单号
        List<OfferItem> offerItems = messageBody.getOfferItems();// 产品集合
        for(OfferItem item:offerItems){
            // xc 产品唯一标识
            String offerItemID = item.getOfferItemID();
            String routeType = item.getRouteType();
            if("S".equals(routeType)){
                List<SingleSegmentOfferItem> singleSegmentOfferItems = item.getSingleSegmentOfferItems();
                if(singleSegmentOfferItems == null || singleSegmentOfferItems.size() == 0){
                    response.setMessage("xc单程产品集合为空，无法创建");
                    response.setStateCode(1);
                    response.setOrderSerialNumber(null);
                    ResultVO<CreateOrderResponse> resultVO = new ResultVO<>();
                    resultVO.setMessageBody(response);
                    resultVO.setMessageHead(new MessageHead());
                    return JSON.toJSONString(resultVO);
                }
                for(SingleSegmentOfferItem singleSegmentOfferItem:singleSegmentOfferItems){
                    List<Passengervo> passengers = singleSegmentOfferItem.getPassengers();
                    if(passengers == null || passengers.size() == 0){
                        response.setMessage("无乘客信息");
                        response.setStateCode(1);
                        response.setOrderSerialNumber(null);
                        ResultVO<CreateOrderResponse> resultVO = new ResultVO<>();
                        resultVO.setMessageBody(response);
                        resultVO.setMessageHead(new MessageHead());
                        return JSON.toJSONString(resultVO);
                    }
                    List<BizPasserVO> passengerVOs = this.exchangeBizPasserVO(passengers,contactName,contactPhone);
                    BizOrderVO orderVO = new BizOrderVO();
                    orderVO.setPasser(passengerVOs);
                    BizProductsVO productsVO = this.exchangeBizProductVO(singleSegmentOfferItem);
                    if(productsVO == null){
                        response.setMessage("航线不匹配或找不到对应商品！");
                        response.setStateCode(1);
                        response.setOrderSerialNumber(null);
                        ResultVO<CreateOrderResponse> resultVO = new ResultVO<>();
                        resultVO.setMessageBody(response);
                        resultVO.setMessageHead(new MessageHead());
                        return JSON.toJSONString(resultVO);
                    }
                    orderVO.setProducts(productsVO);
                    orderVO.setCreator("携程网");
                    orderVO.setType("携程网");
                    orderVO.setPlatformSerialNumber(platformSerialNumber);
                    bizOrdersR2Service.saveOrder(orderVO);
                }
                QueryWrapper<BizOrdersR2> wrapper = new QueryWrapper<>();
                wrapper.eq("order_id",platformSerialNumber);
                BizOrdersR2 one = bizOrdersR2Service.getOne(wrapper);
                response.setMessage("创建订单成功");
                response.setStateCode(0);
                response.setOrderSerialNumber(one.getId());
                ResultVO<CreateOrderResponse> resultVO = new ResultVO<>();
                resultVO.setMessageBody(response);
                resultVO.setMessageHead(new MessageHead());
                return JSON.toJSONString(resultVO);
            }else{
                response.setMessage("只支持单程");
                response.setStateCode(1);
                response.setOrderSerialNumber(null);
                ResultVO<CreateOrderResponse> resultVO = new ResultVO<>();
                resultVO.setMessageBody(response);
                resultVO.setMessageHead(new MessageHead());
                return JSON.toJSONString(resultVO);
            }

        }
        response.setMessage("xc产品集合为空");
        response.setStateCode(1);
        response.setOrderSerialNumber(null);
        ResultVO<CreateOrderResponse> resultVO = new ResultVO<>();
        resultVO.setMessageBody(response);
        resultVO.setMessageHead(new MessageHead());
        return JSON.toJSONString(resultVO);
    }

    /**
     * 转换成B2B的productVO
     * @param singleSegmentOfferItem
     * @return
     */
    private BizProductsVO exchangeBizProductVO(SingleSegmentOfferItem singleSegmentOfferItem) {
        BizProductsVO productsVO = new BizProductsVO();
        // 到达城市
        String arriveCity = singleSegmentOfferItem.getArriveCity();
        // 出发城市
        String departCity = singleSegmentOfferItem.getDepartCity();

        QueryWrapper<BizStock> wrapper = new QueryWrapper<>();
        wrapper.eq("dst_point",arriveCity);
        wrapper.eq("src_point",departCity);
        List<BizStock> list = bizStockService.list(wrapper);
        if(list == null || list.size() == 0){
            return null;
        }
        BizStock bizStock = list.get(0);
        String flightNo = bizStock.getFlightNo();

        // 起飞时间 -> 航班
        Date departTime = singleSegmentOfferItem.getDepartTime();
        String startDate = new SimpleDateFormat("yyyy-MM-dd").format(departTime);
        QueryWrapper<BizSchedule> scheduleQueryWrapper = new QueryWrapper<>();
        scheduleQueryWrapper.eq("flight_date",startDate);
        scheduleQueryWrapper.eq("ivt_id",bizStock.getId());
        BizSchedule schedule = bizScheduleService.getOne(scheduleQueryWrapper);
        if(schedule == null){
            return null;
        }
        // inv_id invs_id 仓位 -> 产品
        String cabin = singleSegmentOfferItem.getCabin();
        QueryWrapper<BizGoods> bizGoodsQueryWrapper = new QueryWrapper<>();
        bizGoodsQueryWrapper.eq("inv_id",bizStock.getId());
        bizGoodsQueryWrapper.eq("invs_id",schedule.getId());
        bizGoodsQueryWrapper.eq("cabin",cabin);
        BizGoods goods = bizGoodsService.getOne(bizGoodsQueryWrapper);
        if(goods == null){
            return null;
        }

        productsVO.setDeparture(departCity);
        productsVO.setArrival(arriveCity);
        productsVO.setStartDate(startDate);
        // flightNo
        productsVO.setFlightNo(flightNo);
        productsVO.setProductId(goods.getId());
        return productsVO;
    }

    /**
     * 转换成B2B的passerVO
     * @param passengers
     * @return
     */
    private List<BizPasserVO> exchangeBizPasserVO(List<Passengervo> passengers,String name, String number) {
        List<BizPasserVO> list = new ArrayList<>();
        for(Passengervo vo:passengers){
            BizPasserVO passerVO = new BizPasserVO();
            Date birthDay = vo.getBirthDay();
            String cardNumber = vo.getCardNumber();
            Enum<CardType> cardType = vo.getCardType();
            Enum<PassengerType> passengerType = vo.getPassengerType();
            String passerName = vo.getName();

            if(passerName.equals(name)){
                passerVO.setPhoneNo(number);
            }
            passerVO.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").format(birthDay));
            passerVO.setName(passerName);
            passerVO.setType(passengerType.toString());
            passerVO.setCardType(cardType.toString());
            passerVO.setCardNumber(cardNumber);
            list.add(passerVO);
        }

        return list;
    }

    @Override
    public String submitOrder(ResultVO<SubmitOrderRequestVO> vo) {
        SubmitOrderRequestVO messageBody = vo.getMessageBody();
        String orderSerialNumber = messageBody.getOrderSerialNumber();
        String platformSerialNumber = messageBody.getPlatformSerialNumber();
        List<PNR> pnRs = messageBody.getPNRs();
        QueryWrapper<BizOrdersR2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderSerialNumber);
        queryWrapper.eq("order_id",platformSerialNumber);
        BizOrdersR2 one = bizOrdersR2Service.getOne(queryWrapper);
        one.setPnr(pnRs.get(0).getPNR());
        SubmitOrderResponse response = new SubmitOrderResponse();
        response.setMessage("提交成功");
        response.setStateCode(0);
        ResultVO<SubmitOrderResponse> resultVO = new ResultVO<>();
        resultVO.setMessageBody(response);
        resultVO.setMessageHead(new MessageHead());
        return JSON.toJSONString(resultVO);
    }
}
