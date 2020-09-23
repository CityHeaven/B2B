package tiane.org.ssm.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class IdUtil {
    public static String exchangeIdString(){
        String s = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        return s;
    }
}
