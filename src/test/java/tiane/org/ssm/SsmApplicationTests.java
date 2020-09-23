package tiane.org.ssm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tiane.org.ssm.vo.xc.PassengerType;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class SsmApplicationTests {

    @Test
    void contextLoads() {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = new Date();
//        date.setMinutes(2);
//        String s = format.format(date);
//        System.out.println(s);
//        System.out.println(s.substring(14,16));
        Enum<PassengerType> typeEnum = PassengerType.ADT;
        System.out.println(typeEnum.toString());
    }

}
