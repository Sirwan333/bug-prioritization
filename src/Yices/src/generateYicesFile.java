import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class generateYicesFile {
    public void writeYicesFile(int[][] ageMatrix, int[][] importanceMatrix, int[][] componentImpactGraph, int[][] relatedBugsGraph, String inputPath, int size) throws IOException {

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
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++){
                if(importanceMatrix[i][j] == 1){
                    myWriter.write("(assert+ (< (BUG " + Integer.toString(i) + ") (BUG " + Integer.toString(j) + ")) " + Integer.toString(prioWeight) + ")");
                    myWriter.write("\n");
                }
            }
        }
        myWriter.write("\n");
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++){
                if(ageMatrix[i][j] == 1){
                    myWriter.write("(assert+ (< (BUG " + Integer.toString(i) + ") (BUG " + Integer.toString(j) + ")) " + Integer.toString(prioWeight) + ")");
                    myWriter.write("\n");
                }
            }
        }
        myWriter.write("\n");
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++){
                if(componentImpactGraph[i][j] == 1){
                    myWriter.write("(assert+ (< (BUG " + Integer.toString(i) + ") (BUG " + Integer.toString(j) + ")) " + Integer.toString(prioWeight) + ")");
                    myWriter.write("\n");
                }
            }
        }
        myWriter.write("\n");
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++){
                if(relatedBugsGraph[i][j] == 1){
                    myWriter.write("(assert+ (< (BUG " + Integer.toString(i) + ") (BUG " + Integer.toString(j) + ")) " + Integer.toString(prioWeight) + ")");
                    myWriter.write("\n");
                }
            }
        }
        myWriter.write("\n");
        myWriter.write("(max-sat)");
        myWriter.close();

    }
}
