package cn.addenda.footprints.expression.evalex.config;

import cn.addenda.footprints.core.pojo.Binary;
import cn.addenda.footprints.expression.evalex.data.DataAccessorIfc;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.data.MapBasedDataAccessor;
import cn.addenda.footprints.expression.evalex.functions.FunctionIfc;
import cn.addenda.footprints.expression.evalex.functions.basic.*;
import cn.addenda.footprints.expression.evalex.functions.datetime.*;
import cn.addenda.footprints.expression.evalex.functions.string.StringContains;
import cn.addenda.footprints.expression.evalex.functions.string.StringLowerFunction;
import cn.addenda.footprints.expression.evalex.functions.string.StringUpperFunction;
import cn.addenda.footprints.expression.evalex.functions.trigonometric.*;
import cn.addenda.footprints.expression.evalex.operators.OperatorIfc;
import cn.addenda.footprints.expression.evalex.operators.arithmetic.*;
import cn.addenda.footprints.expression.evalex.operators.booleans.*;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * The expression configuration can be used to configure various aspects of expression parsing and
 * evaluation. <br>
 * A <code>Builder</code> is provided to create custom configurations, e.g.: <br>
 *
 * <pre>
 *   ExpressionConfiguration config = ExpressionConfiguration.builder().mathContext(MathContext.DECIMAL32).arraysAllowed(false).build();
 * </pre>
 *
 * <br>
 * Additional operators and functions can be added to an existing configuration:<br>
 *
 * <pre>
 *     ExpressionConfiguration.defaultConfiguration()
 *        .withAdditionalOperators(
 *            Map.entry("++", new PrefixPlusPlusOperator()),
 *            Map.entry("++", new PostfixPlusPlusOperator()))
 *        .withAdditionalFunctions(Map.entry("save", new SaveFunction()),
 *            Map.entry("update", new UpdateFunction()));
 * </pre>
 */
@Builder(toBuilder = true)
public class ExpressionConfiguration {

    /**
     * The standard set constants for EvalEx.
     */
    public static final Map<String, EvaluationValue> StandardConstants =
            Collections.unmodifiableMap(getStandardConstants());

    /**
     * Setting the decimal places to unlimited, will disable intermediate rounding.
     */
    public static final int DECIMAL_PLACES_ROUNDING_UNLIMITED = -1;

    /**
     * The default math context has a precision of 68 and {@link RoundingMode#HALF_EVEN}.
     */
    public static final MathContext DEFAULT_MATH_CONTEXT =
            new MathContext(68, RoundingMode.HALF_EVEN);

    /**
     * The operator dictionary holds all operators that will be allowed in an expression.
     */
    @Builder.Default
    @Getter
    @SuppressWarnings("unchecked")
    private final OperatorDictionaryIfc operatorDictionary =
            MapBasedOperatorDictionary.ofOperators(
                    // arithmetic
                    new Binary<>("+", new PrefixPlusOperator()),
                    new Binary<>("-", new PrefixMinusOperator()),
                    new Binary<>("+", new InfixPlusOperator()),
                    new Binary<>("-", new InfixMinusOperator()),
                    new Binary<>("*", new InfixMultiplicationOperator()),
                    new Binary<>("/", new InfixDivisionOperator()),
                    new Binary<>("^", new InfixPowerOfOperator()),
                    new Binary<>("%", new InfixModuloOperator()),
                    // booleans
                    new Binary<>("=", new InfixEqualsOperator()),
                    new Binary<>("==", new InfixEqualsOperator()),
                    new Binary<>("!=", new InfixNotEqualsOperator()),
                    new Binary<>("<>", new InfixNotEqualsOperator()),
                    new Binary<>(">", new InfixGreaterOperator()),
                    new Binary<>(">=", new InfixGreaterEqualsOperator()),
                    new Binary<>("<", new InfixLessOperator()),
                    new Binary<>("<=", new InfixLessEqualsOperator()),
                    new Binary<>("&&", new InfixAndOperator()),
                    new Binary<>("||", new InfixOrOperator()),
                    new Binary<>("!", new PrefixNotOperator()));

    /**
     * The function dictionary holds all functions that will be allowed in an expression.
     */
    @Builder.Default
    @Getter
    @SuppressWarnings("unchecked")
    private final FunctionDictionaryIfc functionDictionary =
            MapBasedFunctionDictionary.ofFunctions(
                    // basic functions
                    new Binary<>("ABS", new AbsFunction()),
                    new Binary<>("CEILING", new CeilingFunction()),
                    new Binary<>("FACT", new FactFunction()),
                    new Binary<>("FLOOR", new FloorFunction()),
                    new Binary<>("IF", new IfFunction()),
                    new Binary<>("LOG", new LogFunction()),
                    new Binary<>("LOG10", new Log10Function()),
                    new Binary<>("MAX", new MaxFunction()),
                    new Binary<>("MIN", new MinFunction()),
                    new Binary<>("NOT", new NotFunction()),
                    new Binary<>("RANDOM", new RandomFunction()),
                    new Binary<>("ROUND", new RoundFunction()),
                    new Binary<>("SUM", new SumFunction()),
                    new Binary<>("SQRT", new SqrtFunction()),
                    // trigonometric
                    new Binary<>("ACOS", new AcosFunction()),
                    new Binary<>("ACOSH", new AcosHFunction()),
                    new Binary<>("ACOSR", new AcosRFunction()),
                    new Binary<>("ACOT", new AcotFunction()),
                    new Binary<>("ACOTH", new AcotHFunction()),
                    new Binary<>("ACOTR", new AcotRFunction()),
                    new Binary<>("ASIN", new AsinFunction()),
                    new Binary<>("ASINH", new AsinHFunction()),
                    new Binary<>("ASINR", new AsinRFunction()),
                    new Binary<>("ATAN", new AtanFunction()),
                    new Binary<>("ATAN2", new Atan2Function()),
                    new Binary<>("ATAN2R", new Atan2RFunction()),
                    new Binary<>("ATANH", new AtanHFunction()),
                    new Binary<>("ATANR", new AtanRFunction()),
                    new Binary<>("COS", new CosFunction()),
                    new Binary<>("COSH", new CosHFunction()),
                    new Binary<>("COSR", new CosRFunction()),
                    new Binary<>("COT", new CotFunction()),
                    new Binary<>("COTH", new CotHFunction()),
                    new Binary<>("COTR", new CotRFunction()),
                    new Binary<>("CSC", new CscFunction()),
                    new Binary<>("CSCH", new CscHFunction()),
                    new Binary<>("CSCR", new CscRFunction()),
                    new Binary<>("DEG", new DegFunction()),
                    new Binary<>("RAD", new RadFunction()),
                    new Binary<>("SIN", new SinFunction()),
                    new Binary<>("SINH", new SinHFunction()),
                    new Binary<>("SINR", new SinRFunction()),
                    new Binary<>("SEC", new SecFunction()),
                    new Binary<>("SECH", new SecHFunction()),
                    new Binary<>("SECR", new SecRFunction()),
                    new Binary<>("TAN", new TanFunction()),
                    new Binary<>("TANH", new TanHFunction()),
                    new Binary<>("TANR", new TanRFunction()),
                    // string functions
                    new Binary<>("STR_CONTAINS", new StringContains()),
                    new Binary<>("STR_LOWER", new StringLowerFunction()),
                    new Binary<>("STR_UPPER", new StringUpperFunction()),
                    // date time functions
                    new Binary<>("DT_DATE_TIME", new DateTimeFunction()),
                    new Binary<>("DT_PARSE", new DateTimeParseFunction()),
                    new Binary<>("DT_ZONED_PARSE", new ZonedDateTimeParseFunction()),
                    new Binary<>("DT_FORMAT", new DateTimeFormatFunction()),
                    new Binary<>("DT_EPOCH", new DateTimeToEpochFunction()),
                    new Binary<>("DT_DATE_TIME_EPOCH", new DateTimeFromEpochFunction()),
                    new Binary<>("DT_DURATION_MILLIS", new DurationFromMillisFunction()),
                    new Binary<>("DT_DURATION_DAYS", new DurationFromDaysFunction()),
                    new Binary<>("DT_DURATION_PARSE", new DurationParseFunction()));

    /**
     * The math context to use.
     */
    @Builder.Default
    @Getter
    private final MathContext mathContext = DEFAULT_MATH_CONTEXT;

    /**
     * The data accessor is responsible for accessing variable and constant values in an expression.
     * The supplier will be called once for each new expression, the default is to create a new {@link
     * MapBasedDataAccessor} instance for each expression, providing a new storage for each
     * expression.
     */
    @Builder.Default
    @Getter
    private final Supplier<DataAccessorIfc> dataAccessorSupplier = MapBasedDataAccessor::new;

    /**
     * Default constants will be added automatically to each expression and can be used in expression
     * evaluation.
     */
    @Builder.Default
    @Getter
    private final Map<String, EvaluationValue> defaultConstants = getStandardConstants();

    /**
     * Support for arrays in expressions are allowed or not.
     */
    @Builder.Default
    @Getter
    private final boolean arraysAllowed = true;

    /**
     * Support for structures in expressions are allowed or not.
     */
    @Builder.Default
    @Getter
    private final boolean structuresAllowed = true;

    /**
     * Support for implicit multiplication, like in (a+b)(b+c) are allowed or not.
     */
    @Builder.Default
    @Getter
    private final boolean implicitMultiplicationAllowed = true;

    /**
     * The power of operator precedence, can be set higher {@link
     * OperatorIfc#OPERATOR_PRECEDENCE_POWER_HIGHER} or to a custom value.
     */
    @Builder.Default
    @Getter
    private final int powerOfPrecedence = OperatorIfc.OPERATOR_PRECEDENCE_POWER;

    /**
     * If specified, all results from operations and functions will be rounded to the specified number
     * of decimal digits, using the MathContexts rounding mode.
     */
    @Builder.Default
    @Getter
    private final int decimalPlacesRounding = DECIMAL_PLACES_ROUNDING_UNLIMITED;

    /**
     * If set to true (default), then the trailing decimal zeros in a number result will be stripped.
     */
    @Builder.Default
    @Getter
    private final boolean stripTrailingZeros = true;

    /**
     * If set to true (default), then variables can be set that have the name of a constant. In that
     * case, the constant value will be removed and a variable value will be set.
     */
    @Builder.Default
    @Getter
    private final boolean allowOverwriteConstants = true;

    /**
     * Set the default zone id. By default, the system default zone id is used.
     */
    @Builder.Default
    @Getter
    private final ZoneId defaultZoneId = ZoneId.systemDefault();

    /**
     * Convenience method to create a default configuration.
     *
     * @return A configuration with default settings.
     */
    public static ExpressionConfiguration defaultConfiguration() {
        return ExpressionConfiguration.builder().build();
    }

    /**
     * Adds additional operators to this configuration.
     * <pre>
     * ExpressionConfiguration.defaultConfiguration()
     *    .withAdditionalOperators(
     *        Map.entry("++", new PrefixPlusPlusOperator()),
     *        Map.entry("++", new PostfixPlusPlusOperator()));
     * </pre>
     *
     * @param operators variable number of arguments with a map entry holding the operator name and
     *                  implementation. <br>
     * @return The modified configuration, to allow chaining of methods.
     */
    @SafeVarargs
    public final ExpressionConfiguration withAdditionalOperators(
            Map.Entry<String, OperatorIfc>... operators) {
        Arrays.stream(operators)
                .forEach(entry -> operatorDictionary.addOperator(entry.getKey(), entry.getValue()));
        return this;
    }

    /**
     * Adds additional functions to this configuration.
     * <pre>
     *   ExpressionConfiguration.defaultConfiguration()
     *     .withAdditionalFunctions(
     *         Map.entry("save", new SaveFunction()),
     *         Map.entry("update", new UpdateFunction()));
     *  </pre>
     *
     * @param functions variable number of arguments with a map entry holding the functions name and
     *                  implementation. <br>
     * @return The mod
     */
    @SafeVarargs
    public final ExpressionConfiguration withAdditionalFunctions(
            Map.Entry<String, FunctionIfc>... functions) {
        Arrays.stream(functions)
                .forEach(entry -> functionDictionary.addFunction(entry.getKey(), entry.getValue()));
        return this;
    }

    private static Map<String, EvaluationValue> getStandardConstants() {

        Map<String, EvaluationValue> constants = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        constants.put("TRUE", new EvaluationValue(true));
        constants.put("FALSE", new EvaluationValue(false));
        constants.put(
                "PI",
                new EvaluationValue(
                        new BigDecimal(
                                "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679")));
        constants.put(
                "E",
                new EvaluationValue(
                        new BigDecimal(
                                "2.71828182845904523536028747135266249775724709369995957496696762772407663")));
        constants.put("NULL", new EvaluationValue(null));

        return constants;
    }
}
