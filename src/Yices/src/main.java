import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class main {
    public static void main(String[] args) throws IOException, InterruptedException {

        // Read inputs from config file
        FileReader reader = new FileReader("config");
        Properties config = new Properties();
        config.load(reader);
        int population = Integer.parseInt(config.getProperty("population"));
        int userError = Integer.parseInt(config.getProperty("userError"));
        int maxNumberOfElicitedPairs = Integer.parseInt(config.getProperty("maxNumberOfElicitedPairs"));
        String inputPath = config.getProperty("inputPath");
        String outputPath = config.getProperty("outputPath");
        String xlsxFile = config.getProperty("xlsxFile");
        String resultPath = config.getProperty("resultPath");
        Boolean isInteractive = Boolean.valueOf(config.getProperty("isInteractive"));

        // get the bugs size
        getBugSize gb = new getBugSize();
        int size = gb.getSize(xlsxFile);

        //get all initial graphs
        getAllGraphs g = new getAllGraphs();
        int[][] ageMatrix = g.getBugGraph(xlsxFile, size, 16);
        int[][] importanceMatrix = g.getBugGraph(xlsxFile, size, 5);
        int[][] componentImpactGraph = g.getBugGraph(xlsxFile, size, 19);
        int[][] relatedBugsGraph = g.getBugGraph(xlsxFile, size, 22);
        int[][] ccListGraph = g.getBugGraph(xlsxFile, size, 23);
        g.printImportanceGraph(importanceMatrix, size);
        g.printComponentGraph(componentImpactGraph, size);
        g.printAgeGraph(ageMatrix, size);
        g.printRelatedBugsGraph(relatedBugsGraph, size);
        g.printCCListGraph(ccListGraph, size);
        ArrayList<String> eliGraph = new ArrayList<>();
        System.out.println();

        //get the gold standard
        getGoldStandard gs = new getGoldStandard();
        String[] goldSatndard = gs.getGS(xlsxFile);
        System.out.print(Arrays.toString(goldSatndard));
        System.out.println();

        // do the first iteration with all initial graphs
        boolean isEliGraphIncluded = false;
        generateYicesFile yices = new generateYicesFile();
        yices.writeYicesFile(ageMatrix, importanceMatrix, componentImpactGraph, relatedBugsGraph, ccListGraph, inputPath, size, eliGraph, isEliGraphIncluded);

        // run the first input file through Yices
        Algorithm al = new Algorithm();
        al.runYices(inputPath, outputPath, size);

        // get the first order
        ArrayList<String[]> orderAlternativesList = new ArrayList<>();
        smtOrder order = new smtOrder();
        String[] order1 = order.getFinalOrder(outputPath, size);
        String cost1 = order.cost;
        System.out.println("---------------------------------------------------------------------");
        System.out.println("First cost: "+cost1);
        orderAlternativesList.add(order1);

        // a loop with pupulation size to get more orders
        for (int i = 0; i < population-1; i++) {
            generateYicesFileWithNewAsserts newYices = new generateYicesFileWithNewAsserts();
            newYices.writeYicesFileWithAsserts(inputPath, outputPath, size);
            al.runYices(inputPath, outputPath, size);
            String[] order2 = order.getFinalOrder(outputPath, size);
            String cost12 = order.cost;
            // if ( (int)Integer.parseInt(cost12) > (int)Integer.parseInt(cost1) ){
                 //flag2 = true;
             //    break;
            // }
            orderAlternativesList.add(order2);
        }

        //print all added alternatives
        System.out.println();
        System.out.println("Alternatives: ");
        for (int i = 0; i < orderAlternativesList.size(); i++) {
            System.out.println(Arrays.toString(orderAlternativesList.get(i)));
        }

        // dis with gold standard
        disagreementWithGoldStandard ds = new disagreementWithGoldStandard();
        float minimumDisagreemntWithGoldStandard = 0.0F;
        float[] intArray = new float[orderAlternativesList.size()];
        float averageDistance = 0.0F;
        int[] dsGS = new int[orderAlternativesList.size()];
        ArrayList<int[]> distance = new ArrayList<>();
        for (int j = 0; j < orderAlternativesList.size(); j++) {
            dsGS[j] = ds.getDisWithGS(orderAlternativesList.get(j), goldSatndard);
            distance.add(ds.getDistanceWithGS(orderAlternativesList.get(j), goldSatndard));
        }
        Arrays.sort(dsGS);
        minimumDisagreemntWithGoldStandard = dsGS[0];

        for (int i = 0; i < distance.size(); i++) {
            for (int j = 0; j < size; j++) {
                averageDistance = averageDistance + distance.get(i)[j];
            }
            averageDistance = averageDistance / size;
            intArray[i] = averageDistance;
            averageDistance = 0;
        }
        Arrays.sort(intArray);
        float minimumAverageDistance = (float) intArray[0];
        System.out.println();
        System.out.println("The average distance of each alternative is : ");
        System.out.println(Arrays.toString(intArray));
        System.out.println();
        System.out.println("The minimum average distanceis : ");
        System.out.println(minimumAverageDistance);
        System.out.println("The disagreement with gold standard of each alternative is : ");
        System.out.print(Arrays.toString(dsGS));
        System.out.println();
        System.out.println("The minimum disagreement with gold standard is : ");
        System.out.println(minimumDisagreemntWithGoldStandard);

        printResult result = new printResult();

        //Keep track of elicted pairs
        int numberOfElictedPairs = 0;
        Boolean isNumberofElictefPairsReached = false;


        // ask the user if more than on alternative
        //array for elicted pairs
        ArrayList<String[]> allEliPairs = new ArrayList<String[]>();
        Elicitation eli = new Elicitation();
        if (orderAlternativesList.size() > 1) {
            for (int j = 0; j < orderAlternativesList.size(); j++) {
                for (int k = j + 1; k < orderAlternativesList.size(); k++) {
                    eli.getElictadePair(orderAlternativesList.get(j), orderAlternativesList.get(k));
                }
            }
            System.out.println();
            System.out.println("All disagreement pairs: ");
            for (int i = 0; i < eli.action.size(); i++) {
                System.out.print(Arrays.toString(eli.action.get(i)) + " ");
            }
            System.out.println();
            ArrayList<String[]> newAray = eli.getFinalEli(eli.action);
            System.out.println();
            System.out.println("Unique disagreement pairs: ");
            for (int i = 0; i < newAray.size(); i++) {
                allEliPairs.add(newAray.get(i));
                System.out.print(Arrays.toString(newAray.get(i)) + " ");
            }

            System.out.println();
            String[] eliGraph1 = new String[newAray.size() * 2];

            int counter2 = 0;
            for (int i = 0; i < maxNumberOfElicitedPairs; i++) {
                String[] strArray = eli.askUserAutmated(allEliPairs.get(i), userError, goldSatndard);
                eliGraph1[counter2] = strArray[0];
                counter2++;
                eliGraph1[counter2] = strArray[1];
                counter2++;
                numberOfElictedPairs++;
                if (numberOfElictedPairs == newAray.size()) {
                    break;
                }
            }

            System.out.println(Arrays.toString(eliGraph1));
            System.out.println("User has been asked");
            for (int i = 0; i < counter2; i++) {
                eliGraph.add(eliGraph1[i]);
            }
            int count = 0;
            long errorPercentage = Math.round((userError*maxNumberOfElicitedPairs)*0.01);
            for (int i = 0; i<errorPercentage; i++){
                //System.out.println(count);
                String str = eliGraph.get(count);
                String str1 = eliGraph.get(count+1);
                eliGraph.set(count, str1);
                eliGraph.set(count+1, str);
                count+=2;
            }

        } else {

            System.out.println("prioritization is finished and the final order(s) is/are");
            for (int i = 0; i < orderAlternativesList.size(); i++) {
                System.out.println(Arrays.toString(orderAlternativesList.get(i)));
            }
            ////////////////////////////////////
            //result printing

            result.generateResult(resultPath, population, userError, maxNumberOfElicitedPairs, minimumAverageDistance, minimumDisagreemntWithGoldStandard, orderAlternativesList, allEliPairs, goldSatndard);
            System.exit(0);
        }


        System.out.println("---------------------------------------------------------------------");
        // ArrayList<String[]> action1 = new ArrayList<>();


        do {
            boolean flag1 = false;
            isEliGraphIncluded = true;
            //myCount++;
            yices.writeYicesFile(ageMatrix, importanceMatrix, componentImpactGraph, relatedBugsGraph, ccListGraph, inputPath, size, eliGraph, isEliGraphIncluded);
            //System.out.println("TRue");
            al.runYices(inputPath, outputPath, size);

            smtOrder order0 = new smtOrder();
            //System.out.println("Final order "+Arrays.toString(order0.getFinalOrder(outputPath, size)));
            String[] order11 = order0.getFinalOrder(outputPath, size);
            String cost11 = order0.cost;
             System.out.println();
                /*if ( ((int)Integer.parseInt(cost11) > (int)Integer.parseInt(cost12)) ) {
                    System.out.println("HERERERER");
                    result.generateResult(resultPath, population, userError, maxNumberOfElicitedPairs, minimumAverageDistance, minimumDisagreemntWithGoldStandard, orderAlternativesList, allEliPairs, goldSatndard);
                    System.exit(0);
                }*/
            ArrayList<String[]> orderAlternativesList1 = new ArrayList<>();
            orderAlternativesList1.add(order11);

            Elicitation eli1 = new Elicitation();
            //System.out.println(Arrays.toString(eli.getElictadePair(order1, order2)));
            for (int i = 0; i < population-1; i++) {
                generateYicesFileWithNewAsserts newYices = new generateYicesFileWithNewAsserts();
                newYices.writeYicesFileWithAsserts(inputPath, outputPath, size);
                al.runYices(inputPath, outputPath, size);
                smtOrder newOrder = new smtOrder();
                String[] order2 = newOrder.getFinalOrder(outputPath, size);
                String cost12 = newOrder.cost;
                System.out.println(cost12);
                    /*if ((int) Integer.parseInt(cost12) > (int) Integer.parseInt(cost11)) {
                        flag1 = true;
                        break;
                    }*/
                orderAlternativesList1.add(order2);
            }

            System.out.println();
            System.out.println("New Alternatives :");
            for (int i = 0; i < orderAlternativesList1.size(); i++) {
                System.out.println(Arrays.toString(orderAlternativesList1.get(i)));
            }

            float minimumDisagreemntWithGoldStandard1 = 0.0F;
            float[] intArray1 = new float[orderAlternativesList1.size()];
            float averageDistance1 = 0.0F;
            int[] dsGS1 = new int[orderAlternativesList1.size()];
            ArrayList<int[]> distance1 = new ArrayList<>();


            for (int j = 0; j < orderAlternativesList1.size(); j++) {

                dsGS1[j] = ds.getDisWithGS(orderAlternativesList1.get(j), goldSatndard);
                distance1.add(ds.getDistanceWithGS(orderAlternativesList1.get(j), goldSatndard));

            }
            Arrays.sort(dsGS1);
            minimumDisagreemntWithGoldStandard1 = dsGS1[0];


            for (int i = 0; i < distance1.size(); i++) {
                for (int j = 0; j < size; j++) {
                    averageDistance1 = averageDistance1 + distance1.get(i)[j];
                }
                averageDistance1 = averageDistance1 / size;
                intArray1[i] = averageDistance1;
                averageDistance1 = 0;
            }
            Arrays.sort(intArray1);
            float minimumAverageDistance1 = (float) intArray1[0];

            System.out.println();
            System.out.println("The average distance of each alternative is : ");
            System.out.println(Arrays.toString(intArray1));
            System.out.println();
            System.out.println("The minimum average distance is : ");
            System.out.println(minimumAverageDistance1);
            System.out.println("The disagreement with gold standard of each alternative is : ");
            System.out.print(Arrays.toString(dsGS1));
            System.out.println();
            System.out.println("The minmum average disagreement with gold standard is : ");
            System.out.println(minimumDisagreemntWithGoldStandard1);



            if (orderAlternativesList1.size() == 1 || (numberOfElictedPairs == maxNumberOfElicitedPairs)) {
                System.out.println("prioritization is finished and the final order(s) is/are");

                System.out.println("Final Alternatives :");
                for (int i = 0; i < orderAlternativesList1.size(); i++) {
                    System.out.println(Arrays.toString(orderAlternativesList1.get(i)));
                }
                ////////////////////////////////////////////////////
                result.generateResult(resultPath, population, userError, maxNumberOfElicitedPairs, minimumAverageDistance1, minimumDisagreemntWithGoldStandard1, orderAlternativesList1, allEliPairs, goldSatndard);
                System.exit(0);

            }


            for (int j = 0; j < orderAlternativesList1.size(); j++) {
                for (int k = j + 1; k < orderAlternativesList1.size(); k++) {
                    eli1.getElictadePair(orderAlternativesList1.get(j), orderAlternativesList1.get(k));
                }
            }


            System.out.println();

            System.out.println("All disagreement pairs: ");
            for (int i = 0; i < eli1.action.size(); i++) {
                System.out.print(Arrays.toString(eli1.action.get(i)) + " ");
            }
            System.out.println();
            ArrayList<String[]> newAray1 = eli1.getFinalEli(eli1.action);
            for (int i = 0; i < allEliPairs.size(); i++) {
                for (int j = 0; j < newAray1.size(); j++) {
                    if ((allEliPairs.get(i)[0].equals(newAray1.get(j)[0]) && allEliPairs.get(i)[1].equals(newAray1.get(j)[1])) || (allEliPairs.get(i)[0].equals(newAray1.get(j)[1]) && allEliPairs.get(i)[1].equals(newAray1.get(j)[0]))) {
                        newAray1.remove(j);
                        j--;
                    }
                }
            }
            if (newAray1.size() == 0){
                result.generateResult(resultPath, population, userError, maxNumberOfElicitedPairs, minimumAverageDistance1, minimumDisagreemntWithGoldStandard1, orderAlternativesList1, allEliPairs, goldSatndard);
                System.exit(0);
            }
            System.out.println("Unique disagreement pairs: ");
            for (int i = 0; i < newAray1.size(); i++) {
                allEliPairs.add(newAray1.get(i));
                System.out.print(Arrays.toString(newAray1.get(i)) + " ");
            }
            String[] eliGraph2 = new String[newAray1.size() * 2];
            System.out.println();
            int counter3 = 0;
            int newCounter = 0;
            int co = numberOfElictedPairs;
            for (int i = 0; i < (maxNumberOfElicitedPairs - co); i++) {
                String[] strArray = eli.askUserAutmated(allEliPairs.get(numberOfElictedPairs), userError, goldSatndard);

                eliGraph2[counter3] = strArray[0];
                counter3++;
                eliGraph2[counter3] = strArray[1];
                counter3++;
                numberOfElictedPairs++;
                newCounter++;
                if (newCounter == newAray1.size()) {

                    break;
                }
            }
            System.out.println(Arrays.toString(eliGraph2));
            System.out.println("User has been asked");
            System.out.println();
            for (int i = 0; i < counter3; i++) {
                eliGraph.add(eliGraph2[i]);
            }


            System.out.println("---------------------------------------------------------------------");


        } while (true);


    }
}
