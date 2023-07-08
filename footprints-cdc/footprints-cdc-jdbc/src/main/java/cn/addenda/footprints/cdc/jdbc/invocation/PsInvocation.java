package cn.addenda.footprints.cdc.jdbc.invocation;

import java.sql.SQLException;

/**
 * @author addenda
 * @since 2022/8/27 17:01
 */
@FunctionalInterface
public interface PsInvocation<R> {

    R invoke() throws SQLException;

}
