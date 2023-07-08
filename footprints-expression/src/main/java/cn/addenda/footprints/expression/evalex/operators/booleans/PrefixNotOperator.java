package cn.addenda.footprints.expression.evalex.operators.booleans;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.operators.AbstractOperator;
import cn.addenda.footprints.expression.evalex.operators.PrefixOperator;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Boolean negation of value.
 */
@PrefixOperator
public class PrefixNotOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(
            Expression expression, Token operatorToken, EvaluationValue... operands) {
        return new EvaluationValue(!operands[0].getBooleanValue());
    }
}
