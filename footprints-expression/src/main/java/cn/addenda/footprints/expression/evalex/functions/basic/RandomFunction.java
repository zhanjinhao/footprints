package cn.addenda.footprints.expression.evalex.functions.basic;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.parser.Token;

import java.security.SecureRandom;

/**
 * Random function produces a random value between 0 and 1.
 */
public class RandomFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        SecureRandom secureRandom = new SecureRandom();

        return expression.convertDoubleValue(secureRandom.nextDouble());
    }
}
