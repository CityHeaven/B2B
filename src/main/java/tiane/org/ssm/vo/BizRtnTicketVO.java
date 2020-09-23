package tiane.org.ssm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 退票VO
 */
@Data
@ApiModel(value = "退票vo",description = "退票vo")
public class BizRtnTicketVO implements Serializable {
    @ApiModelProperty(value = "订单编码")
    private String orderCode;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "产品编码")
    private String productId;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "操作人")
    private String updater;
}
