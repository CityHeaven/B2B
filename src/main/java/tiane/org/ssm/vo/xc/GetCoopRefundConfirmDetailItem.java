package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * xc退票详情vo
 */
@Data
public class GetCoopRefundConfirmDetailItem implements Serializable {
    private Integer GetCoopRefundConfirmDetailItemID;
    private String RefundType;
    private String AirLineCode;
    private String TicketNO;

    private Long IssueBillID;
    private Boolean IsRebook;
    private String RecordNO;
    private String PassengerName;

    private String Dport;
    private String Aport;
    private Integer Sequence;
    private Date TakeOffTime;

    private String Flight;
    private String SubClass;
    private String FlightChangeProve;

    private BigDecimal Cost;
    private BigDecimal OilFee;
    private BigDecimal Tax;
    private BigDecimal RefundRate;
    private BigDecimal RefundFee;
    private BigDecimal UsedCost;
    private BigDecimal UsedTax;
    private BigDecimal PayFlightAgency;
    private BigDecimal PrintPrice;
    private BigDecimal CommissionFee;
    private BigDecimal Subsidy;
    private Long RbkID;

}
