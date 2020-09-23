package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户支付前，调用此接口检查创单结果 请求vo
 */
@Data
public class SubmitOrderRequestVO implements Serializable {
    // 携程订单号
    private String PlatformSerialNumber;
    // 供应商订单号
    private String OrderSerialNumber;
    // PNR集合 非真实PNR传000000
    private List<PNR> PNRs;

}
