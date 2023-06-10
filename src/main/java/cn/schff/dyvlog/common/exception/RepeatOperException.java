package cn.schff.dyvlog.common.exception;

/**
 * @Author：眭传洪
 * @Create：2023/5/23 22:50
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
public class RepeatOperException extends RuntimeException{
    public RepeatOperException(String message) {
        super(message);
    }
}
