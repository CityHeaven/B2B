package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GetCoopRefundConfirmListResponseType implements Serializable {
    private List<GetCoopRefundConfirmItem> GetCoopRefundConfirmListItems;
}
