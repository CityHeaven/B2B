package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * 携程api 统一请求必须携带的message
 * @author dhl
 */
@Data
public class MessageHead implements Serializable {
    private String Token;
    private String Sign;
    private String SerialNumber = UUID.randomUUID().toString();
    private String Version = "0.4.3";
}
