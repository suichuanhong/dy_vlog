package cn.schff.dyvlog.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import static cn.schff.dyvlog.common.util.SystemConstant.FAIL_CODE;
import static cn.schff.dyvlog.common.util.SystemConstant.SUCCESS_CODE;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 21:04
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Data
@AllArgsConstructor
public class Result {

    private Object data;

    private String msg;

    private Integer status;

    public static Result success(Object data) {
        return new Result(data, "success", SUCCESS_CODE);
    }

    public static Result success() {
        return new Result(null, "success", SUCCESS_CODE);
    }

    public static Result fail(String msg) {
        return new Result(null, msg, FAIL_CODE);
    }

    public static Result fail(Object data) {
        return new Result(data, "fail", FAIL_CODE);
    }

}
