/*
 * DTW.java
 */


import java.util.ArrayList;

/**
 * This class implements the Dynamic Time Warping algorithm
 * given two sequences
 * <pre>
 *   X = x1, x2,..., xi,..., xn
 *   Y = y1, y2,..., yj,..., ym
 *  </pre>
 *
 * @author		Cheol-Woo Jung (cjung@gatech.edu)
 * http://trac.research.cc.gatech.edu/GART/browser/GART/weka/edu/gatech/gart/ml/weka/DTW.java?rev=9
 * @version	1.0
 */
public class DTW {

//    protected float[] seq1;
//    protected float[] seq2;
    ArrayList<Double> seq1 = new ArrayList<Double>();
    ArrayList<Double> seq2 = new ArrayList<Double>();

    protected int[][] warpingPath;

    protected int n;
    protected int m;
    protected int K;

    protected double warpingDistance;

    public DTW() {

    }

    public void calcDTW(ArrayList sample, ArrayList templete) {
        seq1 = sample;
        seq2 = templete;

        n = seq1.size();
        m = seq2.size();
        K = 1;

        warpingPath = new int[n + m][2];	// max(n, m) <= K < n + m
        warpingDistance = 0.0;

        this.compute();
    }

    public void compute() {
        double accumulatedDistance = 0.0;

        double[][] d = new double[n][m];	// local distances
        double[][] D = new double[n][m];	// global distances

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                d[i][j] = distanceBetween(seq1.get(i),seq2.get(j));
            }
        }

        D[0][0] = d[0][0];

        for (int i = 1; i < n; i++) {
            D[i][0] = d[i][0] + D[i - 1][0];
        }

        for (int j = 1; j < m; j++) {
            D[0][j] = d[0][j] + D[0][j - 1];
        }

        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                accumulatedDistance = Math.min(Math.min(D[i-1][j], D[i-1][j-1]), D[i][j-1]);
                accumulatedDistance += d[i][j];
                D[i][j] = accumulatedDistance;
            }
        }
        accumulatedDistance = D[n - 1][m - 1];

        int i = n - 1;
        int j = m - 1;
        int minIndex = 1;

        warpingPath[K - 1][0] = i;
        warpingPath[K - 1][1] = j;

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
            warpingPath[K - 1][0] = i;
            warpingPath[K - 1][1] = j;
        } // end while
        warpingDistance = accumulatedDistance / K;

        this.reversePath(warpingPath);
    }

    /**
     * Changes the order of the warping path (increasing order)
     *
     * @param path	the warping path in reverse order
     */
    protected void reversePath(int[][] path) {
        int[][] newPath = new int[K][2];
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < 2; j++) {
                newPath[i][j] = path[K - i - 1][j];
            }
        }
        warpingPath = newPath;
    }
    /**
     * Returns the warping distance
     *
     * @return
     */
    public double getDistance() {
        return warpingDistance;
    }

    /**
     * Computes a distance between two points
     *
     * @param p1	the point 1
     * @param p2	the point 2
     * @return		the distance between two points
     */
    protected double distanceBetween(double p1, double p2) {
        return (p1 - p2) * (p1 - p2);
    }

    /**
     * Finds the index of the minimum element from the given array
     *
     * @param array		the array containing numeric values
     * @return				the min value among elements
     */
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

    /**
     *	Returns a string that displays the warping distance and path
     */
    public String toString() {
        String retVal = "Warping Distance: " + warpingDistance + "\n";
        retVal += "Warping Path: {";
        for (int i = 0; i < K; i++) {
            retVal += "(" + warpingPath[i][0] + ", " +warpingPath[i][1] + ")";
            retVal += (i == K - 1) ? "}" : ", ";
        }
        return retVal;
    }

//    public void action(){
//        d
//    }

    /**
     * Tests this class
     *
     * @param args	ignored
     */

    public static void main(String[] args){
        AMC genAMC;
        AMC basAMC;

        String generatedAMC = "C:/Users/sourceMyLab/Documents/Tesis/v3/tdbnDemo/src/data/generated/output.amc";
        String basedAMC= "C:/Users/sourceMyLab/Documents/Tesis/v3/tdbnDemo/src/data/35_02.amc";

        genAMC = new AMC(generatedAMC);
        basAMC = new AMC(basedAMC);

        DTW dtw = new DTW();

        double save =0;

//        3
        dtw.calcDTW(genAMC.rootTX,basAMC.rootTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.rootTY,basAMC.rootTY);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.rootTZ,basAMC.rootTZ);
        save=save+ dtw.warpingDistance;

//        3
        dtw.calcDTW(genAMC.lowerneckTX,basAMC.lowerneckTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.lowerneckTY,basAMC.lowerneckTY);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.lowerneckTZ,basAMC.lowerneckTZ);
        save=save+ dtw.warpingDistance;

//        3
        dtw.calcDTW(genAMC.upperbackTX,basAMC.upperbackTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.upperbackTY,basAMC.upperbackTY);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.upperbackTZ,basAMC.upperbackTZ);
        save=save+ dtw.warpingDistance;

//        3
        dtw.calcDTW(genAMC.thoraxTX,basAMC.thoraxTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.thoraxTY,basAMC.thoraxTY);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.thoraxTZ,basAMC.thoraxTZ);
        save=save+ dtw.warpingDistance;

//        3
        dtw.calcDTW(genAMC.lowerneckTX,basAMC.lowerneckTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.lowerneckTY,basAMC.lowerneckTY);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.lowerneckTZ,basAMC.lowerneckTZ);
        save=save+ dtw.warpingDistance;

//        3
        dtw.calcDTW(genAMC.uppperneckTX,basAMC.uppperneckTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.uppperneckTY,basAMC.uppperneckTY);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.uppperneckTZ,basAMC.uppperneckTZ);
        save=save+ dtw.warpingDistance;

//        3
        dtw.calcDTW(genAMC.headTX,basAMC.headTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.headTY,basAMC.headTY);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.headTZ,basAMC.headTZ);
        save=save+ dtw.warpingDistance;

//        2
        dtw.calcDTW(genAMC.rclavicleTX,basAMC.rclavicleTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.rclavicleTY,basAMC.rclavicleTY);
        save=save+ dtw.warpingDistance;

//        3
        dtw.calcDTW(genAMC.rhumerusTX,basAMC.rhumerusTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.rhumerusTY,basAMC.rhumerusTY);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.rhumerusTZ,basAMC.rhumerusTZ);
        save=save+ dtw.warpingDistance;

//        1
        dtw.calcDTW(genAMC.rradiusTX,basAMC.rradiusTX);
        save=save+ dtw.warpingDistance;

//        1
        dtw.calcDTW(genAMC.rwristTX,basAMC.rwristTX);
        save=save+ dtw.warpingDistance;

//        2
        dtw.calcDTW(genAMC.rhandTX,basAMC.rhandTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.rhandTY,basAMC.rhandTY);
        save=save+ dtw.warpingDistance;

//        1
        dtw.calcDTW(genAMC.rfingersTX,basAMC.rfingersTX);
        save=save+ dtw.warpingDistance;

//        2
        dtw.calcDTW(genAMC.rthumbTX,basAMC.rthumbTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.rthumbTY,basAMC.rthumbTY);
        save=save+ dtw.warpingDistance;

//        2
        dtw.calcDTW(genAMC.lclavicleTX,basAMC.lclavicleTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.lclavicleTY,basAMC.lclavicleTY);
        save=save+ dtw.warpingDistance;

//        3
        dtw.calcDTW(genAMC.lhumerusTX,basAMC.lhumerusTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.lhumerusTY,basAMC.lhumerusTY);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.lhumerusTZ,basAMC.lhumerusTZ);
        save=save+ dtw.warpingDistance;

//        1
        dtw.calcDTW(genAMC.lradiusTX,basAMC.lradiusTX);
        save=save+ dtw.warpingDistance;

//        1
        dtw.calcDTW(genAMC.lwristTX,basAMC.lwristTX);
        save=save+ dtw.warpingDistance;

//        2
        dtw.calcDTW(genAMC.lhandTX,basAMC.lhandTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.lhandTY,basAMC.lhandTY);
        save=save+ dtw.warpingDistance;

//        1
        dtw.calcDTW(genAMC.lfingersTX,basAMC.lfingersTX);
        save=save+ dtw.warpingDistance;

//        2
        dtw.calcDTW(genAMC.lthumbTX,basAMC.lthumbTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.lthumbTY,basAMC.lthumbTY);
        save=save+ dtw.warpingDistance;

//        3
        dtw.calcDTW(genAMC.rfemurTX,basAMC.rfemurTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.rfemurTY,basAMC.rfemurTY);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.rfemurTZ,basAMC.rfemurTZ);
        save=save+ dtw.warpingDistance;

//        1
        dtw.calcDTW(genAMC.rtibiaTX,basAMC.rtibiaTX);
        save=save+ dtw.warpingDistance;

//        2
        dtw.calcDTW(genAMC.rfootTX,basAMC.rfootTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.rfootTY,basAMC.rfootTY);
        save=save+ dtw.warpingDistance;

//        1
        dtw.calcDTW(genAMC.rtoesTX,basAMC.rtoesTX);
        save=save+ dtw.warpingDistance;

//        3
        dtw.calcDTW(genAMC.lfemurTX,basAMC.lfemurTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.lfemurTY,basAMC.lfemurTY);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.lfemurTZ,basAMC.lfemurTZ);
        save=save+ dtw.warpingDistance;

//        1
        dtw.calcDTW(genAMC.ltibiaTX,basAMC.ltibiaTX);
        save=save+ dtw.warpingDistance;

//        2
        dtw.calcDTW(genAMC.lfootTX,basAMC.lfootTX);
        save=save+ dtw.warpingDistance;
        dtw.calcDTW(genAMC.lfootTY,basAMC.lfootTY);
        save=save+ dtw.warpingDistance;

//        1
        dtw.calcDTW(genAMC.rtoesTX,basAMC.rtoesTX);
        save=save+ dtw.warpingDistance;


        System.out.println("Akumulasi "+save);
    }
}


