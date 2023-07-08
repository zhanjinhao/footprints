package cn.addenda.footsprints.core.visitor.identifier;

import cn.addenda.footprints.core.visitor.identifier.IdentifierExistsVisitor;
import cn.addenda.footprints.core.visitor.identifier.SelectItemIdentifierExistsVisitor;
import cn.addenda.footsprints.core.SqlReader;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author addenda
 * @since 2023/5/3 20:55
 */
public class SelectItemIdentifierExistsVisitorTest {

    private static String[] sqls = new String[]{
    };

    @Test
    public void test1() {
        String[] read = SqlReader.read("src/test/resources/cn/addenda/footprints/core/visitor/identifier/selectitemidentifierexists.test", sqls);
        for (int line = 0; line < read.length; line++) {
            String sql = read[line];
            String source = sql;
            int i = source.lastIndexOf(";");
            sql = source.substring(0, i);
            boolean flag = Boolean.parseBoolean(source.substring(i + 1));
            List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DbType.mysql);
            if (sqlStatements.size() == 0) {
                continue;
            }
            System.out.println(line + " : ------------------------------------------------------------------------------------");
            System.out.println();
            IdentifierExistsVisitor identifierExistsVisitor = new SelectItemIdentifierExistsVisitor(sql, "a");
            identifierExistsVisitor.visit();
            boolean exists = identifierExistsVisitor.isExists();
            if (exists == flag) {
                System.out.println(source + " : " + exists + ":" + identifierExistsVisitor.getAmbiguousInfo());
            } else {
                System.err.println(source + " : " + exists + ":" + identifierExistsVisitor.getAmbiguousInfo());
                Assert.assertEquals(flag, exists);
            }
        }
    }

}
