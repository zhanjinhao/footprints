package cn.addenda.footprints.cdc.jdbc;

import cn.addenda.footprints.cdc.jdbc.sql.SqlHelper;
import cn.addenda.footprints.core.convertor.DataConvertorRegistry;
import cn.addenda.footprints.core.interceptor.WrapperAdapter;
import cn.addenda.footprints.expression.Calculator;
import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author addenda
 * @since 2022/8/24 17:03
 */
public class CdcDataSource extends WrapperAdapter implements DataSource {

    private final Map<String, TableConfig> tableMetaData = new HashMap<>(4);

    private final DataSource delegate;

    @Setter
    @Getter
    private DataConvertorRegistry dataConvertorRegistry;

    @Setter
    @Getter
    private Calculator calculator;

    @Getter
    private SqlHelper sqlHelper;

    public CdcDataSource(DataSource delegate, DataConvertorRegistry dataConvertorRegistry, Calculator calculator) {
        this.delegate = delegate;
        this.dataConvertorRegistry = dataConvertorRegistry;
        this.calculator = calculator;
        this.sqlHelper = new SqlHelper(dataConvertorRegistry, calculator);
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = delegate.getConnection();
        return new CdcConnection(connection, this);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Connection connection = delegate.getConnection(username, password);
        return new CdcConnection(connection, this);
    }

    protected PrintWriter logWriter = new PrintWriter(System.out);

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return logWriter;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.logWriter = out;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setTableMetaData(String tableMetaDataStr) {
        String[] split = tableMetaDataStr.split(";");
        if (split.length == 0) {
            return;
        }
        for (String table : split) {
            StringBuilder tableName = new StringBuilder();
            StringBuilder keyColumn = new StringBuilder();
            List<String> cdcModeList = new ArrayList<>();
            boolean tableNameFg = true;
            for (int i = 0; i < table.length(); i++) {
                char c = table.charAt(i);
                if ('[' == c) {
                    tableNameFg = false;
                    continue;
                }

                if (tableNameFg) {
                    tableName.append(c);
                    continue;
                }

                if (']' != c) {
                    keyColumn.append(c);
                } else {
                    String modesStr = table.substring(i + 1);
                    if (modesStr.length() == 0) {
                        cdcModeList.add(TableConfig.CM_ROW);
                    } else {
                        String[] modes = modesStr.split(",");
                        cdcModeList.addAll(Arrays.asList(modes));
                        break;
                    }
                }
            }

            tableMetaData.put(tableName.toString(),
                    new TableConfig(tableName.toString(), keyColumn.toString(), cdcModeList));
        }
    }

    public boolean tableContains(String tableName) {
        return tableMetaData.containsKey(tableName);
    }

    public TableConfig getTableConfig(String tableName) {
        return tableMetaData.get(tableName);
    }

}
