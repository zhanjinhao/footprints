package cn.addenda.footprints.client.spring.aop;

import cn.addenda.footprints.client.annotation.ConfigPropagation;
import cn.addenda.footprints.client.constant.Propagation;
import cn.addenda.footprints.client.spring.util.SpringAnnotationUtils;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.Method;

/**
 * @author addenda
 * @since 2023/6/11 16:26
 */
public abstract class AbstractFootprintsSpringInterceptor implements MethodInterceptor {

    protected Propagation extract(Method method, Class<?> aClass) {
        ConfigPropagation configPropagation = SpringAnnotationUtils.extractAnnotation(method, aClass, ConfigPropagation.class);
        Propagation propagation = Propagation.NEW;
        if (configPropagation != null) {
            propagation = configPropagation.value();
        }

        return propagation;
    }

}
