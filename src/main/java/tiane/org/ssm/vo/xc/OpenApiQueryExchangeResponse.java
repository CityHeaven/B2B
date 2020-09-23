package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OpenApiQueryExchangeResponse implements Serializable {
    private List<OpenApiQueryExchangeItem> OpenApiQueryExchangeItems;
}
