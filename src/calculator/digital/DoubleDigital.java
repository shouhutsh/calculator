package calculator.digital;

/**
 * Created by Qi on 2015/10/28.
 */
public class DoubleDigital extends Digital {

    private final Double data;

    public DoubleDigital(double data) {
        this.data = data;
    }

    @Override
    public Double getDigital() {
        return data;
    }
}
