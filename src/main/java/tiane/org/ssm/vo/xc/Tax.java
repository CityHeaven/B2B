package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发展基金
 */
@Data
public class Tax implements Serializable {
    // 成人发展基金
    private BigDecimal AdultTax;
    // 儿童发展基金
    private BigDecimal ChildTax;
    // 婴儿发展基金
    private BigDecimal BabyTax;
}
