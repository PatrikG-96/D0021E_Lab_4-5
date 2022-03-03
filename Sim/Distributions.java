package Sim;

import java.util.ArrayList;
import java.util.Random;

public class Distributions {

    private static Random random = new Random();


    public static ArrayList<Double> normalDistribution(double mean, double stdev, int n) {
        ArrayList<Double> distribution = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            distribution.add(random.nextGaussian() * stdev + mean);
        }
        return distribution;
    }

    public static double nextPossion(double lambda) {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do {
            k++;
            p *= Math.random();
        } while (p > L);

        return k - 1;
    }

    public static ArrayList<Double> poissonDistribution(double lambda, int n) {
        ArrayList<Double> distribution = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            distribution.add(nextPossion(lambda));
        }
        return distribution;
    }

    public static ArrayList<Double> cbr(int rate, int time_limit) {
        ArrayList<Double> distribution = new ArrayList<>();
        double delay = 1 / (double) rate;
        double time = 0;
        while (time < time_limit) {
            distribution.add(delay);
            time+=delay;
        }
        return distribution;
    }

}
