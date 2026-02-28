package model;// model.EquationSolver.java
import java.text.DecimalFormat;

public class EquationSolver {

    // Giải phương trình bậc 2: ax² + bx + c = 0
    public static String[] solveQuadratic(double a, double b, double c) {
        if (a == 0) {
            if (b == 0) {
                return new String[]{"No Solution"};
            }
            // Phương trình bậc 1: bx + c = 0
            double x = -c / b;
            return new String[]{"x = " + format(x)};
        }

        double delta = b * b - 4 * a * c;

        if (delta < 0) {
            // Nghiệm phức
            double realPart = -b / (2 * a);
            double imagPart = Math.sqrt(-delta) / (2 * a);
            return new String[]{
                    "x₁ = " + format(realPart) + " + " + format(imagPart) + "i",
                    "x₂ = " + format(realPart) + " - " + format(imagPart) + "i"
            };
        } else if (delta == 0) {
            // Nghiệm kép
            double x = -b / (2 * a);
            return new String[]{"x = " + format(x) + " (nghiệm kép)"};
        } else {
            // Hai nghiệm phân biệt
            double x1 = (-b + Math.sqrt(delta)) / (2 * a);
            double x2 = (-b - Math.sqrt(delta)) / (2 * a);
            return new String[]{
                    "x₁ = " + format(x1),
                    "x₂ = " + format(x2)
            };
        }
    }

    // Giải phương trình bậc 3: ax³ + bx² + cx + d = 0
    public static String[] solveCubic(double a, double b, double c, double d) {
        if (a == 0) {
            return solveQuadratic(b, c, d);
        }

        // Chuẩn hóa về dạng x³ + px + q = 0
        double p = (3 * a * c - b * b) / (3 * a * a);
        double q = (2 * b * b * b - 9 * a * b * c + 27 * a * a * d) / (27 * a * a * a);

        double discriminant = (q * q / 4) + (p * p * p / 27);

        if (discriminant > 0) {
            // Một nghiệm thực, hai nghiệm phức
            double u = Math.cbrt(-q / 2 + Math.sqrt(discriminant));
            double v = Math.cbrt(-q / 2 - Math.sqrt(discriminant));
            double x1 = u + v - b / (3 * a);

            return new String[]{
                    "x₁ = " + format(x1),
                    "x₂, x₃: Complex"
            };
        } else if (discriminant == 0) {
            // Ba nghiệm thực, có nghiệm bội
            double u = Math.cbrt(-q / 2);
            double x1 = 2 * u - b / (3 * a);
            double x2 = -u - b / (3 * a);

            return new String[]{
                    "x₁ = " + format(x1),
                    "x₂ = " + format(x2) + " (bội 2)"
            };
        } else {
            // Ba nghiệm thực phân biệt
            double r = Math.sqrt(-p * p * p / 27);
            double theta = Math.acos(-q / (2 * r));
            double s = Math.cbrt(r);

            double x1 = 2 * s * Math.cos(theta / 3) - b / (3 * a);
            double x2 = 2 * s * Math.cos((theta + 2 * Math.PI) / 3) - b / (3 * a);
            double x3 = 2 * s * Math.cos((theta + 4 * Math.PI) / 3) - b / (3 * a);

            return new String[]{
                    "x₁ = " + format(x1),
                    "x₂ = " + format(x2),
                    "x₃ = " + format(x3)
            };
        }
    }

    // Giải hệ 2 phương trình 2 ẩn
    // a₁x + b₁y = c₁
    // a₂x + b₂y = c₂
    public static String[] solveLinearSystem2(double a1, double b1, double c1,
                                              double a2, double b2, double c2) {
        double det = a1 * b2 - a2 * b1;

        if (Math.abs(det) < 1e-10) {
            // Hệ vô nghiệm hoặc vô số nghiệm
            return new String[]{"Vô nghiệm hoặc VSN"};
        }

        double x = (c1 * b2 - c2 * b1) / det;
        double y = (a1 * c2 - a2 * c1) / det;

        return new String[]{
                "x = " + format(x),
                "y = " + format(y)
        };
    }

    // Giải hệ 3 phương trình 3 ẩn
    // a₁x + b₁y + c₁z = d₁
    // a₂x + b₂y + c₂z = d₂
    // a₃x + b₃y + c₃z = d₃
    public static String[] solveLinearSystem3(double a1, double b1, double c1, double d1,
                                              double a2, double b2, double c2, double d2,
                                              double a3, double b3, double c3, double d3) {
        // Tính định thức chính
        double det = a1 * (b2 * c3 - b3 * c2) -
                b1 * (a2 * c3 - a3 * c2) +
                c1 * (a2 * b3 - a3 * b2);

        if (Math.abs(det) < 1e-10) {
            return new String[]{"Vô nghiệm hoặc VSN"};
        }

        // Tính các định thức con (Cramer's rule)
        double detX = d1 * (b2 * c3 - b3 * c2) -
                b1 * (d2 * c3 - d3 * c2) +
                c1 * (d2 * b3 - d3 * b2);

        double detY = a1 * (d2 * c3 - d3 * c2) -
                d1 * (a2 * c3 - a3 * c2) +
                c1 * (a2 * d3 - a3 * d2);

        double detZ = a1 * (b2 * d3 - b3 * d2) -
                b1 * (a2 * d3 - a3 * d2) +
                d1 * (a2 * b3 - a3 * b2);

        double x = detX / det;
        double y = detY / det;
        double z = detZ / det;

        return new String[]{
                "x = " + format(x),
                "y = " + format(y),
                "z = " + format(z)
        };
    }

    // Giải phương trình bậc 4: ax⁴ + bx³ + cx² + dx + e = 0
    public static String[] solveQuartic(double a, double b, double c, double d, double e) {
        if (a == 0) {
            return solveCubic(b, c, d, e);
        }

        // Đây là thuật toán phức tạp, đơn giản hóa
        return new String[]{"Use numeric method"};
    }

    private static String format(double value) {
        if (Math.abs(value) < 1e-10) {
            return "0";
        }

        if (value == Math.floor(value) && Math.abs(value) < 1e10) {
            return String.format("%.0f", value);
        }

        if (Math.abs(value) >= 1e10 || (Math.abs(value) < 1e-3 && value != 0)) {
            return String.format("%.6E", value);
        }

        DecimalFormat df = new DecimalFormat("#.########");
        return df.format(value);
    }
}