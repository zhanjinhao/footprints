package cn.addenda.footprints.expression.evalex.parser;

import cn.addenda.footprints.expression.evalex.functions.FunctionIfc;
import cn.addenda.footprints.expression.evalex.operators.OperatorIfc;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * A token represents a singe part of an expression, like an operator, number literal, or a brace.
 * Each token has a unique type, a value (its representation) and a position (starting with 1) in
 * the original expression string.
 *
 * <p>For operators and functions, the operator and function definition is also set during parsing.
 */
@Value
@AllArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
public class Token {

    public enum TokenType {
        BRACE_OPEN,
        BRACE_CLOSE,
        COMMA,
        STRING_LITERAL,
        NUMBER_LITERAL,
        VARIABLE_OR_CONSTANT,
        INFIX_OPERATOR,
        PREFIX_OPERATOR,
        POSTFIX_OPERATOR,
        FUNCTION,
        FUNCTION_PARAM_START,
        ARRAY_OPEN,
        ARRAY_CLOSE,
        ARRAY_INDEX,
        STRUCTURE_SEPARATOR
    }

    int startPosition;

    String value;

    TokenType type;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    FunctionIfc functionDefinition;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    OperatorIfc operatorDefinition;

    public Token(int startPosition, String value, TokenType type) {
        this(startPosition, value, type, null, null);
    }

    public Token(int startPosition, String value, TokenType type, FunctionIfc functionDefinition) {
        this(startPosition, value, type, functionDefinition, null);
    }

    public Token(int startPosition, String value, TokenType type, OperatorIfc operatorDefinition) {
        this(startPosition, value, type, null, operatorDefinition);
    }
}
