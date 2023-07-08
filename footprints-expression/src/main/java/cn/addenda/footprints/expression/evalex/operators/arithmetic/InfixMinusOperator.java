package cn.addenda.footprints.expression.evalex.operators.arithmetic;

import cn.addenda.footprints.expression.evalex.EvaluationException;
import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.operators.AbstractOperator;
import cn.addenda.footprints.expression.evalex.operators.InfixOperator;
import cn.addenda.footprints.expression.evalex.parser.Token;

import java.time.Duration;

import static cn.addenda.footprints.expression.evalex.operators.OperatorIfc.OPERATOR_PRECEDENCE_ADDITIVE;

/**
 * Subtraction of two numbers.
 */
@InfixOperator(precedence = OPERATOR_PRECEDENCE_ADDITIVE)
public class InfixMinusOperator extends AbstractOperator {

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
                            .subtract(
                                    rightOperand.getNumberValue(), expression.getConfiguration().getMathContext()));

        } else if (leftOperand.isDateTimeValue() && rightOperand.isDateTimeValue()) {
            return new EvaluationValue(
                    Duration.ofMillis(
                            leftOperand.getDateTimeValue().toEpochMilli()
                                    - rightOperand.getDateTimeValue().toEpochMilli()));

        } else if (leftOperand.isDateTimeValue() && rightOperand.isDurationValue()) {
            return new EvaluationValue(
                    leftOperand.getDateTimeValue().minus(rightOperand.getDurationValue()));
        } else if (leftOperand.isDurationValue() && rightOperand.isDurationValue()) {
            return new EvaluationValue(
                    leftOperand.getDurationValue().minus(rightOperand.getDurationValue()));
        } else if (leftOperand.isDateTimeValue() && rightOperand.isNumberValue()) {
            return new EvaluationValue(
                    leftOperand
                            .getDateTimeValue()
                            .minus(Duration.ofMillis(rightOperand.getNumberValue().longValue())));
        } else {
            throw EvaluationException.ofUnsupportedDataTypeInOperation(operatorToken);
        }
    }
}
