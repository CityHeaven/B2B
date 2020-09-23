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
 * biz_crm
 * @author 
 */
@Data
@ApiModel(value = "crm",description = "crm")
@TableName(value = "biz_crm")
public class BizCrm implements Serializable {
    /**
     * 客户编号
     */
    @ApiModelProperty(value = "客户编号")
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String gender;

    /**
     * 客户类型
     */
    @ApiModelProperty(value = "客户类型")
    private String type;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contact;

    /**
     * 国籍
     */
    @ApiModelProperty(value = "国籍")
    private String nationality;

    /**
     * 出生日期
     */
    @ApiModelProperty(value = "出生日期")
    private Date birthDate;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 积分
     */
    @ApiModelProperty(value = "积分")
    private Integer points;

    private static final long serialVersionUID = 1L;
}