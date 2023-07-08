package cn.addenda.footprints.client.spring.aop.dynamicsql;

import cn.addenda.footprints.client.spring.aop.NamedConfigurer;
import cn.addenda.footprints.core.convertor.DataConvertorRegistry;
import cn.addenda.footprints.core.convertor.DefaultDataConvertorRegistry;
import lombok.Getter;

/**
 * @author addenda
 * @since 2022/11/30 19:21
 */
public class DataConvertorRegistryConfigurer implements NamedConfigurer {

    @Getter
    private final DataConvertorRegistry dataConvertorRegistry;

    public DataConvertorRegistryConfigurer() {
        this.dataConvertorRegistry = new DefaultDataConvertorRegistry();
    }

}
