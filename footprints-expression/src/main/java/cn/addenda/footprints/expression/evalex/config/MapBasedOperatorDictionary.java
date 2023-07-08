package cn.addenda.footprints.expression.evalex.config;

import cn.addenda.footprints.expression.evalex.operators.OperatorIfc;

import java.util.Map;
import java.util.TreeMap;

import static java.util.Arrays.stream;

/**
 * A default case-insensitive implementation of the operator dictionary that uses a local <code>
 * Map.Entry&lt;String,OperatorIfc&gt;</code> for storage.
 */
public class MapBasedOperatorDictionary implements OperatorDictionaryIfc {

    final Map<String, OperatorIfc> prefixOperators = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    final Map<String, OperatorIfc> postfixOperators = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    final Map<String, OperatorIfc> infixOperators = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);


    /**
     * Creates a new operator dictionary with the specified list of operators.
     *
     * @param operators variable number of arguments that specify the operator names and definitions
     *                  that will initially be added.
     * @return A newly created operator dictionary with the specified operators.
     */
    @SuppressWarnings({"unchecked", "varargs"})
    public static OperatorDictionaryIfc ofOperators(Map.Entry<String, OperatorIfc>... operators) {
        OperatorDictionaryIfc dictionary = new MapBasedOperatorDictionary();
        stream(operators).forEach(entry -> dictionary.addOperator(entry.getKey(), entry.getValue()));
        return dictionary;
    }

    @Override
    public void addOperator(String operatorString, OperatorIfc operator) {
        if (operator.isPrefix()) {
            prefixOperators.put(operatorString, operator);
        } else if (operator.isPostfix()) {
            postfixOperators.put(operatorString, operator);
        } else {
            infixOperators.put(operatorString, operator);
        }
    }

    @Override
    public OperatorIfc getPrefixOperator(String operatorString) {
        return prefixOperators.get(operatorString);
    }

    @Override
    public OperatorIfc getPostfixOperator(String operatorString) {
        return postfixOperators.get(operatorString);
    }

    @Override
    public OperatorIfc getInfixOperator(String operatorString) {
        return infixOperators.get(operatorString);
    }
}
