//// MenuManager.java - Complete Menu Management System
//import java.util.*;
//
//public class MenuManager {
//
//    public enum MenuLevel {
//        MAIN,
//        STATISTICS,
//        MATRIX,
//        VECTOR,
//        DISTRIBUTION,
//        INEQUALITY,
//        RATIO,
//        TABLE,
//        SETTINGS,
//        ANGLE_UNIT,
//        DISPLAY_FORMAT,
//        DECIMAL_PLACE,
//        EQUATION_MAIN,
//        EQUATION_POLYNOMIAL,
//        EQUATION_LINEAR
//    }
//
//    private MenuLevel currentLevel;
//    private MenuLevel previousLevel;
//    private String selectedItem;
//    private Map<MenuLevel, List<String>> menuItems;
//    private int fixedDecimalPlaces = 2;
//    private String angleUnit = "DEG";
//    private String displayFormat = "NORM";
//    private String language = "VN";
//
//    public MenuManager() {
//        initializeMenuStructure();
//        currentLevel = MenuLevel.MAIN;
//        previousLevel = MenuLevel.MAIN;
//        selectedItem = "";
//    }
//
//    private void initializeMenuStructure() {
//        menuItems = new HashMap<>();
//
//        // Main Menu
//        menuItems.put(MenuLevel.MAIN, Arrays.asList(
//                "1:Thống kê",
//                "2:Ma trận",
//                "3:Vectơ",
//                "4:Phân phối",
//                "5:Bất phương trình",
//                "6:Tỉ lệ",
//                "7:Bảng hàm",
//                "8:Cài đặt",
//                "9:Phương trình"
//        ));
//
//        // Statistics
//        menuItems.put(MenuLevel.STATISTICS, Arrays.asList(
//                "1:Thống kê 1 biến",
//                "2:Thống kê 2 biến",
//                "3:Hồi quy"
//        ));
//
//        // Matrix
//        menuItems.put(MenuLevel.MATRIX, Arrays.asList(
//                "1:Tạo ma trận",
//                "2:Sửa ma trận",
//                "3:Xóa ma trận"
//        ));
//
//        // Vector
//        menuItems.put(MenuLevel.VECTOR, Arrays.asList(
//                "1:Tạo vectơ",
//                "2:Sửa vectơ",
//                "3:Xóa vectơ"
//        ));
//
//        // Distribution
//        menuItems.put(MenuLevel.DISTRIBUTION, Arrays.asList(
//                "1:Phân phối chuẩn",
//                "2:Phân phối nhị thức",
//                "3:Phân phối Poisson"
//        ));
//
//        // Inequality
//        menuItems.put(MenuLevel.INEQUALITY, Arrays.asList(
//                "1:Bất phương trình bậc 1",
//                "2:Bất phương trình bậc 2"
//        ));
//
//        // Ratio
//        menuItems.put(MenuLevel.RATIO, Arrays.asList(
//                "1:Rút gọn phân số",
//                "2:Chuyển đổi"
//        ));
//
//        // Table
//        menuItems.put(MenuLevel.TABLE, Arrays.asList(
//                "1:Bảng f(x)",
//                "2:Bảng tham số"
//        ));
//
//        // Settings
//        menuItems.put(MenuLevel.SETTINGS, Arrays.asList(
//                "1:Đơn vị góc",
//                "2:Định dạng hiển thị",
//                "3:Số chữ số thập phân",
//                "4:Độ tương phản",
//                "5:Ngôn ngữ"
//        ));
//
//        // Angle Unit
//        menuItems.put(MenuLevel.ANGLE_UNIT, Arrays.asList(
//                "1:Độ (Degree)",
//                "2:Radian",
//                "3:Gradian"
//        ));
//
//        // Display Format
//        menuItems.put(MenuLevel.DISPLAY_FORMAT, Arrays.asList(
//                "1:Fix (Cố định)",
//                "2:Scientific (Khoa học)",
//                "3:Normal (Thường)",
//                "4:Engineering (Kỹ thuật)"
//        ));
//
//        // Equation Main Menu
//        menuItems.put(MenuLevel.EQUATION_MAIN, Arrays.asList(
//                "1:Phương trình bậc 2",
//                "2:Phương trình bậc 3",
//                "3:Phương trình bậc 4",
//                "4:Hệ phương trình 2 ẩn",
//                "5:Hệ phương trình 3 ẩn"
//        ));
//    }
//
//    public void resetToMain() {
//        currentLevel = MenuLevel.MAIN;
//        previousLevel = MenuLevel.MAIN;
//        selectedItem = "";
//    }
//
//    public void selectMenuItem(String code) {
//        // Chuyển đến menu con tương ứng
//        switch (code) {
//            case "1":
//                if (currentLevel == MenuLevel.MAIN) {
//                    currentLevel = MenuLevel.STATISTICS;
//                } else if (currentLevel == MenuLevel.SETTINGS) {
//                    currentLevel = MenuLevel.ANGLE_UNIT;
//                } else if (currentLevel == MenuLevel.EQUATION_MAIN) {
//                    // Xử lý phương trình bậc 2
//                }
//                break;
//            case "2":
//                if (currentLevel == MenuLevel.MAIN) {
//                    currentLevel = MenuLevel.MATRIX;
//                } else if (currentLevel == MenuLevel.SETTINGS) {
//                    currentLevel = MenuLevel.DISPLAY_FORMAT;
//                }
//                break;
//            case "3":
//                if (currentLevel == MenuLevel.MAIN) {
//                    currentLevel = MenuLevel.VECTOR;
//                }
//                break;
//            case "4":
//                if (currentLevel == MenuLevel.MAIN) {
//                    currentLevel = MenuLevel.DISTRIBUTION;
//                }
//                break;
//            case "5":
//                if (currentLevel == MenuLevel.MAIN) {
//                    currentLevel = MenuLevel.INEQUALITY;
//                }
//                break;
//            case "6":
//                if (currentLevel == MenuLevel.MAIN) {
//                    currentLevel = MenuLevel.RATIO;
//                }
//                break;
//            case "7":
//                if (currentLevel == MenuLevel.MAIN) {
//                    currentLevel = MenuLevel.TABLE;
//                }
//                break;
//            case "8":
//                if (currentLevel == MenuLevel.MAIN) {
//                    currentLevel = MenuLevel.SETTINGS;
//                }
//                break;
//            case "9":
//                if (currentLevel == MenuLevel.MAIN) {
//                    currentLevel = MenuLevel.EQUATION_MAIN;
//                }
//                break;
//        }
//    }
//
//    public void goBack() {
//        if (currentLevel != MenuLevel.MAIN) {
//            if (currentLevel == MenuLevel.ANGLE_UNIT ||
//                    currentLevel == MenuLevel.DISPLAY_FORMAT ||
//                    currentLevel == MenuLevel.DECIMAL_PLACE) {
//                currentLevel = MenuLevel.SETTINGS;
//            } else if (currentLevel == MenuLevel.EQUATION_POLYNOMIAL ||
//                    currentLevel == MenuLevel.EQUATION_LINEAR) {
//                currentLevel = MenuLevel.EQUATION_MAIN;
//            } else {
//                currentLevel = MenuLevel.MAIN;
//            }
//        }
//    }
//
//    public MenuLevel getCurrentLevel() {
//        return currentLevel;
//    }
//
//    public String getSelectedItem() {
//        return selectedItem;
//    }
//
//    public String processMenuSelection(String input, model.CalculatorEngine engine) {
//        if (input.equals("0")) {
//            return "Nhập lựa chọn 1-9";
//        }
//
//        System.out.println("Processing menu selection: " + input + ", Current level: " + currentLevel);
//
//        // Nếu ở MAIN, chọn submenu
//        if (currentLevel == MenuLevel.MAIN) {
//            switch (input) {
//                case "1":
//                    currentLevel = MenuLevel.STATISTICS;
//                    return "THỐNG KÊ";
//                case "2":
//                    currentLevel = MenuLevel.MATRIX;
//                    return "MA TRẬN";
//                case "3":
//                    currentLevel = MenuLevel.VECTOR;
//                    return "VECTƠ";
//                case "4":
//                    currentLevel = MenuLevel.DISTRIBUTION;
//                    return "PHÂN PHỐI";
//                case "5":
//                    currentLevel = MenuLevel.INEQUALITY;
//                    return "BẤT PHƯƠNG TRÌNH";
//                case "6":
//                    currentLevel = MenuLevel.RATIO;
//                    return "TỈ LỆ";
//                case "7":
//                    currentLevel = MenuLevel.TABLE;
//                    return "BẢNG HÀM";
//                case "8":
//                    currentLevel = MenuLevel.SETTINGS;
//                    return "CÀI ĐẶT";
//                case "9":
//                    currentLevel = MenuLevel.EQUATION_MAIN;
//                    return "PHƯƠNG TRÌNH";
//            }
//        }
//
//        // Nếu ở SETTINGS
//        if (currentLevel == MenuLevel.SETTINGS) {
//            switch (input) {
//                case "1":
//                    currentLevel = MenuLevel.ANGLE_UNIT;
//                    return "ĐƠN VỊ GÓC";
//                case "2":
//                    currentLevel = MenuLevel.DISPLAY_FORMAT;
//                    return "ĐỊNH DẠNG HIỂN THỊ";
//                case "3":
//                    currentLevel = MenuLevel.DECIMAL_PLACE;
//                    return "SỐ CHỮ SỐ THẬP PHÂN";
//            }
//        }
//
//        // Nếu ở ANGLE_UNIT
//        if (currentLevel == MenuLevel.ANGLE_UNIT) {
//            switch (input) {
//                case "1":
//                    angleUnit = "DEG";
//                    engine.setAngleMode("DEG");
//                    currentLevel = MenuLevel.SETTINGS;
//                    return "✓ Đơn vị góc: Độ (Degree)";
//                case "2":
//                    angleUnit = "RAD";
//                    engine.setAngleMode("RAD");
//                    currentLevel = MenuLevel.SETTINGS;
//                    return "✓ Đơn vị góc: Radian";
//                case "3":
//                    angleUnit = "GRAD";
//                    engine.setAngleMode("GRAD");
//                    currentLevel = MenuLevel.SETTINGS;
//                    return "✓ Đơn vị góc: Gradian";
//            }
//        }
//
//        // Nếu ở DISPLAY_FORMAT
//        if (currentLevel == MenuLevel.DISPLAY_FORMAT) {
//            switch (input) {
//                case "1":
//                    displayFormat = "FIX";
//                    engine.setDisplayMode("FIX");
//                    currentLevel = MenuLevel.SETTINGS;
//                    return "✓ Định dạng: Fix";
//                case "2":
//                    displayFormat = "SCI";
//                    engine.setDisplayMode("SCI");
//                    currentLevel = MenuLevel.SETTINGS;
//                    return "✓ Định dạng: Scientific";
//                case "3":
//                    displayFormat = "NORM";
//                    engine.setDisplayMode("NORM");
//                    currentLevel = MenuLevel.SETTINGS;
//                    return "✓ Định dạng: Normal";
//                case "4":
//                    displayFormat = "ENG";
//                    engine.setDisplayMode("ENG");
//                    currentLevel = MenuLevel.SETTINGS;
//                    return "✓ Định dạng: Engineering";
//            }
//        }
//
//        return "";
//    }
//
//    public String getMenuDisplay() {
//        StringBuilder sb = new StringBuilder();
//        List<String> items = menuItems.get(currentLevel);
//
//        if (items == null) {
//            return "Menu không tìm thấy";
//        }
//
//        sb.append("═══════════════════\n");
//        sb.append(currentLevel.toString()).append("\n");
//        sb.append("═══════════════════\n\n");
//
//        for (String item : items) {
//            sb.append(item).append("\n");
//        }
//
//        return sb.toString();
//    }
//
//    public int getFixedDecimalPlaces() {
//        return fixedDecimalPlaces;
//    }
//
//    public void setFixedDecimalPlaces(int places) {
//        this.fixedDecimalPlaces = Math.max(0, Math.min(places, 9));
//    }
//
//    public String getAngleUnit() {
//        return angleUnit;
//    }
//
//    public void setAngleUnit(String unit) {
//        this.angleUnit = unit;
//    }
//
//    public String getDisplayFormat() {
//        return displayFormat;
//    }
//
//    public void setDisplayFormat(String format) {
//        this.displayFormat = format;
//    }
//
//    public String getLanguage() {
//        return language;
//    }
//
//    public void setLanguage(String lang) {
//        this.language = lang;
//    }
//
//    public boolean isInSubMenu() {
//        return currentLevel != MenuLevel.MAIN;
//    }
//}