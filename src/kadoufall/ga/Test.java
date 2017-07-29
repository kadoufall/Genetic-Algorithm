package kadoufall.ga;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) throws Exception {
        int[] tem = {0, 1, 1, 1, 0, 1, 1, 0, 0, 0,
                1, 1, 0, 1, 0, 1, 0, 1, 1, 1,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                0, 1, 0, 1, 1, 0, 1, 1, 1, 1};


        for (int i = 0; i < tem.length; i++) {
            if (tem[i] == 0) {
                Fitness.solution.add(0);
            } else {
                Fitness.solution.add(1);
            }
        }

        Population pop = new Population(50, 50, 0.8, 0.05, "");

        GA ga = new GA();
/*
        int count  = 0;
        while(pop.getMaxFitness() < 50){
            count ++;
            pop = ga.evolution(pop);
            System.out.println(count + ": Max:" + pop.getMaxFitness() + " Ave" + pop.getAveFitness() + " MaxID" +  pop.getMaxFitnessID() +"   ");
        }
*/

        for (int i = 0; i < 200; i++) {
            Population temPop = ga.evolution(pop);
            if (temPop.getAveFitness() > pop.getAveFitness()) {
                pop = ga.evolution(pop);
            }
            System.out.print(i + ": Max:" + pop.getMaxFitness() + " Ave " + pop.getAveFitness() + " MaxID " + pop.getMaxFitnessID() + "   ");
            for (int j = 0; j < pop.getLength(); j++) {
                System.out.print(pop.getIndividual(j).getFitness() + " ");


            }
            System.out.println();
        }

        int[] numbers = {1, 2, 3, 2};
        Arrays.sort(numbers);

        for (int i :
                numbers) {
            System.out.println(i);
        }

    }


}
