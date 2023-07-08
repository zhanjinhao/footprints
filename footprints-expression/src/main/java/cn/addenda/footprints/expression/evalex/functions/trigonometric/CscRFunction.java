package cn.addenda.footprints.expression.evalex.functions.trigonometric;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Returns the co-secant (in radians).
 */
@FunctionParameter(name = "value", nonZero = true)
public class CscRFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        /* Formula: csc(x) = 1 / sin(x) */
        return expression.convertDoubleValue(
                1 / Math.sin(parameterValues[0].getNumberValue().doubleValue()));
    }
}
