package tiane.org.ssm.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统一返回实体
 * @param <T>
 */
@Data
@ApiModel(value = "返回类")
public class Result<T> {
    @ApiModelProperty(value = "code")
    public int code;
    @ApiModelProperty(value = "返回状态信息")
    private String msg;
    @ApiModelProperty(value = "返回数据")
    private T data;

//    public Result<T> setCode(RetCode retCode) {
//        this.code = retCode.code;
//        return this;
//    }

    public int getCode() {
        return code;
    }

    public Result<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}
