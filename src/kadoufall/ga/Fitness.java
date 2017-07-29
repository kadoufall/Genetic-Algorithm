package kadoufall.ga;

import java.util.ArrayList;

public class Fitness {
    public static ArrayList<Integer> solution = new ArrayList<>();

    public static double calFitness(int[] input) {
        double re = 0;

        for (int i = 0; i < input.length; i++) {
            if (input[i] == solution.get(i)) {
                re++;
            }
        }

        return re;
    }
}
