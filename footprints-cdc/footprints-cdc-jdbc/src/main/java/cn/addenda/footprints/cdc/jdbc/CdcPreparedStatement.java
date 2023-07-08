package cn.addenda.footprints.cdc.jdbc;

import cn.addenda.footprints.cdc.jdbc.invocation.PsDelegate;
import cn.addenda.footprints.cdc.jdbc.invocation.RoutePsDelegate;
import cn.addenda.footprints.cdc.jdbc.sql.ExecutableSQLVisitor;
import cn.addenda.footprints.core.convertor.DataConvertorRegistry;
import cn.addenda.footprints.core.util.ArrayUtils;
import cn.addenda.footprints.core.util.DruidSQLUtils;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author addenda
 * @since 2022/4/13 12:00
 */
public class CdcPreparedStatement extends AbstractCdcStatement<PreparedStatement> implements PreparedStatement {

    private final List<String> executableSqlList = new ArrayList<>();

    private final String tableName;
    private final String parameterizedSql;
    private final SQLStatement parameterizedSqlStatement;
    private final int parameterCount;
    private List<Object> parameterList;
    private final StringBuilder stringBuilder;
    private final ExecutableSQLVisitor executableSQLVisitor;


    /**
     * CdcPreparedStatement能被创建的前提:
     * 1、sql是dml；
     * 2、tableName是被注册为拦截的表。
     *
     * @param delegate
     * @param connection
     * @param tableName
     * @param parameterizedSql
     */
    public CdcPreparedStatement(PreparedStatement delegate, CdcConnection connection, String tableName, String parameterizedSql) {
        super(delegate, connection);
        this.tableName = tableName;
        this.parameterizedSql = parameterizedSql;
        this.parameterizedSqlStatement = SQLUtils.parseSingleStatement(parameterizedSql, DbType.mysql);
        this.parameterCount = DruidSQLUtils.calculateParameterCount(parameterizedSql);
        this.parameterList = newParameterList();
        this.stringBuilder = new StringBuilder();
        this.executableSQLVisitor = new ExecutableSQLVisitor(stringBuilder, connection.getCdcDataSource().getDataConvertorRegistry());
    }

    private PsDelegate createPsDelegate(List<String> executableSqlList) {
        return new RoutePsDelegate(connection, delegate,
                connection.getCdcDataSource().getTableConfig(tableName), parameterizedSqlStatement, executableSqlList);
    }

    @Override
    public void close() throws SQLException {
        delegate.close();
        parameterList.clear();
    }

    @Override
    public void cancel() throws SQLException {
        delegate.cancel();
        parameterList.clear();
    }

    @Override
    public boolean execute() throws SQLException {
        return createPsDelegate(ArrayUtils.asArrayList(getExecutableSql())).execute(delegate::execute);
    }

    @Override
    public int executeUpdate() throws SQLException {
        return createPsDelegate(ArrayUtils.asArrayList(getExecutableSql())).execute(delegate::executeUpdate);
    }

    @Override
    public long executeLargeUpdate() throws SQLException {
        return createPsDelegate(ArrayUtils.asArrayList(getExecutableSql())).execute(delegate::executeLargeUpdate);
    }

    // ---------------------
    //  下面的方法用于批量更新
    // ---------------------

    @Override
    public void addBatch() throws SQLException {
        executableSqlList.add(getExecutableSql());
        delegate.addBatch();
    }

    @Override
    public void clearBatch() throws SQLException {
        parameterList = newParameterList();
        executableSqlList.clear();
        delegate.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return createPsDelegate(executableSqlList).execute(delegate::executeBatch);
    }

    @Override
    public long[] executeLargeBatch() throws SQLException {
        return createPsDelegate(executableSqlList).execute(delegate::executeLargeBatch);
    }

    private List<Object> newParameterList() {
        List<Object> list = new ArrayList<>(parameterCount);
        for (int i = 0; i < parameterCount; i++) {
            list.add(null);
        }
        return list;
    }

    // ---------------------
    //  下面的方法需要拦截参数
    // ---------------------

    @Override
    public void clearParameters() throws SQLException {
        parameterList = newParameterList();
        delegate.clearParameters();
    }

    @Override
    public void setObject(int parameterIndex, Object x, SQLType targetSqlType) throws SQLException {
        assertDataType(x.getClass());
        delegate.setObject(parameterIndex, x, targetSqlType);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        assertDataType(x.getClass());
        delegate.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        delegate.setNull(parameterIndex, sqlType);
        parameterList.set(parameterIndex - 1, null);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        delegate.setBoolean(parameterIndex, x);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        delegate.setByte(parameterIndex, x);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        delegate.setShort(parameterIndex, x);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        delegate.setInt(parameterIndex, x);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        delegate.setLong(parameterIndex, x);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        delegate.setFloat(parameterIndex, x);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        delegate.setDouble(parameterIndex, x);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        delegate.setBigDecimal(parameterIndex, x);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        delegate.setString(parameterIndex, x);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        delegate.setDate(parameterIndex, x);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        delegate.setTimestamp(parameterIndex, x);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        assertDataType(x.getClass());
        delegate.setObject(parameterIndex, x, targetSqlType);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        assertDataType(x.getClass());
        delegate.setObject(parameterIndex, x);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        delegate.setDate(parameterIndex, x, cal);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        delegate.setTime(parameterIndex, x, cal);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        delegate.setTimestamp(parameterIndex, x, cal);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        delegate.setNull(parameterIndex, sqlType, typeName);
        parameterList.set(parameterIndex - 1, null);
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        delegate.setTime(parameterIndex, x);
        parameterList.set(parameterIndex - 1, x);
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        delegate.setNString(parameterIndex, value);
        parameterList.set(parameterIndex - 1, value);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        assertDataType(x.getClass());
        delegate.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        parameterList.set(parameterIndex - 1, x);
    }

    public String getExecutableSql() {
        stringBuilder.setLength(0);
        executableSQLVisitor.setI(0);
        executableSQLVisitor.setParameterList(parameterList);
        parameterizedSqlStatement.accept(executableSQLVisitor);
        return stringBuilder.toString();
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        assertDataType(x.getClass());
        delegate.setBytes(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        assertDataType(x.getClass());
        delegate.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        assertDataType(x.getClass());
        delegate.setUnicodeStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        assertDataType(x.getClass());
        delegate.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        assertDataType(reader.getClass());
        delegate.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        assertDataType(x.getClass());
        delegate.setRef(parameterIndex, x);
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        assertDataType(x.getClass());
        delegate.setBlob(parameterIndex, x);
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        assertDataType(x.getClass());
        delegate.setClob(parameterIndex, x);
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        assertDataType(x.getClass());
        delegate.setArray(parameterIndex, x);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        assertDataType(x.getClass());
        delegate.setURL(parameterIndex, x);
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        assertDataType(x.getClass());
        delegate.setRowId(parameterIndex, x);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        assertDataType(value.getClass());
        delegate.setNCharacterStream(parameterIndex, value, length);
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        assertDataType(value.getClass());
        delegate.setNClob(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        assertDataType(reader.getClass());
        delegate.setClob(parameterIndex, reader, length);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        assertDataType(inputStream.getClass());
        delegate.setBlob(parameterIndex, inputStream, length);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        assertDataType(reader.getClass());
        delegate.setNClob(parameterIndex, reader, length);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        assertDataType(xmlObject.getClass());
        delegate.setSQLXML(parameterIndex, xmlObject);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        assertDataType(x.getClass());
        delegate.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        assertDataType(x.getClass());
        delegate.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        assertDataType(reader.getClass());
        delegate.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        assertDataType(x.getClass());
        delegate.setAsciiStream(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        assertDataType(x.getClass());
        delegate.setBinaryStream(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        assertDataType(reader.getClass());
        delegate.setCharacterStream(parameterIndex, reader);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        assertDataType(value.getClass());
        delegate.setNCharacterStream(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        assertDataType(reader.getClass());
        delegate.setClob(parameterIndex, reader);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        assertDataType(inputStream.getClass());
        delegate.setBlob(parameterIndex, inputStream);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        assertDataType(reader.getClass());
        delegate.setNClob(parameterIndex, reader);
    }

    private CdcDataSource getCdcDataSource() {
        return connection.getCdcDataSource();
    }

    private void assertDataType(Class<?> clazz) {
        DataConvertorRegistry dataConvertorRegistry = getCdcDataSource().getDataConvertorRegistry();
        if (!dataConvertorRegistry.typeAvailable(clazz)) {
            throw new CdcException("不支持的数据类型，当前类型处理中心是：" + dataConvertorRegistry.getClass().getSimpleName() + "。");
        }
    }

    // -------------------
    //  下面的方法与CDC无关
    // -------------------

    @Override
    public ResultSet executeQuery() throws SQLException {
        return delegate.executeQuery();
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return delegate.getParameterMetaData();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return delegate.getMetaData();
    }

    // ---------------------------------------
    //  下面的方法不能在 PrepareStatement 中调用
    // ---------------------------------------

    @Override
    public void addBatch(String sql) throws SQLException {
        delegate.addBatch(sql);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return delegate.executeUpdate(sql);
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return delegate.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return delegate.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return delegate.executeUpdate(sql, columnNames);
    }

    @Override
    public long executeLargeUpdate(String sql) throws SQLException {
        return delegate.executeLargeUpdate(sql);
    }

    @Override
    public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return delegate.executeLargeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return delegate.executeLargeUpdate(sql, columnIndexes);
    }

    @Override
    public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException {
        return delegate.executeLargeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        return delegate.execute(sql);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return delegate.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return delegate.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return delegate.execute(sql, columnNames);
    }

}
