package cn.addenda.footprints.core.convertor.type;

import cn.addenda.footprints.core.convertor.sqlexpr.SQLDateExprDataConvertor;
import cn.addenda.footprints.core.util.DateUtils;
import com.alibaba.druid.sql.ast.expr.SQLDateExpr;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.TimeZone;

/**
 * @author addenda
 * @since 2022/9/10 16:57
 */
public class LocalDateDataConvertor extends AbstractTypeDataConvertor<LocalDate, SQLDateExpr> {

    public LocalDateDataConvertor() {
        super(new SQLDateExprDataConvertor(ZoneId.systemDefault()));
    }

    @Override
    public SQLDateExpr doParse(Object obj) {
        return new SQLDateExpr(DateUtils.localDateToDate((LocalDate) obj), TimeZone.getDefault());
    }

}
