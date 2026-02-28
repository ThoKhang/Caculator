package model;

public class CalculusProcessor {
    private static final double H = 1e-5;
    private static final double EPSILON = 1e-6;
    private CalculatorEngine calculatorEngine;
    // giải tích
    public CalculusProcessor(CalculatorEngine calculatorEngine) {
        this.calculatorEngine = calculatorEngine;
    }

    /**
     * Tính đạo hàm của hàm số tại một điểm
     * Sử dụng công thức sai phân trung tâm: f'(x) ≈ (f(x+h) - f(x-h)) / (2h)
     */
    public double derivative(String expression, String variable, double atPoint) throws Exception {
        double f_plus = evaluateWithVariable(expression, variable, atPoint + H);
        double f_minus = evaluateWithVariable(expression, variable, atPoint - H);

        double result = (f_plus - f_minus) / (2 * H);

        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new Exception("Math ERROR");
        }
        return result;
    }

    /**
     * Tính tích phân xác định sử dụng Simpson's Rule
     * Công thức: ∫[a,b] f(x)dx ≈ (h/3) * [f(a) + 4*Σf(odd) + 2*Σf(even) + f(b)]
     */
    public double integral(String expression, String variable, double lowerBound, double upperBound) throws Exception {
        if (lowerBound >= upperBound) {
            throw new Exception("Invalid bounds");
        }

        int n = 1000;
        double h = (upperBound - lowerBound) / n;
        double sum = 0;

        double f_a = evaluateWithVariable(expression, variable, lowerBound);
        double f_b = evaluateWithVariable(expression, variable, upperBound);

        sum = f_a + f_b;

        for (int i = 1; i < n; i += 2) {
            double x = lowerBound + i * h;
            sum += 4 * evaluateWithVariable(expression, variable, x);
        }

        for (int i = 2; i < n; i += 2) {
            double x = lowerBound + i * h;
            sum += 2 * evaluateWithVariable(expression, variable, x);
        }

        double result = (h / 3) * sum;

        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new Exception("Math ERROR");
        }
        return result;
    }

    /**
     * Đánh giá biểu thức với giá trị của biến
     */
    private double evaluateWithVariable(String expression, String variable, double value) throws Exception {
        String modifiedExpr = expression.replace(variable, "(" + value + ")");
        modifiedExpr = modifiedExpr.replace("×", "*");
        modifiedExpr = modifiedExpr.replace("÷", "/");
        modifiedExpr = modifiedExpr.replace("²", "^2");
        modifiedExpr = modifiedExpr.replace("⁻¹", "^(-1)");
        modifiedExpr = modifiedExpr.replace("√", "sqrt");
        modifiedExpr = modifiedExpr.replace("(-", "(0-");

        return calculatorEngine.evaluateExpressionPublic(modifiedExpr);
    }
}