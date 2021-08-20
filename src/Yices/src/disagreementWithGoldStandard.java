

public class disagreementWithGoldStandard {

    public int getDisWithGS(String[] order1, String[] order2) {
        int counter = 0;
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
                            counter++;
                        }
                        flag = false;
                    }
                }
            }
        }

        return counter;
    }

    public int[] getDistanceWithGS(String[] order1, String[] order2) {
        int[] ds = new int[order1.length];

        boolean flag = false;
        for (int i = 0; i < order1.length; i++) {
                for (int k = 0; k < order2.length; k++) {
                    if (order1[i].equals(order2[k])) {
                        int distance = Math.abs(k-i);
                        ds[i] = distance;
                    }
                }
        }
        return ds;
    }
}
