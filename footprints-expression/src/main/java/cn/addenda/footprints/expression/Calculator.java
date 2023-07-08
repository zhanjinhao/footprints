package cn.addenda.footprints.expression;

import com.alibaba.druid.sql.ast.SQLExpr;

/**
 * @author addenda
 * @since 2023/6/17 20:08
 */
public interface Calculator {

    boolean isLiteral(SQLExpr sqlObject);

    boolean computable(SQLExpr sqlObject);

    Object calculate(SQLExpr sqlObject);

}
