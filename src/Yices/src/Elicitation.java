import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Elicitation {
    ArrayList<String[]> action = new ArrayList<>();
    ArrayList<String[]> action1 = new ArrayList<>();

    public ArrayList<String[]> getElictadePair(String[] order1, String[] order2) {

        boolean flag = false;
        for (int i = 0; i < order1.length; i++) {
            for (int j = i + 1; j < order1.length; j++) {
                for (int k = 0; k < order2.length; k++) {
                    if (order1[i].equals(order2[k])) {
                        for (int m = k + 1; m < order2.length; m++) {
                            if (order1[j].equals(order2[m])) {
                                flag = true;
                            }
                        }
                        if (!flag) {
                            action.add(new String[]{order1[i], order1[j]});
                        }
                        flag = false;
                    }
                }
            }
        }

        return action;
    }

    public ArrayList<String[]> getFinalEli(ArrayList<String[]> preEli){
        for (int i = 0; i <preEli.size(); i++){
            for (int k = i+1; k<preEli.size(); k++){
                if ((preEli.get(i)[0].equals(preEli.get(k)[0]) && preEli.get(i)[1].equals(preEli.get(k)[1])) || (preEli.get(i)[0].equals(preEli.get(k)[1]) && preEli.get(i)[1].equals(preEli.get(k)[0]))){
                    preEli.remove(k);
                    k--;
                }
            }
        }
        return preEli;
    }

    public String[] askUser(ArrayList<String[]> preEli) {
        Scanner sc= new Scanner(System.in);
        String[] newArray = new String[preEli.size()*2];
        int counter = 0;
        for (int i = 0; i<preEli.size(); i++){
            System.out.print(preEli.get(i)[0]+" (press 1) or "+preEli.get(i)[1]+" (press2) ?");
            int choice = sc.nextInt();
            if(choice == 1){
                newArray[counter] = preEli.get(i)[0];
                counter++;
                newArray[counter] = preEli.get(i)[1];
                counter++;
            } else if (choice == 2){
                newArray[counter] = preEli.get(i)[1];
                counter++;
                newArray[counter] = preEli.get(i)[0];
                counter++;
            }
        }
        return newArray;
    }
}




