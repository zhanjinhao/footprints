package cn.addenda.footprints.cdc.jdbc.invocation;

import cn.addenda.footprints.cdc.jdbc.CdcConnection;
import cn.addenda.footprints.cdc.jdbc.TableConfig;
import cn.addenda.footprints.core.pojo.Binary;
import cn.addenda.footprints.core.util.IterableUtils;
import com.alibaba.druid.sql.ast.SQLStatement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author addenda
 * @since 2022/9/24 9:52
 */
public class DeletePsDelegate extends AbstractPsDelegate {

    public DeletePsDelegate(CdcConnection cdcConnection, PreparedStatement ps,
                            TableConfig tableConfig, SQLStatement parameterizedSqlStatement, List<String> sqlList) {
        super(cdcConnection, ps, tableConfig, parameterizedSqlStatement, sqlList);
    }

    @Override
    public <T> T doExecute(PsInvocation<T> pi) throws SQLException {
        // -------------------------------------
        //  对于Statement模式来说，记录下来SQL就行了
        // -------------------------------------
        if (checkTableMode(TableConfig.CM_STATEMENT)) {
            List<String> statementCdcSqlList = new ArrayList<>(sqlList);
            executeCdcSql(TableConfig.CM_STATEMENT, statementCdcSqlList);
        }

        T invoke = null;

        // ----------------------------------
        //  对于ROW模式，需要记录下来具体删除的行。
        // ----------------------------------
        if (checkTableMode(TableConfig.CM_ROW)) {
            Binary<T, List<List<Long>>> lockKeyBinary = lockKey(pi);
            invoke = lockKeyBinary.getF1();
            List<List<Long>> keyListList = lockKeyBinary.getF2();

            List<String> rowCdcSqlList = new ArrayList<>();
            // 多余delete语句来说，在batch模式下，如果sqlX和sqlY同时命中了KeyN，则只应该记录一次。
            Set<Long> keyValueSet = keyListList.stream()
                    .flatMap((Function<List<Long>, Stream<Long>>) Collection::stream)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            // 对于simple模式，会进行 1:n -> 1:1 优化；
            // 对于batch模式，也会进行 1:1 -> n:1 优化。
            if (!keyValueSet.isEmpty()) {
                List<List<Long>> listList = IterableUtils.split(new ArrayList<>(keyValueSet), IN_SIZE);
                for (List<Long> item : listList) {
                    rowCdcSqlList.add("delete from " + tableName + " where " + keyColumn + " in (" + longListToString(item) + ")");
                }
            }
            executeCdcSql(TableConfig.CM_ROW, rowCdcSqlList);
        }

        if (invoke != null) {
            return invoke;
        }
        return pi.invoke();
    }

}
