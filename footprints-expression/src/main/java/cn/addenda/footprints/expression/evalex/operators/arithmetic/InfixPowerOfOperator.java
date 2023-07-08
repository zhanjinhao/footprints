package cn.addenda.footprints.expression.evalex.operators.arithmetic;

import cn.addenda.footprints.expression.evalex.EvaluationException;
import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.config.ExpressionConfiguration;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.operators.AbstractOperator;
import cn.addenda.footprints.expression.evalex.operators.InfixOperator;
import cn.addenda.footprints.expression.evalex.parser.Token;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static cn.addenda.footprints.expression.evalex.operators.OperatorIfc.OPERATOR_PRECEDENCE_POWER;

/**
 * Power of operator, calculates the power of right operand of left operand. The precedence is read
 * from the configuration during parsing.
 *
 * @see #getPrecedence(ExpressionConfiguration)
 */
@InfixOperator(precedence = OPERATOR_PRECEDENCE_POWER, leftAssociative = false)
public class InfixPowerOfOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(
            Expression expression, Token operatorToken, EvaluationValue... operands)
            throws EvaluationException {
        EvaluationValue leftOperand = operands[0];
        EvaluationValue rightOperand = operands[1];

        if (leftOperand.isNumberValue() && rightOperand.isNumberValue()) {
            /*-
             * Thanks to Gene Marin:
             * http://stackoverflow.com/questions/3579779/how-to-do-a-fractional-power-on-bigdecimal-in-java
             */

            MathContext mathContext = expression.getConfiguration().getMathContext();
            BigDecimal v1 = leftOperand.getNumberValue();
            BigDecimal v2 = rightOperand.getNumberValue();

            int signOf2 = v2.signum();
            double dn1 = v1.doubleValue();
            v2 = v2.multiply(new BigDecimal(signOf2)); // n2 is now positive
            BigDecimal remainderOf2 = v2.remainder(BigDecimal.ONE);
            BigDecimal n2IntPart = v2.subtract(remainderOf2);
            BigDecimal intPow = v1.pow(n2IntPart.intValueExact(), mathContext);
            BigDecimal doublePow = BigDecimal.valueOf(Math.pow(dn1, remainderOf2.doubleValue()));

            BigDecimal result = intPow.multiply(doublePow, mathContext);
            if (signOf2 == -1) {
                result = BigDecimal.ONE.divide(result, mathContext.getPrecision(), RoundingMode.HALF_UP);
            }
            return new EvaluationValue(result);
        } else {
            throw EvaluationException.ofUnsupportedDataTypeInOperation(operatorToken);
        }
    }

    @Override
    public int getPrecedence(ExpressionConfiguration configuration) {
        return configuration.getPowerOfPrecedence();
    }
}
