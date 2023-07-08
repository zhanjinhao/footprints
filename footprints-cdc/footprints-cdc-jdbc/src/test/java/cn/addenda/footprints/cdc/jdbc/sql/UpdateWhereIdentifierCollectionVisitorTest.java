package cn.addenda.footprints.cdc.jdbc.sql;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author addenda
 * @since 2023/6/23 19:50
 */
public class UpdateWhereIdentifierCollectionVisitorTest {

    @Test
    public void test1() {
        String sql = "update A set age = 1 where name = 'abc'";
        UpdateWhereIdentifierCollectionVisitor visitor = new UpdateWhereIdentifierCollectionVisitor(sql);
        visitor.visit();
        Assert.assertEquals("[name]", visitor.getIdentifierSet().toString());
    }

    @Test
    public void test2() {
        String sql = "update A set age = 1 where id in (select outer_id from B where name = 'abc')";
        UpdateWhereIdentifierCollectionVisitor visitor = new UpdateWhereIdentifierCollectionVisitor(sql);
        visitor.visit();
        Assert.assertEquals("[id]", visitor.getIdentifierSet().toString());
    }

}
