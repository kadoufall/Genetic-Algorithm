package kadoufall.ga;


import java.util.ArrayList;

public class GA {

    // 进化
    public Population evolution(Population input) {
        Population re = new Population(input.getLength(), input.getGeneLength(), input.getCrossFata(), input.getMutationRate(), input.getQuestionType());

        // 自然选择
        // 精英策略
        Individual newInd1 = new Individual(re.getGeneLength(), re.getQuestionType());
        newInd1.setGene(input.getFittest().getGene());
        re.setIndividual(0, newInd1);
        for (int i = 1; i < re.getLength(); i++) {
            Individual newInd = new Individual(re.getGeneLength(), re.getQuestionType());
            newInd.setGene(naturalSelection(input).getGene());
            re.setIndividual(i, newInd);
            //   System.out.print(choice[i] + " ");
        }
/*
        System.out.print("1:");
        for (int i = 0; i < re.getLength(); i++) {
            System.out.print(String.format("%.2f", -re.getIndividual(i).getFitness()) + " ");
        }
        System.out.println();
*/
        // 交叉互换
        int crossoverNum = (int) (input.getCrossFata() * input.getLength());    // 互换数
        if (crossoverNum % 2 != 0) {
            crossoverNum++;
        }

        int[] flags = new int[input.getLength()];   // 是否进行交换
        for (int i = 0; i < flags.length; i++) {
            flags[i] = 0;
        }
        ArrayList<Integer> flags1 = new ArrayList<>();  // 需要交换的下标
        // 随机选择crossoverNum个
        for (int i = 0; i < crossoverNum; i++) {
            while (true) {
                int temm = (int) (Math.random() * flags.length);
                if (flags[temm] == 0 && temm != 0) {
                    flags[temm] = 1;
                    flags1.add(temm);
                    break;
                }
            }
        }
        // 随机选择需要交换的两个进行交换，交换后crossoverNum减2
        while (flags1.size() != 0) {
            crossoverNum = flags1.size();
            if (crossoverNum == 2) {
                crossover(re.getIndividual(flags1.get(0)), re.getIndividual(flags1.get(1)));
                flags1.remove(0);
                flags1.remove(0);
            } else {
                int ch1 = (int) (Math.random() * crossoverNum);
                int ch2 = 0;
                while (true) {
                    ch2 = (int) (Math.random() * crossoverNum);
                    if (ch2 != ch1) {
                        break;
                    }
                }
                crossover(re.getIndividual(flags1.get(ch1)), re.getIndividual(flags1.get(ch2)));
                flags1.remove(ch1);
                if (ch2 < ch1) {
                    flags1.remove(ch2);
                } else {
                    flags1.remove(ch2 - 1);
                }

            }
        }
/*
        System.out.print("2:");
        for (int i = 0; i < re.getLength(); i++) {
            System.out.print(String.format("%.2f", re.getIndividual(i).getFitness()) + " ");
        }
        System.out.println();
*/

        // 突变
        int mutateNum = (int) (input.getMutationRate() * input.getLength()) - 1;    // 变异数
        int[] flags00 = new int[input.getLength()];   // 是否进行变异,初始为全否
        for (int i = 0; i < flags00.length; i++) {
            flags00[i] = 0;
        }
        ArrayList<Integer> flags11 = new ArrayList<>();  // 需要变异的下标
        // 随机选择mutateNum个
        for (int i = 0; i < mutateNum; i++) {
            while (true) {
                int temm = (int) (Math.random() * flags00.length);
                if (flags00[temm] == 0 && temm != 0) {
                    flags00[temm] = 1;
                    flags11.add(temm);
                    break;
                }
            }
        }
        // 进行变异
        for (int i : flags11) {
            mutate(re.getIndividual(i), re);
        }

        flags11.clear();
/*
        System.out.print("3:");
        for (int i = 0; i < re.getLength(); i++) {
            System.out.print(String.format("%.2f", re.getIndividual(i).getFitness()) + " ");
        }
        System.out.println();
*/
        return re;
    }

    // 根据个体选择概率选择一个个体，轮盘赌选择
    public Individual naturalSelection(Population input) {

        // 累加概率
        double[] cumulative = input.getSelectRates();
        for (int i = 1; i < cumulative.length; i++) {
            cumulative[i] += cumulative[i - 1];
        }

        // length次，每次找到命中位置，并累计命中次数
        int[] hitNum = new int[cumulative.length];
        for (int i = 0; i < hitNum.length; i++) {
            hitNum[i] = 0;
        }
        for (int i = 0; i < cumulative.length; i++) {
            double ram = Math.random();
            for (int j = 0; j < cumulative.length - 1; j++) {
                // ram<=q[0]，则选择个体1
                if (j == 0 && ram <= cumulative[0]) {
                    hitNum[0]++;
                    break;
                } else if (ram > cumulative[j] && ram <= cumulative[j + 1]) {
                    hitNum[j + 1]++;
                    break;
                }
            }
        }

        // 找到hit最多的那个
        int reNum = 0;
        for (int i = 1; i < hitNum.length; i++) {
            if (hitNum[i] > hitNum[reNum]) {
                reNum = i;
            }
        }
        return input.getIndividual(reNum);
    }

    // 交叉
    public void crossover(Individual ind1, Individual ind2) {
        // 是否互换
        int[] cross = new int[ind1.getGeneLength()];
        for (int i = 0; i < cross.length; i++) {
            double tem = Math.random();
            boolean go = true;
            if (tem <= 0.5) {
                go = false;
            }
            if (go) {
                cross[i] = 1;
            }
        }

        for (int i = 0; i < cross.length; i++) {
            if (cross[i] == 1) {
                int mom = ind1.getGene()[i];
                int dad = ind2.getGene()[i];
                ind2.setOneGene(i, mom);
                ind1.setOneGene(i, dad);
            }
        }

    }

    // 变异
    public void mutate(Individual individual, Population pop) {
        int tem = (int) (Math.random() * individual.getGeneLength());
        int mom = individual.getGene()[tem];
        if (mom == 1) {
            individual.setOneGene(tem, 0);
        } else {
            individual.setOneGene(tem, 1);
        }

    }


}
