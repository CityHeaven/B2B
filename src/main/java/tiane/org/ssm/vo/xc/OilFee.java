package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 燃油费
 */
@Data
public class OilFee implements Serializable {
    // 成人燃油费
    private BigDecimal AdultFee;
    // 儿童燃油费
    private BigDecimal ChildFee;
    // 婴儿燃油费
    private BigDecimal BabyFee;
}
