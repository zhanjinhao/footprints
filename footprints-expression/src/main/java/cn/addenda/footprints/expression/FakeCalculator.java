package cn.addenda.footprints.expression;

import com.alibaba.druid.sql.ast.SQLExpr;

/**
 * @author addenda
 * @since 2023/6/23 19:49
 */
public class FakeCalculator implements Calculator {
    @Override
    public boolean isLiteral(SQLExpr sqlObject) {
        return false;
    }

    @Override
    public boolean computable(SQLExpr sqlObject) {
        return false;
    }

    @Override
    public Object calculate(SQLExpr sqlObject) {
        return null;
    }
}
