package tiane.org.ssm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tiane.org.ssm.entity.BizCrm;
import tiane.org.ssm.entity.BizOrdersR2;

import java.io.Serializable;
import java.util.Date;

/**
 * 国内订单详情返回体
 */
@Data
@ApiModel(value = "国内订单详情返回体",description = "国内订单详情返回体")
public class BizOrderR2DetailVO implements Serializable {
    @ApiModelProperty(value = "订单基本信息")
    private BizOrdersR2 order;
    @ApiModelProperty(value = "乘客信息")
    private BizCrm passer;

    // 起飞时间
    private String departureTime;
    // 到达时间
    private String arrivalTime;
    // 剩余支付时间
    private Integer payTime;
    // 仓位
    private String cabin;
    // 国际or国内
    private String flightType;
    // 起飞机场
    private String srcAirport;
    // 到达机场
    private String dstAirport;

    // 起飞日期
    private String flightDate;
    // 证件类型
    private String cardType;
    // 证件有效期
    private Date expiry;
    // 起飞地点
    private String srcCity;
    // 到达地点
    private String dstCity;

    // 航空公司
    private String airline;
    // 飞机类型
    private String modelPlane;
    // 落地日期
    private String flightEndDate;
}
