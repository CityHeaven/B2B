package tiane.org.ssm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * biz_orders_r1 国际订单
 * @author 
 */
@Data
public class BizOrdersR1 implements Serializable {
    /**
     * 记录编号
     */
    private String id;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 订单类型
     */
    private String orderType;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 产品编号
     */
    private String productId;

    /**
     * 进单日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date orderDate;

    /**
     * 黑屏操作日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date airlineOptDate;

    /**
     * 航班日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date flightDate;

    /**
     * 国籍
     */
    private String nationality;

    /**
     * 护照号
     */
    private String passport;

    /**
     * 护照号有效期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date passportExpiry;

    /**
     * 旅客姓名
     */
    private String passenger;
    /**
     * 联系方式
     */
    private String contact;

    /**
     * 性别
     */
    private String gender;

    /**
     * 出生日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date birthDate;

    /**
     * 旅客类别
     */
    private String passengerType;

    /**
     * 航段
     */
    private String flightSegment;

    /**
     * 航班号
     */
    private String flightNo;

    /**
     * 销售渠道
     */
    private String salesChannel;

    /**
     * 进账金额
     */
    private Double incomeAmount;

    /**
     * 改签费
     */
    private Double changeFee;

    /**
     * 代理费
     */
    private Double agentFee;

    /**
     * 销售价格
     */
    private Double salePrice;

    /**
     * 票面价
     */
    private Double ticketPar;

    /**
     * 税费
     */
    private Double tax;

    /**
     * 收款方式
     */
    private String payment;

    /**
     * PNR
     */
    private String pnr;

    /**
     * 票号
     */
    private String ticketNo;

    /**
     * 客票状态
     */
    private String ticketStatus;

    /**
     * 行程单号
     */
    private String flightItinerary;

    /**
     * 是否作废
     */
    private String isVoid = "否";

    /**
     * 去程税费
     */
    private Double outboundTax;

    /**
     * 回程税费
     */
    private Double inboundTax;

    /**
     * 销售单位
     */
    private String salesUnit;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 确认退票日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date confirmRefundDate;

    /**
     * 单班调减日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date singleReduceDate;

    /**
     * 删除标识 0:不删除，1:删除
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updatedTime;

    /**
     * 录入方式 0：线上 1：手动
     */
    private Integer inputMode = 0;

    private static final long serialVersionUID = 1L;
}