package tiane.org.ssm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 中国国内航线销售订单
 * biz_orders_r2
 * @author 
 */
@Data
@ApiModel(value = "中国国内航线销售订单",description = "中国国内航线销售订单")
@TableName(value = "biz_orders_r2")
public class BizOrdersR2 implements Serializable {
    /**
     * 记录编号
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderId;

    /**
     * 订单类型
     */
    @ApiModelProperty(value = "订单类型")
    private String orderType;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单类型")
    private String orderStatus;

    /**
     * 产品编号
     */
    @JsonFormat
    @ApiModelProperty(value = "产品编号")
    private String productId;

    /**
     * 进单日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "进单日期")
    private Date orderDate;

    /**
     * 黑屏出退票日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "黑屏出退票日期")
    private Date airlineOptDate;

    /**
     * 航班日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "航班日期")
    private Date flightDate;

    /**
     * 旅客姓名
     */
    @ApiModelProperty(value = "旅客姓名")
    private String passenger;

    /**
     * 旅客类别
     */
    @ApiModelProperty(value = "旅客类别")
    private String passengerType;

    /**
     * 证件号
     */
    @ApiModelProperty(value = "证件号")
    private String credential;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contact;

    /**
     * 航段
     */
    @ApiModelProperty(value = "航段")
    private String flightSegment;

    /**
     * 航班号
     */
    @ApiModelProperty(value = "航班号")
    private String flightNo;

    /**
     * 销售渠道
     */
    @ApiModelProperty(value = "销售渠道")
    private String salesChannel;

    /**
     * 进账金额
     */
    @ApiModelProperty(value = "进账金额")
    private Double incomeAmount = 0.0;

    /**
     * 退改签手续费
     */
    @ApiModelProperty(value = "退改签手续费")
    private Double changeFee =0.0;

    /**
     * 代理费
     */
    @ApiModelProperty(value = "代理费")
    private Double agentFee = 0.0;

    /**
     * 销售价格
     */
    @ApiModelProperty(value = "销售价格")
    private Double salePrice =0.0;

    /**
     * 票款
     */
    @ApiModelProperty(value = "票款")
    private Double fare = 0.0;

    /**
     * 民航发展基金
     */
    @ApiModelProperty(value = "民航发展基金")
    private Double cadf = 0.0;

    /**
     * 收款方式
     */
    @ApiModelProperty(value = "收款方式")
    private String payment;

    /**
     * PNR
     */
    @ApiModelProperty(value = "PNR")
    private String pnr;

    /**
     * 票号
     */
    @ApiModelProperty(value = "票号")
    private String ticketNo;

    /**
     * 客票状态 待出票  出票完成  待退票  退票完成 改签完成 待改签
     */
    @ApiModelProperty(value = "客票状态")
    private String ticketStatus;

    /**
     * 行程单号
     */
    @ApiModelProperty(value = "行程单号")
    private String flightItinerary;

    /**
     * 是否作废
     */
    @ApiModelProperty(value = "是否作废")
    private String isVoid = "否";

//    /**
//     * 退改签手续费
//     */
//    @ApiModelProperty(value = "退改签手续费")
//    private Double refundFee;

    /**
     * 去程民航发展基金
     */
    @ApiModelProperty(value = "去程民航发展基金")
    private Double outboundCadf = 0.0;

    /**
     * 回程民航发展基金
     */
    @ApiModelProperty(value = "回程民航发展基金")
    private Double inboundCadf = 0.0;

    /**
     * 销售公司
     */
    @ApiModelProperty(value = "销售公司")
    private String salesCompany;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 确认退票日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "确认退票日期")
    private Date confirmRefundDate;

    /**
     * 单班调减日期
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(value = "单班调减日期")
    private Date singleReduceDate;

    /**
     * 删除标识 0代表不删除，1代表删除
     */
    @ApiModelProperty(value = "删除标识")
    private Integer delFlag = 0;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createdBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updatedBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updatedTime;

    /**
     * 录入方式 0：线上 1：手动
     */
    private Integer inputMode = 0;

    private static final long serialVersionUID = 1L;
}