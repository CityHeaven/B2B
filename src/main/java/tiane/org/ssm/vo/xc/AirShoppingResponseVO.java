package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 询价响应体VO
 */
@Data
public class AirShoppingResponseVO implements Serializable {
    // 单程产品集合
    private List<SingleSegmentOfferItem> SingleSegmentOfferItems;
    // 多程产品集合
    private List<MutlipleSegmentOfferItem> MutlipleSegmentOfferItems = null;
}
