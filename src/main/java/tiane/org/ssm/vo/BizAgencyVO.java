package tiane.org.ssm.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import tiane.org.ssm.entity.BizAgency;

import java.io.Serializable;

@Data
@ApiModel(value = "登录vo",description = "登录vo")
public class BizAgencyVO implements Serializable {
    private String username;
    private String password;
    private String token;
    private BizAgency agency;
}
