package tiane.org.ssm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 下订单的vo
 */
@Data
@ApiModel(value = "订单vo",description = "订单vo")
public class BizOrderVO implements Serializable {
   @ApiModelProperty(value = "乘客列表")
   private List<BizPasserVO> passer;

   @ApiModelProperty(value = "产品vo")
   private BizProductsVO products;
//   private String connector;
//   private String connectorPhone;
   @ApiModelProperty(value = "创建人")
   private String creator;

//   @ApiModelProperty(value = "创建公司")
//   private String company;

   @ApiModelProperty(value = "备注")
   private String remark;// 正常or内部

   @ApiModelProperty(value = "类型")
   private String type = "b2b";

   @ApiModelProperty(value = "携程订单号")
   private String platformSerialNumber = null;
}
