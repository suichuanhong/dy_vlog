package cn.schff.dyvlog.config;

import cn.schff.dyvlog.common.exception.*;
import cn.schff.dyvlog.common.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 22:03
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginException.class)
    public Result handleLoginException(RuntimeException e) {
        log.error(e.getMessage());
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(UpdateFailException.class)
    public Result handleUpdateFailException(RuntimeException e) {
        log.error(e.getMessage());
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(RemoveException.class)
    public Result handleRemoveException(RuntimeException e) {
        log.error(e.getMessage());
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(ParameterException.class)
    public Result handleParameterException(RuntimeException e) {
        log.error(e.getMessage());
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(RepeatOperException.class)
    public Result handleRepeatOperException(RuntimeException e) {
        log.error(e.getMessage());
        return Result.fail(e.getMessage());
    }

}
