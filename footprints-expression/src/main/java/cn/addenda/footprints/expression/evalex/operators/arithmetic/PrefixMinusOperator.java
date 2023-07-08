package cn.addenda.footprints.expression.evalex.operators.arithmetic;

import cn.addenda.footprints.expression.evalex.EvaluationException;
import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.operators.AbstractOperator;
import cn.addenda.footprints.expression.evalex.operators.PrefixOperator;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Unary prefix minus.
 */
@PrefixOperator(leftAssociative = false)
public class PrefixMinusOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(
            Expression expression, Token operatorToken, EvaluationValue... operands)
            throws EvaluationException {
        EvaluationValue operand = operands[0];

        if (operand.isNumberValue()) {
            return new EvaluationValue(
                    operand.getNumberValue().negate(expression.getConfiguration().getMathContext()));
        } else {
            throw EvaluationException.ofUnsupportedDataTypeInOperation(operatorToken);
        }
    }
}
