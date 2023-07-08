package cn.addenda.footprints.client.spring.aop.sqlcheck;

import cn.addenda.footprints.client.annotation.*;
import cn.addenda.footprints.client.constant.Propagation;
import cn.addenda.footprints.client.spring.aop.AbstractFootprintsSpringInterceptor;
import cn.addenda.footprints.client.spring.util.SpringAnnotationUtils;
import cn.addenda.footprints.client.utils.ConfigContextUtils;
import cn.addenda.footprints.core.interceptor.sqlcheck.SQLCheckContext;
import cn.addenda.footprints.core.util.ExceptionUtil;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author addenda
 * @since 2022/9/29 13:51
 */
public class SpringSQLCheckInterceptor extends AbstractFootprintsSpringInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> aClass = invocation.getThis().getClass();
        Method method = invocation.getMethod();
        Propagation propagation = extract(method, aClass);

        ConfigContextUtils.pushSQLCheck(propagation);
        try {
            ConfigContextUtils.configSQLCheck(propagation,
                    SpringAnnotationUtils.extractAnnotation(method, aClass, UnCheckAllColumn.class),
                    SpringAnnotationUtils.extractAnnotation(method, aClass, UnCheckExactIdentifier.class),
                    SpringAnnotationUtils.extractAnnotation(method, aClass, UnCheckDmlCondition.class));

            return invocation.proceed();
        } catch (Throwable throwable) {
            throw ExceptionUtil.unwrapThrowable(throwable);
        } finally {
            SQLCheckContext.pop();
        }
    }

}
