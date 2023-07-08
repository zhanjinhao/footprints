package cn.addenda.footsprints.core.interceptor.dynamicsql;

import cn.addenda.footprints.core.convertor.DefaultDataConvertorRegistry;
import cn.addenda.footprints.core.interceptor.dynamicsql.DruidDynamicSQLRewriter;
import cn.addenda.footprints.core.interceptor.dynamicsql.DynamicSQLRewriter;
import cn.addenda.footprints.core.util.DruidSQLUtils;
import cn.addenda.footprints.core.visitor.item.Item;
import cn.addenda.footprints.core.visitor.item.UpdateItemMode;
import cn.addenda.footsprints.core.SqlReader;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author addenda
 * @since 2023/5/13 12:41
 */
public class DynamicSQLAssemblerUpdateAddItemTest {

    private static String[] sqls = new String[]{
    };

    @Test
    public void test1() {
        String[] read = SqlReader.read("src/test/resources/cn/addenda/footprints/core/interceptor/dynamic/sqlassemblerupdateadditem.test", sqls);
        for (int line = 0; line < read.length; line++) {
            String sql = read[line];
            String source = sql;
            int i = source.lastIndexOf(";");
            sql = source.substring(0, i);
            String expect = source.substring(i + 1);
            List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DbType.mysql);
            if (sqlStatements.size() == 0) {
                continue;
            }
            System.out.println(line + " : ------------------------------------------------------------------------------------");
            DynamicSQLRewriter druidDynamicSQLAssembler = new DruidDynamicSQLRewriter(new DefaultDataConvertorRegistry());
            Item item = new Item("if_del", 0);
            String s = druidDynamicSQLAssembler.updateAddItem(DruidSQLUtils.toLowerCaseSQL(sqlStatements.get(0)),
                    null, item, UpdateItemMode.NOT_NULL);
            sqlStatements = SQLUtils.parseStatements(s, DbType.mysql);
            List<SQLStatement> expectSqlStatements = SQLUtils.parseStatements(expect, DbType.mysql);
            Assert.assertEquals(expectSqlStatements.get(0), sqlStatements.get(0));

        }
    }

}
