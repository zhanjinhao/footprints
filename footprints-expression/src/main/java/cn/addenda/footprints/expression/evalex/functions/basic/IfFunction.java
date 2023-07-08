package cn.addenda.footprints.expression.evalex.functions.basic;

import cn.addenda.footprints.expression.evalex.EvaluationException;
import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Conditional evaluation function. If parameter <code>condition</code> is <code>true</code>, the
 * <code>resultIfTrue</code> value is returned, else the <code>resultIfFalse</code> value. <code>
 * resultIfTrue</code> and <code>resultIfFalse</code> are only evaluated (lazily evaluated),
 * <b>after</b> the condition was evaluated.
 */
@FunctionParameter(name = "condition")
@FunctionParameter(name = "resultIfTrue", isLazy = true)
@FunctionParameter(name = "resultIfFalse", isLazy = true)
public class IfFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues)
            throws EvaluationException {
        if (Boolean.TRUE.equals(parameterValues[0].getBooleanValue())) {
            return expression.evaluateSubtree(parameterValues[1].getExpressionNode());
        } else {
            return expression.evaluateSubtree(parameterValues[2].getExpressionNode());
        }
    }
}
