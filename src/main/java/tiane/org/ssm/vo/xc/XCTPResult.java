package tiane.org.ssm.vo.xc;

import lombok.Data;

import java.io.Serializable;

/**
 * 携程退改签 请求VO
 * @param <T>
 */
@Data
public class XCTPResult<T> implements Serializable {
    private String UserName = "123_1637";
    private String UserPassword = "cb099cd16b65f63008053f2ebb95395a"; // 123_1637#Swan11111
    private T RequestBody;
}
