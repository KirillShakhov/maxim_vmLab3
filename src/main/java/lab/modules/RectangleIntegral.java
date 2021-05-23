package lab.modules;
import lab.models.IFunc;
import lab.models.Separation;
import java.util.ArrayList;
import java.util.Scanner;

import static lab.modules.FindSeparation.findSeparations;

public class RectangleIntegral {
    static Scanner scanner = new Scanner(System.in);
    public static void execute(IFunc f){
        System.out.println("Введите a:");
        double a = Double.parseDouble(scanner.nextLine());
        System.out.println("Введите b:");
        double b = Double.parseDouble(scanner.nextLine());
        while (true) {
            System.out.println("Выберите вариант подсчета:\n1. Подсчитать по точности\n2. Подсчитать по количеству шагов");
            String ss = scanner.nextLine();
            if (ss.equals("1")){
                System.out.println("Введите погрешность:");
                double eps = Double.parseDouble(scanner.nextLine());
                ArrayList<Separation> array = findSeparations(f, a, b);
                double sum_left = 0.0d;
                double sum_right = 0.0d;
                double sum_mid = 0.0d;
                for(Separation s : array){
                    RectangleResult result = solve(f, s.getLeft(), s.getRight(), eps);
                    sum_left += result.getLeft();
                    sum_right += result.getRight();
                    sum_mid += result.getMid();
                    System.out.println("Результаты для отрезка от "+s.getLeft()+" до "+s.getRight()+":");
                    System.out.println("Левые прямоугольники: "+String.format("%.8f", result.getLeft())+" eps: "+String.format("%.8f", result.getEpsLeft()));
                    System.out.println("Правые прямоугольники: "+String.format("%.8f", result.getRight())+" eps: "+String.format("%.8f", result.getEpsRight()));
                    System.out.println("Средние прямоугольники: "+String.format("%.8f", result.getMid())+" eps: "+String.format("%.8f", result.getEpsMid()));
                }
                System.out.println("Сумма для метода левых прямоугольников: "+sum_left);
                System.out.println("Сумма для метода правых прямоугольников: "+sum_right);
                System.out.println("Сумма для метода средних прямоугольников: "+sum_mid);
            }else if (ss.equals("2")){
                System.out.println("Введите количество шагов n:");
                int n = Integer.parseInt(scanner.nextLine());
                ArrayList<Separation> array = findSeparations(f, a, b);
                double sum_left = 0.0d;
                double sum_right = 0.0d;
                double sum_mid = 0.0d;
                for(Separation s : array){
                    double left = left_rectangle_integral(f, s.getLeft(), s.getRight(), n);
                    double right = right_rectangle_integral(f, s.getLeft(), s.getRight(), n);
                    double mid = mid_rectangle_integral(f, s.getLeft(), s.getRight(), n);
                    sum_left += left;
                    sum_right += right;
                    sum_mid += mid;
                    System.out.println("Результаты для отрезка от "+s.getLeft()+" до "+s.getRight()+":");
                    System.out.println("Левые прямоугольники: "+String.format("%.8f", left));
                    System.out.println("Правые прямоугольники: "+String.format("%.8f", right));
                    System.out.println("Средние прямоугольники: "+String.format("%.8f", mid));
                }
                System.out.println("Сумма для метода левых прямоугольников: "+sum_left);
                System.out.println("Сумма для метода правых прямоугольников: "+sum_right);
                System.out.println("Сумма для метода средних прямоугольников: "+sum_mid);
            }else{
                System.out.println("Такого варианта нет. Введите 1 или 2.");
                continue;
            }
            break;
        }
    }
    //функция для вычисления интеграла методом левых прямоугольников
    static double left_rectangle_integral(IFunc f, double a, double b, int n) {
        boolean isNegative = false;
        if(a>b){
            double t = a;
            a = b;
            b = t;
            isNegative = true;
        }
        double step;
        double sum = 0;
        step = (b - a) / n;  //шаг
        for(double i=a;i<b; i+=step) {
            sum += f.solve(i);//суммируем значения функции в узловых точках
        }
        //приближенное значение интеграла равно сумме площадей прямоугольников
        double result = sum*step;//множим на величину шага и возвращаем в вызывающую функцию
        if(isNegative){
            return -1*result;
        }
        else {
            return result;
        }
    }
    static double right_rectangle_integral(IFunc f, double a, double b, int n) {
        boolean isNegative = false;
        if(a>b){
            double t = a;
            a = b;
            b = t;
            isNegative = true;
        }
        double step;
        double sum = 0;
        step = (b - a) / n;  //шаг
        for(int i = 1; i <= n; i++)
        {
            sum += f.solve(a + i*step);
        }
        //приближенное значение интеграла равно сумме площадей прямоугольников
        double result = sum*step;//множим на величину шага и возвращаем в вызывающую функцию
        if(isNegative){
            return -1*result;
        }
        else {
            return result;
        }    }
    static double mid_rectangle_integral(IFunc f, double a, double b, int n) {
        boolean isNegative = false;
        if(a>b){
            double t = a;
            a = b;
            b = t;
            isNegative = true;
        }
        double sum = 0;
        double step = (b - a) / n;  //шаг
        for(int i = 0; i < n; i++) {
            sum += f.solve(a + step * (i + 0.5));//0.5 это тип 1/2
        }
        //приближенное значение интеграла равно сумме площадей прямоугольников
        double result = sum*step;//множим на величину шага и возвращаем в вызывающую функцию
        if(isNegative){
            return -1*result;
        }
        else {
            return result;
        }
    }


    public static RectangleResult solve(IFunc f, double a, double b, double eps){
        RectangleResult resultSet = new RectangleResult();
        // left
        int iter = 0;
        int n = 1; //начальное число шагов
        double s, s1 = left_rectangle_integral(f, a, b, n); //первое приближение для интеграла
        do {
            s = s1;     //второе приближение
            n = 2 * n;  //увеличение числа шагов в два раза,
            //т.е. уменьшение значения шага в два раза
            s1 = left_rectangle_integral(f, a, b, n);
            iter++;
        }
        while (Math.abs(s1 - s) > eps);  //сравнение приближений с заданной точностью
        resultSet.setLeft(s1);
        resultSet.setLeft_n(iter);
        // left eps
        iter*=2;
        n = 1; //начальное число шагов
        s1 = left_rectangle_integral(f, a, b, n); //первое приближение для интеграла
        do {
            s = s1;     //второе приближение
            n = 2 * n;  //увеличение числа шагов в два раза,
            //т.е. уменьшение значения шага в два раза
            s1 = left_rectangle_integral(f, a, b, n);
        }
        while (iter-->0);  //сравнение приближений с заданной точностью
        resultSet.setEpsLeft(Math.abs(resultSet.getLeft()-s1));

        // right
        iter = 0;
        n = 1; //начальное число шагов
        s1 = right_rectangle_integral(f, a, b, n); //первое приближение для интеграла
        do {
            s = s1;     //второе приближение
            n = 2 * n;  //увеличение числа шагов в два раза,
            //т.е. уменьшение значения шага в два раза
            s1 = right_rectangle_integral(f, a, b, n);
            iter++;
        }
        while (Math.abs(s1 - s) > eps);  //сравнение приближений с заданной точностью
        resultSet.setRight(s1);
        resultSet.setRight_n(iter);
        // right eps
        iter*=2;
        n = 1; //начальное число шагов
        s1 = right_rectangle_integral(f, a, b, n); //первое приближение для интеграла
        do {
            s = s1;     //второе приближение
            n = 2 * n;  //увеличение числа шагов в два раза,
            //т.е. уменьшение значения шага в два раза
            s1 = right_rectangle_integral(f, a, b, n);
        }
        while (iter-->0);  //сравнение приближений с заданной точностью
        resultSet.setEpsRight(Math.abs(resultSet.getRight()-s1));

        // mid
        iter = 0;
        n = 1; //начальное число шагов
        s1 = mid_rectangle_integral(f, a, b, n); //первое приближение для интеграла
        do {
            s = s1;     //второе приближение
            n = 2 * n;  //увеличение числа шагов в два раза,
            //т.е. уменьшение значения шага в два раза
            s1 = mid_rectangle_integral(f, a, b, n);
            iter++;
        }
        while (Math.abs(s1 - s) > eps);  //сравнение приближений с заданной точностью
        resultSet.setMid(s1);
        resultSet.setMid_n(iter);
        // mid eps
        iter *= 2;
        n = 1; //начальное число шагов
        s1 = mid_rectangle_integral(f, a, b, n); //первое приближение для интеграла
        do {
            s = s1;     //второе приближение
            n = 2 * n;  //увеличение числа шагов в два раза,
            //т.е. уменьшение значения шага в два раза
            s1 = mid_rectangle_integral(f, a, b, n);
        }
        while (iter-->0);  //сравнение приближений с заданной точностью
        resultSet.setEpsMid(Math.abs(resultSet.getMid()-s1));
        return resultSet;
    }
}

class RectangleResult {
    private double left;
    private double right;
    private double mid;
    private double left_n;
    private double right_n;
    private double mid_n;
    private double epsLeft;
    private double epsRight;
    private double epsMid;

    public RectangleResult() {

    }

    /*
    Getter and Setter
     */
    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public double getRight() {
        return right;
    }

    public void setRight(double right) {
        this.right = right;
    }

    public double getMid() {
        return mid;
    }

    public void setMid(double mid) {
        this.mid = mid;
    }

    public double getEpsLeft() {
        return epsLeft;
    }

    public void setEpsLeft(double epsLeft) {
        this.epsLeft = epsLeft;
    }

    public double getEpsRight() {
        return epsRight;
    }

    public void setEpsRight(double epsRight) {
        this.epsRight = epsRight;
    }

    public double getEpsMid() {
        return epsMid;
    }

    public void setEpsMid(double epsMid) {
        this.epsMid = epsMid;
    }

    public double getLeft_n() {
        return left_n;
    }

    public void setLeft_n(double left_n) {
        this.left_n = left_n;
    }

    public double getRight_n() {
        return right_n;
    }

    public void setRight_n(double right_n) {
        this.right_n = right_n;
    }

    public double getMid_n() {
        return mid_n;
    }

    public void setMid_n(double mid_n) {
        this.mid_n = mid_n;
    }
}


