package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 改期的回报vo
 */
@Data
public class OpenApiQueryExchangeItem implements Serializable {
    private Long RbkId;// 改签单号
    private Integer Sequence;// 航程序号(对应出票记录里的航程序号)
    private String ProcessStatus;// 改签申请处理类型
    private String Remark;// 显示改签备注
    private String PassengerName;// 乘客姓名
    private String OldFlight;// 原航班号
    private String OldDPort;
    private String OldAPort;
    private Date OldTakeOffTime;// 原航班起飞时间
    private String OldSubClass;
    private String OldRecordNo;// 原pnr

    private String OldTicketNO;// 原票号
    private String Flight;
    private String DPort;
    private String APort;
    private Date TakeOffTime;
    private String SubClass;
    private String RecordNo;
    private String TicketNO;
    private BigDecimal PriceDifferential;
    private BigDecimal ChangeFee;
}
