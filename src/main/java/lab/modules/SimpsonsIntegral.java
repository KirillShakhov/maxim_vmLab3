package lab.modules;
import lab.models.IFunc;
import lab.models.Separation;
import java.util.ArrayList;
import java.util.Scanner;

import static lab.modules.FindSeparation.findSeparations;

public class SimpsonsIntegral {
    public static void execute(IFunc f){
        Scanner scanner = new Scanner(System.in);
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
                double sum_mid = 0.0d;
                for(Separation s : array){
                    SimpsonsResult result = solve(f, s.getLeft(), s.getRight(), eps);
                    sum_mid += result.getRes();
                    System.out.println("Результаты для отрезка от "+s.getLeft()+" до "+s.getRight()+":");
                    System.out.println("Метод Симпсона: "+String.format("%.8f", result.getRes())+" eps: "+String.format("%.8f", result.getResEps()));
                    System.out.println("Количество шагов, которое понадобилось для достижения точности: "+result.getN());
                }
                System.out.println("Сумма для метода Симпсона: "+sum_mid);
            }else if (ss.equals("2")){
                System.out.println("Введите количество шагов n:");
                int n = Integer.parseInt(scanner.nextLine());
                ArrayList<Separation> array = findSeparations(f, a, b);
                double sum = 0.0d;
                for(Separation s : array){
                    double result = simpson_integral(f, a, b, n);
                    sum += result;
                    System.out.println("Результаты для отрезка от "+s.getLeft()+" до "+s.getRight()+":");
                    System.out.println("Метод Симпсона: "+String.format("%.8f", result));
                }
                System.out.println("Сумма для метода Симпсона: "+sum);
            }else{
                System.out.println("Такого варианта нет. Введите 1 или 2.");
                continue;
            }
            break;
        }
    }
    public static SimpsonsResult solve(IFunc f, double a, double b, double eps){
        SimpsonsResult resultSet = new SimpsonsResult();
        // left
        int iter = 0;
        int n = 1; //начальное число шагов
        double s, s1 = simpson_integral(f, a, b, n); //первое приближение для интеграла
        do {
            s = s1;     //второе приближение
            n = 2 * n;  //увеличение числа шагов в два раза,
            //т.е. уменьшение значения шага в два раза
            s1 = simpson_integral(f, a, b, n);
            iter++;
        }
        while (Math.abs(s1 - s) > eps);  //сравнение приближений с заданной точностью
        resultSet.setRes(s1);
        resultSet.setN(iter);
        // left eps
        iter*=2;
        n = 1; //начальное число шагов
        s1 = simpson_integral(f, a, b, n); //первое приближение для интеграла
        do {
            s = s1;     //второе приближение
            n = 2 * n;  //увеличение числа шагов в два раза,
            //т.е. уменьшение значения шага в два раза
            s1 = simpson_integral(f, a, b, n);
        }
        while (iter-->0);  //сравнение приближений с заданной точностью
        resultSet.setResEps(Math.abs(resultSet.getRes()-s1));
        return resultSet;
    }

    public static double simpson_integral(IFunc f, double a, double b, double n){
        final double h = (b-a)/n;
        double k1 = 0, k2 = 0;
        for(int i = 1; i < n; i += 2) {
            k1 += f.solve(a + i*h);
            k2 += f.solve(a + (i+1)*h);
        }
        return h/3*(f.solve(a) + 4*k1 + 2*k2);
    }
}

class SimpsonsResult {
    private double res;
    private double resEps;
    private double n;
    public SimpsonsResult() { }
    public double getRes() { return res; }
    public void setRes(double res) { this.res = res;  }
    public double getResEps() { return resEps; }
    public void setResEps(double resEps) { this.resEps = resEps; }
    public void setN(double n){ this.n = n; }
    public double getN(){ return n; }
}

