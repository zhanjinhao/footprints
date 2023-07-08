package cn.addenda.footprints.expression.evalex.functions.basic;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Rounds the given value to the specified scale, using the {@link java.math.MathContext} of the
 * expression configuration.
 */
@FunctionParameter(name = "value")
@FunctionParameter(name = "scale")
public class RoundFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        EvaluationValue value = parameterValues[0];
        EvaluationValue precision = parameterValues[1];

        return new EvaluationValue(
                value
                        .getNumberValue()
                        .setScale(
                                precision.getNumberValue().intValue(),
                                expression.getConfiguration().getMathContext().getRoundingMode()));
    }
}
