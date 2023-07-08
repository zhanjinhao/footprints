package cn.addenda.footprints.cdc.jdbc.sql;

import cn.addenda.footprints.core.convertor.DataConvertorRegistry;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;

/**
 * @author addenda
 * @since 2023/6/15 21:23
 */
public class StorableSQLVisitor extends MySqlOutputVisitor {

    private final DataConvertorRegistry dataConvertorRegistry;

    public StorableSQLVisitor(Appendable appender, DataConvertorRegistry dataConvertorRegistry) {
        super(appender);
        this.dataConvertorRegistry = dataConvertorRegistry;
    }

    @Override
    public boolean visit(SQLTimestampExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLTimeExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLBooleanExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLRealExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLTinyIntExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLCharExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLNumberExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLHexExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLJSONExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLFloatExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLDateTimeExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLDoubleExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLBigIntExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLDecimalExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLBinaryExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLSmallIntExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLDateExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLNullExpr x) {
        return doVisit(x);
    }

    @Override
    public boolean visit(SQLIntegerExpr x) {
        return doVisit(x);
    }

    private boolean doVisit(Object o) {
        String format = dataConvertorRegistry.format(o);
        print0(format);
        return true;
    }

}
