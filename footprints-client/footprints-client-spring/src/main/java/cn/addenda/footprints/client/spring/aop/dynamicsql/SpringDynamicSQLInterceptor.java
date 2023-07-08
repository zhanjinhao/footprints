package cn.addenda.footprints.client.spring.aop.dynamicsql;

import cn.addenda.footprints.client.annotation.*;
import cn.addenda.footprints.client.constant.Propagation;
import cn.addenda.footprints.client.spring.aop.AbstractFootprintsSpringInterceptor;
import cn.addenda.footprints.client.spring.util.SpringAnnotationUtils;
import cn.addenda.footprints.client.utils.ConfigContextUtils;
import cn.addenda.footprints.core.convertor.DataConvertorRegistry;
import cn.addenda.footprints.core.interceptor.dynamicsql.DynamicSQLContext;
import cn.addenda.footprints.core.util.ExceptionUtil;
import lombok.Setter;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author addenda
 * @since 2022/9/29 13:51
 */
public class SpringDynamicSQLInterceptor extends AbstractFootprintsSpringInterceptor {

    @Setter
    private DataConvertorRegistry dataConvertorRegistry;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> aClass = invocation.getThis().getClass();
        Method method = invocation.getMethod();
        Propagation propagation = extract(method, aClass);

        ConfigContextUtils.pushDynamicSQL(propagation);
        try {
            ConfigContextUtils.configDynamicSQL(propagation, dataConvertorRegistry,
                    SpringAnnotationUtils.extractAnnotation(method, aClass, DynamicConditions.class),
                    SpringAnnotationUtils.extractAnnotation(method, aClass, ConfigJoinUseSubQuery.class),
                    SpringAnnotationUtils.extractAnnotation(method, aClass, DynamicItems.class),
                    SpringAnnotationUtils.extractAnnotation(method, aClass, ConfigDupThenNew.class),
                    SpringAnnotationUtils.extractAnnotation(method, aClass, ConfigDuplicateKeyUpdate.class),
                    SpringAnnotationUtils.extractAnnotation(method, aClass, ConfigUpdateItemMode.class),
                    SpringAnnotationUtils.extractAnnotation(method, aClass, ConfigInsertSelectAddItemMode.class));
            return invocation.proceed();
        } catch (Throwable throwable) {
            throw ExceptionUtil.unwrapThrowable(throwable);
        } finally {
            DynamicSQLContext.pop();
        }
    }

}
