package kadoufall.ga.tsp;

import kadoufall.ga.GA;
import kadoufall.ga.Individual;
import kadoufall.ga.Population;

import java.util.ArrayList;

public class TSPGA extends GA {

    // 进化
    public Population evolution(Population input) {
        Population re = new Population(input.getLength(), input.getGeneLength(), input.getCrossFata(), input.getMutationRate(), input.getQuestionType());

        // 自然选择
        // 精英策略
        boolean HGA = false;
        boolean THGA = true;
        Individual newInd = new Individual(re.getGeneLength(), re.getQuestionType());
        newInd.setGene(input.getFittest().getGene());
        re.setIndividual(0, newInd);
        for (int i = 1; i < re.getLength(); i++) {
            newInd = new Individual(re.getGeneLength(), re.getQuestionType());
            newInd.setGene(naturalSelection(input).getGene());
            re.setIndividual(i, newInd);
        }

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

        if (THGA) {
            ArrayList<Individual> newIndiArr = new ArrayList<>();
            for (int i = 0; i < crossoverNum; i++) {
                int cho1 = (int) (Math.random() * crossoverNum);
                int cho2 = (int) (Math.random() * crossoverNum);
                while (cho1 == cho2 && crossoverNum >= 2) {
                    cho2 = (int) (Math.random() * crossoverNum);
                }
                int cho3 = (int) (Math.random() * crossoverNum);
                while (cho3 == cho1 && cho3 == cho2 && crossoverNum >= 3) {
                    cho3 = (int) (Math.random() * crossoverNum);
                }

                int[] ii = crossoverTHGA(re.getIndividual(0), re.getIndividual(flags1.get(cho2)), re.getIndividual(flags1.get(cho3)));
                Individual indd = new Individual(re.getGeneLength(), re.getQuestionType());
                indd.setGene(ii);
                newIndiArr.add(indd);
            }

            for (int i = 0; i < flags1.size(); i++) {
                re.setIndividual(flags1.get(i), newIndiArr.get(0));
                newIndiArr.remove(0);
            }
        } else if (HGA) {
            ArrayList<Individual> newIndiArr = new ArrayList<>();
            for (int i = 0; i < crossoverNum; i++) {
                int cho1 = (int) (Math.random() * crossoverNum);
                int cho2 = (int) (Math.random() * crossoverNum);
                while (cho1 == cho2) {
                    cho2 = (int) (Math.random() * crossoverNum);
                }
                int[] ii = crossoverHGA(re.getIndividual(flags1.get(cho1)), re.getIndividual(flags1.get(cho2)));
                Individual indd = new Individual(re.getGeneLength(), re.getQuestionType());
                indd.setGene(ii);
                newIndiArr.add(indd);
            }

        } else {
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
        }

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

        return re;
    }


    // 交叉
    public void crossover(Individual ind1, Individual ind2) {
        int length = ind1.getGeneLength();
        while (true) {
            // 初始化子代基因数组
            int[] crossBefore1 = new int[length];
            int[] crossBefore2 = new int[length];
            for (int i = 0; i < length; i++) {
                crossBefore1[i] = -1;
            }
            for (int i = 0; i < length; i++) {
                crossBefore2[i] = -1;
            }

            //   int[] cross = new int[length];
/*
            //顺序交叉法
            int choiceLoc1 = (int) (Math.random() * length);
            int choiceLoc2 = (int) (Math.random() * length);
            while (choiceLoc1 == choiceLoc2) {
                choiceLoc2 = (int) (Math.random() * length);
            }
            if (choiceLoc1 > choiceLoc2) {      // choiceLoc1 <= choiceLoc2
                int tem = choiceLoc1;
                choiceLoc1 = choiceLoc2;
                choiceLoc2 = tem;
            }
            // 复制基因片段
            for (int i = choiceLoc1; i <= choiceLoc2; i++) {
                crossBefore1[i] = ind1.getOneGene(i);
                crossBefore2[i] = ind2.getOneGene(i);
            }
            // 复制其他基因
            for (int i = 0; i < length; i++) {
                if (i >= choiceLoc1 && i <= choiceLoc2) {    // 已经复制的跳过
                    continue;
                }
                for (int j = 0; j < length; j++) {      // 遍历B
                    if (!inArray(ind2.getOneGene(j),crossBefore1)) {
                        crossBefore1[i] = ind2.getOneGene(j);
                        break;
                    }
                }
                for (int j = 0; j < length; j++) {      // 遍历A
                    if (!inArray(ind1.getOneGene(j),crossBefore2)) {
                        crossBefore2[i] = ind1.getOneGene(j);
                        break;
                    }
                }
            }
*/
/*
            // 顺序交叉法(后序)
            int choiceLoc1 = (int) (Math.random() * length);
            int choiceLoc2 = (int) (Math.random() * length);
            while (choiceLoc1 == choiceLoc2) {
                choiceLoc2 = (int) (Math.random() * length);
            }
            if (choiceLoc1 > choiceLoc2) {      // choiceLoc1 <= choiceLoc2
                int tem = choiceLoc1;
                choiceLoc1 = choiceLoc2;
                choiceLoc2 = tem;
            }
            // 复制基因片段
            for (int i = choiceLoc1; i <= choiceLoc2; i++) {
                crossBefore1[i] = ind1.getOneGene(i);
                crossBefore2[i] = ind2.getOneGene(i);
            }
            // 复制其他基因
            ArrayList<Integer> aArray = new ArrayList<>();      // 父代A的右边部分+左边部分
            ArrayList<Integer> bArray = new ArrayList<>();
            for (int i = choiceLoc1; i < length; i++) {
                aArray.add(ind1.getOneGene(i));
                bArray.add(ind2.getOneGene(i));
            }
            for (int i = 0; i < choiceLoc1; i++) {
                aArray.add(ind1.getOneGene(i));
                bArray.add(ind2.getOneGene(i));
            }

            for (int i = 0; i < length; i++) {
                if (i >= choiceLoc1 && i <= choiceLoc2) {    // 已经复制的跳过
                    continue;
                }

                for (int j = 0; j < bArray.size(); j++) {      // 遍历B
                    if (!inArray(bArray.get(j),crossBefore1)) {
                        crossBefore1[i] = bArray.get(j);
                        break;
                    }
                }
                for (int j = 0; j < aArray.size(); j++) {      // 遍历A
                    if (!inArray(aArray.get(j),crossBefore2)) {
                        crossBefore2[i] = aArray.get(j);
                        break;
                    }
                }
            }
*/
/*
            // 部分匹配交叉
            for (int i = 0; i < length; i++) {
                crossBefore1[i] = ind1.getOneGene(i);
                crossBefore2[i] = ind2.getOneGene(i);
            }
            int choiceLoc1 = (int) (Math.random() * length);
            int choiceLoc2 = (int) (Math.random() * length);
            while (choiceLoc1 == choiceLoc2) {
                choiceLoc2 = (int) (Math.random() * length);
            }
            if (choiceLoc1 > choiceLoc2) {      // choiceLoc1 <= choiceLoc2
                int tem = choiceLoc1;
                choiceLoc1 = choiceLoc2;
                choiceLoc2 = tem;
            }
            ArrayList<Integer> aArray = new ArrayList<>();
            ArrayList<Integer> bArray = new ArrayList<>();
            // 复制基因片段
            for (int i = choiceLoc1; i <= choiceLoc2; i++) {
                crossBefore1[i] = ind2.getOneGene(i);
                crossBefore2[i] = ind1.getOneGene(i);
                aArray.add(crossBefore1[i]);
                bArray.add(crossBefore2[i]);
            }
            // 替换重复基因
            int[] aArray1 = new int[aArray.size()];
            for (int i = 0; i < aArray.size(); i++) {
                aArray1[i] = aArray.get(i);
            }
            int[] bArray1 = new int[bArray.size()];
            for (int i = 0; i < bArray.size(); i++) {
                bArray1[i] = bArray.get(i);
            }
            // 执行替换
            for (int i = 0; i < length; i++) {
                if (i >= choiceLoc1 && i <= choiceLoc2) {    // 已经复制的跳过
                    continue;
                }
                if (inArray(crossBefore1[i],aArray1)) {
                    for (int j = 0; j < aArray1.length; j++) {
                        if (aArray1[j] == crossBefore1[i]) {
                            crossBefore1[i] = bArray1[j];
                            break;
                        }
                    }
                }
                if (inArray(crossBefore2[i],bArray1)) {
                    for (int j = 0; j < bArray1.length; j++) {
                        if (bArray1[j] == crossBefore2[i]) {
                            crossBefore2[i] = aArray1[j];
                            break;
                        }
                    }
                }
            }
*/

            // 若变换后是合法的，则更新输入
            if (TSP.isValid(crossBefore1) && TSP.isValid(crossBefore2)) {
                ind1.setGene(crossBefore1);
                ind2.setGene(crossBefore2);
                break;
            }
        }
    }

    public int[] crossoverHGA(Individual ind1, Individual ind2) {
        int length = ind1.getGeneLength();
        // 初始化子代基因数组
        int[] crossBefore1 = new int[length];
        int[] crossBefore2 = new int[length];

        // 两交换启发交叉(HGA)
        for (int i = 0; i < length; i++) {
            crossBefore1[i] = ind1.getOneGene(i);
            crossBefore2[i] = ind2.getOneGene(i);
        }
        // 随机找到一个起始位置
        int first = (int) (Math.random() * length);
        ArrayList<Integer> finalSequence = new ArrayList<>();
        // 移动A和B，都以first为首
        ArrayList<Integer> temA = new ArrayList<>();
        ArrayList<Integer> temB = new ArrayList<>();
        int flagA = 0;
        int flagB = 0;
        for (int i = 0; i < length; i++) {
            if (crossBefore1[i] == first) {
                flagA = i;
                break;
            }
        }
        for (int i = 0; i < length; i++) {
            if (crossBefore2[i] == first) {
                flagB = i;
                break;
            }
        }
        for (int i = flagA; i < length; i++) {
            temA.add(crossBefore1[i]);
        }
        for (int i = 0; i < flagA; i++) {
            temA.add(crossBefore1[i]);
        }
        for (int i = flagB; i < length; i++) {
            temB.add(crossBefore2[i]);
        }
        for (int i = 0; i < flagB; i++) {
            temB.add(crossBefore2[i]);
        }

        while (temA.size() > 0) {
            int flag = 0;
            if (temA.size() == 1) {
                finalSequence.add(temA.get(0));
                break;
            }

            int ABetter = 1;
            double sta_x = TSP.xLoc[temA.get(flag)];
            double sta_y = TSP.yLoc[temA.get(flag)];
            double A_x = TSP.xLoc[temA.get(flag + 1)];
            double A_y = TSP.yLoc[temA.get(flag + 1)];
            double B_x = TSP.xLoc[temB.get(flag + 1)];
            double B_y = TSP.yLoc[temB.get(flag + 1)];
            double dis1 = TSP.calTwoDistance(sta_x, sta_y, A_x, A_y);
            double dis2 = TSP.calTwoDistance(sta_x, sta_y, B_x, B_y);
            if (dis1 >= dis2) {
                ABetter = 0;
            }

            if (ABetter == 1) {
                // B重新找顺序
                int locTem = 0;
                for (int i = 0; i < temB.size(); i++) {         // 找到A更优的那个在B的位置,A更优的那个为temA.get(flag+1)
                    if (temB.get(i) == temA.get(flag + 1)) {
                        locTem = i;
                        break;
                    }
                }
                ArrayList<Integer> betterArray = new ArrayList<>();     // 将之后的加入，之前的加入
                for (int i = locTem; i < temB.size(); i++) {
                    betterArray.add(temB.get(i));
                }
                for (int i = 0; i < locTem; i++) {
                    betterArray.add(temB.get(i));
                }

                finalSequence.add(temA.get(flag));
                temA.remove(flag);     // 清除相同的第一个
                temB.clear();       // 清除B所有的，后面重新添加
                for (int i : betterArray) {
                    temB.add(i);
                }
                betterArray.clear();
            } else if (ABetter == 0) {
                // A重新找位置
                int locTem = 0;
                for (int i = 0; i < temA.size(); i++) {         // 找到B更优的那个在A的位置,B更优的那个为temB.get(flag+1)
                    if (temA.get(i) == temB.get(flag + 1)) {
                        locTem = i;
                        break;
                    }
                }
                ArrayList<Integer> betterArray = new ArrayList<>();     // 将之后的加入，之前的加入
                for (int i = locTem; i < temA.size(); i++) {
                    betterArray.add(temA.get(i));
                }
                for (int i = 0; i < locTem; i++) {
                    betterArray.add(temA.get(i));
                }

                finalSequence.add(temB.get(flag));
                temB.remove(flag);     // 清除相同的第一个
                temA.clear();       // 清除A所有的，后面重新添加
                for (int i : betterArray) {
                    temA.add(i);
                }
                betterArray.clear();
            }
        }

        int[] re = new int[length];
        for (int i = 0; i < length; i++) {
            re[i] = finalSequence.get(i);
        }

        // 旋转ind1和ind2基因串
        rotate(ind1.getGene(), (int) (Math.random() * length));
        rotate(ind2.getGene(), (int) (Math.random() * length));
        return re;
    }

    public int[] crossoverTHGA(Individual ind1, Individual ind2, Individual ind3) {
        int length = ind1.getGeneLength();
        // 初始化子代基因数组
        int[] crossBefore1 = new int[length];
        int[] crossBefore2 = new int[length];
        int[] crossBefore3 = new int[length];

        for (int i = 0; i < length; i++) {
            crossBefore1[i] = ind1.getOneGene(i);
            crossBefore2[i] = ind2.getOneGene(i);
            crossBefore3[i] = ind3.getOneGene(i);
        }
        // 随机找到一个起始位置
        int first = (int) (Math.random() * length);
        ArrayList<Integer> finalSequence = new ArrayList<>();
        // 移动A和B，都以first为首
        ArrayList<Integer> temA = new ArrayList<>();
        ArrayList<Integer> temB = new ArrayList<>();
        ArrayList<Integer> temC = new ArrayList<>();
        int flagA = 0;
        int flagB = 0;
        int flagC = 0;
        for (int i = 0; i < length; i++) {
            if (crossBefore1[i] == first) {
                flagA = i;
                break;
            }
        }
        for (int i = 0; i < length; i++) {
            if (crossBefore2[i] == first) {
                flagB = i;
                break;
            }
        }
        for (int i = 0; i < length; i++) {
            if (crossBefore3[i] == first) {
                flagC = i;
                break;
            }
        }
        for (int i = flagA; i < length; i++) {
            temA.add(crossBefore1[i]);
        }
        for (int i = 0; i < flagA; i++) {
            temA.add(crossBefore1[i]);
        }
        for (int i = flagB; i < length; i++) {
            temB.add(crossBefore2[i]);
        }
        for (int i = 0; i < flagB; i++) {
            temB.add(crossBefore2[i]);
        }
        for (int i = flagC; i < length; i++) {
            temC.add(crossBefore3[i]);
        }
        for (int i = 0; i < flagC; i++) {
            temC.add(crossBefore3[i]);
        }

        while (temA.size() > 0) {
            if (temA.size() == 2) {
                finalSequence.add(temA.get(0));
                finalSequence.add(temA.get(1));
                break;
            }

            int whoBetter = 1;      //  默认A
            double sta_x = TSP.xLoc[temA.get(0)];
            double sta_y = TSP.yLoc[temA.get(0)];
            double A_x = TSP.xLoc[temA.get(1)];
            double A_y = TSP.yLoc[temA.get(1)];
            double B_x = TSP.xLoc[temB.get(1)];
            double B_y = TSP.yLoc[temB.get(1)];
            double C_x = TSP.xLoc[temC.get(1)];
            double C_y = TSP.yLoc[temC.get(1)];
            double dis1 = TSP.calTwoDistance(sta_x, sta_y, A_x, A_y);
            double dis2 = TSP.calTwoDistance(sta_x, sta_y, B_x, B_y);
            double dis3 = TSP.calTwoDistance(sta_x, sta_y, C_x, C_y);
            double min = (dis3 >= dis2) ? dis2 : dis3;
            min = (min >= dis1) ? dis1 : min;
            if (min == dis1) {
                whoBetter = 1;
            } else if (min == dis2) {
                whoBetter = 2;
            } else {
                whoBetter = 3;
            }

            finalSequence.add(temA.get(0));
            temA.remove(0);
            temB.remove(0);
            temC.remove(0);

            if (whoBetter == 1) {
                // BC重新找顺序
                int locTem = 0;
                for (int i = 0; i < temB.size(); i++) {         // 找到A更优的那个在B的位置,A更优的那个为temA.get(0)
                    if (temB.get(i) == temA.get(0)) {
                        locTem = i;
                        break;
                    }
                }
                ArrayList<Integer> betterArray = new ArrayList<>();     // 将之后的加入，之前的加入     B
                for (int i = locTem; i < temB.size(); i++) {
                    betterArray.add(temB.get(i));
                }
                for (int i = 0; i < locTem; i++) {
                    betterArray.add(temB.get(i));
                }

                locTem = 0;
                for (int i = 0; i < temC.size(); i++) {         // 找到A更优的那个在C的位置,A更优的那个为temA.get(0)
                    if (temC.get(i) == temA.get(0)) {
                        locTem = i;
                        break;
                    }
                }
                ArrayList<Integer> betterArray1 = new ArrayList<>();     // 将之后的加入，之前的加入        C
                for (int i = locTem; i < temC.size(); i++) {
                    betterArray1.add(temC.get(i));
                }
                for (int i = 0; i < locTem; i++) {
                    betterArray1.add(temC.get(i));
                }

                temB.clear();       // 清除B所有的，后面重新添加
                for (int i : betterArray) {
                    temB.add(i);
                }
                betterArray.clear();
                temC.clear();       // 清除C所有的，后面重新添加
                for (int i : betterArray1) {
                    temC.add(i);
                }
                betterArray1.clear();
            } else if (whoBetter == 2) {
                // AC重新找位置
                int locTem = 0;
                for (int i = 0; i < temA.size(); i++) {         // 找到B更优的那个在A的位置,B更优的那个为temB.get(flag+1)
                    if (temA.get(i) == temB.get(0)) {
                        locTem = i;
                        break;
                    }
                }
                ArrayList<Integer> betterArray = new ArrayList<>();     // 将之后的加入，之前的加入
                for (int i = locTem; i < temA.size(); i++) {
                    betterArray.add(temA.get(i));
                }
                for (int i = 0; i < locTem; i++) {
                    betterArray.add(temA.get(i));
                }

                locTem = 0;
                for (int i = 0; i < temC.size(); i++) {         // 找到B更优的那个在C的位置,B更优的那个为temB.get(1)
                    if (temC.get(i) == temB.get(0)) {
                        locTem = i;
                        break;
                    }
                }
                ArrayList<Integer> betterArray1 = new ArrayList<>();     // 将之后的加入，之前的加入        C
                for (int i = locTem; i < temC.size(); i++) {
                    betterArray1.add(temC.get(i));
                }
                for (int i = 0; i < locTem; i++) {
                    betterArray1.add(temC.get(i));
                }

                temA.clear();       // 清除A所有的，后面重新添加
                for (int i : betterArray) {
                    temA.add(i);
                }
                betterArray.clear();
                temC.clear();       // 清除C所有的，后面重新添加
                for (int i : betterArray1) {
                    temC.add(i);
                }
                betterArray1.clear();
            } else if (whoBetter == 3) {
                // AB重新找位置
                int locTem = 0;
                for (int i = 0; i < temA.size(); i++) {         // 找到C更优的那个在A的位置,C更优的那个为temC.get(0)
                    if (temA.get(i) == temC.get(0)) {
                        locTem = i;
                        break;
                    }
                }
                ArrayList<Integer> betterArray = new ArrayList<>();     // 将之后的加入，之前的加入
                for (int i = locTem; i < temA.size(); i++) {
                    betterArray.add(temA.get(i));
                }
                for (int i = 0; i < locTem; i++) {
                    betterArray.add(temA.get(i));
                }

                locTem = 0;
                for (int i = 0; i < temB.size(); i++) {         // 找到C更优的那个在B的位置,C更优的那个为temC.get(1)
                    if (temB.get(i) == temC.get(0)) {
                        locTem = i;
                        break;
                    }
                }
                ArrayList<Integer> betterArray1 = new ArrayList<>();     // 将之后的加入，之前的加入    B
                for (int i = locTem; i < temB.size(); i++) {
                    betterArray1.add(temB.get(i));
                }
                for (int i = 0; i < locTem; i++) {
                    betterArray1.add(temB.get(i));
                }

                temA.clear();       // 清除A所有的，后面重新添加
                for (int i : betterArray) {
                    temA.add(i);
                }
                betterArray.clear();
                temB.clear();       // 清除B所有的，后面重新添加
                for (int i : betterArray1) {
                    temB.add(i);
                }
                betterArray1.clear();
            }
        }

        int[] re = new int[length];
        for (int i = 0; i < length; i++) {
            re[i] = finalSequence.get(i);
        }

        // 旋转基因串
        rotate(ind1.getGene(), (int) (Math.random() * length));
        rotate(ind2.getGene(), (int) (Math.random() * length));
        rotate(ind3.getGene(), (int) (Math.random() * length));

        return re;
    }


    public boolean inArray(int i, int[] arr) {
        boolean re = false;
        for (int has : arr) {
            if (has == i) {
                re = true;
                break;
            }
        }
        return re;
    }

    // 突变
    public void mutate(Individual individual, Population pop) {
        while (true) {
            // 复制基因数组
            int[] crossBefore1 = new int[individual.getGeneLength()];
            for (int i = 0; i < individual.getGeneLength(); i++) {
                crossBefore1[i] = individual.getOneGene(i);
            }
/*
            //单点相邻变异 选择一个位置进行变异（与旁边的交换）
            int tem = (int) (Math.random() * (individual.getGeneLength()-1));
            int mom = individual.getGene()[tem];
            individual.setOneGene(tem,individual.getOneGene(tem+1));
            individual.setOneGene(tem+1,mom);
*/
/*
            //单点随机变异
            int tem = (int) (Math.random() * (individual.getGeneLength() - 1));
            int tem1 = (int) (Math.random() * (individual.getGeneLength() - 1));
            while (tem == tem1) {
                tem1 = (int) (Math.random() * individual.getGeneLength());
            }
            int mom = individual.getGene()[tem];
            individual.setOneGene(tem, individual.getOneGene(tem1));
            individual.setOneGene(tem1, mom);
*/
            // 翻转
            for (int i = 0; i < 10; i++) {
                int tem = (int) (Math.random() * (individual.getGeneLength() - 1));
                int tem1 = (int) (Math.random() * (individual.getGeneLength() - 1));
                while (tem >= tem1) {
                    tem1 = (int) (Math.random() * individual.getGeneLength());
                }
                reversal(crossBefore1, tem, tem1);
            }

            //　变异后是否合法
            if (TSP.isValid(crossBefore1)) {
                for (int i = 0; i < crossBefore1.length; i++) {
                    individual.setOneGene(i, crossBefore1[i]);
                }
                break;
            }

        }
    }

    public static void rotate(int[] nums, int k) {
        int length = nums.length;

        k = k % length;

        if (length == 1)
            return;

        if (k == 0)
            return;

        reversal(nums, 0, length - k - 1);
        reversal(nums, length - k, length - 1);
        reversal(nums, 0, length - 1);
    }

    public static void reversal(int[] nums, int i, int j) {
        int t = 0;
        while (i < j && i >= 0) {
            t = nums[i];
            nums[i] = nums[j];
            nums[j] = t;
            i++;
            j--;
        }
    }
}
