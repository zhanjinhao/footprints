package cn.addenda.footprints.expression.evalex.operators.arithmetic;

import cn.addenda.footprints.expression.evalex.EvaluationException;
import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.operators.AbstractOperator;
import cn.addenda.footprints.expression.evalex.operators.InfixOperator;
import cn.addenda.footprints.expression.evalex.parser.Token;

import java.math.BigDecimal;

import static cn.addenda.footprints.expression.evalex.operators.OperatorIfc.OPERATOR_PRECEDENCE_MULTIPLICATIVE;

/**
 * Remainder (modulo) of two numbers.
 */
@InfixOperator(precedence = OPERATOR_PRECEDENCE_MULTIPLICATIVE)
public class InfixModuloOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(
            Expression expression, Token operatorToken, EvaluationValue... operands)
            throws EvaluationException {
        EvaluationValue leftOperand = operands[0];
        EvaluationValue rightOperand = operands[1];

        if (leftOperand.isNumberValue() && rightOperand.isNumberValue()) {

            if (rightOperand.getNumberValue().equals(BigDecimal.ZERO)) {
                throw new EvaluationException(operatorToken, "Division by zero");
            }

            return new EvaluationValue(
                    leftOperand
                            .getNumberValue()
                            .remainder(
                                    rightOperand.getNumberValue(), expression.getConfiguration().getMathContext()));
        } else {
            throw EvaluationException.ofUnsupportedDataTypeInOperation(operatorToken);
        }
    }
}
