package cn.addenda.footprints.core.interceptor.sqlcheck;


import cn.addenda.footprints.core.visitor.condition.DmlConditionExistsVisitor;
import cn.addenda.footprints.core.visitor.identifier.ExactIdentifierVisitor;
import cn.addenda.footprints.core.visitor.identifier.SelectItemStarExistsVisitor;

/**
 * @author addenda
 * @since 2023/5/7 19:58
 */
public class DruidSQLChecker implements SQLChecker {

    @Override
    public boolean exactIdentifier(String sql) {
        ExactIdentifierVisitor exactIdentifierVisitor = new ExactIdentifierVisitor(sql);
        exactIdentifierVisitor.visit();
        return exactIdentifierVisitor.isExact();
    }

    @Override
    public boolean allColumnExists(String sql) {
        SelectItemStarExistsVisitor selectItemStarExistsVisitor = new SelectItemStarExistsVisitor(sql);
        selectItemStarExistsVisitor.visit();
        return selectItemStarExistsVisitor.isExists();
    }

    @Override
    public boolean dmlConditionExists(String sql) {
        DmlConditionExistsVisitor dmlConditionExistsVisitor = new DmlConditionExistsVisitor(sql);
        dmlConditionExistsVisitor.visit();
        return dmlConditionExistsVisitor.isExists();
    }

}
