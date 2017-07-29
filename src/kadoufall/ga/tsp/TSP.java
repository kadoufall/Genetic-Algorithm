package kadoufall.ga.tsp;


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
import java.util.Arrays;
import java.util.Collections;

public class TSP extends Application {
    static int geneNum;         //基因长度
    static double[] xLoc;       //横坐标
    static double[] yLoc;       //纵坐标

    static ArrayList<Double> ave;       // 平均适应度
    static ArrayList<Double> max;       // 最佳适应度

    public static void main(String[] args) {
        test("src/testtsp/TSP1.txt", "src/testtsp/TSP-1[14302010049].txt");
        test("src/testtsp/TSP2.txt", "src/testtsp/TSP-2[14302010049].txt");
        test("src/testtsp/TSP3.txt", "src/testtsp/TSP-3[14302010049].txt");
        //launch(args);
    }

    private static void test(String fileName, String outFilename) {
        ave = new ArrayList<>();
        max = new ArrayList<>();
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
                    geneNum = Integer.parseInt(tem[0]);
                    xLoc = new double[geneNum];
                    yLoc = new double[geneNum];
                } else {
                    xLoc[line - 1] = Double.parseDouble(tem[1]);
                    yLoc[line - 1] = Double.parseDouble(tem[2]);
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
        int individualNum = 300;     // 种群大小
        Population p = new Population(individualNum, geneNum, 0.9, 0.1, "TSP");

        for (int i = 0; i < individualNum; i++) {
            ArrayList<Integer> cityArray = new ArrayList<>();
            for (int j = 0; j < geneNum; j++) {
                cityArray.add(j);
            }
            Collections.shuffle(cityArray);

            int[] temArray = new int[geneNum];
            for (int j = 0; j < geneNum; j++) {
                temArray[j] = cityArray.get(j);
            }
            p.getIndividual(i).setGene(temArray);
        }
        System.out.println("初始化完毕");
/*
        for (int i = 0; i < individualNum; i++) {
            for (int j = 0; j < p.getIndividual(i).getGeneLength(); j++) {
                System.out.print(p.getIndividual(i).getOneGene(j) + " ");
            }
            System.out.println();
        }
*/

        // 开始进化
        TSPGA ga = new TSPGA();
        int count = 0;
        System.out.print("  " + 0 + ": Max:" + String.format("%.2f", 1 / p.getMaxFitness()) + "  Ave " + String.format("%.2f", 1 / p.getAveFitness()) + "  MaxID " + p.getMaxFitnessID() + "   ");
        for (int j = 0; j < p.getLength(); j++) {
            System.out.print(String.format("%.2f", 1 / p.getIndividual(j).getFitness()) + " ");
        }
        System.out.println();

        int gene = 400;
        while (true) {
            count++;
            double fitBefore = p.getAveFitness();
            p = ga.evolution(p);
  /*          System.out.print("  " + count + ": Max:" + String.format("%.2f", (1 / p.getMaxFitness())) + "  Ave " + String.format("%.2f", 1 / p.getAveFitness()) + "  MaxID " + p.getMaxFitnessID() + "   ");
            for (int j = 0; j < p.getLength(); j++) {
                System.out.print(String.format("%.2f", 1/p.getIndividual(j).getFitness()) + " ");
            }

            System.out.println();
*/
            if (count < gene) {
                ave.add(1 / p.getAveFitness());
                max.add(1 / p.getMaxFitness());
            }

            //     System.out.println();     p.getAveFitness() == fitBefore ||
            if (count == gene) {
                break;
            }
        }

        // 将最后一代输出
        Individual last = p.getIndividual(p.getMaxFitnessID());
        String content = "";
        content += (1 / last.getFitness()) + "\n";
        for (int i = 0; i < last.getGeneLength(); i++) {
            content += (last.getOneGene(i) + 1) + "\n";
        }

        file = new File(outFilename);
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
/*
        System.out.println(getDistance(p.getIndividual(p.getMaxFitnessID()).getGene()));
        int[] tem = new int[p.getGeneLength()];
        for (int i = 20; i < p.getGeneLength(); i++) {
            tem[i] = p.getIndividual(p.getMaxFitnessID()).getOneGene(i);
        }
        for (int i = 0; i < 20; i++) {
            tem[i] = p.getIndividual(p.getMaxFitnessID()).getOneGene(i);
        }
        System.out.println(getDistance(tem));
*/

    }

    public static boolean isValid(int[] input) {
        int[] tem = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            tem[i] = input[i];
        }
        boolean re = true;
        Arrays.sort(tem);
        for (int i = 0; i < tem.length - 1; i++) {
            if (tem[i] == tem[i + 1]) {
                re = false;
                break;
            }
        }

        return re;
    }

    // 当前路径总的长度
    public static double getDistance(int[] input) {
        double reDistance = 0;

        for (int i = 0; i < input.length; i++) {
            if (i == input.length - 1) {
                int firstCity = input[i];
                int secondCity = input[0];
                reDistance += calTwoDistance(xLoc[firstCity], yLoc[firstCity], xLoc[secondCity], yLoc[secondCity]);
            } else {
                int firstCity = input[i];
                int secondCity = input[i + 1];
                reDistance += calTwoDistance(xLoc[firstCity], yLoc[firstCity], xLoc[secondCity], yLoc[secondCity]);
            }
        }

        return reDistance;
    }

    public static double calTwoDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    public static double calFitness(int[] gene) {
        return (1 / getDistance(gene));
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

