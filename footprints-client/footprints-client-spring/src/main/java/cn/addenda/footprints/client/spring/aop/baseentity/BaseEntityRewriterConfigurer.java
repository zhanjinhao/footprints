package cn.addenda.footprints.client.spring.aop.baseentity;

import cn.addenda.footprints.client.spring.aop.NamedConfigurer;
import cn.addenda.footprints.core.convertor.DefaultDataConvertorRegistry;
import cn.addenda.footprints.core.interceptor.baseentity.BaseEntityRewriter;
import cn.addenda.footprints.core.interceptor.baseentity.DefaultBaseEntitySource;
import cn.addenda.footprints.core.interceptor.baseentity.DruidBaseEntityRewriter;
import lombok.Getter;

/**
 * @author addenda
 * @since 2023/6/11 20:59
 */
public class BaseEntityRewriterConfigurer implements NamedConfigurer {

    @Getter
    private final BaseEntityRewriter baseEntityRewriter;

    public BaseEntityRewriterConfigurer() {
        this.baseEntityRewriter = new DruidBaseEntityRewriter(null, null, new DefaultBaseEntitySource(), new DefaultDataConvertorRegistry());
    }

}
