package calculator.digital;

import calculator.Formula;

/**
 * Created by Qi on 2015/10/28.
 */
public class StringDigital extends Digital {

    private final StringBuilder data;

    public StringDigital(Formula formula) {
        data = new StringBuilder();
        while (formula.hasNext() && isDigital(formula.readahead())) {
            data.append(formula.getchar());
        }
    }

    @Override
    public Double getDigital() {
        return Double.valueOf(data.toString());
    }
}
