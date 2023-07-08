package cn.addenda.footprints.expression.evalex.config;

import cn.addenda.footprints.expression.evalex.functions.FunctionIfc;

import java.util.Map;
import java.util.TreeMap;

import static java.util.Arrays.stream;

/**
 * A default case-insensitive implementation of the function dictionary that uses a local <code>
 * Map.Entry&lt;String, FunctionIfc&gt;</code> for storage.
 */
public class MapBasedFunctionDictionary implements FunctionDictionaryIfc {

    private final Map<String, FunctionIfc> functions = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Creates a new function dictionary with the specified list of functions.
     *
     * @param functions variable number of arguments that specify the function names and definitions
     *                  that will initially be added.
     * @return A newly created function dictionary with the specified functions.
     */
    @SuppressWarnings({"unchecked", "varargs"})
    public static FunctionDictionaryIfc ofFunctions(Map.Entry<String, FunctionIfc>... functions) {
        FunctionDictionaryIfc dictionary = new MapBasedFunctionDictionary();
        stream(functions).forEach(entry -> dictionary.addFunction(entry.getKey(), entry.getValue()));
        return dictionary;
    }

    @Override
    public FunctionIfc getFunction(String functionName) {
        return functions.get(functionName);
    }

    @Override
    public void addFunction(String functionName, FunctionIfc function) {
        functions.put(functionName, function);
    }
}
