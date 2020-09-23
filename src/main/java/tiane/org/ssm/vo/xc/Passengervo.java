package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Passengervo implements Serializable {
    private String Name;
    private Enum<PassengerType> PassengerType;
    private Enum<CardType> CardType;
    private String CardNumber;
    private Date BirthDay;
    private List<String> OfferItemIDs;
}
