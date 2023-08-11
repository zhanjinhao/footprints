package cn.addenda.footprints.cdc.jdbc;

import cn.addenda.footprints.cdc.DBUtils;
import org.junit.*;
import org.junit.runners.MethodSorters;

import javax.sql.DataSource;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author addenda
 * @since 2022/9/4 18:23
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LiteralValueExecuteTest {

    private static Connection connection;

    @BeforeClass
    public static void before() {
        DataSource dataSource = DBUtils.getDataSource();
        try {
            connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("truncate t_cdc_test");
            statement.executeUpdate("truncate t_cdc_test_cdc_r");
            statement.executeUpdate("truncate t_cdc_test_cdc_s");
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test01_insert() throws Exception {
        PreparedStatement ps = connection.prepareStatement(
                "insert into t_cdc_test(long_d, int_d, string_d, date_d, time_d, datetime_d, float_d, double_d) " +
                        "values (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, 1L);
        ps.setInt(2, 2);
        ps.setString(3, "3\\'\t");
        ps.setDate(4, new Date(System.currentTimeMillis()));
        ps.setTime(5, new Time(System.currentTimeMillis()));
        ps.setObject(6, ZonedDateTime.now(ZoneId.of("America/New_York")));
        ps.setFloat(7, 1.1f);
        ps.setDouble(8, 2.2d);

        ps.executeUpdate();

        connection.commit();
    }


    @Test
    public void test02_update() throws Exception {
        PreparedStatement ps = connection.prepareStatement(
                "update t_cdc_test set date_d = ?, time_d = ?, datetime_d = ? where long_d = ?");
        ps.setDate(1, new Date(System.currentTimeMillis()));
        ps.setTime(2, new Time(System.currentTimeMillis()));
        ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
//        ps.setString(4, "update");
        ps.setLong(4, 1L);

        ps.executeUpdate();

        connection.commit();
    }

    @Test
    public void test03_delete() throws Exception {
        PreparedStatement ps = connection.prepareStatement(
                "delete from t_cdc_test  where long_d = ?");
        ps.setLong(1, 1L);

        ps.executeUpdate();

        connection.commit();
    }

    @AfterClass
    public static void after() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
