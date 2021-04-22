import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String inputPath = "C:\\Users\\serwa\\Desktop\\ThesisProject\\resourses\\input.ys";
        String outputPath = "C:\\Users\\serwa\\Desktop\\ThesisProject\\resourses\\output.txt";
        String xlsxFile = "C:\\Users\\serwa\\Desktop\\ThesisProject\\resourses\\dataset\\bugs.xlsx";

        getBugSize gb = new getBugSize();
        int size = gb.getSize(xlsxFile);

        getAllGraphs g = new getAllGraphs();
        int[][] ageMatrix = g.getBugGraph(xlsxFile, size, 16);
        int[][] importanceMatrix = g.getBugGraph(xlsxFile, size, 5);
        int[][] componentImpactGraph = g.getBugGraph(xlsxFile, size, 19);
        int[][] relatedBugsGraph = g.getBugGraph(xlsxFile, size, 22);
        ArrayList<String> eliGraph = new ArrayList<>();
        boolean flag = false;
        g.printImportanceGraph(importanceMatrix, size);
        g.printComponentGraph(componentImpactGraph, size);
        g.printAgeGraph(ageMatrix, size);
        g.printRelatedBugsGraph(relatedBugsGraph, size);
        System.out.println();

        generateYicesFile yices = new generateYicesFile();
        yices.writeYicesFile(ageMatrix, importanceMatrix, componentImpactGraph, relatedBugsGraph, inputPath, size, eliGraph, flag);
        ArrayList<String[]> action = new ArrayList<>();
        Algorithm al = new Algorithm();
        al.runYices(inputPath, outputPath, size);
        smtOrder order = new smtOrder();
        //System.out.println(Arrays.toString(order.getFinalOrder(outputPath, size)));
        String [] order1 = order.getFinalOrder(outputPath, size);
        String cost1 = order.cost;
        System.out.println("First cost: "+cost1);
        Elicitation eli = new Elicitation();
        //System.out.println(Arrays.toString(eli.getElictadePair(order1, order2)));
        action.add(order1);
        for (int i = 0; i< 20; i++){
            generateYicesFileWithNewAsserts newYices = new generateYicesFileWithNewAsserts();
            newYices.writeYicesFileWithAsserts(ageMatrix, importanceMatrix, componentImpactGraph, relatedBugsGraph, inputPath, outputPath, size);
            al.runYices(inputPath, outputPath, size);
            String [] order2 = order.getFinalOrder(outputPath, size);
            String cost2 = order.cost;
            if ( (int)Integer.parseInt(cost2) > (int)Integer.parseInt(cost1) ){
                break;
            }

            action.add(order2);
        }
        for (int j =0; j<action.size(); j++) {
            for (int k=j+1; k<action.size(); k++){
                eli.getElictadePair(action.get(j), action.get(k));
            }
        }
        System.out.println();
        System.out.println("Alternatives: ");
        for (int i = 0; i < action.size();i++)
        {
            System.out.println(Arrays.toString(action.get(i)));
        }
        System.out.println();
        System.out.println("disagreement pairs: ");
        for (int i = 0; i < eli.action.size(); i++) {
            System.out.print(Arrays.toString(eli.action.get(i)) + " ");
        }
        System.out.println();
        ArrayList<String[]> newAray = eli.getFinalEli(eli.action);
        System.out.println();
        System.out.println("unique disagreement pairs: ");
        for (int i = 0; i < newAray.size(); i++) {
            System.out.print(Arrays.toString(newAray.get(i)) + " ");
        }
        System.out.println();

        String [] eliGraph1 = eli.askUser(newAray);
        System.out.println();
        for (int i = 0; i < eliGraph1.length; i++){
            eliGraph.add(eliGraph1[i]);
        }
        //System.out.print(Arrays.toString(eliGraph));
        flag = true;
        do {
            yices.writeYicesFile(ageMatrix, importanceMatrix, componentImpactGraph, relatedBugsGraph, inputPath, size, eliGraph, flag);
            ArrayList<String[]> action1 = new ArrayList<>();

            al.runYices(inputPath, outputPath, size);
            smtOrder order0 = new smtOrder();
            //System.out.println("Final order "+Arrays.toString(order0.getFinalOrder(outputPath, size)));
            String[] order11 = order0.getFinalOrder(outputPath, size);
            String cost11 = order0.cost;
            System.out.println("New Cost: " + cost11);
            Elicitation eli1 = new Elicitation();
            //System.out.println(Arrays.toString(eli.getElictadePair(order1, order2)));
            action1.add(order11);
            for (int i = 0; i < 20; i++) {
                generateYicesFileWithNewAsserts newYices = new generateYicesFileWithNewAsserts();
                newYices.writeYicesFileWithAsserts(ageMatrix, importanceMatrix, componentImpactGraph, relatedBugsGraph, inputPath, outputPath, size);
                al.runYices(inputPath, outputPath, size);
                String[] order2 = order0.getFinalOrder(outputPath, size);
                String cost2 = order0.cost;
                if ((int) Integer.parseInt(cost2) > (int) Integer.parseInt(cost1)) {
                    break;
                }

                action1.add(order2);
            }
            for (int j = 0; j < action1.size(); j++) {
                for (int k = j + 1; k < action1.size(); k++) {
                    eli1.getElictadePair(action1.get(j), action1.get(k));
                }
            }
            System.out.println();
            System.out.println("Alternatives :");
            for (int i = 0; i < action1.size(); i++) {
                System.out.println(Arrays.toString(action1.get(i)));
            }
            System.out.println();
            System.out.println("disagreement pairs: ");
            for (int i = 0; i < eli1.action.size(); i++) {
                System.out.print(Arrays.toString(eli1.action.get(i)) + " ");
            }
            System.out.println();
            ArrayList<String[]> newAray1 = eli1.getFinalEli(eli1.action);
            System.out.println("unique disagreement pairs: ");
            for (int i = 0; i < newAray1.size(); i++) {
                System.out.print(Arrays.toString(newAray1.get(i)) + " ");
            }
            System.out.println();
            String[] eliGraph2 = eli1.askUser(newAray1);
            System.out.println();
            for (int i = 0; i < eliGraph1.length; i++){
                eliGraph.add(eliGraph2[i]);
            }
        }while(true);



    }
}
