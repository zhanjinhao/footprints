package cn.addenda.footprints.core.interceptor.tombstone;

import cn.addenda.footprints.core.function.TRunnable;
import cn.addenda.footprints.core.function.TSupplier;
import cn.addenda.footprints.core.util.ExceptionUtil;

/**
 * @author addenda
 * @since 2023/5/28 17:56
 */
public class TombstoneUtils {

    private TombstoneUtils() {
    }

    public static <T> T useJoinQuery(boolean joinUseSubQuery, TSupplier<T> supplier) {
        TombstoneContext.push();
        try {
            TombstoneContext.setJoinUseSubQuery(joinUseSubQuery);
            return supplier.get();
        } catch (Throwable throwable) {
            ExceptionUtil.reportAsRuntimeException(throwable, TombstoneException.class);
            return null;
        } finally {
            TombstoneContext.pop();
        }
    }

    public static void useJoinQuery(boolean joinUseSubQuery, TRunnable runnable) {
        TombstoneContext.push();
        try {
            TombstoneContext.setJoinUseSubQuery(joinUseSubQuery);
            runnable.run();
        } catch (Throwable throwable) {
            ExceptionUtil.reportAsRuntimeException(throwable, TombstoneException.class);
        } finally {
            TombstoneContext.pop();
        }
    }

    public static <T> T disable(boolean disable, TSupplier<T> supplier) {
        TombstoneContext.push();
        try {
            TombstoneContext.setDisable(disable);
            return supplier.get();
        } catch (Throwable throwable) {
            ExceptionUtil.reportAsRuntimeException(throwable, TombstoneException.class);
            return null;
        } finally {
            TombstoneContext.pop();
        }
    }

    public static void disable(boolean disable, TRunnable runnable) {
        TombstoneContext.push();
        try {
            TombstoneContext.setDisable(disable);
            runnable.run();
        } catch (Throwable throwable) {
            ExceptionUtil.reportAsRuntimeException(throwable, TombstoneException.class);
        } finally {
            TombstoneContext.pop();
        }
    }

}
