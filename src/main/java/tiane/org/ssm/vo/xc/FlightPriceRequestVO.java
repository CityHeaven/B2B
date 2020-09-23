package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;

/**
 * 验价请求VO
 */
@Data
public class FlightPriceRequestVO implements Serializable {
    // 产品唯一标识
    private String OfferItemID;
    // 成人数量
    private Integer AdultCount;
    // 儿童数量
    private Integer ChildCount;
    // 单程产品
    private SingleSegmentOfferItem SingleSegmentOfferItem;
    // 多程产品
    private MutlipleSegmentOfferItem MutlipleSegmentOfferItem = null;

}
