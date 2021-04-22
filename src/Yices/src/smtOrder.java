import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class smtOrder {
    public smtOrder() {

    }
    public String cost;
    public String[] getFinalOrder(String filePath, int size) throws IOException, InterruptedException {

        String[] finalOrder = new String[size];
        int[] bugIndexes = new int[size];

        FileInputStream fs = new FileInputStream(filePath);
        DataInputStream ds = new DataInputStream(fs);
        BufferedReader br = new BufferedReader(new InputStreamReader(ds));

        int bugPair = 0;
        replaceString rep = new replaceString();

        String tmp;
        int i = 0;
        String line;
        while ((line = br.readLine()) != null) {
            if (line.indexOf("(=") != -1) {
                bugPair++;
                String bug = line.substring(8, line.length());
                String search = ")";
                String replace = "";
                bug = rep.replaceAllString(bug, search, replace);
                String[] result = bug.split(" ");
                bugIndexes[bugPair - 1] = i;
                tmp = "BUG";
                String BUG = tmp.concat(result[i]);
                finalOrder[Integer.parseInt(result[1])-1] = BUG;
            }

            if (bugPair == size) {
                break;
            }
        }
        while ((line = br.readLine()) != null) {
            if (line.indexOf("cost") != -1) {
                cost = line.substring(6, line.length());

            }

        }

        br.close();
        ds.close();
        fs.close();

        for (int k = 0; k < size - 1; k++) {
            for (int j = k + 1; j < size; ++j) {
                if (bugIndexes[k] > bugIndexes[j]) {
                    int tmpe = bugIndexes[k];
                    bugIndexes[k] = bugIndexes[j];
                    bugIndexes[j] = tmpe;
                    String str = finalOrder[k];
                    finalOrder[k] = finalOrder[j];
                    finalOrder[j] = str;
                }
            }
        }
        return finalOrder;

    }

}
