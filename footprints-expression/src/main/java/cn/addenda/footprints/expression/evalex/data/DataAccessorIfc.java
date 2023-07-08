package cn.addenda.footprints.expression.evalex.data;

/**
 * A data accessor is responsible for accessing data, e.g. variable and constant values during an
 * expression evaluation. The default implementation for setting and reading local data is the
 * {@link MapBasedDataAccessor}.
 */
public interface DataAccessorIfc {

    /**
     * Retrieves a data value.
     *
     * @param variable The variable name, e.g. a variable or constant name.
     * @return The data value, or <code>null</code> if not found.
     */
    EvaluationValue getData(String variable);

    /**
     * Sets a data value.
     *
     * @param variable The variable name, e.g. a variable or constant name.
     * @param value    The value to set.
     */
    void setData(String variable, EvaluationValue value);
}
