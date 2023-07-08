package cn.addenda.footprints.client.spring.aop.sqlcheck;

import cn.addenda.footprints.client.spring.aop.NamedConfigurer;
import cn.addenda.footprints.core.interceptor.sqlcheck.DruidSQLChecker;
import cn.addenda.footprints.core.interceptor.sqlcheck.SQLChecker;
import lombok.Getter;

/**
 * @author addenda
 * @since 2023/6/14 21:03
 */
public class SQLCheckConfigurer implements NamedConfigurer {

    @Getter
    private SQLChecker sqlChecker;

    public SQLCheckConfigurer() {
        this.sqlChecker = new DruidSQLChecker();
    }

}
