package kadoufall.ga.knapsack;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import kadoufall.ga.Individual;
import kadoufall.ga.Population;

import java.io.*;
import java.util.ArrayList;

public class Knapsack extends Application {
    static int capacity;        //背包容量
    static int geneNum;         //基因长度
    static double[] weight;     //总重量
    static double[] value;      //总价值

    static ArrayList<Double> ave = new ArrayList<>();
    static ArrayList<Double> max = new ArrayList<>();

    public static void main(String[] args) {
        //test("src/testknapsack/Knapsack1.txt", "src/testknapsack/Knapsack-1[14302010049].txt");
        test("src/testknapsack/Knapsack2.txt", "src/testknapsack/Knapsack-2[14302010049].txt");
        test("src/testknapsack/Knapsack3.txt", "src/testknapsack/Knapsack-3[14302010049].txt");
        //launch(args);
    }

    // 测试并输出
    public static void test(String fileName, String outputFile) {
        capacity = 0;
        geneNum = 0;

        // 读取输入并初始化
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 0;
            while ((tempString = reader.readLine()) != null) {
                String[] tem = tempString.split(" ");
                if (line == 0) {
                    capacity = Integer.parseInt(tem[0]);
                    geneNum = Integer.parseInt(tem[1]);
                    weight = new double[geneNum];
                    value = new double[geneNum];
                } else {
                    weight[line - 1] = Double.parseDouble(tem[0]);
                    value[line - 1] = Double.parseDouble(tem[1]);
                }
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        // 初始化种群
        int individualNum = 100;
        Population p = new Population(individualNum, geneNum, 0.9, 0.05, "knapsack");

        for (int i = 0; i < individualNum; i++) {
            int[] temArray = new int[geneNum];
            for (int j = 0; j < geneNum; j++) {
                temArray[j] = (int) Math.round(Math.random());
            }

            if (!isValid(temArray)) {
                while (!isValid(temArray)) {
                    int ram = (int) (Math.random() * temArray.length);
                    if (temArray[ram] == 1) {
                        temArray[ram] = 0;
                    }
                }
            }

            p.getIndividual(i).setGene(temArray);

        }
        System.out.println("初始化完毕");
        // 开始进化
        KnapsackGA ga = new KnapsackGA();
        int count = 0;
        while (true) {
            count++;
            double fitBefore = p.getAveFitness();
            p = ga.evolution(p);
            /*
            System.out.print("  " + count + ": Max:" + String.format("%.2f", p.getMaxFitness()) + "  Ave " + String.format("%.2f", p.getAveFitness()) + "  MaxID " + p.getMaxFitnessID() + "   ");
            for (int j = 0; j < p.getLength(); j++) {
                System.out.print(String.format("%.2f", p.getIndividual(j).getFitness()) + " ");
            }
            System.out.println();
            */
            if (count < 300) {
                ave.add(p.getAveFitness());
                max.add(p.getMaxFitness());
            }

            if (count == 300) {
                break;
            }
        }

        // 将最后一代输出
        Individual last = p.getIndividual(p.getMaxFitnessID());
        String content = "";
        content += last.getFitness() + "\n";
        for (int i = 0; i < last.getGeneLength(); i++) {
            content += (i + 1) + " " + last.getOneGene(i) + "\n";
        }

        file = new File(outputFile);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Finish");

    }

    /////////////////////////////////////
    //辅助函数
    /////////////////////////////////////

    // 获取当前背包总重量
    public static double getWeight(int[] input) {
        double reWeight = 0;

        for (int i = 0; i < input.length; i++) {
            if (input[i] == 1) {
                reWeight += weight[i];
            }
        }

        return reWeight;
    }

    public static boolean isValid(int[] input) {
        boolean re = false;
        double all = getWeight(input);
        String tem = String.format("%.2f", all);
        if (Double.parseDouble(tem) <= capacity) {
            re = true;
        }
        return re;
    }

    // 当前背包的总价值
    public static double getValue(int[] input) {
        double reValue = 0;

        for (int i = 0; i < input.length; i++) {
            if (input[i] == 1) {
                reValue += value[i];
            }
        }

        return reValue;
    }

    // 单个个体(就是一个背包)的适应度
    public static double calFitness(int[] input) {
        return getValue(input);
    }


    @Override
    public void start(Stage stage) {
        stage.setTitle("Data");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("进化代数");
        yAxis.setLabel("适应度");
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("平均适应度");
        for (int i = 0; i < ave.size(); i++) {
            series1.getData().add(new XYChart.Data(i, ave.get(i)));
        }

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("最佳适应度");
        for (int i = 0; i < max.size(); i++) {
            series2.getData().add(new XYChart.Data(i, max.get(i)));
        }

        Scene scene = new Scene(lineChart, 1200, 600);
        lineChart.getData().addAll(series1, series2);
        //scene.getStylesheets().add("path/stylesheet.css");
        stage.setScene(scene);
        stage.show();
    }
}
