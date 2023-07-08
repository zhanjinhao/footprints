package cn.addenda.footprints.core.visitor.item;

import cn.addenda.footprints.core.FootprintsException;
import cn.addenda.footprints.core.convertor.DataConvertorRegistry;
import cn.addenda.footprints.core.util.DruidSQLUtils;
import cn.addenda.footprints.core.visitor.SQLBoundVisitor;
import cn.addenda.footprints.core.visitor.ViewToTableVisitor;
import cn.addenda.footprints.core.util.ArrayUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;

import java.util.List;

/**
 * - select：增加result column
 * - update: set
 * - insert: values 或 duplicateKeyUpdate
 *
 * @author addenda
 * @since 2023/5/17 18:12
 */
public abstract class AbstractAddItemVisitor<T extends SQLStatement, R> extends SQLBoundVisitor<T> {

    private R result;

    protected final List<String> included;

    protected final List<String> notIncluded;

    protected final DataConvertorRegistry dataConvertorRegistry;

    protected AbstractAddItemVisitor(String sql, List<String> included, List<String> notIncluded, DataConvertorRegistry dataConvertorRegistry) {
        super(sql);
        this.included = included;
        this.notIncluded = notIncluded == null ? ArrayUtils.asArrayList("dual") : notIncluded;
        this.dataConvertorRegistry = dataConvertorRegistry;
    }

    protected AbstractAddItemVisitor(T sqlStatement, List<String> included, List<String> notIncluded, DataConvertorRegistry dataConvertorRegistry) {
        super(sqlStatement);
        this.included = included;
        this.notIncluded = notIncluded == null ? ArrayUtils.asArrayList("dual") : notIncluded;
        this.dataConvertorRegistry = dataConvertorRegistry;
    }

    @Override
    public T visitAndOutputAst() {
        sqlStatement.accept(ViewToTableVisitor.getInstance());
        sqlStatement.accept(this);
        return sqlStatement;
    }

    public R getResult() {
        return result;
    }

    protected void setResult(R result) {
        this.result = result;
    }


    /**
     * @return 是否注入item
     */
    protected boolean checkItemNameExists(SQLObject x, List<SQLExpr> columnList, String itemName, boolean reportItemNameExists) {
        for (SQLExpr column : columnList) {
            if (DruidSQLUtils.toLowerCaseSQL(column).equalsIgnoreCase(itemName)) {
                if (reportItemNameExists) {
                    String msg = String.format("SQLObject：[%s]种已经存在[%s]，无法新增！", DruidSQLUtils.toLowerCaseSQL(x), itemName);
                    throw new FootprintsException(msg);
                } else {
                    // 不能新增，否则SQL执行时会报错
                    return true;
                }
            }
        }
        return false;
    }

    protected SQLExpr newItemBinaryOpExpr(String itemName, Object itemValue) {
        SQLBinaryOpExpr sqlBinaryOpExpr = new SQLBinaryOpExpr();
        sqlBinaryOpExpr.setLeft(new SQLIdentifierExpr(itemName));
        sqlBinaryOpExpr.setOperator(SQLBinaryOperator.Equality);
        sqlBinaryOpExpr.setRight(dataConvertorRegistry.parse(itemValue));
        return sqlBinaryOpExpr;
    }

    @Override
    public String toString() {
        return "AbstractAddItemVisitor{" +
                ", included=" + included +
                ", notIncluded=" + notIncluded +
                "} " + super.toString();
    }
}
