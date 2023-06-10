package cn.schff.dyvlog.common.util;

import java.time.LocalDate;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 21:05
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
public class SystemConstant {

    public static final Integer SUCCESS_CODE = 200;

    public static final Integer FAIL_CODE = 500;

    public static final Integer CODE_LENGTH = 6;

    public static final Integer CODE_TIMEOUT = 5;

    public static final LocalDate DEFAULT_DATE = LocalDate.of(2023,1,1);

    public static final String NICK_NAME = "dy_";

    public static final String DEFAULT_FACE = "http://192.168.23.135:9001/dyvlog/22.jpg";

    public static final Integer BACK_IMG_TYPE = 1;

    public static final Integer FACE_IMAGE_TYPE = 2;

    public static final Integer DEFAULT_VLOG_STATUS = 0;

    public static final Integer INIT_LIKE_COUNT = 0;

    public static final Integer VLOG_COMMENTS = 0;

    public static final boolean IS_PUBLIC = true;

    public static final Integer IS_FRIEND = 1;

    public static final Integer NOT_FRIEND = 0;

    public static final Long ZERO = 0L;

    public static final Integer FOLLOW_TYPE = 1;

    public static final Integer LIKE_VLOG_TYPE = 2;

    public static final Integer COMMENT_VLOG_TYPE = 3;

    public static final Integer ANSWER_COMMENT_TYPE = 4;

    public static final Integer LIKE_COMMENT_TYPE = 5;




}
