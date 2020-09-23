package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultVO<T> implements Serializable {
    private MessageHead MessageHead;
    private T MessageBody;
}
