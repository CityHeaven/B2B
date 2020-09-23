package tiane.org.ssm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tiane.org.ssm.service.SupportXCService;
import tiane.org.ssm.vo.xc.*;

/**
 * 携程
 */
@RequestMapping("/xc")
@RestController
public class SupportXCController {

    @Autowired
    private SupportXCService service;

    /**
     * 询价接口
     * @return
     */
    @RequestMapping("/AirShopping")
    public String airShopping(@RequestBody ResultVO<AirShoppingRequestVO> vo){
        return service.airShopping(vo);
    }

    /**
     * 验价接口
     * @return
     */
    @RequestMapping("/FlightPrice")
    public String flightPrice(@RequestBody ResultVO<FlightPriceRequestVO> vo){
        return service.flightPrice(vo);
    }

    /**
     * 创建订单
     * @return
     */
    @RequestMapping("/CreateOrder")
    public String createOrder(@RequestBody ResultVO<CreateOrderRequestVO> vo){
        return service.createOrder(vo);
    }

    /**
     * 用户支付前，调用此接口检查创单结果，成功：允许用户支付；否则：拦截。
     * @return
     */
    @RequestMapping("/SubmitOrder")
    public String submitOrder(@RequestBody ResultVO<SubmitOrderRequestVO> vo){
        return service.submitOrder(vo);
    }


}
