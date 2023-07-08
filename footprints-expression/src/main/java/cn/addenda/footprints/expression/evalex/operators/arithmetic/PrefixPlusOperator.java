package cn.addenda.footprints.expression.evalex.operators.arithmetic;

import cn.addenda.footprints.expression.evalex.EvaluationException;
import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.operators.AbstractOperator;
import cn.addenda.footprints.expression.evalex.operators.PrefixOperator;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Unary prefix plus.
 */
@PrefixOperator(leftAssociative = false)
public class PrefixPlusOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(
            Expression expression, Token operatorToken, EvaluationValue... operands)
            throws EvaluationException {
        EvaluationValue operator = operands[0];

        if (operator.isNumberValue()) {
            return new EvaluationValue(
                    operator.getNumberValue().plus(expression.getConfiguration().getMathContext()));
        } else {
            throw EvaluationException.ofUnsupportedDataTypeInOperation(operatorToken);
        }
    }
}
