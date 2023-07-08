package cn.addenda.footprints.client.spring.aop.dynamicsql;

import cn.addenda.footprints.client.spring.aop.NamedConfigurer;
import cn.addenda.footprints.core.convertor.DefaultDataConvertorRegistry;
import cn.addenda.footprints.core.interceptor.dynamicsql.DruidDynamicSQLRewriter;
import cn.addenda.footprints.core.interceptor.dynamicsql.DynamicSQLRewriter;
import lombok.Getter;

/**
 * @author addenda
 * @since 2023/6/13 21:07
 */
public class DynamicSQLRewriterConfigurer implements NamedConfigurer {

    @Getter
    private final DynamicSQLRewriter dynamicSQLRewriter;

    public DynamicSQLRewriterConfigurer() {
        this.dynamicSQLRewriter = new DruidDynamicSQLRewriter(new DefaultDataConvertorRegistry());
    }

}
