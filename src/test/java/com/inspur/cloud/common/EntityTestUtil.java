package com.inspur.cloud.common;

import org.junit.Assert;
import org.springframework.test.util.ReflectionTestUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 此类为Entity 的测试工具类
 *
 * @author xiaopeiyu
 * @since 2019/8/19
 */
public class EntityTestUtil {

    /**
     * 测试实体中的hashCode/toString/equals 方法
     * <p>
     * 注意：使用该方法时，请确认该类有相应的方法。
     * eg: EntityTestUtil.testEntity(ProductInst.class);
     *
     * @param cls
     * @param <T>
     */
    public static <T> void testEntity(Class<T> cls) {
        // 判断是否是接口
        boolean isInterface = Modifier.isInterface(cls.getModifiers());
        if (isInterface) {
            return;
        }
        // 判断是否是抽象类
        boolean isAbstract = Modifier.isAbstract(cls.getModifiers());
        if (isAbstract) {
            return;
        }
        T t = null;
        T t1 = null;
        try {
            t = cls.newInstance();
            t1 = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (null == t) {
            return;
        }
        ReflectionTestUtils.invokeMethod(t, "hashCode");
        ReflectionTestUtils.invokeMethod(t, "toString");
        Object value = ReflectionTestUtils.invokeMethod(t, "equals", t1);
        //Assert.assertTrue((boolean) value);
        Object object = new Object();
        Object value1 = ReflectionTestUtils.invokeMethod(t, "equals", object);
        //Assert.assertFalse((boolean) value1);
        //覆盖get,set方法
        try {
            Object obj = cls.newInstance();
            Field[] fields = cls.getDeclaredFields();
            for (Field f : fields) {
                //JavaBean属性名要求：前两个字母要么都大写，要么都小写
                //对于首字母是一个单词的情况，要么过滤掉，要么自己拼方法名
                //f.isSynthetic()过滤合成字段
           /* if (f.getName().equals("aLike")
                    || f.isSynthetic()) {
                continue;
            }*/
                PropertyDescriptor pd = new PropertyDescriptor(f.getName(), cls);
                Method get = pd.getReadMethod();
                Method set = pd.getWriteMethod();
                set.invoke(obj, get.invoke(obj));
            }
        }catch (InstantiationException | IllegalAccessException | IntrospectionException | InvocationTargetException e){
            e.printStackTrace();
        }
    }

}
