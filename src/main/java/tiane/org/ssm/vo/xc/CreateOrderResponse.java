package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建订单响应vo
 */
@Data
public class CreateOrderResponse implements Serializable {
    private Integer StateCode;
    private String Message;
    private String OrderSerialNumber;
    private List<PNR> PNRs;
}
