package cn.addenda.footprints.expression.evalex.parser;

import lombok.Value;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Expressions are parsed into an abstract syntax tree (AST). The tree has one root node and each
 * node has zero or more children (parameters), depending on the operation. A leaf node is a
 * numerical or string constant that has no more children (parameters). Other nodes define
 * operators, functions and special operations like array index and structure separation.
 *
 * <p>The tree is evaluated from bottom (leafs) to top, in a recursive way, until the root node is
 * evaluated, which then holds the result of the complete expression.
 *
 * <p>To be able to visualize the tree, a <code>toJSON</code> method is provided. The produced JSON
 * string can be used to visualize the tree. OE.g. with this online tool:
 *
 * <p><a href="https://vanya.jp.net/vtree/">Online JSON to Tree Diagram Converter</a>
 */
@Value
public class ASTNode {

    /**
     * The children od the tree.
     */
    List<ASTNode> parameters;

    /**
     * The token associated with this tree node.
     */
    Token token;

    public ASTNode(Token token, ASTNode... parameters) {
        this.token = token;
        this.parameters = Arrays.asList(parameters);
    }

    /**
     * Produces a JSON string representation of this node ad all its children.
     *
     * @return A JSON string of the tree structure starting at this node.
     */
    public String toJSON() {
        if (parameters.isEmpty()) {
            return String.format(
                    "{" + "\"type\":\"%s\",\"value\":\"%s\"}", token.getType(), token.getValue());
        } else {
            String childrenJson =
                    parameters.stream().map(ASTNode::toJSON).collect(Collectors.joining(","));
            return String.format(
                    "{" + "\"type\":\"%s\",\"value\":\"%s\",\"children\":[%s]}",
                    token.getType(), token.getValue(), childrenJson);
        }
    }
}
