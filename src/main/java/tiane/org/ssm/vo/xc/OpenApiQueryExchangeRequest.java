package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 携程 改签VO
 */
@Data
public class OpenApiQueryExchangeRequest implements Serializable {
    // 开始日期
    private Date FromDateTime;
    // 结束日期
    private Date ToDateTime;
    // 票号
    private String TicketNO;
    // Unhandle:未处理申请（通用） Handled:已处理申请（通用） All:所有(国际-地区不支持) Success: 改签成功（仅国际-地区） Fail: 改签失败（仅国际-地区）
    private String RebookQueryOptionType = "Handled";
    private String FlightClass = "N";
}
