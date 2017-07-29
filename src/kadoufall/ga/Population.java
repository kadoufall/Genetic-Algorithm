package kadoufall.ga;

public class Population {

    private double crossFata;     // 交叉概率
    private double mutationRate;   // 变异概率

    private Individual[] individuals;   // 个体数组
    private int individualNum;          // 个体数
    private int geneLength;             // 个体的基因长度

    private String questionType;    // 问题类型

    // 构造方法
    public Population(int length, int geneLength, double crossFata, double mutationRate, String questionType) {
        this.crossFata = crossFata;
        this.mutationRate = mutationRate;
        this.geneLength = geneLength;
        this.questionType = questionType;
        individuals = new Individual[length];
        individualNum = length;
        for (int i = 0; i < length; i++) {
            Individual tem = new Individual(geneLength, questionType);
            individuals[i] = tem;
        }

    }

    // 获取交叉概率
    public double getCrossFata() {
        return crossFata;
    }

    // 设置交叉概率
    public void setCrossFata(double crossFata) {
        this.crossFata = crossFata;
    }

    // 获取变异概率
    public double getMutationRate() {
        return mutationRate;
    }

    // 设置变异概率
    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    // 获取单个个体的引用
    public Individual getIndividual(int i) {
        return individuals[i];
    }

    // 设置单个个体，传入参数为新对象
    public void setIndividual(int index, Individual i) {
        individuals[index] = i;
    }

    // 种群大小
    public int getLength() {
        return this.individualNum;
    }

    // 获取个体的基因长度
    public int getGeneLength() {
        return geneLength;
    }

    // 获取问题类型
    public String getQuestionType() {
        return questionType;
    }

    // 最大适应度的个体
    public Individual getFittest() {
        Individual fittest = individuals[0];
        for (int i = 0; i < getLength(); i++) {
            if (fittest.getFitness() < getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    // 种群最大适应度
    public double getMaxFitness() {
        double re = individuals[0].getFitness();

        for (Individual i : individuals) {
            if (i.getFitness() > re) {
                re = i.getFitness();
            }
        }

        return re;
    }

    // 最大适应度个体ID
    public int getMaxFitnessID() {
        double re = individuals[0].getFitness();
        int re1 = 0;

        for (int i = 0; i < individuals.length; i++) {
            if (individuals[i].getFitness() > re) {
                re = individuals[i].getFitness();
                re1 = i;
            }
        }

        return re1;
    }

    // 种群选择概率数组
    public double[] getSelectRates() {
        double[] re = new double[getLength()];
        double sumFitness = getAllFitness();

        for (int i = 0; i < getLength(); i++) {
            re[i] = individuals[i].getFitness() / sumFitness;
        }

        return re;
    }

    // 种群最小适应度
    public double getMinFitness() {
        double re = individuals[0].getFitness();

        for (Individual i : individuals) {
            if (i.getFitness() < re) {
                re = i.getFitness();
            }
        }

        return re;
    }

    // 最小适应度的个体
    public Individual getMinFittest() {
        Individual minFittest = individuals[0];
        for (int i = 0; i < getLength(); i++) {
            if (minFittest.getFitness() > getIndividual(i).getFitness()) {
                minFittest = getIndividual(i);
            }
        }
        return minFittest;
    }

    // 最小适应度个体ID
    public int getMinFitnessID() {
        double re = individuals[0].getFitness();
        int re1 = 0;

        for (int i = 0; i < individuals.length; i++) {
            if (individuals[i].getFitness() < re) {
                re = individuals[i].getFitness();
                re1 = i;
            }
        }

        return re1;
    }

    // 种群平均适应度
    public double getAveFitness() {
        double sumFitness = 0;
        for (Individual i : individuals) {
            sumFitness += i.getFitness();
        }

        return sumFitness / getLength();
    }

    // 种群总的适应度
    public double getAllFitness() {
        double sumFitness = 0;
        for (Individual i : individuals) {
            sumFitness += i.getFitness();
        }

        return sumFitness;
    }

}
