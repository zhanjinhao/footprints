package cn.addenda.footprints.core.convertor.type;

import cn.addenda.footprints.core.convertor.sqlexpr.SQLDateTimeExprDataConvertor;
import cn.addenda.footprints.core.util.DateUtils;
import com.alibaba.druid.sql.ast.expr.SQLDateTimeExpr;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

/**
 * @author addenda
 * @since 2022/9/10 16:57
 */
public class LocalDateTimeDataConvertor extends AbstractTypeDataConvertor<LocalDateTime, SQLDateTimeExpr> {

    public LocalDateTimeDataConvertor() {
        super(new SQLDateTimeExprDataConvertor(ZoneId.systemDefault()));
    }

    @Override
    public SQLDateTimeExpr doParse(Object obj) {
        return new SQLDateTimeExpr(DateUtils.localDateTimeToDate((LocalDateTime) obj), TimeZone.getDefault());
    }

}
