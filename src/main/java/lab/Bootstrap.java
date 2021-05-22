package lab;
import lab.models.IFunc;
import lab.modules.RectangleIntegral;
import lab.modules.SimpsonsIntegral;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Bootstrap {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Нахождение интегралов методом прямоугольников(левых, правых, средних) и методом Симпсона");
        System.out.println("Выберите уравнение:");
        Map<String, IFunc> funcs = new HashMap<>();
        // 1
        funcs.put("sin(x)/x", x -> Math.sin(x)/x);
        funcs.put("sin(x)/x-6", x -> Math.sin(x)/(x-6));
        // 2
        funcs.put("2x", x -> 2*x);
        //

        /*
        Вывод и обработка ввода. Не трогать.
        */
        int i = 1;
        ArrayList<String> keys = new ArrayList<>();
        for (Map.Entry<String, IFunc> entry : funcs.entrySet()) {
            System.out.println((i++) + ". " + entry.getKey());
            keys.add(entry.getKey());
        }
        String str = scanner.nextLine();
        try {
            IFunc func1 = funcs.get(keys.get(Integer.parseInt(str) - 1));

            System.out.println("Выберите метод:\n1. Метод прямоугольников\n2. Метод Симпсона");
            if(scanner.nextLine().equals("1")) {
                System.out.println("Решение методом прямоугольников:");
                RectangleIntegral.execute(func1);
            }
            else {
                System.out.println("Решение методом Симпсона:");
                SimpsonsIntegral.execute(func1);
            }
        } catch (Exception e) {
            System.out.println("Нет такого уравнения");
        }
    }
}
