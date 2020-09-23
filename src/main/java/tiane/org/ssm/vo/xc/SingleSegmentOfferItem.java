package tiane.org.ssm.vo.xc;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 单程产品
 */
@Data
public class SingleSegmentOfferItem implements Serializable {
    // 航空公司
    private String Airline;
    // 航班号
    private String FlightNumber;
    // 共享航班号
    private String OperateFlightNumber;
    // 出发城市
    private String DepartCity;
    // 到达城市
    private String ArriveCity;
    // 出发机场
    private String DepartAirport;
    // 到达机场
    private String ArriveAirport;
    // 出发航站楼
    private String DepartTerminal;
    // 到达航站楼
    private String ArriveTerminal;
    // 出发日期
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date DepartTime;
    // 到达日期
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date ArriveTime;
    // 可售仓位
    private List<FlightOfferSubClass> SubClassList;
    // 机型
    private String CraftType;
    // 餐食
    private String Meal;
    // 经停信息集合
    private List<FlightStopsInfo> StopsInfo;
    // 燃油费
    private OilFee OilFee;
    // 发展基金
    private Tax Tax;

    // flight price
    // 仓位
    private String Cabin;

    // create order
    private List<Passengervo> Passengers;

}
