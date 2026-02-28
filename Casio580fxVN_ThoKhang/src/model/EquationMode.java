package model;

import java.util.ArrayList;
import java.util.List;

public class EquationMode {
    public enum Mode {
        NORMAL,
        MENU,
        EQUATION_MENU,
        POLYNOMIAL,
        LINEAR_SYSTEM,
        TABLE_INPUT_FUNC,
        TABLE_INPUT_START,
        TABLE_INPUT_END,
        TABLE_INPUT_STEP,
        BASEN_MAIN
    }

    public enum EquationType {
        NONE, QUADRATIC, CUBIC, QUARTIC, LINEAR_2, LINEAR_3,
    }

    private Mode currentMode;
    private EquationType equationType;
    private List<Double> coefficients;
    private int currentCoefficientIndex;
    private String[] coefficientNames;
    private String currentInput;

    // Biến riêng cho Table Mode
    private String tableFunction;
    private double tableStart, tableEnd, tableStep;

    public EquationMode() { reset(); }

    public void enterMenu() { currentMode = Mode.MENU; currentInput = ""; }

    public void reset() {
        currentMode = Mode.NORMAL;
        equationType = EquationType.NONE;
        coefficients = new ArrayList<>();
        currentCoefficientIndex = 0;
        currentInput = "";
        tableFunction = "";
    }

    public void enterEquationMenu() { currentMode = Mode.EQUATION_MENU; currentInput = ""; }

    public void selectPolynomial(int degree) {
        currentMode = Mode.POLYNOMIAL;
        switch (degree) {
            case 2: equationType = EquationType.QUADRATIC; coefficientNames = new String[]{"a", "b", "c"}; break;
            case 3: equationType = EquationType.CUBIC; coefficientNames = new String[]{"a", "b", "c", "d"}; break;
            case 4: equationType = EquationType.QUARTIC; coefficientNames = new String[]{"a", "b", "c", "d", "e"}; break;
        }
        coefficients.clear(); currentCoefficientIndex = 0; currentInput = "";
    }

    public void selectLinearSystem(int variables) {
        currentMode = Mode.LINEAR_SYSTEM;
        switch (variables) {
            case 2: equationType = EquationType.LINEAR_2; coefficientNames = new String[]{"a₁", "b₁", "c₁", "a₂", "b₂", "c₂"}; break;
            case 3: equationType = EquationType.LINEAR_3; coefficientNames = new String[]{"a₁", "b₁", "c₁", "d₁", "a₂", "b₂", "c₂", "d₂", "a₃", "b₃", "c₃", "d₃"}; break;
        }
        coefficients.clear(); currentCoefficientIndex = 0; currentInput = "";
    }

    // --- LOGIC BẢNG GIÁ TRỊ (TABLE) ---
    public void startTableMode() { currentMode = Mode.TABLE_INPUT_FUNC; currentInput = ""; tableFunction = ""; }
    public void confirmTableFunction(String func) { this.tableFunction = func; currentMode = Mode.TABLE_INPUT_START; currentInput = ""; }

    public boolean confirmTableParam() {
        try {
            double val = Double.parseDouble(currentInput);
            if (currentMode == Mode.TABLE_INPUT_START) { tableStart = val; currentMode = Mode.TABLE_INPUT_END; }
            else if (currentMode == Mode.TABLE_INPUT_END) { tableEnd = val; currentMode = Mode.TABLE_INPUT_STEP; }
            else if (currentMode == Mode.TABLE_INPUT_STEP) {
                tableStep = val; if (tableStep == 0) tableStep = 1;
                return true;
            }
            currentInput = ""; return false;
        } catch (Exception e) { return false; }
    }

    public void startBaseNMode() { currentMode = Mode.BASEN_MAIN; currentInput = ""; }

    public void inputDigit(String digit) { currentInput += digit; }
    public void inputDecimal() { if (!currentInput.contains(".")) { if (currentInput.isEmpty()) currentInput = "0."; else currentInput += "."; } }
    public void inputNegative() { if (currentInput.isEmpty() || currentInput.equals("0")) currentInput = "-"; else if (currentInput.startsWith("-")) currentInput = currentInput.substring(1); else currentInput = "-" + currentInput; }
    public void deleteLastChar() { if (!currentInput.isEmpty()) currentInput = currentInput.substring(0, currentInput.length() - 1); }
    public void appendRaw(String str) { currentInput += str; }

    public boolean confirmCoefficient() {
        try {
            double value = currentInput.isEmpty() ? 0 : Double.parseDouble(currentInput);
            coefficients.add(value); currentCoefficientIndex++; currentInput = ""; return true;
        } catch (NumberFormatException e) { return false; }
    }

    public boolean isInputComplete() { return currentCoefficientIndex >= coefficientNames.length; }

    public String[] solve() {
        if (!isInputComplete()) return new String[]{"Chưa đủ dữ liệu"};
        switch (equationType) {
            case QUADRATIC: return EquationSolver.solveQuadratic(coefficients.get(0), coefficients.get(1), coefficients.get(2));
            case CUBIC: return EquationSolver.solveCubic(coefficients.get(0), coefficients.get(1), coefficients.get(2), coefficients.get(3));
            case QUARTIC: return EquationSolver.solveQuartic(coefficients.get(0), coefficients.get(1), coefficients.get(2), coefficients.get(3), coefficients.get(4));
            case LINEAR_2: return EquationSolver.solveLinearSystem2(coefficients.get(0), coefficients.get(1), coefficients.get(2), coefficients.get(3), coefficients.get(4), coefficients.get(5));
            case LINEAR_3: return EquationSolver.solveLinearSystem3(coefficients.get(0), coefficients.get(1), coefficients.get(2), coefficients.get(3), coefficients.get(4), coefficients.get(5), coefficients.get(6), coefficients.get(7), coefficients.get(8), coefficients.get(9), coefficients.get(10), coefficients.get(11));
            default: return new String[]{"Lỗi"};
        }
    }

    // --- GETTERS ---
    public Mode getCurrentMode() { return currentMode; }
    public String getCurrentInput() { return currentInput; }
    public String getTableFunction() { return tableFunction; }
    public double getTableStart() { return tableStart; }
    public double getTableEnd() { return tableEnd; }
    public double getTableStep() { return tableStep; }

    // --- VIỆT HÓA PROMPT ---
    public String getCurrentPrompt() {
        switch (currentMode) {
            case MENU: return "MENU CHỨC NĂNG\n1:Phương trình / Hệ PT\n2:Bảng giá trị (Table)\n3:Hệ đếm (Base-N)";
            case EQUATION_MENU: return "1:Hệ phương trình\n2:Phương trình đa thức";

            case POLYNOMIAL: case LINEAR_SYSTEM:
                if (currentCoefficientIndex < coefficientNames.length) return coefficientNames[currentCoefficientIndex] + "?";
                else return "Nhấn = để xem đáp án";

            case TABLE_INPUT_FUNC: return "f(x) = ";
            case TABLE_INPUT_START: return "Bắt đầu?";
            case TABLE_INPUT_END: return "Kết thúc?";
            case TABLE_INPUT_STEP: return "Bước nhảy?";

            case BASEN_MAIN: return "DEC [=] để đổi cơ số";

            default: return "";
        }
    }

    public String getEquationDisplay() {
        switch (equationType) {
            case QUADRATIC: return "ax²+bx+c=0";
            case CUBIC: return "ax³+bx²+cx+d=0";
            case QUARTIC: return "ax⁴+bx³+cx²+dx+e=0";
            case LINEAR_2: return "a₁x+b₁y=c₁\na₂x+b₂y=c₂";
            case LINEAR_3: return "a₁x+b₁y+c₁z=d₁\na₂x+b₂y+c₂z=d₂\na₃x+b₃y+c₃z=d₃";
            default: return "";
        }
    }
}