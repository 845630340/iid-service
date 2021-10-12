package com.inspur.cloud.common.http;


/**
 * 缓存用户信息
 *
 * @author: ruanchanglong
 * @date: 2019/10/14
 */
public class RequestHolder {
    private static ThreadLocal<String> userIdThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> userNameThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> creatorIdThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> userPhoneThreadLocal = new ThreadLocal<>();

    public static void setUserId(String userId) {
        userIdThreadLocal.set(userId);
    }

    public static String getUserId() {
        return userIdThreadLocal.get();
    }

    /**
     * 用户名
     *
     * @param userName
     */
    public static void setUserName(String userName) {
        userNameThreadLocal.set(userName);
    }

    public static String getUserName() {
        return userNameThreadLocal.get();
    }

    /**
     * 创建/登录id
     *
     * @param creatorId
     */
    public static void setCreatorId(String creatorId) {
        creatorIdThreadLocal.set(creatorId);
    }

    public static String getCreatorId() {
        return creatorIdThreadLocal.get();
    }

    public static void setPhone(String phone) {
        userPhoneThreadLocal.set(phone);
    }

    public static String getPhone() {
        return userPhoneThreadLocal.get();
    }

    public static void remove() {
        userIdThreadLocal.remove();
        userNameThreadLocal.remove();
        creatorIdThreadLocal.remove();
        userPhoneThreadLocal.remove();
    }


}
