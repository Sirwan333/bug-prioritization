import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class random {

    public static void main(String[] args) throws IOException, InterruptedException {
        int size = 20;
        ArrayList<String[]> orderAlternativesList = new ArrayList<>();
        String[] radomOrder = {"BUG13", "BUG19", "BUG20", "BUG07", "BUG03", "BUG14", "BUG17", "BUG12", "BUG02", "BUG15", "BUG18", "BUG16", "BUG11", "BUG10", "BUG05", "BUG06", "BUG09", "BUG01", "BUG08", "BUG04"};

        Collections.shuffle(Arrays.asList(radomOrder));

        System.out.println(Arrays.toString(radomOrder));
        orderAlternativesList.add(radomOrder);

        String[] goldSatndard = {"BUG04", "BUG03", "BUG01", "BUG12", "BUG18", "BUG06", "BUG05", "BUG14", "BUG09", "BUG15", "BUG13", "BUG17", "BUG19", "BUG20", "BUG16", "BUG11", "BUG10", "BUG08", "BUG07", "BUG02"};
        disagreementWithGoldStandard ds = new disagreementWithGoldStandard();
        float minimumDisagreemntWithGoldStandard = 0.0F;
        float[] intArray = new float[orderAlternativesList.size()];
        float averageDistance = 0.0F;
        int[] dsGS = new int[orderAlternativesList.size()];
        ArrayList<int[]> distance = new ArrayList<>();
        for (int j = 0; j < orderAlternativesList.size(); j++) {
            dsGS[j] = ds.getDisWithGS(orderAlternativesList.get(j), goldSatndard);
            distance.add(ds.getDistanceWithGS(orderAlternativesList.get(j), goldSatndard));
        }
        Arrays.sort(dsGS);
        minimumDisagreemntWithGoldStandard = dsGS[0];

        for (int i = 0; i < distance.size(); i++) {
            for (int j = 0; j < size; j++) {
                averageDistance = averageDistance + distance.get(i)[j];
            }
            averageDistance = averageDistance / size;
            intArray[i] = averageDistance;
            averageDistance = 0;
        }
        Arrays.sort(intArray);
        float minimumAverageDistance = (float) intArray[0];
        System.out.println();
        System.out.println("The average distance of each alternative is : ");
        System.out.println(Arrays.toString(intArray));
        System.out.println();
        System.out.println("The minimum average distanceis : ");
        System.out.println(minimumAverageDistance);
        System.out.println("The disagreement with gold standard of each alternative is : ");
        System.out.print(Arrays.toString(dsGS));
        System.out.println();
        System.out.println("The minimum disagreement with gold standard is : ");
        System.out.println(minimumDisagreemntWithGoldStandard);
    }
}