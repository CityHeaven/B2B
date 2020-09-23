package tiane.org.ssm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品
 * @author dhl
 */
@Data
@ApiModel(value = "产品",description = "产品")
@TableName(value = "biz_goods")
public class BizGoods implements Serializable {
    /**
     * 产品编号
     */
    @ApiModelProperty(value = "产品编号")
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 销售渠道
     */
    @ApiModelProperty(value = "销售渠道")
    private String salesChannel;

    /**
     * 座位数量
     */
    @ApiModelProperty(value = "座位数量")
    private Integer numberSeats;

    /**
     * 销售数量
     */
    @ApiModelProperty(value = "销售数量")
    private Integer saleQuantity;

    /**
     * 剩余数量
     */
    @ApiModelProperty(value = "剩余数量")
    private Integer numberSurplus;

    /**
     * 销售价格
     */
    @ApiModelProperty(value = "销售价格")
    private Double salePrice;

    /**
     * 税费
     */
    private Double tax;

    /**
     * 代理费
     */
    private Double agencyFees;

    /**
     * 库存编号
     */
    private String invId;

    /**
     * 航班时刻编号
     */
    private String invsId;

    /**
     * 退改签规则路径
     */
    private String refundRule;

    /**
     * 销售日期
     */
    private Date saleDate;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 销售目标
     */
    private String salesTarget = "散客";

    /**
     * 支付超时限定 默认20分钟
     */
    private Integer paymentTimeout = 20;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 仓位
     */
    private String cabin;

    /**
     * 退改签规则详情id
     */
    private String refundRuleDetails;

    private static final long serialVersionUID = 1L;
}