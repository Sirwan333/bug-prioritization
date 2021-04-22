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
        String tempFilepath = "C:\\Users\\serwa\\Desktop\\ThesisProject\\resourses\\.tmp";
        File myInputFile = new File(inputPath);
        File tempFile = new File(tempFilepath);
        BufferedReader bf11 = new BufferedReader(new FileReader(myInputFile));

        FileWriter myWriter = new FileWriter(tempFile, true);
        PrintWriter pw = new PrintWriter(myWriter);
        String line11 = null;

        //Read from the original file and write to the new
        //unless content matches data to be removed.
        while ((line11 = bf11.readLine()) != null) {

            if (!line11.trim().equals("(max-sat)")) {
                pw.println(line11);
                pw.flush();
            }
        }
        pw.write("\n");
        for(int i = 0; i<newAssertString.size(); i++){
            pw.write(newAssertString.get(i));
        }
        pw.write("\n");
        pw.write("(max-sat)");
        pw.close();
        bf11.close();

        if (!myInputFile.delete())
            System.out.println("Could not delete file");

        //Rename the new file to the filename the original file had.
        if (!tempFile.renameTo(myInputFile))
            System.out.println("Could not rename file");


        /*myWriter.write("(set-evidence! true)");
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
        }*/


    }

}
