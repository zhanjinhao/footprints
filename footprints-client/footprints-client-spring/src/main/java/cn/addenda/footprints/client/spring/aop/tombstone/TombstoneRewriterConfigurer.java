package cn.addenda.footprints.client.spring.aop.tombstone;

import cn.addenda.footprints.client.spring.aop.NamedConfigurer;
import cn.addenda.footprints.core.convertor.DefaultDataConvertorRegistry;
import cn.addenda.footprints.core.interceptor.tombstone.DruidTombstoneSqlRewriter;
import cn.addenda.footprints.core.interceptor.tombstone.TombstoneSqlRewriter;
import lombok.Getter;

/**
 * @author addenda
 * @since 2023/6/14 21:26
 */
public class TombstoneRewriterConfigurer implements NamedConfigurer {

    @Getter
    private final TombstoneSqlRewriter tombstoneSqlRewriter;

    public TombstoneRewriterConfigurer() {
        this.tombstoneSqlRewriter = new DruidTombstoneSqlRewriter(null, null, new DefaultDataConvertorRegistry());
    }

}
