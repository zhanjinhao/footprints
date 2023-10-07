package cn.addenda.footsprints.core.interceptor.tombstone;

import cn.addenda.footprints.core.convertor.DefaultDataConvertorRegistry;
import cn.addenda.footprints.core.interceptor.tombstone.DruidTombstoneSqlRewriter;
import cn.addenda.footprints.core.util.ArrayUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author addenda
 * @since 2023/9/11 15:02
 */
@Slf4j
public class DruidTombstoneConvertorShowCaseTest {

    @Test
    public void testInsert1() {
        DruidTombstoneSqlRewriter druidTombstoneSqlRewriter = new DruidTombstoneSqlRewriter();
        String sql = "insert into score ( SNO, CNO, DEGREE ) values ( 109, '\\\\3-105', 76)";

        String s = druidTombstoneSqlRewriter.rewriteInsertSql(sql, false);

        //  insert into score (SNO, CNO, DEGREE, if_del) values (109, '\\3-105', 76, 0)
        log.info(s);
    }


    @Test
    public void testInsert2() {
        DruidTombstoneSqlRewriter druidTombstoneSqlRewriter = new DruidTombstoneSqlRewriter();
        String sql = "insert  into score set SNO=109, CNO='\\\\3-105', DEGREE=76";

        String s = druidTombstoneSqlRewriter.rewriteInsertSql(sql, false);

        //  insert into score (SNO, CNO, DEGREE, if_del) values (109, '\\3-105', 76, 0)
        log.info(s);
    }


    @Test
    public void testInsert3() {
        DruidTombstoneSqlRewriter druidTombstoneSqlRewriter = new DruidTombstoneSqlRewriter();
        String sql = "insert into t_cdc_test(long_d, int_d, string_d, date_d, time_d, datetime_d, float_d, double_d) "
            + "values (? + 1, ?, replace(?,'a','b'), date_add(?, interval 1 day), ?, now(), ?, ?)";

        String s = druidTombstoneSqlRewriter.rewriteInsertSql(sql, false);

        //  insert into t_cdc_test (long_d, int_d, string_d, date_d, time_d , datetime_d, float_d, double_d, if_del)
        //  values (? + 1, ?, replace(?, 'a', 'b'), date_add(?, interval 1 day), ? , now(), ?, ?, 0)
        log.info(s);
    }


    @Test
    public void testUpdate1() {
        DruidTombstoneSqlRewriter druidTombstoneSqlRewriter = new DruidTombstoneSqlRewriter();
        String sql = "update runoob_tbl set runoob_title=replace( runoob_title , 'c++', 'python' ) , a=?  + 1 , b=?";

        String s = druidTombstoneSqlRewriter.rewriteUpdateSql(sql);

        //  update runoob_tbl set runoob_title = replace(runoob_title, 'c++', 'python'), a = ? + 1, b = ? where runoob_tbl.if_del = 0
        log.info(s);
    }


    @Test
    public void testUpdate2() {
        DruidTombstoneSqlRewriter druidTombstoneSqlRewriter = new DruidTombstoneSqlRewriter();
        String sql = "update runoob_tbl set runoob_title= replace( runoob_title , 'c++', 'python' ) where id in (select outer_id from A)";

        String s = druidTombstoneSqlRewriter.rewriteUpdateSql(sql);

        //  update runoob_tbl set runoob_title = replace(runoob_title, 'c++', 'python') where id in ( select outer_id from A where A.if_del = 0 ) and runoob_tbl.if_del = 0
        log.info(s);
    }

    @Test
    public void testUpdate3() {
        DruidTombstoneSqlRewriter druidTombstoneSqlRewriter = new DruidTombstoneSqlRewriter();
        String sql = "update t_idempotent_storage_center A inner join ("
            + "           select id from t_idempotent_storage_center "
            + "           where `namespace` = ? and `prefix` = ? and `key` = ? and `consume_mode` = ? order by id desc limit 1) B on B.id = A.id "
            + "           set A.`consume_status` = 'EXCEPTION'";

        String s = druidTombstoneSqlRewriter.rewriteUpdateSql(sql);

        //  update t_idempotent_storage_center A inner join (select id
        //                                                 from t_idempotent_storage_center
        //                                                 where `namespace` = ?
        //                                                   and `prefix` = ?
        //                                                   and `key` = ?
        //                                                   and `consume_mode` = ?
        //                                                   and t_idempotent_storage_center.if_del = 0
        //                                                 order by id desc
        //                                                 limit 1) B on B.id = A.id and A.if_del = 0
        // set A.`consume_status` = 'EXCEPTION'
        log.info(s);
    }


    @Test
    public void testDelete1() {
        DruidTombstoneSqlRewriter druidTombstoneSqlRewriter = new DruidTombstoneSqlRewriter();
        String sql = "delete from A where id in (1, 2, 3)";

        String s = druidTombstoneSqlRewriter.rewriteDeleteSql(sql);

        //  update A set if_del=1 where id in (1, 2, 3) and A.if_del = 0
        log.info(s);
    }


    @Test
    public void testSelect1() {
        DruidTombstoneSqlRewriter druidTombstoneSqlRewriter = new DruidTombstoneSqlRewriter();
        String sql = "  select a from b where c > 1";

        String s = druidTombstoneSqlRewriter.rewriteSelectSql(sql, false);

        //  select a from b where c > 1 and b.if_del = 0
        log.info(s);
    }


    @Test
    public void testSelect2() {
        DruidTombstoneSqlRewriter druidTombstoneSqlRewriter = new DruidTombstoneSqlRewriter();
        String sql = "  select customername , orderdate , lead ( orderdate  , 1  ) over ( partition by customernumber  order by orderdate   )   as nextorderdate from orders join customers;";
        String s = druidTombstoneSqlRewriter.rewriteSelectSql(sql, false);

        //   select customername, orderdate , lead(orderdate, 1) over (partition by customernumber order by orderdate) as nextorderdate
        //   from orders join customers on orders.if_del = 0 and customers.if_del = 0;
        log.info(s);
    }


    @Test
    public void testSelect3() {
        DruidTombstoneSqlRewriter druidTombstoneSqlRewriter = new DruidTombstoneSqlRewriter();
        String sql = "    select * from t_a left join t_b on t_a.b_id = t_b.id ";
        String s = druidTombstoneSqlRewriter.rewriteSelectSql(sql, false);

        //   select * from t_a
        //      left join t_b on t_a.b_id = t_b.id and t_b.if_del = 0
        //      where t_a.if_del = 0
        log.info(s);
    }


    @Test
    public void testSelect4() {
        DruidTombstoneSqlRewriter druidTombstoneSqlRewriter = new DruidTombstoneSqlRewriter();
        String sql = "  select a , b  from tab2  left join tab3  on tab2.c  = tab3.c   and tab2.d  = tab2.c     ,  (  select *  from tab5   )  t5";
        String s = druidTombstoneSqlRewriter.rewriteSelectSql(sql, false);

        //   select a, b from tab2
        //   left join tab3 on tab2.c = tab3.c and tab2.d = tab2.c and tab3.if_del = 0
        //   join ( select * from tab5 where tab5.if_del = 0 ) t5 where tab2.if_del = 0
        log.info(s);
    }


    @Test
    public void testSelect5() {
        DruidTombstoneSqlRewriter druidTombstoneSqlRewriter = new DruidTombstoneSqlRewriter(null, ArrayUtils.asArrayList("tab3"), new DefaultDataConvertorRegistry());
        String sql = "  select a , b  from tab2  left join tab3  on tab2.c  = tab3.c   and tab2.d  = tab2.c     ,  (  select *  from tab5   )  t5";
        String s = druidTombstoneSqlRewriter.rewriteSelectSql(sql, false);

        //   select a, b from tab2
        //      left join tab3 on tab2.c = tab3.c and tab2.d = tab2.c
        //      join ( select * from tab5 where tab5.if_del = 0 ) t5 where tab2.if_del = 0
        log.info(s);
    }


    @Test
    public void testSelect6() {
        DruidTombstoneSqlRewriter druidTombstoneSqlRewriter = new DruidTombstoneSqlRewriter(null, null, new DefaultDataConvertorRegistry());
        String sql = "  select a , b from tab2 t left join tab3 on tab2.c = tab3.c and tab2.d = tab2.c, (select * from tab5) t5 "
            + "where t.m  = ?  and  exists  (  select 1 from tab4  t4  where t1.n  = t4.n   )   and t.tm  >= '2016-11-11'";
        String s = druidTombstoneSqlRewriter.rewriteSelectSql(sql, false);

        //   select a, b from tab2 t
        //      left join tab3 on tab2.c = tab3.c and tab2.d = tab2.c and tab3.if_del = 0
        //      join ( select * from tab5 where tab5.if_del = 0 ) t5
        //   where t.m = ? and exists ( select 1 from tab4 t4 where t1.n = t4.n and t4.if_del = 0 ) and t.tm >= '2016-11-11' and t.if_del = 0
        log.info(s);
    }


}
