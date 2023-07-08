package cn.addenda.footprints.expression.evalex.operators;

/**
 * Operator properties are defined through a class annotation, this exception is thrown if no
 * annotation was found when creating the operator instance.
 */
public class OperatorAnnotationNotFoundException extends RuntimeException {

    public OperatorAnnotationNotFoundException(String className) {
        super("Operator annotation for '" + className + "' not found");
    }
}
