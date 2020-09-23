package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class GetCoopRefundConfirmItem implements Serializable {
    private Long OrderId;
    private Integer Prid;
    private Integer IsForceAudited;
    private BigDecimal RefundServerFee;

    private String Currency;
    private BigDecimal ConversionRate;
    private List<GetCoopRefundConfirmDetailItem> GetCoopRefundConfirmDetailItems;
    private Integer IsConfirm;

    private String Audited;
    private String RT_Oper;
    private String RT_Time;
    private String FlightClass;
    private String PrepayType;

    private Integer Tickets;
    private String RefundDesc;
    private String IsRefund;
    private Date TakeOffTime;

    private Integer row;
    private Integer CountNumber;
    private Integer Urgency;
    private String Refundreason;
    private Integer RejectReasonType;
    private Integer CancelReasonType;

}
