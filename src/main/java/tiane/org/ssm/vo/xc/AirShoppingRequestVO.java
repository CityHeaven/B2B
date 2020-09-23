package tiane.org.ssm.vo.xc;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 询价请求vo
 */
@Data
public class AirShoppingRequestVO implements Serializable {
    // 出发城市 (均为三字码)
    private String DepartCity;
    // 到达城市
    private String ArriveCity;
    // 出发机场
    private String DepartPort;
    // 达到机场
    private String ArrivePort;
    // 出发日期
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date DepartDate;
    // 返程日期
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date ReturnDate;
    // 航程类型 默认 A 0 所有类型 S 1单程 M 2联程 D 3往返
    private String RouteType;
}
