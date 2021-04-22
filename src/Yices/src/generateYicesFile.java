import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class generateYicesFile {
    public void writeYicesFile(int[][] ageMatrix, int[][] importanceMatrix, int[][] componentImpactGraph, int[][] relatedBugsGraph, String inputPath, int size, ArrayList<String> eliGraph, Boolean flag) throws IOException {
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
                    myWriter.write("(assert+ (< (BUG " + Integer.toString(i+1) + ") (BUG " + Integer.toString(j+1) + ")) " + Integer.toString(prioWeight) + ")");
                    myWriter.write("\n");
                }
            }
        }
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
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++){
                if(componentImpactGraph[i][j] == 1){
                    myWriter.write("(assert+ (< (BUG " + Integer.toString(i+1) + ") (BUG " + Integer.toString(j+1) + ")) " + Integer.toString(prioWeight) + ")");
                    myWriter.write("\n");
                }
            }
        }
        myWriter.write("\n");
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++){
                if(relatedBugsGraph[i][j] == 1){
                    myWriter.write("(assert+ (< (BUG " + Integer.toString(i+1) + ") (BUG " + Integer.toString(j+1) + ")) " + Integer.toString(prioWeight) + ")");
                    myWriter.write("\n");
                }
            }
        }
        if(flag){
            myWriter.write("\n");
            for(int i = 0; i < eliGraph.size(); i++) {
                myWriter.write("(assert+ (< (BUG "+eliGraph.get(i).substring(3)+ ") (BUG "+eliGraph.get(i+1).substring(3) + ")) " + Integer.toString(prioWeight) + ")");
                myWriter.write("\n");
                i++;
            }
        }

        myWriter.write("\n");
        myWriter.write("(max-sat)");
        myWriter.close();

        if (flag){
            String [] toDelete = new String[eliGraph.size()];
            int counter = 0;
            for(int i = 0; i < eliGraph.size(); i++) {
                toDelete[counter]=("(assert+ (< (BUG "+eliGraph.get(i+1).substring(3)+ ") (BUG "+eliGraph.get(i).substring(3) + ")) " + Integer.toString(prioWeight) + ")");
                counter++;
                i++;
            }
            //System.out.println(Arrays.toString(toDelete));
            String tempFilepath = "C:\\Users\\serwa\\Desktop\\ThesisProject\\resourses\\.t";
            File myInputFile1 = new File(inputPath);
            File tempFile = new File(tempFilepath);
            BufferedReader bf11 = new BufferedReader(new FileReader(myInputFile1));

            FileWriter myWriter1 = new FileWriter(tempFile);
            PrintWriter pw = new PrintWriter(myWriter1);
            String line11 = null;
            boolean found = false;
            //Read from the original file and write to the new
            //unless content matches data to be removed.
            while ((line11 = bf11.readLine()) != null) {

                for (int i = 0; i<toDelete.length; i++){
                    if (line11.trim().equals(toDelete[i])) {
                        found = true;
                    }
                }
                if (!found) {
                    pw.println(line11);
                    pw.flush();
                }
                found = false;

            }

            pw.close();
            bf11.close();

            if (!myInputFile1.delete())
                System.out.println("Could not delete file");

            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(myInputFile1))
                System.out.println("Could not rename file");

        }

    }
}
