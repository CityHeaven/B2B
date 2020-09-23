package tiane.org.ssm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * biz_agency 代理商
 * @author 
 */
@Data
@ApiModel(value = "代理商实体",description = "代理商实体")
@TableName("biz_agency")
public class BizAgency implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 代理商名称
     */
    @ApiModelProperty(value = "代理商名称")
    private String company;

    /**
     * 上级代理id
     */
    @ApiModelProperty(value = "上级代理")
    private String parentId;

    /**
     * 账户名称
     */
    @ApiModelProperty(value = "账户名称")
    private String name;

    /**
     * 账户密码
     */
    @ApiModelProperty(value = "账户密码")
    private String password;

    /**
     * 所在国家
     */
    @ApiModelProperty(value = "所在国家")
    private String country;

    /**
     * 所在城市
     */
    @ApiModelProperty(value = "所在城市")
    private String city;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contact;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String tel;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "删除标识")
    private Integer delFlag = 0;

    private static final long serialVersionUID = 1L;

}