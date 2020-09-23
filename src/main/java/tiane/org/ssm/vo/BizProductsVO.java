package tiane.org.ssm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "产品vo",description = "产品vo")
public class BizProductsVO implements Serializable {
    /**
     * 产品编号
     */
    @ApiModelProperty(value = "产品编号")
    private String productId;
    /**
     * 出发地
     */
    @ApiModelProperty(value = "出发地")
    private String departure;
    /**
     * 目的地
     */
    @ApiModelProperty(value = "目的地")
    private String arrival;
    /**
     * 出发日期
     */
    @ApiModelProperty(value = "出发日期")
    private String startDate;
    /**
     * 返回日期
     */
    @ApiModelProperty(value = "返回日期")
    private String endDate;
    /**
     * 起飞时间
     */
    @ApiModelProperty(value = "起飞时间")
    private String startTime;
    /**
     * 落地时间
     */
    @ApiModelProperty(value = "落地时间")
    private String endTime;
    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer counts;
    /**
     * 结算价 = 总价(成人票面价 + 民航发展基金)
     */
    @ApiModelProperty(value = "结算价")
    private Double totalPrice;
    /**
     * 成人票面价
     */
    @ApiModelProperty(value = "成人票面价")
    private Double ticketPar;
    /**
     * 民航发展基金
     */
    @ApiModelProperty(value = "民航发展基金")
    private Double myc;

    @ApiModelProperty(value = "航班号")
    private String flightNo;

    @ApiModelProperty(value = "飞机型号")
    private String modelPlane;

    @ApiModelProperty(value = "代理费")
    private Double agencyFees;

    // 航空公司
    private String airline;

    // 起飞机场
    private String srcAirport;

    // 到达机场
    private String dstAirport;

    // 退改签规则路径
    private String refundRule;

    // 行李额
    private String baggageAllowance;
}
