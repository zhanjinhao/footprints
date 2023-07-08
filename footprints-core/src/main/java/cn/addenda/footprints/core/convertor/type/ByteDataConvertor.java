package cn.addenda.footprints.core.convertor.type;

import cn.addenda.footprints.core.convertor.sqlexpr.SQLTinyIntExprDataConvertor;
import com.alibaba.druid.sql.ast.expr.SQLTinyIntExpr;

/**
 * @author addenda
 * @since 2022/9/10 16:57
 */
public class ByteDataConvertor extends AbstractTypeDataConvertor<Byte, SQLTinyIntExpr> {

    public ByteDataConvertor() {
        super(new SQLTinyIntExprDataConvertor());
    }

    @Override
    public SQLTinyIntExpr doParse(Object obj) {
        return new SQLTinyIntExpr((Byte) obj);
    }

}
