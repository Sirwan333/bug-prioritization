
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;


public class Algorithm {
    public Algorithm(){

    }
    public void runYices(String inputFile, String outputFile, int size) throws IOException, InterruptedException {
        String command = "cmd.exe /E:1900 /C C:\\Users\\serwa\\Desktop\\ThesisProject\\resourses\\smt\\yices.exe "+inputFile+"  > "+outputFile+" ";
        Runtime run = null;
        Process process = null;
        run = Runtime.getRuntime();
        process = run.exec(command);
        InputStream stderr = process.getErrorStream();
        InputStreamReader isr = new InputStreamReader(stderr);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        while ((line = br.readLine()) != null)
            System.out.println(line);
        int exitValue = process.waitFor();
        System.out.println(exitValue);
        stderr.close();
        isr.close();
        br.close();

    }
}







