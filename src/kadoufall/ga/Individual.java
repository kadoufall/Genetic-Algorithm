package kadoufall.ga;

import kadoufall.ga.knapsack.Knapsack;
import kadoufall.ga.tsp.TSP;

public class Individual {
    private int geneLength;     // 基因总数
    private int[] gene;         // 基因
    private double fitness;     // 适应度
    private String questionType;    // 问题类型

    public Individual(int length, String questionType) {
        this.geneLength = length;
        this.gene = new int[length];
        this.fitness = 0;
        this.questionType = questionType;

        for (int i = 0; i < geneLength; i++) {
            double tem = Math.random();
            if (tem <= 0.5) {
                gene[i] = 0;
            } else {
                gene[i] = 1;
            }
        }
    }

    public int getGeneLength() {
        return this.geneLength;
    }

    public int[] getGene() {
        return gene;
    }

    public void setGene(int[] gene) {
        for (int i = 0; i < getGeneLength(); i++) {
            this.gene[i] = gene[i];
        }
    }

    public int getOneGene(int i) {
        return this.gene[i];
    }

    public void setOneGene(int index, int gene) {
        this.gene[index] = gene;
    }

    public double getFitness() {
        double re = 0;
        if (questionType == "") {
            re = Fitness.calFitness(this.gene);
        } else if (questionType == "knapsack") {
            re = Knapsack.calFitness(this.gene);
        } else if (questionType == "TSP") {
            re = TSP.calFitness(this.gene);
        }

        return re;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

}
