package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;

/**
 * 检验订单创建回报vo
 */
@Data
public class SubmitOrderResponse implements Serializable {
    // 状态代码
    private Integer StateCode;
    // 状态描述
    private String Message;
}
