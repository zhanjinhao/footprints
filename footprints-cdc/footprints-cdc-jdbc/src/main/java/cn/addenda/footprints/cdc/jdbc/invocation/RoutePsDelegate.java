package cn.addenda.footprints.cdc.jdbc.invocation;

import cn.addenda.footprints.cdc.jdbc.CdcConnection;
import cn.addenda.footprints.cdc.jdbc.CdcException;
import cn.addenda.footprints.cdc.jdbc.TableConfig;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author addenda
 * @since 2022/8/24 22:35
 */
public class RoutePsDelegate implements PsDelegate {

    private final PsDelegate realPsDelegate;

    public RoutePsDelegate(CdcConnection cdcConnection, PreparedStatement ps, TableConfig tableConfig, SQLStatement parameterizedSqlStatement, List<String> executableSqlList) {
        if (parameterizedSqlStatement instanceof MySqlInsertStatement) {
            realPsDelegate = new InsertPsDelegate(cdcConnection, ps, tableConfig, parameterizedSqlStatement, executableSqlList);
        } else if (parameterizedSqlStatement instanceof MySqlUpdateStatement) {
            realPsDelegate = new UpdatePsDelegate(cdcConnection, ps, tableConfig, parameterizedSqlStatement, executableSqlList);
        } else if (parameterizedSqlStatement instanceof MySqlDeleteStatement) {
            realPsDelegate = new DeletePsDelegate(cdcConnection, ps, tableConfig, parameterizedSqlStatement, executableSqlList);
        } else {
            throw new CdcException("Error, only support delete, insert, update sql. ");
        }
    }

    @Override
    public <T> T execute(PsInvocation<T> pi) throws SQLException {
        return realPsDelegate.execute(pi);
    }

}
