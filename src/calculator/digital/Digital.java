package calculator.digital;

import calculator.Node;

/**
 * Created by Qi on 2015/10/28.
 */
public abstract class Digital extends Node {

    public abstract Double getDigital();

    public static boolean isDigital(char c) {
        return "0123456789.".contains(c + "");
    }
}
