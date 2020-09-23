package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 产品
 */
@Data
public class OfferItem implements Serializable {
    // 产品唯一标识
    private String OfferItemID;
    // 航程类型
    private String RouteType;
    private List<SingleSegmentOfferItem> SingleSegmentOfferItems;
    private List<MutlipleSegmentOfferItem> MutliSegmentOfferItems = null;
}
