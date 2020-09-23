package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;

/**
 * 验价回报vo
 */
@Data
public class FlightPriceResponseVO implements Serializable {
    // 状态码
    private Integer StateCode;
    // 状态描述
    private String Message;
    // 可售座位 验价成功必须返回座位数
    private Integer Seats;
}
