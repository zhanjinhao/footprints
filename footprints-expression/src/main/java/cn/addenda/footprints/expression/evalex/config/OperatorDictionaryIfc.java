package cn.addenda.footprints.expression.evalex.config;

import cn.addenda.footprints.expression.evalex.operators.OperatorIfc;

/**
 * An operator dictionary holds all the operators, that can be used in an expression. <br>
 * The default implementation is the {@link MapBasedOperatorDictionary}.
 */
public interface OperatorDictionaryIfc {

    /**
     * Allows to add an operator to the dictionary. Implementation is optional, if you have a fixed
     * set of operators, this method can throw an exception.
     *
     * @param operatorString The operator name.
     * @param operator       The operator implementation.
     */
    void addOperator(String operatorString, OperatorIfc operator);

    /**
     * Check if the dictionary has a prefix operator with that name.
     *
     * @param operatorString The operator name to look for.
     * @return <code>true</code> if an operator was found or <code>false</code> if not.
     */
    default boolean hasPrefixOperator(String operatorString) {
        return getPrefixOperator(operatorString) != null;
    }

    /**
     * Check if the dictionary has a postfix operator with that name.
     *
     * @param operatorString The operator name to look for.
     * @return <code>true</code> if an operator was found or <code>false</code> if not.
     */
    default boolean hasPostfixOperator(String operatorString) {
        return getPostfixOperator(operatorString) != null;
    }

    /**
     * Check if the dictionary has an infix operator with that name.
     *
     * @param operatorString The operator name to look for.
     * @return <code>true</code> if an operator was found or <code>false</code> if not.
     */
    default boolean hasInfixOperator(String operatorString) {
        return getInfixOperator(operatorString) != null;
    }

    /**
     * Get the operator definition for a prefix operator name.
     *
     * @param operatorString The name of the operator.
     * @return The operator definition or <code>null</code> if no operator was found.
     */
    OperatorIfc getPrefixOperator(String operatorString);

    /**
     * Get the operator definition for a postfix operator name.
     *
     * @param operatorString The name of the operator.
     * @return The operator definition or <code>null</code> if no operator was found.
     */
    OperatorIfc getPostfixOperator(String operatorString);

    /**
     * Get the operator definition for an infix operator name.
     *
     * @param operatorString The name of the operator.
     * @return The operator definition or <code>null</code> if no operator was found.
     */
    OperatorIfc getInfixOperator(String operatorString);
}
