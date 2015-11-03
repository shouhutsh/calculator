package calculator;

/**
 * Created by Qi on 2015/10/28.
 */
public class Digital extends Node {

    private final double data;

    public Digital(double data) {
        this.data = data;
    }

    public Digital(Formula formula) {
        StringBuilder sb = new StringBuilder();
        while (formula.hasNext() && isDigital(formula.readahead())) {
            sb.append(formula.getchar());
        }
        data = Double.valueOf(sb.toString());
    }

    public double getDigital() {
        return data;
    }

    public static boolean isDigital(char c) {
        return "0123456789.".contains(c + "");
    }
}
