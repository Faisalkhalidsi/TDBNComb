/**
 * Created by sourcemylaptop on 1/4/2017.
 */
public class oriDTW {
    protected float[] seq1;
    protected float[] seq2;
   // protected int[][] warpingPath;

    protected int n;
    protected int m;
    protected int K;

    protected double warpingDistance;

    public oriDTW(float[] sample, float[] templete) {
        seq1 = sample;
        seq2 = templete;

        n = seq1.length;
        m = seq2.length;
        K = 1;
        warpingDistance = 0.0;

        this.compute();
    }

    public void compute() {
        System.out.println(m);

        double accumulatedDistance = 0.0;

        double[][] d = new double[n][m];	// local distances
        double[][] D = new double[n][m];	// global distances


        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                d[i][j] = distanceBetween(seq1[i], seq2[j]);
                System.out.println(d[i][j]);
            }
        }
        System.out.println();

        D[0][0] = d[0][0];



        for (int i = 1; i < n; i++) {
            D[i][0] = d[i][0] + D[i - 1][0];
            System.out.println(D[i][0]);
        }
        System.out.println();


        for (int j = 1; j < m; j++) {
            D[0][j] = d[0][j] + D[0][j - 1];
            System.out.println(D[0][j]);
        }


        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                System.out.println();
                System.out.println(i);
                System.out.println(j);
                System.out.println(D[i-1][j]);
                System.out.println(D[i-1][j-1]);
                System.out.println(D[i][j-1]);
                System.out.println();
                accumulatedDistance = Math.min(Math.min(D[i-1][j], D[i-1][j-1]), D[i][j-1]);

                accumulatedDistance += d[i][j];

                D[i][j] = accumulatedDistance;
                System.out.println(D[i][j]);
            }

        }
        accumulatedDistance = D[n - 1][m - 1];
        System.out.println(accumulatedDistance);

        int i = n - 1;
        int j = m - 1;
        int minIndex = 1;

        while ((i + j) != 0) {
            if (i == 0) {
                j -= 1;
            } else if (j == 0) {
                i -= 1;
            } else {	// i != 0 && j != 0
                double[] array = { D[i - 1][j], D[i][j - 1], D[i - 1][j - 1] };
                minIndex = this.getIndexOfMinimum(array);

                if (minIndex == 0) {
                    i -= 1;
                } else if (minIndex == 1) {
                    j -= 1;
                } else if (minIndex == 2) {
                    i -= 1;
                    j -= 1;
                }
            } // end else
            K++;
        } // end while

        System.out.println(K);
        warpingDistance = accumulatedDistance / K;
    }
    protected double distanceBetween(double p1, double p2) {
        return (p1 - p2) * (p1 - p2);
    }

    protected int getIndexOfMinimum(double[] array) {
        int index = 0;
        double val = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] < val) {
                val = array[i];
                index = i;
            }
        }
        return index;
    }

    public String toString() {
        String retVal = "Warping Distance: " + warpingDistance + "\n";
        return retVal;
    }

    public static void main(String[] args) {
        float[] n1 = {1,2};
        float[] n2 = {1,2,3,4};
        oriDTW dtw = new oriDTW(n1, n2);
        System.out.println(dtw);
    }
}
