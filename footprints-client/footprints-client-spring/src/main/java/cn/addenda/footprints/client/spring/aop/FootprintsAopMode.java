package cn.addenda.footprints.client.spring.aop;

/**
 * @author addenda
 * @since 2023/6/12 19:27
 */
public enum FootprintsAopMode {

    /**
     * 仅开启AOP配置BaseEntity
     */
    ONLY_CONFIG,

    /**
     * 代理DataSource并开启AOP配置BaseEntity
     */
    PROXY_CONFIG
}
