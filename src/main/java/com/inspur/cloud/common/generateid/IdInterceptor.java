package com.inspur.cloud.common.generateid;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.util.*;

/**
 * @author zhangrui
 */
@Slf4j
@Component
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class
        }),
})
public class IdInterceptor implements Interceptor {

    private static final Set<Class<?>> PRIMITIVE_WRAPPER_TYPE_SET = new HashSet<>();

    static {
        PRIMITIVE_WRAPPER_TYPE_SET.add(Boolean.class);
        PRIMITIVE_WRAPPER_TYPE_SET.add(Byte.class);
        PRIMITIVE_WRAPPER_TYPE_SET.add(Character.class);
        PRIMITIVE_WRAPPER_TYPE_SET.add(Double.class);
        PRIMITIVE_WRAPPER_TYPE_SET.add(Float.class);
        PRIMITIVE_WRAPPER_TYPE_SET.add(Integer.class);
        PRIMITIVE_WRAPPER_TYPE_SET.add(Long.class);
        PRIMITIVE_WRAPPER_TYPE_SET.add(Short.class);
    }

    /**
     * 拦截的方法
     *
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        //args数组对应对象就是上面@Signature注解中args对应的对应类型
        MappedStatement mappedStatement = (MappedStatement) args[0];
        //实体对象
        Object entity = args[1];
        if (SqlCommandType.INSERT.equals(mappedStatement.getSqlCommandType())) {
            final Class<?> objectClass = entity.getClass();
            if (!(objectClass.isPrimitive() || PRIMITIVE_WRAPPER_TYPE_SET.contains(objectClass))) {
                // 获取实体集合
                Set<Object> entitySet = getEntitySet(entity);
                // 批量设置id
                entitySet.forEach(this::process);
            }
        }
        //执行sql
        return invocation.proceed();
    }

    private void process(Object paramObject) {
        ReflectionUtils.doWithFields(paramObject.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);
            IdType type = field.getAnnotation(Id.class).type();
            if (type == IdType.ASSIGN_ID) {
                ReflectionUtils.setField(field, paramObject, IdWorker.getNextId());
            } else if (type == IdType.ASSIGN_UUID) {
                ReflectionUtils.setField(field, paramObject, UUID.randomUUID().toString());
            }
        }, field -> field.isAnnotationPresent(Id.class));
    }

    private Set<Object> getEntitySet(Object object) {
        //
        Set<Object> set = new HashSet<>();
        if (object instanceof Map) {
            //批量插入对象
            Collection values = ((Map) object).values();
            for (Object value : values) {
                if (value instanceof Collection) {
                    set.addAll((Collection) value);
                } else {
                    set.add(value);
                }
            }
        } else {
            //单个插入对象
            set.add(object);
        }
        return set;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
