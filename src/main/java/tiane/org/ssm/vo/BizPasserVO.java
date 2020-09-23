package tiane.org.ssm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 乘坐人信息
 */
@Data
@ApiModel(value = "乘坐人vo",description = "乘坐人vo")
public class BizPasserVO implements Serializable {
//    private static final Integer CR_TYPE = 1;
//    private static final Integer ET_TYPE = 2;
//    private static final Integer TE_TYPE = 3;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "国籍")
    private String nationality;

    @ApiModelProperty(value = "生日")
    private String birthDate;

    @ApiModelProperty(value = "人员类别")
    private String type;

    @ApiModelProperty(value = "证件类别")
    private String cardType;

    @ApiModelProperty(value = "手机号")
    private String phoneNo;

    @ApiModelProperty(value = "证件号")
    private String cardNumber;// 证件号

    @ApiModelProperty(value = "身份证号")
    private String idCard; // 身份证号

    @ApiModelProperty(value = "证件有效期")
    private String expiry; // 证件有效期
}
