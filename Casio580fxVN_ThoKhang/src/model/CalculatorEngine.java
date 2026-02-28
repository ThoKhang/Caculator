package model;

import java.util.*;
import java.text.DecimalFormat;

public class CalculatorEngine {
    // ... (Giữ nguyên các biến và constructor như cũ)
    private StringBuilder currentInput;
    private String currentResult;
    private double lastResult;
    private double memory;
    private boolean shiftMode;
    private boolean alphaMode;
    private String angleMode;
    private boolean hasResult;
    private EquationMode equationMode;
    private OptionMenu optionMenu;
    private boolean isPoweredOn;
    private int cursorPosition;
    private List<String> history;
    private int historyIndex;
    private CalculusProcessor calculusProcessor;
    private boolean isCalculusMode;
    private String currentCalculusType;

    public CalculatorEngine() {
        isPoweredOn = false;
        history = new ArrayList<>();
        historyIndex = -1;
        isCalculusMode = false;
        currentCalculusType = "";
        optionMenu = new OptionMenu();
        initializeState();
        calculusProcessor = new CalculusProcessor(this);
    }

    private void initializeState() {
        currentInput = new StringBuilder();
        currentResult = "";
        lastResult = 0;
        memory = 0;
        shiftMode = false;
        alphaMode = false;
        angleMode = optionMenu.getAngleMode();
        hasResult = false;
        equationMode = new EquationMode();
        cursorPosition = 0;
        isCalculusMode = false;
        currentCalculusType = "";
    }

    public void powerOn() { isPoweredOn = true; initializeState(); }
    public void powerOff() { isPoweredOn = false; currentInput = new StringBuilder(); currentResult = ""; }
    public boolean isPoweredOn() { return isPoweredOn; }

    // --- PROCESS INPUT ---
    public void processInput(String input) {
        if (!isPoweredOn) { if (input.equals("ON")) powerOn(); return; }
        if (shiftMode && input.equals("AC")) { powerOff(); return; }

        if (optionMenu.getCurrentMode() != OptionMenu.Mode.NORMAL) {
            if (input.equals("▲")) optionMenu.scrollUp();
            else if (input.equals("▼")) optionMenu.scrollDown();
            else handleOptionMenuInput(input);
            return;
        }

        if (equationMode.getCurrentMode() != EquationMode.Mode.NORMAL) {
            handleEquationModeInput(input);
            return;
        }

        if (input.equals("SHIFT")) { shiftMode = !shiftMode; return; }
        if (input.equals("ALPHA")) { alphaMode = !alphaMode; return; }
        if (input.equals("OPTN")) { enterOptionMenu(); return; }

        if (input.equals("◄")) { moveCursorLeft(); return; }
        if (input.equals("►")) { moveCursorRight(); return; }
        if (input.equals("▲")) { navigateHistoryUp(); return; }
        if (input.equals("▼")) { navigateHistoryDown(); return; }

        String actualInput = shiftMode ? getShiftFunction(input) : input;
        if (shiftMode && !input.equals("SHIFT")) shiftMode = false;

        if (isCalculusMode) { handleCalculusInput(input, actualInput); return; }

        switch (actualInput) {
            case "ON": initializeState(); break;
            case "AC": clear(); break;
            case "DEL": deleteLastChar(); break;
            case "=": calculate(); break;
            case "+": case "-": case "×": case "÷": handleOperator(actualInput); break;
            case "0": case "1": case "2": case "3": case "4":
            case "5": case "6": case "7": case "8": case "9": handleNumber(actualInput); break;
            case ".": handleDecimal(); break;
            case ",": handleComma(); break;
            case "x²": handleSquare(); break;
            case "√□": handleFunction("√("); break;
            case "x□": handlePower(); break;
            case "x⁻¹": handleReciprocal(); break;
            case "sin": handleFunction("sin("); break;
            case "cos": handleFunction("cos("); break;
            case "tan": handleFunction("tan("); break;
            case "sin⁻¹": handleFunction("asin("); break;
            case "cos⁻¹": handleFunction("acos("); break;
            case "tan⁻¹": handleFunction("atan("); break;
            case "log□": handleFunction("log("); break;
            case "ln": handleFunction("ln("); break;
            case "10^x": handle10Power(); break;
            case "e^x": handleEPower(); break;
            case "□/□": handleFraction(); break;
            case "(": case ")": handleParenthesis(actualInput); break;
            case "Ans": handleAns(); break;
            case "×10ˣ": handleScientificNotation(); break;
            case "M+": memoryAdd(); break;
            case "M-": memorySubtract(); break;
            case "MR": memoryRecall(); break;
            case "MC": memoryClear(); break;
            case "Abs": handleFunction("abs("); break;
            case "(-)": handleNegate(); break;
            case "x": handleVariable("x"); break;
            case "∫dx": startIntegral(); break;
            case "d/dx": startDerivative(); break;
            case "MENU": enterMenu(); break;
            default: break;
        }
    }

    // --- GETTERS ---
    public boolean isShiftMode() { return shiftMode; }
    public boolean isAlphaMode() { return alphaMode; }
    public String getAngleMode() { return angleMode; }
    public boolean hasResult() { return hasResult; }
    public String getResultText() { return currentResult; }
    public OptionMenu getOptionMenu() { return optionMenu; }
    public EquationMode getEquationMode() { return equationMode; }
    public double evaluateExpressionPublic(String expr) throws Exception { return evaluateExpression(expr); }

    public String getDisplayText() {
        if (!isPoweredOn) return "";
        if (currentInput.length() == 0 && currentResult.isEmpty()) return "|";
        if (!hasResult && cursorPosition >= 0 && cursorPosition <= currentInput.length()) {
            StringBuilder display = new StringBuilder(currentInput);
            display.insert(cursorPosition, "|");
            return display.toString();
        }
        return currentInput.toString();
    }

    // --- LOGIC MENU VÀ CÁC MODE ĐẶC BIỆT ---
    private void enterMenu() { equationMode.enterMenu(); currentInput = new StringBuilder(); currentResult = ""; hasResult = false; }

    private void handleEquationModeInput(String input) {
        EquationMode.Mode mode = equationMode.getCurrentMode();
        if (input.equals("AC")) { equationMode.reset(); clear(); return; }

        if (mode == EquationMode.Mode.MENU) {
            if (input.equals("1") || input.equals("9")) { equationMode.enterEquationMenu(); currentInput = new StringBuilder(); }
            else if (input.equals("2")) { equationMode.startTableMode(); currentInput = new StringBuilder(); }
            else if (input.equals("3")) { equationMode.startBaseNMode(); currentInput = new StringBuilder(); }
            else { equationMode.reset(); }
            return;
        }

        // --- VIỆT HÓA LOGIC MENU ---
        if (mode == EquationMode.Mode.EQUATION_MENU) {
            String currentText = currentInput.toString();
            // Đổi "Degree" thành "Bậc" và "Unknowns" thành "Số ẩn"
            if (input.equals("1") && !currentText.contains("Bậc") && !currentText.contains("Số ẩn")) {
                currentInput = new StringBuilder("Số ẩn?\n2:Hệ 2 ẩn\n3:Hệ 3 ẩn"); currentResult = "";
            } else if (input.equals("2") && !currentText.contains("Bậc") && !currentText.contains("Số ẩn")) {
                currentInput = new StringBuilder("Bậc?\n2:Bậc 2\n3:Bậc 3\n4:Bậc 4"); currentResult = "";
            } else if ("234".contains(input)) {
                if (currentText.contains("Bậc")) {
                    equationMode.selectPolynomial(Integer.parseInt(input)); currentInput = new StringBuilder(); currentResult = "";
                } else if (currentText.contains("Số ẩn")) {
                    equationMode.selectLinearSystem(Integer.parseInt(input)); currentInput = new StringBuilder(); currentResult = "";
                }
            }
            return;
        }

        if (mode == EquationMode.Mode.POLYNOMIAL || mode == EquationMode.Mode.LINEAR_SYSTEM) {
            switch (input) {
                case "0": case "1": case "2": case "3": case "4": case "5": case "6": case "7": case "8": case "9": equationMode.inputDigit(input); break;
                case ".": equationMode.inputDecimal(); break; case "(-)": equationMode.inputNegative(); break; case "DEL": equationMode.deleteLastChar(); break;
                case "=": if (!equationMode.isInputComplete()) { if (!equationMode.confirmCoefficient()) currentResult = "Lỗi nhập"; } else { String[] solutions = equationMode.solve(); currentResult = String.join("\n", solutions); hasResult = true; currentInput = new StringBuilder(equationMode.getEquationDisplay()); } break;
            }
            return;
        }

        if (mode == EquationMode.Mode.TABLE_INPUT_FUNC) {
            if (input.equals("=")) { equationMode.confirmTableFunction(equationMode.getCurrentInput()); }
            else if (input.equals("DEL")) { equationMode.deleteLastChar(); }
            else {
                String toAppend = input;
                if (input.equals("x")) toAppend = "x";
                else if (input.equals("x²")) toAppend = "^2";
                else if (input.equals("x□")) toAppend = "^";
                else if (input.equals("√□")) toAppend = "sqrt(";
                else if (input.equals("sin")) toAppend = "sin(";
                else if (input.equals("cos")) toAppend = "cos(";
                else if (input.equals("tan")) toAppend = "tan(";
                else if (input.equals("ln")) toAppend = "ln(";
                else if (input.equals("log□")) toAppend = "log(";

                if ("0123456789.+-×÷()x".contains(input) || toAppend.length() > 1) {
                    equationMode.appendRaw(toAppend);
                }
            }
            return;
        }

        if (mode == EquationMode.Mode.TABLE_INPUT_START || mode == EquationMode.Mode.TABLE_INPUT_END || mode == EquationMode.Mode.TABLE_INPUT_STEP) {
            switch (input) {
                case "0": case "1": case "2": case "3": case "4": case "5": case "6": case "7": case "8": case "9": equationMode.inputDigit(input); break;
                case ".": equationMode.inputDecimal(); break; case "(-)": equationMode.inputNegative(); break; case "DEL": equationMode.deleteLastChar(); break;
                case "=": if (equationMode.confirmTableParam()) generateTable(); break;
            }
            return;
        }

        if (mode == EquationMode.Mode.BASEN_MAIN) {
            if (input.equals("=")) convertBaseN();
            else if (input.matches("[0-9]")) equationMode.inputDigit(input);
            else if (input.equals("DEL")) equationMode.deleteLastChar();
        }
    }

    // --- HÀM TÍNH TOÁN BẢNG ---
    private void generateTable() {
        String func = equationMode.getTableFunction();
        double start = equationMode.getTableStart();
        double end = equationMode.getTableEnd();
        double step = equationMode.getTableStep();

        StringBuilder result = new StringBuilder("   X       |   F(X)\n");
        result.append("--------------------\n");

        int count = 0;
        for (double x = start; (step > 0 ? x <= end : x >= end); x += step) {
            if (count++ > 20) break;
            try {
                String expr = func.replace("x", "(" + x + ")");
                expr = expr.replace("×", "*").replace("÷", "/").replace("²", "^2").replace("√", "sqrt");
                double val = new ExpressionEvaluator().evaluate(expr);

                String xStr = (x == (long)x) ? String.format("%d", (long)x) : String.format("%.2f", x);
                String valStr = formatResult(val);
                result.append(String.format("%-8s | %s\n", xStr, valStr));
            } catch (Exception e) {
                result.append(String.format("%-8.2f | ERROR\n", x));
            }
        }
        currentResult = result.toString();
        hasResult = true;
        currentInput = new StringBuilder(); // Xóa input để hiển thị kết quả full màn hình
    }

    private void convertBaseN() {
        try {
            String decStr = equationMode.getCurrentInput();
            if (decStr.isEmpty()) return;
            long decVal = Long.parseLong(decStr);
            StringBuilder sb = new StringBuilder();
            sb.append("DEC: ").append(decVal).append("\n");
            sb.append("HEX: ").append(Long.toHexString(decVal).toUpperCase()).append("\n");
            sb.append("BIN: ").append(Long.toBinaryString(decVal)).append("\n");
            sb.append("OCT: ").append(Long.toOctalString(decVal));
            currentResult = sb.toString(); hasResult = true;
        } catch (NumberFormatException e) { currentResult = "Lỗi Toán Học"; }
    }

    // ... (Giữ nguyên phần OptionMenu, Calculus, Basic Ops, ExpressionEvaluator như cũ)
    // COPY LẠI CÁC HÀM CŨ:
    private void enterOptionMenu() { optionMenu.enterOptionMenu(); currentInput = new StringBuilder(); currentResult = ""; hasResult = false; }
    private void handleOptionMenuInput(String input) { OptionMenu.Mode mode = optionMenu.getCurrentMode(); if (input.equals("AC")) { optionMenu.reset(); clear(); return; } if (mode == OptionMenu.Mode.OPTION_MENU) { if (input.equals("1")) optionMenu.enterHyperbolicMenu(); else if (input.equals("2")) optionMenu.enterAngleModeMenu(); else if (input.equals("3")) optionMenu.enterEngineeringSymbolMenu(); else optionMenu.reset(); currentInput = new StringBuilder(); currentResult = ""; return; } if (mode == OptionMenu.Mode.ANGLE_MODE_MENU) { if (input.matches("[1-3]")) { optionMenu.setAngleMode(input); this.angleMode = optionMenu.getAngleMode(); currentResult = "Đơn vị: " + this.angleMode; hasResult = true; optionMenu.reset(); } else optionMenu.reset(); return; } if (mode == OptionMenu.Mode.HYPERBOLIC_MENU) { handleHyperbolicSelection(input); return; } if (mode == OptionMenu.Mode.ENGINEERING_SYMBOL_MENU) { handleEngineeringSymbolSelection(input); return; } }
    private void handleHyperbolicSelection(String input) { String hypFunc = null; switch (input) { case "1": hypFunc = "sinh("; break; case "2": hypFunc = "cosh("; break; case "3": hypFunc = "tanh("; break; case "4": hypFunc = "asinh("; break; case "5": hypFunc = "acosh("; break; case "6": hypFunc = "atanh("; break; default: optionMenu.reset(); return; } if (hasResult) { currentInput = new StringBuilder(); currentResult = ""; hasResult = false; cursorPosition = 0; } currentInput.insert(cursorPosition, hypFunc); cursorPosition += hypFunc.length(); optionMenu.reset(); }
    private void handleEngineeringSymbolSelection(String input) { String symbol = null; switch (input) { case "1": symbol = "×10⁻³"; break; case "2": symbol = "×10⁻⁶"; break; case "3": symbol = "×10⁻⁹"; break; case "4": symbol = "×10⁻¹²"; break; case "5": symbol = "×10⁻¹⁵"; break; case "6": symbol = "×10³"; break; case "7": symbol = "×10⁶"; break; case "8": symbol = "×10⁹"; break; case "9": symbol = "×10¹²"; break; default: optionMenu.reset(); return; } if (hasResult) { currentInput = new StringBuilder(); currentResult = ""; hasResult = false; cursorPosition = 0; } currentInput.insert(cursorPosition, symbol); cursorPosition += symbol.length(); optionMenu.reset(); }
    private void handleNormalInputForModes(String input) { if ("0123456789".contains(input)) handleNumber(input); else if ("+-×÷".contains(input)) handleOperator(input); else if (input.equals("x")) handleVariable("x"); else if (input.equals("DEL")) deleteLastChar(); else if (input.equals(".")) handleDecimal(); else if (input.equals("(")) handleParenthesis("("); else if (input.equals(")")) handleParenthesis(")"); else if (input.equals("x²")) handleSquare(); else if (input.equals("x□")) handlePower(); }
    private void calculate() { if (currentInput.length() == 0) return; try { String expression = currentInput.toString(); if (!expression.isEmpty() && (history.isEmpty() || !history.get(history.size() - 1).equals(expression))) { history.add(expression); if (history.size() > 50) history.remove(0); } historyIndex = -1; double result = evaluateExpression(expression); lastResult = result; currentResult = formatResult(result); hasResult = true; cursorPosition = currentInput.length(); } catch (Exception e) { currentResult = "Lỗi Cú Pháp"; hasResult = true; } }
    private String formatResult(double value) { if (Double.isNaN(value) || Double.isInfinite(value)) return "Lỗi Toán Học"; if (Math.abs(value) < 1e-10) return "0"; if (value == Math.floor(value) && Math.abs(value) < 1e10) return String.format("%.0f", value); if (Math.abs(value) >= 1e10 || (Math.abs(value) < 1e-3 && value != 0)) return String.format("%.6E", value); DecimalFormat df = new DecimalFormat("#.##########"); return df.format(value); }
    private void calculateDerivative() throws Exception { String input = currentInput.toString().trim(); int lastComma = input.lastIndexOf(","); if (lastComma == -1) { currentResult = "ERROR: d/dx(f,x0)"; return; } String expression = input.substring(0, lastComma).trim(); String pointStr = input.substring(lastComma + 1).trim(); if (expression.isEmpty() || pointStr.isEmpty()) { currentResult = "ERROR: Nhập đầy đủ"; return; } try { double point = Double.parseDouble(pointStr); double result = calculusProcessor.derivative(expression, "x", point); currentResult = "f'(" + point + ")=" + formatResult(result); lastResult = result; hasResult = true; } catch (Exception ex) { currentResult = "Math ERROR"; } }
    private void calculateIntegral() throws Exception { String input = currentInput.toString().trim(); String[] parts = input.split(","); if (parts.length != 3) { currentResult = "ERROR: ∫(f,a,b)"; return; } try { double lower = Double.parseDouble(parts[1].trim()); double upper = Double.parseDouble(parts[2].trim()); double result = calculusProcessor.integral(parts[0].trim(), "x", lower, upper); currentResult = "∫[" + lower + "," + upper + "]=" + formatResult(result); lastResult = result; hasResult = true; } catch (Exception ex) { currentResult = "Math ERROR"; } }
    private void startDerivative() { isCalculusMode = true; currentCalculusType = "derivative"; currentInput = new StringBuilder(); currentResult = "d/dx(f(x),x0)="; hasResult = false; cursorPosition = 0; }
    private void startIntegral() { isCalculusMode = true; currentCalculusType = "integral"; currentInput = new StringBuilder(); currentResult = "∫(f(x),a,b)="; hasResult = false; cursorPosition = 0; }
    private void handleCalculusInput(String input, String actualInput) { if (input.equals("AC")) { isCalculusMode = false; clear(); return; } if (actualInput.equals("=")) { try { if (currentCalculusType.equals("derivative")) calculateDerivative(); else calculateIntegral(); } catch (Exception e) { currentResult = "Math ERROR"; } isCalculusMode = false; return; } switch (actualInput) { case "0": case "1": case "2": case "3": case "4": case "5": case "6": case "7": case "8": case "9": handleNumber(actualInput); break; case ".": handleDecimal(); break; case "+": case "-": case "×": case "÷": handleOperator(actualInput); break; case "x": handleVariable("x"); break; case "x□": handlePower(); break; case "(": case ")": handleParenthesis(actualInput); break; case "x²": handleSquare(); break; case "sin": handleFunction("sin("); break; case "cos": handleFunction("cos("); break; case "tan": handleFunction("tan("); break; case "ln": handleFunction("ln("); break; case "log□": handleFunction("log("); break; case "√□": handleFunction("√("); break; case "Abs": handleFunction("abs("); break; case ",": handleComma(); break; case "DEL": deleteLastChar(); break; default: break; } }
    private void moveCursorLeft() { if (cursorPosition > 0) cursorPosition--; }
    private void moveCursorRight() { if (cursorPosition < currentInput.length()) cursorPosition++; }
    private void navigateHistoryUp() { if (history.isEmpty()) return; if (historyIndex == -1) historyIndex = history.size() - 1; else if (historyIndex > 0) historyIndex--; if (historyIndex >= 0 && historyIndex < history.size()) { currentInput = new StringBuilder(history.get(historyIndex)); cursorPosition = currentInput.length(); hasResult = false; currentResult = ""; } }
    private void navigateHistoryDown() { if (history.isEmpty() || historyIndex == -1) return; if (historyIndex < history.size() - 1) { historyIndex++; currentInput = new StringBuilder(history.get(historyIndex)); cursorPosition = currentInput.length(); } else { historyIndex = -1; currentInput = new StringBuilder(); cursorPosition = 0; } hasResult = false; currentResult = ""; }
    private String getShiftFunction(String input) { Map<String, String> shiftMap = new HashMap<>(); shiftMap.put("sin", "sin⁻¹"); shiftMap.put("cos", "cos⁻¹"); shiftMap.put("tan", "tan⁻¹"); shiftMap.put("log□", "10^x"); shiftMap.put("ln", "e^x"); shiftMap.put("x²", "√"); shiftMap.put("M+", "MR"); shiftMap.put("M-", "MC"); return shiftMap.getOrDefault(input, input); }
    private void clear() { currentInput = new StringBuilder(); currentResult = ""; hasResult = false; cursorPosition = 0; }
    private void deleteLastChar() { if (currentInput.length() > 0) { if (cursorPosition > 0) { currentInput.deleteCharAt(cursorPosition - 1); cursorPosition--; } currentResult = ""; hasResult = false; } }
    private void handleNumber(String num) { if (hasResult) { currentInput = new StringBuilder(); currentResult = ""; hasResult = false; cursorPosition = 0; } currentInput.insert(cursorPosition, num); cursorPosition++; }
    private void handleDecimal() { if (hasResult) { currentInput = new StringBuilder("0"); currentResult = ""; hasResult = false; cursorPosition = 1; } if (!currentInput.toString().contains(".")) { currentInput.insert(cursorPosition, "."); cursorPosition++; } }
    private void handleComma() { currentInput.insert(cursorPosition, ","); cursorPosition++; }
    private void handleOperator(String op) { if (hasResult) { hasResult = false; currentResult = ""; } currentInput.insert(cursorPosition, op); cursorPosition++; }
    private void handleParenthesis(String p) { if (hasResult && p.equals("(")) { currentInput = new StringBuilder(); hasResult = false; cursorPosition = 0; } currentInput.insert(cursorPosition, p); cursorPosition++; }
    private void handleFunction(String f) { if (hasResult) { currentInput = new StringBuilder(); hasResult = false; cursorPosition = 0; } currentInput.insert(cursorPosition, f); cursorPosition += f.length(); }
    private void handleSquare() { currentInput.insert(cursorPosition, "²"); cursorPosition++; }
    private void handlePower() { currentInput.insert(cursorPosition, "^"); cursorPosition++; }
    private void handleReciprocal() { if (currentInput.length() > 0) { currentInput.insert(0, "("); currentInput.append(")⁻¹"); cursorPosition = currentInput.length(); } }
    private void handle10Power() { handleFunction("10^("); }
    private void handleEPower() { handleFunction("e^("); }
    private void handleFraction() { handleOperator("÷"); }
    private void handleNegate() { currentInput.insert(cursorPosition, "(-"); cursorPosition += 2; }
    private void handleVariable(String v) { handleFunction(v); }
    private void handleScientificNotation() { handleFunction("×10^"); }
    private void handleAns() { if (hasResult) { currentInput = new StringBuilder(); hasResult = false; cursorPosition = 0; } String ans = formatResult(lastResult); currentInput.insert(cursorPosition, ans); cursorPosition += ans.length(); }
    private void memoryAdd() { try { if (hasResult && !currentResult.isEmpty()) memory += Double.parseDouble(currentResult); else if (currentInput.length() > 0) memory += evaluateExpression(currentInput.toString()); } catch (Exception e) {} }
    private void memorySubtract() { try { if (hasResult && !currentResult.isEmpty()) memory -= Double.parseDouble(currentResult); else if (currentInput.length() > 0) memory -= evaluateExpression(currentInput.toString()); } catch (Exception e) {} }
    private void memoryRecall() { if (hasResult) { currentInput = new StringBuilder(); hasResult = false; cursorPosition = 0; } String mem = formatResult(memory); currentInput.insert(cursorPosition, mem); cursorPosition += mem.length(); currentResult = ""; }
    private void memoryClear() { memory = 0; }
    private double evaluateExpression(String expr) throws Exception { expr = expr.trim(); expr = expr.replace("×", "*"); expr = expr.replace("÷", "/"); expr = expr.replace("²", "^2"); expr = expr.replace("⁻¹", "^(-1)"); expr = expr.replace("√", "sqrt"); expr = expr.replace("(-", "(0-"); return new ExpressionEvaluator().evaluate(expr); }

    public class ExpressionEvaluator {
        private int pos = -1, ch;
        private String str;
        public double evaluate(String expression) throws Exception { this.str = expression; this.pos = -1; nextChar(); double result = parseExpression(); if (pos < str.length()) throw new Exception("Unexpected: " + (char)ch); return result; }
        private void nextChar() { ch = (++pos < str.length()) ? str.charAt(pos) : -1; }
        private boolean eat(int charToEat) { while (ch == ' ') nextChar(); if (ch == charToEat) { nextChar(); return true; } return false; }
        private double parseExpression() throws Exception { double x = parseTerm(); for (;;) { if (eat('+')) x += parseTerm(); else if (eat('-')) x -= parseTerm(); else return x; } }
        private double parseTerm() throws Exception { double x = parseFactor(); for (;;) { if (eat('*')) x *= parseFactor(); else if (eat('/')) x /= parseFactor(); else return x; } }
        private double parseFactor() throws Exception { if (eat('+')) return parseFactor(); if (eat('-')) return -parseFactor(); double x; int startPos = this.pos; if (eat('(')) { x = parseExpression(); eat(')'); } else if ((ch >= '0' && ch <= '9') || ch == '.') { while ((ch >= '0' && ch <= '9') || ch == '.') nextChar(); x = Double.parseDouble(str.substring(startPos, this.pos)); } else if (ch >= 'a' && ch <= 'z') { while (ch >= 'a' && ch <= 'z') nextChar(); String func = str.substring(startPos, this.pos); if (func.equals("e")) x = Math.E; else if (func.equals("pi")) x = Math.PI; else { eat('('); x = parseExpression(); eat(')'); x = applyFunction(func, x); } } else { throw new Exception("Unexpected: " + (char)ch); } if (eat('^')) x = Math.pow(x, parseFactor()); return x; }
        private double applyFunction(String func, double x) throws Exception { switch (func) { case "sin": return angleMode.equals("DEG") ? Math.sin(Math.toRadians(x)) : Math.sin(x); case "cos": return angleMode.equals("DEG") ? Math.cos(Math.toRadians(x)) : Math.cos(x); case "tan": return angleMode.equals("DEG") ? Math.tan(Math.toRadians(x)) : Math.tan(x); case "asin": return angleMode.equals("DEG") ? Math.toDegrees(Math.asin(x)) : Math.asin(x); case "acos": return angleMode.equals("DEG") ? Math.toDegrees(Math.acos(x)) : Math.acos(x); case "atan": return angleMode.equals("DEG") ? Math.toDegrees(Math.atan(x)) : Math.atan(x); case "sqrt": return Math.sqrt(x); case "log": return Math.log10(x); case "ln": return Math.log(x); case "abs": return Math.abs(x); default: throw new Exception("Unknown function: " + func); } }
    }
}