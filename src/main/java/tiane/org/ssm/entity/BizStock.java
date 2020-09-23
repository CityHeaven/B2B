package tiane.org.ssm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * biz_stock
 * @author 
 */
@Data
@TableName(value = "biz_stock")
public class BizStock implements Serializable {
    /**
     * 库存编号
     */
    private String id;

    /**
     * 航班号
     */
    private String flightNo;

    /**
     * 航空公司
     */
    private String airline;

    /**
     * 航班类型 国际/国内
     */
    private String flightType;

    /**
     * 出发地
     */
    private String srcPoint;

    /**
     * 目的地
     */
    private String dstPoint;

    /**
     * 出发机场
     */
    private String srcAirport;

    /**
     * 到达机场
     */
    private String dstAirport;

    /**
     * 行程天数
     */
    private Integer tripDays;

    /**
     * 提前预定天数
     */
    private Integer bookDays;

    /**
     * 提前出票天数
     */
    private Integer drawDays;

    /**
     * 机型
     */
    private String modelPlane;

    /**
     * 行李额
     */
    private String baggageAllowance;

    /**
     * 座位数量
     */
    private Integer numberSeats;

    /**
     * 起飞时间
     */
    private String departureTime;

    /**
     * 到达时间
     */
    private String arrivalTime;

    /**
     * 状态 0:启动；1:禁用
     */
    private Integer status;

    /**
     * 库存起始时间
     */
    private Date startTime;

    /**
     * 库存结束时间
     */
    private Date finishTime;

    /**
     * 组
     */
    private String groupId;

    /**
     * 周计划
     */
    private String schedule;

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
     * 民航发展基金
     */
    private Double cadf;
    /**
     * 去程民航发展基金
     */
    private Double outboundCadf;
    /**
     * 回程民航发展基金
     */
    private Double inboundCadf;

    /**
     * 全额票价
     */
    private Double price;


    private static final long serialVersionUID = 1L;

}