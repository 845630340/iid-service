package com.inspur.cloud.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 序列号生成器
 *
 * @author xushiqiang
 * @version 1.0
 */
public class SerialNumberUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(SerialNumberUtil.class);
    private static final DecimalFormat DF = new DecimalFormat("#0.00");

    /**
     * 生成主键ID[长度为20对应数据库类型bigint(20)] [当前毫秒级时间+三位随机码]
     *
     * @return 序列号字符串
     */
    public static String getSerialsNumber() {
        // 获取当前时间[毫秒级]
        StringBuilder sb = new StringBuilder();
        sb.append(getcurrentssstime());

        // 三位随机码 + 6位随机码
        String body = ((int) (new SecureRandom().nextDouble() * 900) + 100) + getValidateCode();

        return sb.append(body).toString();
    }

    /**
     * 返回当前时间[毫秒级]
     *
     * @return mmssSSS 格式时间
     */
    public static String getcurrentssstime() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    /**
     * 生成UUID序列 [去掉横杠的序列号]
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成6位的随机码 [生成机制：随机生成UUID序列，并随机截取6位连续的字符串]
     *
     * @return 返回随机6位序列号（组成：字母、数字）
     */
    public static String getValidateCode() {
        // 先生成一个32位的随机数
        String randomCode = getUUID();
        // 从这个32位数中，随机截取6位连续的字符串
        int begin = (int) (new SecureRandom().nextDouble() * 26) + 1;
        randomCode = randomCode.substring(begin, begin + 6);

        return randomCode;
    }

    /**
     * 生成6位随机数字
     *
     * @return 返回6位的数字随机码
     */
    public static String getRandomNum() {
        return String.valueOf((int) (new SecureRandom().nextDouble() * 900000) + 100000);
    }

    /**
     * 将金额转换成字符串型 [小数点保留两位]
     *
     * @param money 金额[单位：分]
     * @return 返回两位小数的金额字符串
     */
    public static String moneyToString(int money) {
        double a = (double) money / 100;

        return DF.format(a);
    }

    /**
     * 生成不等于0的随机金额
     *
     * @return 返回随机金额【int型整数】
     */
    public static int getRandomMoney() {
        //Random random = new Random();
        Random random;
        int money = 0;
        try{
            random = SecureRandom.getInstanceStrong();
            money = new SecureRandom().nextInt(100);
            if (money == 0)
            {
                money++;
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(),e);
        }


        return money;
    }
}
