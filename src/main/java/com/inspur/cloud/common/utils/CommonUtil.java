package com.inspur.cloud.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author mysterious guest
 */
public class CommonUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(CommonUtil.class);

    private static final Pattern PATTERN = Pattern.compile("([A-Z])([a-z]+)");

    /**
     * UUID去掉"-"
     */
    public static String getuuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getUriParam(Collection<String> keySet){
        return keySet.stream()
                .map( key -> key+"={"+key+"}")
                .collect(Collectors.joining("&"));
    }


    /**
     * List -> ["obj","obj"];
     * List转格式化数组字符串
     */
    public static String list2ArrayStringFormat(List<?> target) {
        if (!CollectionUtils.isEmpty(target)) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (Object o : target) {
                sb.append("\"").append(o).append("\",");
            }
            return sb.replace(sb.length() - 1, sb.length(), "]").toString();
        }
        return "[]";
    }

    /**
     * ["obj","obj"]->List;
     * 数组字符串格式化转List
     */
    public static List<String> arrayStringFormat2List(String target) {
        if (!StringUtils.isEmpty(target)) {
            String[] strings = target.replaceAll("[\"\\[\\]]", "").split(",");
            if(strings.length ==1 && "".equals(strings[0])){
                return null;
            }
            return Arrays.asList(strings);
        }
        return null;
    }

    /**
     * 驼峰转下划线
     */
    public static String camelToUnderLine(String camle) {
        Matcher matcher = PATTERN.matcher(camle);
        StringBuffer sb = new StringBuffer();
        StringBuilder sbTmp = new StringBuilder();
        while (matcher.find()) {
            sbTmp.setLength(0);
            sbTmp.append("_").append(matcher.group(1).toLowerCase()).append(matcher.group(2));
            matcher.appendReplacement(sb, sbTmp.toString());
        }
        matcher.appendTail(sb);
        return sb.toString();

    }

    /**
     * bean转map
     * @param isCastUnderline 字段名是否转换为下划线命名方式
     */
    public static Map<String, String> beanToMap(Object o, boolean isCastUnderline) {
        Set<Map.Entry<String, Object>> set = BeanMap.create(o).entrySet();
        if (isCastUnderline) {
            return set.stream().filter(e -> e.getValue() != null).collect(Collectors.toMap(e -> camelToUnderLine(e.getKey()), e -> e.getValue().toString()));
        } else {
            return set.stream().filter(e -> e.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
        }
    }

    public static void shutDownExecutor(ExecutorService executorService) {
        LOGGER.info("关闭线程池公共方法: 开始关闭线程池");
        if (null != executorService) {
            try {
                executorService.shutdown();
                LOGGER.info("关闭线程池公共方法：发送关闭通知");
                if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                    LOGGER.info("等待一分钟后开始关闭线程false");
                    executorService.shutdownNow();
                    LOGGER.info("关闭线程池成功{}", executorService.isTerminated());
                } else {
                    LOGGER.info("等待一分钟后关闭线程true");
                }
            } catch (InterruptedException e) {
                LOGGER.error("关闭线程池失败{}", executorService.isTerminated());
                Thread.currentThread().interrupt();
            }
        }
        LOGGER.info("关闭线程池公共方法：关闭完毕");
    }


    public static boolean checkEmail(String email) {
        String ruleEmail = "^\\w+((-\\w+)|(\\.\\w+))*@[A-Za-z0-9]+(([.\\-])[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(ruleEmail);
        //正则表达式的匹配器
        Matcher m = p.matcher(email);
        //进行正则匹配
        return m.matches();
    }
    public static boolean checkReferUrl(String url) {
        String referUrl = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\\\\\\\\\\\\\/])+$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(referUrl);
        //正则表达式的匹配器
        Matcher m = p.matcher(url);
        //进行正则匹配
        return m.matches();
    }

    public static boolean checkDomain(String domain) {
        String ruleDomain = "^(?=^.{3,255}$)([a-z0-9][-_a-z0-9]{0,62})(\\.[-_a-z0-9]{1,62})+(\\.[-_a-z0-9]{0,5}[a-z0-9])+$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(ruleDomain);
        //正则表达式的匹配器
        Matcher m = p.matcher(domain);
        //进行正则匹配
        return m.matches();
    }

    /**
     * description:判断手机号格式是否正确
     */
    public static boolean checkMobile(String mobile) {
        String ruleMobile = "^[1][3,4,5,7,8][0-9]{9}$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(ruleMobile);
        //正则表达式的匹配器
        Matcher m = p.matcher(mobile);
        //进行正则匹配
        return m.matches();
    }

    /**
     * 解析token获取用户名
     */
    public static String analysis(String token){
        try {
            token = token.substring(token.indexOf(".") + 1,token.lastIndexOf("."));
            byte[] bytes = Base64Utils.decodeFromString(token);
            token = new String(bytes, StandardCharsets.UTF_8);
            JSONObject jsonObject = JSONObject.parseObject(token);
            return jsonObject.get("preferred_username").toString();

        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
        return "";
    }

    public static HttpHeaders getJsonUtf8Headers(){
        return getContentTypeHeaders(MediaType.APPLICATION_PROBLEM_JSON_UTF8);
    }

    public static HttpHeaders getJsonHeaders(){
        return getContentTypeHeaders(MediaType.APPLICATION_PROBLEM_JSON);
    }

    public static HttpHeaders getJsonHeaders(String token){
        HttpHeaders jsonHeaders = getJsonHeaders();
        jsonHeaders.set("Authorization",token);
        return jsonHeaders;
    }

    public static HttpHeaders getContentTypeHeaders(MediaType mediaType){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return headers;
    }




}
