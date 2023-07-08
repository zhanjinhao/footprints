package cn.addenda.footprints.expression.evalex.operators.arithmetic;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.operators.AbstractOperator;
import cn.addenda.footprints.expression.evalex.operators.InfixOperator;
import cn.addenda.footprints.expression.evalex.parser.Token;

import java.time.Duration;

import static cn.addenda.footprints.expression.evalex.operators.OperatorIfc.OPERATOR_PRECEDENCE_ADDITIVE;

/**
 * Addition of numbers and strings. If one operand is a string, a string concatenation is performed.
 */
@InfixOperator(precedence = OPERATOR_PRECEDENCE_ADDITIVE)
public class InfixPlusOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(
            Expression expression, Token operatorToken, EvaluationValue... operands) {
        EvaluationValue leftOperand = operands[0];
        EvaluationValue rightOperand = operands[1];

        if (leftOperand.isNumberValue() && rightOperand.isNumberValue()) {
            return new EvaluationValue(
                    leftOperand
                            .getNumberValue()
                            .add(rightOperand.getNumberValue(), expression.getConfiguration().getMathContext()));
        } else if (leftOperand.isDateTimeValue() && rightOperand.isDurationValue()) {
            return new EvaluationValue(
                    leftOperand.getDateTimeValue().plus(rightOperand.getDurationValue()));
        } else if (leftOperand.isDurationValue() && rightOperand.isDurationValue()) {
            return new EvaluationValue(
                    leftOperand.getDurationValue().plus(rightOperand.getDurationValue()));
        } else if (leftOperand.isDateTimeValue() && rightOperand.isNumberValue()) {
            return new EvaluationValue(
                    leftOperand
                            .getDateTimeValue()
                            .plus(Duration.ofMillis(rightOperand.getNumberValue().longValue())));
        } else {
            return new EvaluationValue(leftOperand.getStringValue() + rightOperand.getStringValue());
        }
    }
}
