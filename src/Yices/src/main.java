import java.io.IOException;
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
        g.printImportanceGraph(importanceMatrix, size);
        g.printComponentGraph(componentImpactGraph, size);
        g.printAgeGraph(ageMatrix, size);
        g.printRelatedBugsGraph(relatedBugsGraph, size);
        System.out.println();

        generateYicesFile yices = new generateYicesFile();
        yices.writeYicesFile(ageMatrix, importanceMatrix, componentImpactGraph, relatedBugsGraph, inputPath, size);

        Algorithm al = new Algorithm();
        al.runYices(inputPath, outputPath, size);
        smtOrder order = new smtOrder();
        System.out.println(Arrays.toString(order.getFinalOrder(outputPath, size)));

        generateYicesFileWithNewAsserts newYices = new generateYicesFileWithNewAsserts();
        newYices.writeYicesFileWithAsserts(ageMatrix, importanceMatrix, componentImpactGraph, relatedBugsGraph, inputPath, outputPath, size);
        al.runYices(inputPath, outputPath, size);
        System.out.println(Arrays.toString(order.getFinalOrder(outputPath, size)));


    }
}
