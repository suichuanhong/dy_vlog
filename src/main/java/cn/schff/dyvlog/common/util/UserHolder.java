package cn.schff.dyvlog.common.util;

import cn.schff.dyvlog.pojo.User;

/**
 * @Author：眭传洪
 * @Create：2023/5/1 15:52
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
public class UserHolder {

    private static final ThreadLocal<User> tl = new ThreadLocal<>();

    public static void add(User user) {
        tl.set(user);
    }

    public static void remove(User user) {
        tl.remove();
    }

    public static User getUser() {
        return tl.get();
    }

}
