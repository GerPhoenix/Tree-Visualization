package graphvisualizer;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;

/**
 * css wrapper class to easily generate dynamic css
 */
public class CssGenerator {
    private String element;
    private String selector;
    private String id;
    private HashMap<String, String> keyValues;

    public CssGenerator(String element) {
        this(element, "", "");
    }

    public CssGenerator(String element, String selector, String id) {
        this.element = element;
        this.selector = selector;
        this.id = id;
        this.keyValues = new HashMap<>();
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, String> getKeyValues() {
        return keyValues;
    }

    public void set(String key, String value) {
        this.keyValues.put(key, value);
    }

    public String get(String key) {
        return this.keyValues.get(key);
    }

    /**
     * @return css compliant String
     */
    public String toString() {
        return CssGenerator.build(element + selector + " " + id, keyValues.entrySet().stream().reduce("", (str, b) -> str + b.getKey() + ": " + b.getValue() + ";", (a, b) -> a + b));
    }

    private static String build(String cssElement, String... keyValuePair) {
        return cssElement + "{" + Arrays.stream(keyValuePair).reduce("", (a, b) -> a + b) + "}";
    }

    /**
     * @param color AWT Color Object
     * @return css compliant rgb String
     */
    public static String rgbString(Color color) {
        return "rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
    }


}
