package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class GetCoopRefundConfirmListRequestType implements Serializable {
    // 进入开始日期时间
    private Date StartDate;
    // 进入结束日期时间
    private Date EndDate;
    private Date StartRefundConfirmDate;
    private Date EndRefundConfirmDate;
    // 0 国内，1 国际，2 国内+国际
    private Integer IsInternational;
    private Long OrderID;
    private String TicketNo;
    // 默认 1.全部 2.未退票 3.已退票 4.已拒绝 5.已取消
    private String FeeType;
    // 不传默认全部; 可传:自愿、航变、病退、备降、重购全退、形式全退、其它、废票、拒签、特殊事件
    private String RefundType;
    private Long IssueBillID;
    // 1按起飞时间，2按进单时间
    private Integer OrderBy;
    private Integer FlightAgency;
    // 请求者
    private String OperateUser;
    private Integer BeginRow = 1;
    private Integer EndRow = 10;

}
