package tiane.org.ssm.util;

public class ReResponse {
    private final static String SUCCESS = "success";

    public static <T> Result<T> success() {
        return new Result<T>().setCode(200).setMsg(SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>().setCode(200).setMsg(SUCCESS).setData(data);
    }

    public static <T> Result<T> error(String message) {
        return new Result<T>().setCode(400).setMsg(message);
    }

    public static <T> Result<T> makeRsp(int code, String msg) {
        return new Result<T>().setCode(code).setMsg(msg);
    }

    public static <T> Result<T> makeRsp(int code, String msg, T data) {
        return new Result<T>().setCode(code).setMsg(msg).setData(data);
    }
}
