package lab.modules;

import lab.models.IFunc;
import lab.models.Separation;

import java.math.BigDecimal;
import java.util.ArrayList;

public class FindSeparation {
    public static ArrayList<Separation> findSeparations(IFunc func, double a, double b){
        ArrayList<Separation> array = new ArrayList<>();
        double eps = 0.00000001;
        int scale = 8; // Количество знаков после запятой.
        // Проверка первого элемента
        Double first = func.solve(round(a, scale));
        double left_now = first.isNaN() || first.isInfinite() ? a + eps : a;
        // Проверка элементов (a, b)
        if(a<=b){
            for(double i = a+0.0001; i < b; i+=0.0001){
                if (func.solve(round(i, scale)).isNaN() || func.solve(round(i, scale)).isInfinite()) {
                    array.add(new Separation(left_now, i-eps));
                    left_now = i+eps;
                }
            }
        }
        else{
            for(double i = a; i > b; i-=0.0001){
                if (func.solve(round(i, scale)).isNaN() || func.solve(round(i, scale)).isInfinite()) {
                    array.add(new Separation(left_now, i+eps));
                    left_now = i-eps;
                }
            }
        }
        // Проверка последнего элемента
        Double end = func.solve(round(b, scale));
        end = end.isNaN() || end.isInfinite() ? b - eps : b;
        array.add(new Separation(left_now, end));
        return array;
    }

    public static Double round(double x, int scale) {
        try {
            double rounded = (new BigDecimal(Double.toString(x))).setScale(scale, 4).doubleValue();
            return rounded == 0.0D ? 0.0D * x : rounded;
        } catch (NumberFormatException var6) {
            return Double.isInfinite(x) ? x : Double.NaN;
        }
    }
}
