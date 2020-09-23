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
 * biz_credentials
 * @author 
 */
@Data
@ApiModel(value = "证件",description = "证件")
@TableName("biz_credentials")
public class BizCredentials implements Serializable {
    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 乘客编号
     */
    @ApiModelProperty(value = "乘客编号")
    private String crmId;

    /**
     * 证件类型
     */
    @ApiModelProperty(value = "证件类型")
    private String type;

    /**
     * 证件号
     */
    @ApiModelProperty(value = "证件号")
    private String number;

    /**
     * 证件有效期
     */
    @ApiModelProperty(value = "证件有效期")
    private Date expiry;

    private static final long serialVersionUID = 1L;

}