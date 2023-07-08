package cn.addenda.footprints.expression.evalex.operators;

import cn.addenda.footprints.expression.evalex.EvaluationException;
import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.config.ExpressionConfiguration;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Interface that is required for all operators in an operator dictionary for evaluation of
 * expressions. There are three operator type: prefix, postfix and infix. Every operator has a
 * precedence, which defines the order of operator evaluation. The associativity of an operator is a
 * property that determines how operators of the same precedence are grouped in the absence of
 * parentheses.
 */
public interface OperatorIfc {

    /**
     * The operator type.
     */
    enum OperatorType {
        /**
         * Unary prefix operator, like -x
         */
        PREFIX_OPERATOR,
        /**
         * Unary postfix operator,like x!
         */
        POSTFIX_OPERATOR,
        /**
         * Binary infix operator, like x+y
         */
        INFIX_OPERATOR
    }

    /**
     * Or operator precedence: ||
     */
    int OPERATOR_PRECEDENCE_OR = 2;

    /**
     * And operator precedence: &&
     */
    int OPERATOR_PRECEDENCE_AND = 4;

    /**
     * Equality operators precedence: =, ==, !=, <>
     */
    int OPERATOR_PRECEDENCE_EQUALITY = 7;

    /**
     * Comparative operators precedence: <, >, <=, >=
     */
    int OPERATOR_PRECEDENCE_COMPARISON = 10;

    /**
     * Additive operators precedence: + and -
     */
    int OPERATOR_PRECEDENCE_ADDITIVE = 20;

    /**
     * Multiplicative operators precedence: *, /, %
     */
    int OPERATOR_PRECEDENCE_MULTIPLICATIVE = 30;

    /**
     * Power operator precedence: ^
     */
    int OPERATOR_PRECEDENCE_POWER = 40;

    /**
     * Unary operators precedence: + and - as prefix
     */
    int OPERATOR_PRECEDENCE_UNARY = 60;

    /**
     * An optional higher power operator precedence, higher than the unary prefix, e.g. -2^2 equals to
     * 4 or -4, depending on precedence configuration.
     */
    int OPERATOR_PRECEDENCE_POWER_HIGHER = 80;

    /**
     * @return The operator's precedence.
     */
    int getPrecedence();

    /**
     * If operators with same precedence are evaluated from left to right.
     *
     * @return The associativity.
     */
    boolean isLeftAssociative();

    /**
     * If it is a prefix operator.
     *
     * @return <code>true</code> if it is a prefix operator.
     */
    boolean isPrefix();

    /**
     * If it is a postfix operator.
     *
     * @return <code>true</code> if it is a postfix operator.
     */
    boolean isPostfix();

    /**
     * If it is an infix operator.
     *
     * @return <code>true</code> if it is an infix operator.
     */
    boolean isInfix();

    /**
     * Called during parsing, can be implemented to return a customized precedence.
     *
     * @param configuration The expression configuration.
     * @return The default precedence from the operator annotation, or a customized value.
     */
    int getPrecedence(ExpressionConfiguration configuration);

    /**
     * Performs the operator logic and returns an evaluation result.
     *
     * @param expression    The expression, where this function is executed. Can be used to access the
     *                      expression configuration.
     * @param operatorToken The operator token from the parsed expression.
     * @param operands      The operands, one for prefix and postfix operators, two for infix operators.
     * @return The evaluation result in form of a {@link EvaluationValue}.
     * @throws EvaluationException In case there were problems during evaluation.
     */
    EvaluationValue evaluate(Expression expression, Token operatorToken, EvaluationValue... operands)
            throws EvaluationException;
}
