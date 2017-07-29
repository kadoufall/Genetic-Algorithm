package kadoufall.ga.knapsack;

import kadoufall.ga.GA;
import kadoufall.ga.Individual;
import kadoufall.ga.Population;

public class KnapsackGA extends GA {

    // 交叉
    public void crossover(Individual ind1, Individual ind2) {
        while (true) {
            // 复制基因数组
            int[] crossBefore1 = new int[ind1.getGeneLength()];
            int[] crossBefore2 = new int[ind2.getGeneLength()];
            for (int i = 0; i < ind1.getGeneLength(); i++) {
                crossBefore1[i] = ind1.getOneGene(i);
            }
            for (int i = 0; i < ind2.getGeneLength(); i++) {
                crossBefore2[i] = ind2.getOneGene(i);
            }

            int[] cross = new int[ind1.getGeneLength()];

            // 均匀交叉算法， 哪些位置进行交换
            for (int i = 0; i < cross.length; i++) {
                cross[i] = (int) Math.round(Math.random());
            }

/*
            // 单点交叉
            int choiceLoc = (int) (Math.random() * cross.length);
            for (int i = 0; i < cross.length; i++) {
                if (i < choiceLoc) {
                    cross[i] = 0;
                }else {
                    cross[i] = 1;
                }
            }
*/
/*
            // 两点交叉
            int choiceLoc1 = (int) (Math.random() * cross.length);
            int choiceLoc2 = (int) (Math.random() * cross.length);
            while(choiceLoc1 == choiceLoc2){
                choiceLoc2 = (int) (Math.random() * cross.length);
            }
            for (int i = 0; i < cross.length; i++) {
                if (i < choiceLoc2 && i>choiceLoc1) {
                    cross[i] = 1;
                }else {
                    cross[i] = 0;
                }
            }
*/
            // 进行交换
            for (int i = 0; i < cross.length; i++) {
                if (cross[i] == 1) {
                    int mom = ind1.getGene()[i];
                    int dad = ind2.getGene()[i];
                    crossBefore1[i] = dad;
                    crossBefore2[i] = mom;
                }
            }

            // 若变换后是合法的，则更新输入
            if (Knapsack.isValid(crossBefore1) && Knapsack.isValid(crossBefore2)) {
                for (int i = 0; i < cross.length; i++) {
                    ind1.setOneGene(i, crossBefore1[i]);
                    ind2.setOneGene(i, crossBefore2[i]);
                }
                break;
            }
        }
    }

    // 突变
    public void mutate(Individual individual, Population pop) {
        while (true) {
            // 复制基因数组
            int[] crossBefore1 = new int[individual.getGeneLength()];
            for (int i = 0; i < individual.getGeneLength(); i++) {
                crossBefore1[i] = individual.getOneGene(i);
            }

            //单点变异 选择一个位置进行变异
            int tem = (int) (Math.random() * individual.getGeneLength());
            int mom = individual.getGene()[tem];
            if (mom == 1) {
                crossBefore1[tem] = 0;
            } else {
                crossBefore1[tem] = 1;
            }

/*
            // 两点变异
            int tem = (int) (Math.random() * individual.getGeneLength());
            int tem1 = (int) (Math.random() * individual.getGeneLength());
            while (tem == tem1) {
                tem1 = (int) (Math.random() * individual.getGeneLength());
            }
            int mom = individual.getGene()[tem];
            int mom1 = individual.getGene()[tem1];
            if (mom == 1) {
                crossBefore1[tem] = 0;
            } else {
                crossBefore1[tem] = 1;
            }
            if (mom1 == 1) {
                crossBefore1[tem1] = 0;
            } else {
                crossBefore1[tem1] = 1;
            }
*/
/*
            // 根据适应度启发式变异
            if (individual.getFitness() > pop.getAveFitness()) {
                //单点变异 选择一个位置进行变异
                int tem = (int) (Math.random() * individual.getGeneLength());
                int mom = individual.getGene()[tem];
                if (mom == 1) {
                    crossBefore1[tem] = 0;
                }else{
                    crossBefore1[tem] = 1;
                }
            }else {
                // 均匀变异
                for (int i = 0; i < crossBefore1.length; i++) {
                    if ((int) Math.round(Math.random()) == 1) {
                        crossBefore1[i] = (crossBefore1[i]==1)?0:1;
                    }
                }
            }
*/

            //　变异后是否合法
            if (Knapsack.isValid(crossBefore1)) {
                for (int i = 0; i < crossBefore1.length; i++) {
                    individual.setOneGene(i, crossBefore1[i]);
                }
                break;
            }
        }
    }

}

