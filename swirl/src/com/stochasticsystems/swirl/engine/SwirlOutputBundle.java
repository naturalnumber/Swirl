package com.stochasticsystems.swirl.engine;

import java.util.Arrays;

/**
 * Created by Allan Stewart on 2017-11-02.
 */
public class SwirlOutputBundle { // Parcelable (via wrapper? inheritance?), Serializable, thread safe
    public static final String TAG = "SwirlOutputBundle";
    protected final SwirlParameterBundle mParameters;

    protected volatile int     mCompleteRuns    = 0;
    //protected volatile int     mCompletePeriods = 0;
    protected volatile boolean mCompleted       = false;
    protected final int I, T, S, A;

    protected           long[][][][] mPopulationData; //  volatile? i, t, s, a
    protected transient long[][][]   mRData; //  volatile? i, t, s
    protected transient long[][][]   mPopulationSums; //  volatile? t, s, a
    protected transient long[][][]   mPopulationSquares; //  volatile? t, s, a
    protected transient double[][][] mPopulationMeans; //  transient? volatile? t, s, a
    protected transient double[][][] mPopulationSTDs; //  transient? volatile? t, s, a
    protected transient long[][]     mGSummarySums; //  volatile? t, s
    protected transient long[][]     mGSummarySquares; //  volatile? t, s
    protected transient double[][]   mGSummaryMeans; //  transient? volatile? t, s
    protected transient double[][]   mGSummarySTDs; //  transient? volatile? t, s
    protected transient long[][]     mRSummarySums; //  volatile? t, s
    protected transient long[][]     mRSummarySquares; //  volatile? t, s
    protected transient double[][]   mRSummaryMeans; //  transient? volatile? t, s
    protected transient double[][]   mRSummarySTDs; //  transient? volatile? t, s
    protected transient long[]       mSummarySums; //  volatile? t
    protected transient long[]       mSummarySquares; //  volatile? t
    protected transient double[]     mSummaryMeans; //  transient? volatile? t
    protected transient double[]     mSummarySTDs; //  transient? volatile? t
    protected transient int          mNExtinct;

    protected volatile boolean mIsFinal = false;

    public SwirlOutputBundle(SwirlParameterBundle parameters) {
        if (parameters == null)
            throw new NullPointerException("Invalid Parameters"); // Assumes validity
        mParameters = parameters;
        I = mParameters.getNRuns();
        T = mParameters.getNPeriods()+1;
        S = mParameters.isGendered() ? 2 : 1;
        A = mParameters.getMaxAge()+1;

        //  Init population stuff

        mCompleteRuns = 0;
        mIsFinal = false;

        initialize();
    }

    private void initialize() {
        //if (mParameters == null) throw new IllegalStateException("Invalid Parameters");

        mPopulationData = new long[I][][][];//[t][s][a]; // i, t, s, a
        mRData = new long[I][T][S]; // i, t, s
        mPopulationSums = new long[T][S][A]; //  volatile? t, s, a
        mPopulationSquares = new long[T][S][A]; //  volatile? t, s, a
        mPopulationMeans = new double[T][S][A]; //  transient? volatile? t, s, a
        mPopulationSTDs = new double[T][S][A]; //  transient? volatile? t, s, a
        mGSummarySums = new long[T][S]; //  volatile? t, s
        mGSummarySquares = new long[T][S]; //  volatile? t, s
        mGSummaryMeans = new double[T][S]; //  transient? volatile? t, s
        mGSummarySTDs = new double[T][S]; //  transient? volatile? t, s
        mRSummarySums = new long[T][S]; //  volatile? t, s
        mRSummarySquares = new long[T][S]; //  volatile? t, s
        mRSummaryMeans = new double[T][S]; //  transient? volatile? t, s
        mRSummarySTDs = new double[T][S]; //  transient? volatile? t, s
        mSummarySums = new long[T]; //  volatile? t
        mSummarySquares = new long[T]; //  volatile? t
        mSummaryMeans = new double[T]; //  transient? volatile? t
        mSummarySTDs = new double[T]; //  transient? volatile? t
        mNExtinct = 0;
    }

    public synchronized boolean addData(long[][][][] newData) { // Double check synchronization
        if (isBadData(newData) || newData.length > mParameters.getNRuns()-mCompleteRuns) {
            return false;
        }

        for (long[][][] sub : newData) if (isBadData(sub)) return false;

        return addDataFast(newData);
    }

    protected synchronized boolean addDataFast(long[][][][] newData) {
        int newCompleted = mCompleteRuns+newData.length;
        int[] rAges = mParameters.getReproductionAge();
        long temp;
        double mean, weight = 1.0d / newCompleted;

        for (int i = mCompleteRuns, j = 0; i < newCompleted; i++, j++) {
            mPopulationData[i] = newData[j];

            for (int t = 0; t < T; t++) {
                for (int s = 0; s < S; s++) {
                    for (int a = 0; a < A; a++) {
                        temp = newData[j][t][s][a];
                        mPopulationSums[t][s][a] += temp;
                        mPopulationSquares[t][s][a] += temp*temp;
                        mGSummarySums[t][s] += temp;
                        mGSummarySquares[t][s] += temp * temp;
                        mSummarySums[t] += temp;
                        mSummarySquares[t] += temp * temp;
                        if (a >= rAges[s]) {
                            mRSummarySums[t][s] += temp;
                            mRSummarySquares[t][s] += temp * temp;
                            mRData[i][t][s] += temp;
                        }
                    }
                }
            }
        }

        for (int t = 0; t < T; t++) {
            mean = mSummarySums[t] * weight;
            mSummaryMeans[t] = mean;
            mSummarySTDs[t] = Math.sqrt(mSummarySquares[t] * weight + mean * mean);

            for (int s = 0; s < S; s++) {
                mean = mGSummarySums[t][s] * weight;
                mGSummaryMeans[t][s] = mean;
                mGSummarySTDs[t][s] = Math.sqrt(mGSummarySquares[t][s] * weight + mean * mean);

                mean = mRSummarySums[t][s] * weight;
                mRSummaryMeans[t][s] = mean;
                mRSummarySTDs[t][s] = Math.sqrt(mRSummarySquares[t][s] * weight + mean * mean);

                for (int a = 0; a < A; a++) {
                    mean = mPopulationSums[t][s][a] * weight;
                    mPopulationMeans[t][s][a] = mean;
                    mPopulationSTDs[t][s][a] = Math.sqrt(mPopulationSquares[t][s][a] * weight + mean * mean);
                }
            }
        }

        for (int i = mCompleteRuns; i < newCompleted; i++) {
            if (mRData[i][T-1][0] == 0 || (S>1 && mRData[i][T-1][1] == 0)) mNExtinct++;
        }

        mCompleteRuns = newCompleted;

        mIsFinal = mCompleteRuns == I;

        return true;
    }

    public boolean isBadData(long[][][][] data) {
        return data == null || data[0].length != T || data[0][0].length != S || data[0][0][0].length != A;
    }

    private boolean isBadData(long[][][] data) {
        return data == null || data.length != T || data[0].length != S || data[0][0].length != A;
    }

    public SwirlParameterBundle getParameters() {
        return mParameters;
    }

    public int getCompleteRuns() {
        return mCompleteRuns;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public long[][][][] getPopulationData() {
        return mPopulationData;
    }

    public double[][][] getPopulationMeans() {
        return mPopulationMeans;
    }

    public double[][][] getPopulationSTDs() {
        return mPopulationSTDs;
    }

    public double[][] getGenderedSummaryMeans() {
        return mGSummaryMeans;
    }

    public double[][] getGenderedSummarySTDs() {
        return mGSummarySTDs;
    }

    public double[][] getReproductiveSummaryMeans() {
        return mRSummaryMeans;
    }

    public double[][] getReproductiveSummarySTDs() {
        return mRSummarySTDs;
    }

    public double[] getSummaryMeans() {
        return mSummaryMeans;
    }

    public double[] getSummarySTDs() {
        return mSummarySTDs;
    }

    public int getNumberExtinction() {
        return mNExtinct;
    }

    public double getExtinctionPercent() {
        return mNExtinct / (double) mCompleteRuns;
    }

    public boolean isFinal() {
        return mIsFinal;
    }

    public static char[][] toCharArray(int per, double[] output) {
        int xPad = 2;
        int yPad = 2;

        int dx = per;
        int xs = (output.length-1) / dx;

        //System.err.println("xs: "+xs+" dx: "+dx);

        double max = 0L;

        for ( double d : output ) if (d > max) max = d;

        int ys = 10;
        double dy = max/10;

        //System.err.println("ys: "+ys+" dy: "+dy);

        char[][] plot = new char[ys + yPad][xs + xPad];

        for (char[] chars : plot) {
            Arrays.fill(chars, ' ');
        }


        int xAxisy = plot.length - 1 - yPad;
        int yAxisx = xPad;

        for (int x = xPad; x < plot[0].length; x++) {
            plot[xAxisy][x] = '━';
        }

        for (int y = 0; y < xAxisy; y++) {
            plot[y][yAxisx] = '┃';
        }

        plot[xAxisy][yAxisx] = '┗';

        plot[xAxisy+1][yAxisx] = '0';
        plot[xAxisy][0] = '0';


        int hl = toHL(output[0], dy);

        plot[toY(output[0], dy, ys, yPad, plot.length)][yAxisx] = (hl == 0) ? '┝' : (hl > 0) ? '┞' : '┟' ;

        for (int x = xPad+1; x < plot[0].length; x++) {
            int y = toY(output[(x-xPad)*dx], dy, ys, yPad, plot.length);
            hl = toHL(output[(x-xPad)*dx], dy);
            plot[y][x] = (y == xAxisy) ? (hl == 0) ? '┷' : (hl > 0) ? '┻' : '╍' :
                                         (hl == 0) ? '-' : (hl > 0) ? '‾' : '_' ;
        }

        return plot;
    }

    private static int toY(double val, double dy, int ys, int yPad, int yLength) {
        return yLength - (int) (Math.min(Math.round((val+dy/2)/dy), ys) + yPad);
    }

    private static int toHL(double val, double dy) {
        int temp = (int) Math.round(((val % dy) + dy/6)/(dy/3))-2;
        if (temp > 1) temp = 1;
        if (temp < -1) temp = -1;
        return temp;
    }

    public static String toPlot(int per, double[] output) {
        char[][] plot = toCharArray(per, output);

        StringBuilder sb = new StringBuilder(plot.length*plot[0].length);

        sb.append(plot[0]);

        for (int y = 1; y < plot.length; y++) {
            sb.append('\n').append(plot[y]);
        }

        return sb.toString();
    }
}
