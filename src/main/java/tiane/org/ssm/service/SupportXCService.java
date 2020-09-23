package tiane.org.ssm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tiane.org.ssm.vo.xc.*;

public interface SupportXCService {
    String airShopping(ResultVO<AirShoppingRequestVO> vo);

    String flightPrice(ResultVO<FlightPriceRequestVO> vo);

    String createOrder(ResultVO<CreateOrderRequestVO> vo);

    String submitOrder(ResultVO<SubmitOrderRequestVO> vo);
}
