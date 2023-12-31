package cn.addenda.footprints.client.spring.aop.tombstone;

import cn.addenda.footprints.client.annotation.*;
import cn.addenda.footprints.client.spring.util.SpringAnnotationUtils;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * @author addenda
 * @since 2022/9/29 13:52
 */
public class TombstoneAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    @Override
    public Pointcut getPointcut() {
        return new TombstonePointcut();
    }

    public static class TombstonePointcut extends StaticMethodMatcherPointcut {

        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            return SpringAnnotationUtils.annotationExists(method, targetClass, ConfigPropagation.class)
                    || SpringAnnotationUtils.annotationExists(method, targetClass, DisableTombstone.class)
                    || SpringAnnotationUtils.annotationExists(method, targetClass, ConfigJoinUseSubQuery.class);
        }

    }

}
