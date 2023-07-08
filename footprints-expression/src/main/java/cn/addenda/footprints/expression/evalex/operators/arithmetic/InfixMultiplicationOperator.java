package cn.addenda.footprints.expression.evalex.operators.arithmetic;

import cn.addenda.footprints.expression.evalex.EvaluationException;
import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.operators.AbstractOperator;
import cn.addenda.footprints.expression.evalex.operators.InfixOperator;
import cn.addenda.footprints.expression.evalex.parser.Token;

import static cn.addenda.footprints.expression.evalex.operators.OperatorIfc.OPERATOR_PRECEDENCE_MULTIPLICATIVE;

/**
 * Multiplication of two numbers.
 */
@InfixOperator(precedence = OPERATOR_PRECEDENCE_MULTIPLICATIVE)
public class InfixMultiplicationOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(
            Expression expression, Token operatorToken, EvaluationValue... operands)
            throws EvaluationException {
        EvaluationValue leftOperand = operands[0];
        EvaluationValue rightOperand = operands[1];

        if (leftOperand.isNumberValue() && rightOperand.isNumberValue()) {
            return new EvaluationValue(
                    leftOperand
                            .getNumberValue()
                            .multiply(
                                    rightOperand.getNumberValue(), expression.getConfiguration().getMathContext()));
        } else {
            throw EvaluationException.ofUnsupportedDataTypeInOperation(operatorToken);
        }
    }
}
