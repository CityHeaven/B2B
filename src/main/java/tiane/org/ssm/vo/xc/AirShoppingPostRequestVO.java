package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 主动推送 产品请求VO
 */
@Data
public class AirShoppingPostRequestVO implements Serializable {
    private List<SingleSegmentOfferItem> SingleSegmentOfferItems;
    private List<MutlipleSegmentOfferItem> MutlipleSegmentOfferItems = null;
}
