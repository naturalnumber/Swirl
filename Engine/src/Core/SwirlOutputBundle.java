package Core;

/**
 * Created by androiddev on 2017-11-02.
 */
public class SwirlOutputBundle { // Parcelable (via wrapper? inheritance?), Serializable, thread safe
    protected final SwirlParameterBundle mParameters;

    protected volatile int     mCompleteRuns    = 0;
    //protected volatile int     mCompletePeriods = 0;
    protected volatile boolean mCompleted       = false;
    protected final int I, T, S, A;

    protected           long[][][][] mPopulationData; //  volatile? i, t, s, a
    protected transient long[][][]   mPopulationSums; //  volatile? t, s, a
    protected transient long[][][]   mPopulationSquares; //  volatile? t, s, a
    protected transient double[][][] mPopulationMeans; //  transient? volatile? t, s, a
    protected transient double[][][] mPopulationSTDs; //  transient? volatile? t, s, a
    protected transient long[][]     mGSummarySums; //  volatile? t, s
    protected transient long[][]     mGSummarySquares; //  volatile? t, s
    protected transient double[][]   mGSummaryMeans; //  transient? volatile? t, s
    protected transient double[][]   mGSummarySTDs; //  transient? volatile? t, s
    protected transient long[]       mSummarySums; //  volatile? t
    protected transient long[]       mSummarySquares; //  volatile? t
    protected transient double[]     mSummaryMeans; //  transient? volatile? t
    protected transient double[]     mSummarySTDs; //  transient? volatile? t

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
        mPopulationSums = new long[T][S][A]; //  volatile? t, s, a
        mPopulationSquares = new long[T][S][A]; //  volatile? t, s, a
        mPopulationMeans = new double[T][S][A]; //  transient? volatile? t, s, a
        mPopulationSTDs = new double[T][S][A]; //  transient? volatile? t, s, a
        mGSummarySums = new long[T][S]; //  volatile? t, s
        mGSummarySquares = new long[T][S]; //  volatile? t, s
        mGSummaryMeans = new double[T][S]; //  transient? volatile? t, s
        mGSummarySTDs = new double[T][S]; //  transient? volatile? t, s
        mSummarySums = new long[T]; //  volatile? t
        mSummarySquares = new long[T]; //  volatile? t
        mSummaryMeans = new double[T]; //  transient? volatile? t
        mSummarySTDs = new double[T]; //  transient? volatile? t
    }

    public synchronized boolean addData(long[][][][] newData) { // Double check synchronization
        if (isBadData(newData) || newData.length > mParameters.getNRuns()-mCompleteRuns) {
            return false;
        }

        int newCompleted = mCompleteRuns+newData.length;
        long temp;
        double mean, weight = 1.0d / newCompleted;

        for (int i = mCompleteRuns, j = 0; i < newCompleted; i++, j++) {
            if (isBadData(newData))
                return false;

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
                    }
                }
            }
        }

        for (int t = 0; t < T; t++) {
            for (int s = 0; s < S; s++) {
                for (int a = 0; a < A; a++) {
                    mean = mPopulationSums[t][s][a] * weight;
                    mPopulationMeans[t][s][a] = mean;
                    mPopulationSTDs[t][s][a] = Math.sqrt(mPopulationSquares[t][s][a] * weight + mean * mean);
                    mean = mGSummarySums[t][s] * weight;
                    mGSummaryMeans[t][s] = mean;
                    mGSummarySTDs[t][s] = Math.sqrt(mGSummarySquares[t][s] * weight + mean * mean);
                    mean = mSummarySums[t] * weight;
                    mSummaryMeans[t] = mean;
                    mSummarySTDs[t] = Math.sqrt(mSummarySquares[t] * weight + mean * mean);
                }
            }
        }

        mCompleteRuns = newCompleted;

        return true;
    }

    public synchronized boolean addDataFast(long[][][][] newData) {
        int newCompleted = mCompleteRuns+newData.length;

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
                    }
                }
            }
        }

        for (int t = 0; t < T; t++) {
            for (int s = 0; s < S; s++) {
                for (int a = 0; a < A; a++) {
                    mean = mPopulationSums[t][s][a] * weight;
                    mPopulationMeans[t][s][a] = mean;
                    mPopulationSTDs[t][s][a] = Math.sqrt(mPopulationSquares[t][s][a] * weight + mean * mean);
                    mean = mGSummarySums[t][s] * weight;
                    mGSummaryMeans[t][s] = mean;
                    mGSummarySTDs[t][s] = Math.sqrt(mGSummarySquares[t][s] * weight + mean * mean);
                }
            }
        }

        mCompleteRuns = newCompleted;

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

    public double[] getSummaryMeans() {
        return mSummaryMeans;
    }

    public double[] getSummarySTDs() {
        return mSummarySTDs;
    }

    public boolean isFinal() {
        return mIsFinal;
    }
}
