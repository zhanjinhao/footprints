package cn.addenda.footprints.core.convertor.type;

import cn.addenda.footprints.core.convertor.sqlexpr.SQLNumberExprDataConvertor;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;

/**
 * @author addenda
 * @since 2022/9/10 16:57
 */
public class DoubleDataConvertor extends AbstractTypeDataConvertor<Double, SQLNumberExpr> {

    public DoubleDataConvertor() {
        super(new SQLNumberExprDataConvertor());
    }

    @Override
    public SQLNumberExpr doParse(Object obj) {
        return new SQLNumberExpr((Double) obj);
    }

}
