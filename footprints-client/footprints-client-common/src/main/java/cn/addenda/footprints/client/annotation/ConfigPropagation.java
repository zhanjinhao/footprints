package cn.addenda.footprints.client.annotation;

import cn.addenda.footprints.client.constant.Propagation;

/**
 * @author addenda
 * @since 2023/6/10 20:36
 */
public @interface ConfigPropagation {

    Propagation value() default Propagation.NEW;

}
