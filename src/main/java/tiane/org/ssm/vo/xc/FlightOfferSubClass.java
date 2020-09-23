package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 可售舱位
 */
@Data
public class FlightOfferSubClass implements Serializable {
    //产品id
    private String OfferItemID;
    //仓位
    private String Cabin;
    //可售座位数
    private Integer Seats;
    // 成人结算价
    private BigDecimal CostPrice;
    //儿童结算价
    private BigDecimal ChildCostPrice;
    // 票面价
    private BigDecimal PrintPrice;
    // 证件类型集合
    private List<Integer> AllowCardTypeList;
    private List<Integer> AllowPassengerTypeList;
    // 乘客类型
    private String PassengerType;
    //改签公式
    private String RescheduleRemark;
    // 预订备注
    private String Remark;
    // 产品类型
    private Integer ProductType;
    // 库存类型
    private Integer InventoryType;
    // IATA号
    private String IATANumber;
}
