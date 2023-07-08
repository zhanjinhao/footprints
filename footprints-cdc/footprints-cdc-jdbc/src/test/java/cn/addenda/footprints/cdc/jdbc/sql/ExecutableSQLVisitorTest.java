package cn.addenda.footprints.cdc.jdbc.sql;

import cn.addenda.footprints.core.convertor.DefaultDataConvertorRegistry;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author addenda
 * @since 2023/6/15 21:42
 */
public class ExecutableSQLVisitorTest {

    @Test
    public void test1() {
        String sql = "insert into t_cdc_test(long_d, int_d, string_d, date_d, time_d, datetime_d, float_d, double_d) " +
                "values(?,?,?,?,?,?,?,?)";
        StringBuilder out = new StringBuilder();
        List<Object> objectList = new ArrayList<>();
        objectList.add(1L);
        objectList.add(123);
        objectList.add("'abc\\''");
        objectList.add(LocalDate.now());
        objectList.add(new java.sql.Time(new Date().getTime()));
        objectList.add(new java.sql.Date(new Date().getTime()));
        objectList.add(1.1f);
        objectList.add(2.2d);

        ExecutableSQLVisitor executableSQLVisitor = new ExecutableSQLVisitor(out, new DefaultDataConvertorRegistry());
        executableSQLVisitor.setParameterList(objectList);

        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DbType.mysql);
        sqlStatements.get(0).accept(executableSQLVisitor);

        System.out.println(out);

    }

}
