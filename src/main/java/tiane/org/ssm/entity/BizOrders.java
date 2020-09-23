package tiane.org.ssm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单
 * @author dhl
 */
@Data
@TableName(value = "biz_orders")
public class BizOrders implements Serializable {
    /**
     * 订单状态（++ 改签完成、退票完成）
     */
    public static final Integer WAIT_PAY = 0;
    public static final Integer FINISH_PAY = 1;
    public static final Integer WAIT_RTN = 2;
    public static final Integer WAIT_UPDATE = 3;
    public static final Integer FINISH_RTN = 4;
    public static final Integer FINISH_UPDATE = 5;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 产品编号
     */
    private String orderCode;

    /**
     * 产品编号
     */
    private String productId;

    /**
     * 乘机人
     */
    private String passenger;

    /**
     * 票号
     */
    private String ticketNumber;

    /**
     * PNR
     */
    private String pnr;

    /**
     * 出发地
     */
    private String srcPoint;

    /**
     * 目的地
     */
    private String dstPoint;

    /**
     * 航班号
     */
    private String flightNumber;

    /**
     * 出票日期
     */
    private Date draftDate;

    /**
     * 起飞时间
     */
    private Date departureTime;

    /**
     * 进单日期
     */
    private Date orderDate;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 舱位
     */
    private String cabin;

    /**
     * 票面价
     */
    private Double ticketPar;

    /**
     * 民航发展基金/燃油费
     */
    private Double myc;

    /**
     * 底价/含税底价
     */
    private Double basePrice;

    /**
     * 扣率
     */
    private Double discountRate;

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
     * 退票类型 1：自愿 0：非自愿
     */
    private Integer rtnType;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 退票联系电话
     */
    private String rtnPhone;

    private static final long serialVersionUID = 1L;

}