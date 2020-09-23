package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建订单请求接口
 */
@Data
public class CreateOrderRequestVO implements Serializable {
    // 携程订单号
    private String PlatformSerialNumber;
    // 联系人姓名
    private String ContactName;
    // 联系人电话
    private String ContactPhone;
    // 产品集合
    private List<OfferItem> OfferItems;
}
