package model;

import java.util.ArrayList;
import java.util.List;

public class OptionMenu {
    public enum Mode {
        NORMAL,
        OPTION_MENU,            // Menu chính (OPTN)
        HYPERBOLIC_MENU,        // 1: Hypebol
        ANGLE_MODE_MENU,        // 2: Đơn vị góc
        ENGINEERING_SYMBOL_MENU // 3: Ký hiệu kỹ thuật
    }

    private Mode currentMode;
    private String selectedAngleMode;

    // Biến để quản lý việc cuộn trang
    private int scrollOffset = 0; // Dòng đầu tiên đang hiển thị
    private final int MAX_DISPLAY_LINES = 3; // Màn hình chỉ hiện tối đa 3 dòng menu

    public OptionMenu() {
        this.currentMode = Mode.NORMAL;
        this.selectedAngleMode = "DEG"; // Mặc định là Độ (Degree)
    }

    public Mode getCurrentMode() {
        return this.currentMode;
    }

    // --- CÁC HÀM ĐIỀU HƯỚNG MENU ---

    public void enterOptionMenu() {
        this.currentMode = Mode.OPTION_MENU;
        this.scrollOffset = 0; // Reset vị trí cuộn về đầu
    }

    public void enterHyperbolicMenu() {
        this.currentMode = Mode.HYPERBOLIC_MENU;
        this.scrollOffset = 0;
    }

    public void enterAngleModeMenu() {
        this.currentMode = Mode.ANGLE_MODE_MENU;
        this.scrollOffset = 0;
    }

    public void enterEngineeringSymbolMenu() {
        this.currentMode = Mode.ENGINEERING_SYMBOL_MENU;
        this.scrollOffset = 0;
    }

    public void reset() {
        this.currentMode = Mode.NORMAL;
        this.scrollOffset = 0;
    }

    // --- XỬ LÝ CUỘN TRANG (LÊN / XUỐNG) ---

    public void scrollDown() {
        List<String> items = getCurrentMenuItems();
        // Nếu vẫn còn dòng ở dưới chưa hiển thị hết thì cuộn xuống
        if (scrollOffset + MAX_DISPLAY_LINES < items.size()) {
            scrollOffset++;
        }
    }

    public void scrollUp() {
        // Nếu không phải đang ở dòng đầu tiên thì cuộn lên
        if (scrollOffset > 0) {
            scrollOffset--;
        }
    }

    // --- XỬ LÝ ĐƠN VỊ GÓC ---

    public void setAngleMode(String mode) {
        // mode truyền vào: "1", "2", "3" tương ứng với menu
        if (mode.equals("1")) this.selectedAngleMode = "DEG";
        else if (mode.equals("2")) this.selectedAngleMode = "RAD";
        else if (mode.equals("3")) this.selectedAngleMode = "GRA";
    }

    public String getAngleMode() {
        return this.selectedAngleMode;
    }

    // --- HIỂN THỊ MENU (VIỆT HÓA & CÓ CUỘN) ---

    public String getMenuDisplay() {
        List<String> items = getCurrentMenuItems();

        if (items.isEmpty()) return "";

        StringBuilder display = new StringBuilder();

        // Tiêu đề menu (Luôn hiển thị ở dòng đầu, không bị cuộn)
        String title = getMenuTitle();
        if (!title.isEmpty()) {
            display.append(title).append("\n");
        }

        // Logic cắt danh sách: Chỉ lấy các dòng từ scrollOffset đến scrollOffset + 3
        int count = 0;
        for (int i = scrollOffset; i < items.size(); i++) {
            if (count >= MAX_DISPLAY_LINES) break; // Chỉ lấy đủ số dòng màn hình chứa được

            display.append(items.get(i));
            // Thêm dấu xuống dòng nếu không phải dòng cuối cùng hiển thị
            if (count < MAX_DISPLAY_LINES - 1 && i < items.size() - 1) {
                display.append("\n");
            }
            count++;
        }

        // Thêm chỉ dẫn mũi tên nếu còn nội dung ẩn
        if (scrollOffset > 0) {
            // Có nội dung ở trên -> Thêm ký hiệu (thực tế màn hình bé nên ta ngầm hiểu)
        }
        if (scrollOffset + MAX_DISPLAY_LINES < items.size()) {
            // Có nội dung ở dưới -> Thêm ký hiệu xuống dòng ▼ (tùy chọn)
            display.append(" ▼");
        }

        return display.toString();
    }

    // Helper: Lấy danh sách mục menu dựa trên chế độ hiện tại (ĐÃ VIỆT HÓA)
    private List<String> getCurrentMenuItems() {
        List<String> items = new ArrayList<>();

        switch (currentMode) {
            case OPTION_MENU:
                items.add("1:Hypebol (Hyperbolic)");
                items.add("2:Đơn vị góc (Angle)");
                items.add("3:Ký hiệu kỹ thuật (Eng)");
                break;

            case HYPERBOLIC_MENU:
                items.add("1:sinh");
                items.add("2:cosh");
                items.add("3:tanh");
                items.add("4:sinh⁻¹");
                items.add("5:cosh⁻¹");
                items.add("6:tanh⁻¹");
                break;

            case ANGLE_MODE_MENU:
                items.add("1:Độ (Degree)");
                items.add("2:Radian");
                items.add("3:Gradian");
                break;

            case ENGINEERING_SYMBOL_MENU:
                items.add("1:m (milli 10⁻³)");
                items.add("2:μ (micro 10⁻⁶)");
                items.add("3:n (nano 10⁻⁹)");
                items.add("4:p (pico 10⁻¹²)");
                items.add("5:f (femto 10⁻¹⁵)");
                items.add("6:k (kilo 10³)");
                items.add("7:M (mega 10⁶)");
                items.add("8:G (giga 10⁹)");
                items.add("9:T (tera 10¹²)");
                break;
        }
        return items;
    }

    // Helper: Lấy tiêu đề menu
    private String getMenuTitle() {
        switch (currentMode) {
            case OPTION_MENU: return "[OPTION]";
            case HYPERBOLIC_MENU: return "[Hypebol]";
            case ANGLE_MODE_MENU: return "[Đơn vị góc]";
            case ENGINEERING_SYMBOL_MENU: return "[Ký hiệu Kỹ thuật]";
            default: return "";
        }
    }
}