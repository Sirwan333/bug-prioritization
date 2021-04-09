import java.io.*;
import java.util.ArrayList;

public class generateYicesFileWithNewAsserts {
    public void writeYicesFileWithAsserts(int[][] ageMatrix, int[][] importanceMatrix, int[][] componentImpactGraph, int[][] relatedBugsGraph, String inputPath, String outputPath, int size) throws IOException {
        FileInputStream fs = new FileInputStream(outputPath);
        DataInputStream ds = new DataInputStream(fs);
        BufferedReader bf = new BufferedReader(new InputStreamReader(ds));

        int bugPair = 0;
        replaceString rep = new replaceString();
        ArrayList<String> newAssertString = new ArrayList();
        String str = "(assert ";
        newAssertString.add(str);
        for(int i  = 0; i < size-1 ; i++) {
            newAssertString.add("(or ");
        }
        String tmp;
        String line;
        while ((line = bf.readLine()) != null) {
            if (line.indexOf("(=") != -1) {
                bugPair++;
                String search = "(=";
                String replace = "(/=";
                line = rep.replaceAllString(line, search, replace);
                newAssertString.add(line);
                if (bugPair == 1) {
                    newAssertString.add(" ");
                } else if (bugPair == size) {
                    newAssertString.add("))");
                } else if (bugPair < size) {
                    newAssertString.add(")");
                }
            }


            if (bugPair == size) {
                break;
            }
        }

        bf.close();
        ds.close();
        fs.close();



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
        for(int i = 0; i<newAssertString.size(); i++){
            myWriter.write(newAssertString.get(i));
        }
        myWriter.write("\n");
        myWriter.write("(max-sat)");
        myWriter.close();

    }

}
