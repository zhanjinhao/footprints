package cn.addenda.footprints.cdc.jdbc.sql;

import cn.addenda.footprints.core.convertor.DataConvertorRegistry;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import lombok.Setter;

import java.util.List;

/**
 * @author addenda
 * @since 2023/6/15 21:23
 */
public class ExecutableSQLVisitor extends MySqlOutputVisitor {

    @Setter
    private List<Object> parameterList;

    private final DataConvertorRegistry dataConvertorRegistry;

    @Setter
    private int i = 0;

    public ExecutableSQLVisitor(Appendable appender, DataConvertorRegistry dataConvertorRegistry) {
        super(appender);
        this.dataConvertorRegistry = dataConvertorRegistry;
    }

    @Override
    public boolean visit(SQLVariantRefExpr x) {
        String format = dataConvertorRegistry.format(parameterList.get(i));
        i++;
        print0(format);
        return true;
    }

}
