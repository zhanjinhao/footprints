package cn.addenda.footprints.cdc.jdbc;

import cn.addenda.footprints.core.util.DruidSQLUtils;
import cn.addenda.footprints.core.util.JdbcSQLUtils;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * CdcSimpleStatement 的作用是监听所有执行sql，并拦截DML
 *
 * @author addenda
 * @since 2022/8/24 17:20
 */
public class CdcSimpleStatement extends AbstractCdcStatement<Statement> {

    public CdcSimpleStatement(Statement delegate, CdcConnection connection) {
        super(delegate, connection);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        assertSql(sql);
        return delegate.executeUpdate(sql);
    }

    @Override
    public void close() throws SQLException {
        delegate.close();
    }

    @Override
    public void cancel() throws SQLException {
        delegate.cancel();
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        assertSql(sql);
        return delegate.execute(sql);
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        assertSql(sql);
        delegate.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        delegate.clearBatch();
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        assertSql(sql);
        return delegate.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        assertSql(sql);
        return delegate.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        assertSql(sql);
        return delegate.executeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        assertSql(sql);
        return delegate.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        assertSql(sql);
        return delegate.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        assertSql(sql);
        return delegate.execute(sql, columnNames);
    }

    @Override
    public long[] executeLargeBatch() throws SQLException {
        return delegate.executeLargeBatch();
    }

    @Override
    public long executeLargeUpdate(String sql) throws SQLException {
        assertSql(sql);
        return delegate.executeLargeUpdate(sql);
    }

    @Override
    public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        assertSql(sql);
        return delegate.executeLargeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
        assertSql(sql);
        return delegate.executeLargeUpdate(sql, columnIndexes);
    }

    @Override
    public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException {
        assertSql(sql);
        return delegate.executeLargeUpdate(sql, columnNames);
    }

    private void assertSql(String sql) {
        if (JdbcSQLUtils.isSelect(sql)) {
            return;
        }
        String tableName = DruidSQLUtils.extractDmlTableName(sql);
        if (connection.getCdcDataSource().tableContains(tableName)) {
            throw new CdcException(tableName + " has being registered as cdc table, please use java.sql.PreparedStatement to execute dml sql! ");
        }
    }

}
