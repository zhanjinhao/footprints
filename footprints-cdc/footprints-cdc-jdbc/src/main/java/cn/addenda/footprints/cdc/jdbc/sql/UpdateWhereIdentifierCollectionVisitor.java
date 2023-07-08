package cn.addenda.footprints.cdc.jdbc.sql;

import cn.addenda.footprints.core.visitor.identifier.AbstractIdentifierVisitor;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author addenda
 * @since 2023/6/23 19:37
 */
public class UpdateWhereIdentifierCollectionVisitor extends AbstractIdentifierVisitor {

    @Getter
    private final Set<String> identifierSet = new HashSet<>();

    public UpdateWhereIdentifierCollectionVisitor(String sql) {
        super(sql, null);
    }

    public UpdateWhereIdentifierCollectionVisitor(SQLStatement sql) {
        super(sql, null);
    }

    @Override
    public SQLStatement visitAndOutputAst() {
        sqlStatement.accept(this);
        return sqlStatement;
    }

    @Override
    public boolean visit(SQLExprTableSource x) {
        return false;
    }

    @Override
    public boolean visit(SQLUpdateSetItem x) {
        return false;
    }

    @Override
    public void endVisit(MySqlUpdateStatement x) {
        List<String> pop = identifierListStack.pop();
        identifierSet.addAll(pop);
    }

}
