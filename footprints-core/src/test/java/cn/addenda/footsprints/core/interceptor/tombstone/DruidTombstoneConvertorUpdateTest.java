package cn.addenda.footsprints.core.interceptor.tombstone;

import cn.addenda.footprints.core.interceptor.tombstone.DruidTombstoneSqlRewriter;
import cn.addenda.footprints.core.util.DruidSQLUtils;
import cn.addenda.footsprints.core.SqlReader;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author addenda
 * @since 2023/5/10 20:39
 */
public class DruidTombstoneConvertorUpdateTest {

    private static String[] sqls = new String[]{
    };

    @Test
    public void test1() {
        for (String sql : SqlReader.read("src/test/resources/cn/addenda/footprints/core/interceptor/tombstone/tombstoneupdate.test", sqls)) {
            String source = sql;
            int i = source.lastIndexOf(";");
            sql = source.substring(0, i);
            String expect = source.substring(i + 1);
            List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DbType.mysql);
            if (sqlStatements.size() == 0) {
                continue;
            }
            System.out.println("------------------------------------------------------------------------------------");
            DruidTombstoneSqlRewriter druidTombstoneSqlRewriter = new DruidTombstoneSqlRewriter();
            String s = druidTombstoneSqlRewriter.rewriteUpdateSql(DruidSQLUtils.toLowerCaseSQL(sqlStatements.get(0)));
            sqlStatements = SQLUtils.parseStatements(s, DbType.mysql);
            List<SQLStatement> expectSqlStatements = SQLUtils.parseStatements(expect, DbType.mysql);
            Assert.assertEquals(expectSqlStatements.get(0), sqlStatements.get(0));

        }
    }

}
