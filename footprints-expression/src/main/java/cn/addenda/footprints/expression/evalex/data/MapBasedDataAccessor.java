package cn.addenda.footprints.expression.evalex.data;

import java.util.Map;
import java.util.TreeMap;

/**
 * A default case-insensitive implementation of the data accessor that uses a local <code>
 * Map.Entry&lt;String, EvaluationValue&gt;</code> for storage.
 */
public class MapBasedDataAccessor implements DataAccessorIfc {

    private final Map<String, EvaluationValue> variables =
            new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    @Override
    public EvaluationValue getData(String variable) {
        return variables.get(variable);
    }

    @Override
    public void setData(String variable, EvaluationValue value) {
        variables.put(variable, value);
    }
}
