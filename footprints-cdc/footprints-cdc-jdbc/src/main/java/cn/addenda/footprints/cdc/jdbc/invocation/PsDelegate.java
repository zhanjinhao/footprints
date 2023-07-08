package cn.addenda.footprints.cdc.jdbc.invocation;

import java.sql.SQLException;

/**
 * @author addenda
 * @since 2022/9/24 10:03
 */
public interface PsDelegate {

    <T> T execute(PsInvocation<T> pi) throws SQLException;

}
