package tiane.org.ssm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 航班时刻
 * @author dhl
 */
@Data
@TableName(value = "biz_schedule")
public class BizSchedule implements Serializable {
    /**
     * 编号
     */
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 库存ID
     */
    private String ivtId;

    /**
     * 组
     */
    private String groupId;

    /**
     * 航班日期
     */
    private Date flightDate;

    /**
     * 库存总量
     */
    private Integer stockTotal;

    /**
     * 投放数量
     */
    private Integer productQuantity;

    /**
     * 库存剩余
     */
    private Integer stockSurplus;

    /**
     * 销售数量
     */
    private Integer saleQuantity;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remarks;

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

    private static final long serialVersionUID = 1L;

}