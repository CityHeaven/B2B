package tiane.org.ssm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tiane.org.ssm.entity.BizCrm;
import tiane.org.ssm.entity.BizOrdersR1;
import tiane.org.ssm.entity.BizStock;

import java.io.Serializable;
import java.util.Date;

@Data
public class BizOrderR1DetailVO implements Serializable {
    @ApiModelProperty(value = "订单基本信息")
    private BizOrdersR1 order;
    @ApiModelProperty(value = "乘客信息")
    private BizCrm passer;
    @ApiModelProperty(value = "航线")
    private BizStock stock;

    // 剩余支付时间
    private Integer payTime;
    // 起飞日期
    private String flightDate;
    // 降落日期
    private String flightEndDate;

    // 证件类型
    private String cardType;
    // 证件有效期
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date expiry;

    // 仓位
    private String cabin;

}
