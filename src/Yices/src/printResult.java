import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

public class printResult {

    public void generateResult(String inputPath, int population, int userError, int maxNumberOfElicitedPairs, float minimumAverageDistance, float minimumDisagreement, ArrayList<String[]> orders, ArrayList<String[]> eliPairs, String[] goldStandard) throws IOException {
        File myInputFile = new File(inputPath);




       FileWriter myWriter1 = new FileWriter(myInputFile);

        myWriter1.write("population: ");
        myWriter1.write(Integer.toString(population));
        myWriter1.write("\n");
        myWriter1.write("\n");
        myWriter1.write("User Error: ");
        myWriter1.write(Integer.toString(userError)+" %");
        myWriter1.write("\n");
        myWriter1.write("\n");
        myWriter1.write("Max Number Of Elicited Pairs: ");
        myWriter1.write(Integer.toString(maxNumberOfElicitedPairs));
        myWriter1.write("\n");
        myWriter1.write("\n");
        myWriter1.write("The minimum average distance: ");
        myWriter1.write(Double.toString(minimumAverageDistance));
        myWriter1.write("\n");
        myWriter1.write("\n");
        myWriter1.write("The minimum disagreement: ");
        myWriter1.write(Double.toString(minimumDisagreement));
        myWriter1.write("\n");
        myWriter1.write("\n");
        myWriter1.write("The final order(s): ");
        myWriter1.write("\n");
        for (int i = 0; i < orders.size(); i++) {
            myWriter1.write(Arrays.toString(orders.get(i)));
            myWriter1.write("\n");
        }
        myWriter1.write("\n");
        myWriter1.write("Elicited pairs: ");
        myWriter1.write("\n");
        for (int i = 0; i < eliPairs.size(); i++) {
            myWriter1.write(Arrays.toString(eliPairs.get(i)));
        }
        myWriter1.write("\n");
        myWriter1.write("\n");
        myWriter1.write("The gold standard: ");
        myWriter1.write("\n");
        myWriter1.write(Arrays.toString(goldStandard));
        myWriter1.flush();
        myWriter1.close();
    }

}
