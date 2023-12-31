package cn.addenda.footprints.core.convertor.type;

import cn.addenda.footprints.core.convertor.DataConvertorRegistry;
import cn.addenda.footprints.core.convertor.AbstractDataConvertor;
import cn.addenda.footprints.core.util.JdbcSQLUtils;
import cn.addenda.footprints.core.util.ArrayUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;

import java.util.List;

/**
 * @author addenda
 * @since 2022/9/10 16:57
 */
public class CharSequenceDataConvertor extends AbstractDataConvertor<CharSequence, SQLExpr> {

    private final DataConvertorRegistry dataConvertorRegistry;

    public CharSequenceDataConvertor(DataConvertorRegistry dataConvertorRegistry) {
        this.dataConvertorRegistry = dataConvertorRegistry;
    }

    @Override
    public Class<CharSequence> getType() {
        return CharSequence.class;
    }

    @Override
    public String format(Object obj) {
        SQLExpr parse = parse(obj);
        return parse.toString().replace("\\", "\\\\");
    }

    @Override
    public SQLExpr doParse(Object obj) {
        String text = String.valueOf(obj);
        SQLMethodInvokeExpr methodInvokeExpr = extractDateFunction(text);
        if (methodInvokeExpr != null) {
            return methodInvokeExpr;
        }
        return new SQLCharExpr(text);
    }

    @Override
    public SQLExpr doParse(String str) {
        return new SQLCharExpr(str);
    }

    @Override
    public boolean strMatch(String str) {
        return true;
    }

    private static final List<String> dateFunctionList = ArrayUtils.asArrayList("now", "sysdate", "current_timestamp");

    private SQLMethodInvokeExpr extractDateFunction(String text) {
        for (String dateFunction : dateFunctionList) {
            if (text.length() > dateFunction.length() && JdbcSQLUtils.hasPrefix(text, dateFunction)) {
                SQLExpr param = dataConvertorRegistry.parse(Integer.valueOf(text.substring(dateFunction.length() + 1, text.length() - 1)));
                return new SQLMethodInvokeExpr(dateFunction, null, param);
            }
        }
        return null;
    }

}
