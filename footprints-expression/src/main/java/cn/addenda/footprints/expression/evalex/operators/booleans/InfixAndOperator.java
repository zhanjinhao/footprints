package cn.addenda.footprints.expression.evalex.operators.booleans;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.operators.AbstractOperator;
import cn.addenda.footprints.expression.evalex.operators.InfixOperator;
import cn.addenda.footprints.expression.evalex.parser.Token;

import static cn.addenda.footprints.expression.evalex.operators.OperatorIfc.OPERATOR_PRECEDENCE_AND;

/**
 * Boolean AND of two values.
 */
@InfixOperator(precedence = OPERATOR_PRECEDENCE_AND)
public class InfixAndOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(
            Expression expression, Token operatorToken, EvaluationValue... operands) {
        return new EvaluationValue(operands[0].getBooleanValue() && operands[1].getBooleanValue());
    }
}
