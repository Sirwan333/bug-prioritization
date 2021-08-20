import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class generateYicesFile {
    public void writeYicesFile(int[][] ageMatrix, int[][] importanceMatrix, int[][] componentImpactGraph, int[][] relatedBugsGraph,int[][] ccListGraph, String inputPath, int size, ArrayList<String> eliGraph, Boolean flag) throws IOException {
        int prioWeight = 1;

        File myInputFile = new File(inputPath);

        FileWriter myWriter = new FileWriter(myInputFile);
        myWriter.write("(set-evidence! true)");
        myWriter.write("\n");
        myWriter.write("(define BUG::(-> nat nat))");
        myWriter.write("\n");
        myWriter.write("(define N::nat "+Integer.toString(size)+")");
        myWriter.write("\n");
        myWriter.write("(assert (forall (i::(subrange 1 N)) (and (>= (BUG i) 1) (<= (BUG i) N))))");
        myWriter.write("\n");
        myWriter.write("(assert (forall (i::(subrange 1 (- N 1))) (forall (j::(subrange (+ i 1) N)) (/= (BUG i) (BUG j)))))");
        myWriter.write("\n");
        myWriter.write(";; Importance");
        myWriter.write("\n");
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++){
                if(importanceMatrix[i][j] == 1){
                    myWriter.write("(assert+ (< (BUG " + Integer.toString(i+1) + ") (BUG " + Integer.toString(j+1) + ")) " + Integer.toString(prioWeight) + ")");
                    myWriter.write("\n");
                }
            }
        }
        myWriter.write("\n");
        myWriter.write(";; Age");
        myWriter.write("\n");
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++){
                if(ageMatrix[i][j] == 1){
                    myWriter.write("(assert+ (< (BUG " + Integer.toString(i+1) + ") (BUG " + Integer.toString(j+1) + ")) " + Integer.toString(prioWeight) + ")");
                    myWriter.write("\n");
                }
            }
        }
        myWriter.write("\n");
        myWriter.write(";; Component Impact");
        myWriter.write("\n");
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++){
                if(componentImpactGraph[i][j] == 1){
                    myWriter.write("(assert+ (< (BUG " + Integer.toString(i+1) + ") (BUG " + Integer.toString(j+1) + ")) " + Integer.toString(prioWeight) + ")");
                    myWriter.write("\n");
                }
            }
        }
        myWriter.write("\n");
        myWriter.write(";; Related Bugs");
        myWriter.write("\n");
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++){
                if(relatedBugsGraph[i][j] == 1){
                    myWriter.write("(assert+ (< (BUG " + Integer.toString(i+1) + ") (BUG " + Integer.toString(j+1) + ")) " + Integer.toString(prioWeight) + ")");
                    myWriter.write("\n");
                }
            }
        }
        myWriter.write("\n");
        myWriter.write(";; CC List");
        myWriter.write("\n");
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++){
                if(ccListGraph[i][j] == 1){
                    myWriter.write("(assert+ (< (BUG " + Integer.toString(i+1) + ") (BUG " + Integer.toString(j+1) + ")) " + Integer.toString(prioWeight) + ")");
                    myWriter.write("\n");
                }
            }
        }
        if(flag){
            myWriter.write("\n");
            for(int i = 0; i < eliGraph.size(); i++) {
                myWriter.write("(assert+ (< (BUG "+eliGraph.get(i).substring(3, eliGraph.get(i).length())+ ") (BUG "+eliGraph.get(i+1).substring(3, eliGraph.get(i+1).length()) + ")) " + Integer.toString(prioWeight) + ")");
                myWriter.write("\n");
                i++;
            }
        }

        myWriter.write("\n");
        myWriter.write("(max-sat)");
        myWriter.flush();
        myWriter.close();

    }
}
