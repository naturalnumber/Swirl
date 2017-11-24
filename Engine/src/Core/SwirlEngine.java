package Core;

import java.security.InvalidParameterException;
import java.util.Random;

/**
 * Created by natur on 10/8/2017.
 */

public class SwirlEngine {
    /*private enum SwirlStep {
        SETUP, RUN_INITIALIZATION, PERIOD_INITIALIZATION, REPRODUCTION, MORTALITY, HARVEST,
        SUPPLEMENT, ENFORCE_CAPACITY, CENSUS, PERIOD_END, RUN_END, SUMMERIZE, OUTPUT, END, DONE
    }//*/

    // TODO: Thread safety

    private final SwirlParameterBundle mParameters;
    //private final SwirlOutputBundle    mOutput;

    //  Local final constants
    private final int I;
    private final int T;
    private final int S;
    private final int A;

    //  Local use stuff
    private Random mGenerator;

    //  Status
    //private          SwirlStep mCurrentStep;
    // TODO: volatile?
    private int       mI; // Current iteration step number
    //private int       mT; // Current time step number
    private boolean   mCompleted = false;
    private boolean   mInitialized = false;

    //  Data
    protected           long[][][][] mPopulationData; //  volatile? i, t, s, a

    public SwirlEngine(SwirlParameterBundle parameters) {
        mParameters = parameters;
        I = mParameters.getNRuns();
        T = mParameters.getNPeriods();
        S = mParameters.isGendered() ? 2 : 1;
        A = mParameters.getMaxAge()+1;

        //mOutput = new SwirlOutputBundle(mParameters); // TODO:
        mI = 0;
        //mT = 0;
        mCompleted = false;
    }

    /*public SwirlEngine(SwirlOutputBundle resumeFrom) {
        mParameters = resumeFrom.mParameters;
        I = mParameters.mNRuns;
        T = mParameters.mNPeriods;

        //mOutput = resumeFrom;
        mI = resumeFrom.mCompleteRuns;
        mCompleted = resumeFrom.mCompleted;

        //initializeFromResume(); // TODO:
    }//*/

    //  Double check compliance with threading used in android
    //public SwirlOutputBundle getOutputBundle() {
    //    return mOutput;
    //}

    //  Run methods

    /**
     * Runs any initialization required after transferred to a new thread.
     *
     * @return success
     */
    public boolean initialize() {
        if (mInitialized) {
            //  This shouldn't happen, maybe throw an error?
            return false;
        }

        //  These should be handled already in the constructor, but better safe than sorry...
        mI = 0;
        //mT = 0;
        mCompleted = false;

        mPopulationData = new long[I][][][];//[t][s][a]; // i, t, a, s

        mGenerator = new Random(System.currentTimeMillis());

        return mInitialized = true;
    }

    /**
     * Iterates up to n iterations, after moved to separate thread.
     *
     * @return success
     */
    public boolean iterate(int n) {
        if (mInitialized && !mCompleted) {
            //  Constants
            double norm = 1.0d / Math.sqrt(2.0d*Math.PI);

            //  I think it is faster to have local variables...
            int endI = mI+n;
            int T = this.T;
            int S = this.S;
            int A = this.A;
            boolean gendered = mParameters.isGendered();
            long[][] initialPop = mParameters.getInitialPopulation();
            int[] rAges = mParameters.getReproductionAge();
            int rAgeF = rAges[0];
            int rAgeM = (gendered) ? rAges[1] : -1;
            double sr = (gendered) ? mParameters.getSexRatio() : 1.0d;
            double sr2 = 1.0 - sr;
            boolean correlateRM = mParameters.getRMCorrelation() != 0.0d;
            boolean equateRM = mParameters.getRMCorrelation() == 1.0d;
            double rhoRM = mParameters.getRMCorrelation();
            double rhoRM2 = Math.sqrt(1.0d-rhoRM*rhoRM);
            int evR = 0, evM = 1, evK = 2, evs = 3; // Indices
            double[] ev = new double[evs];
            double[] litters = mParameters.getLitterProbability();
            int L = litters.length; // mParameters.getMaxLitterSize()+1;
            //double rSD = mParameters.mReproductionPercentSD;
            double rVP = litters[0]*(1-litters[0]);
            double[][] mortality = mParameters.getMortality();
            double[][] mortalitySD = mParameters.getSDMortality();
            int harvest = mParameters.getHarvestRate();
            int supplement = mParameters.getSupplementRate();
            double[][] delta = new double[S][A];
            long k = mParameters.getCarryingCapacity();
            double kSD = mParameters.getSDCarryingCapacity();


            //  Counters
            long nF = 0, nM = 0, nRF = 0, nRM = 0;
            long nFStart = 0, nMStart = 0, nRFStart = 0, nRMStart = 0;

            for (int a = 0; a < rAgeF; a++) {
                nFStart += initialPop[0][a];
            }
            for (int a = rAgeF; a < A; a++) {
                nFStart += initialPop[0][a];
                nRFStart += initialPop[0][a];
            }
            nF = nFStart;
            nRF = nRFStart;

            if (gendered) {
                for (int a = 0; a < rAgeM; a++) {
                    nMStart += initialPop[1][a];
                }
                for (int a = rAgeM; a < A; a++) {
                    nMStart += initialPop[1][a];
                    nRMStart += initialPop[1][a];
                }
                nM = nMStart;
                nRM = nRMStart;
            }

            long[][] tempPop;//, tempPopOld;
            long[] females, femalesOld, males = null, malesOld = null;
            long tempL;
            double tempD, tempF, tempM;

            //  Init delta, skips 0 by default // TODO: Verify this
            tempF = tempM = 0.0d;
            tempL = supplement - harvest;
            if (gendered) {
                for (int a = 1; a < A; a++) {
                    tempF += mortality[0][a];
                    delta[0][a] = tempF;
                    tempM += mortality[1][a];
                    delta[1][a] = tempM;
                }
                tempF = tempL*sr2/tempF;
                tempM = tempL*sr/tempM;
                for (int a = 1; a < A; a++) { // Normalize
                    delta[0][a] *= tempF;
                    delta[1][a] *= tempM;
                }
            } else {
                for (int a = 1; a < A; a++) {
                    tempF += mortality[0][a];
                    delta[0][a] = tempF;
                }
                tempF = tempL/tempF;
                for (int a = 1; a < A; a++) { // Normalize
                    delta[0][a] *= tempF;
                }
            }

            for (int i = mI; i < endI; i++) {

                //  Iteration init
                //mT = 0;
                mPopulationData[i] = new long[T][][];
                nF = nFStart;
                nRF = nRFStart;
                if (gendered) {
                    nM = nMStart;
                    nRM = nRMStart;
                }
                //  Should we add some randomization?

                //  Initial time period
                mPopulationData[i][0] = initialPop.clone();
                //tempPopOld = mPopulationData[i][0];
                femalesOld = mPopulationData[i][0][0];//tempPopOld[0];
                if (gendered) malesOld = mPopulationData[i][0][1];//tempPopOld[1];

                for (int t = 1; t < T; t++) { // Transition to time t
                    //  Time init

                    //  Set EVs
                    if (correlateRM) { // If correlated
                        if (equateRM) { // If perfectly correlated
                            for (int j = evM; j < evs; j++) ev[j] = mGenerator.nextGaussian();
                            ev[evR] = ev[evM];
                        } else {
                            for (int j = 0; j < evs; j++) ev[j] = mGenerator.nextGaussian();
                            ev[evR] = ev[evM] * rhoRM + ev[evR] * rhoRM2; // Add correlation
                        }
                    } else {
                        for (int j = 0; j < evs; j++) ev[j] = mGenerator.nextGaussian();
                    }

                    tempPop = new long[S][A];
                    females = tempPop[0];
                    females[0] = 0;

                    //  Reproduction
                    if (gendered) {
                        males = tempPop[1];
                        males[0] = 0;
                        if (nRF > 0 && nRM > 0) { // viability check
                            tempF = tempM = 0.0d;
                            for (int l = 1; l < L; l++) {
                                tempF += l * litters[l];
                            }
                            //  Multiply by reproductive population & ev effect
                            //(1.0d + Math.exp(ev[evR] * ev[evR] / -2.0d) *Math.signum(ev[evR]));
                            tempF *= nRF * (1.0d + ev[evR] * Math.sqrt(rVP / nRF));
                            tempF = Math.max(0.0d, tempF); // Check and remove negatives
                            tempM = tempF; // Prepare for sex ratio
                            tempM *= sr; // Sex ratio applied
                            tempF *= sr2; // TODO: variation in sex ratio?

                            //  Assign the pops
                            females[0] = Math.round(tempF);
                            males[0] = Math.round(tempM);
                        }
                    } else {
                        if (nRF > 0) { // viability check
                            tempF = 0.0d;
                            for (int l = 1; l < L; l++) {
                                tempF += l * litters[l];
                            }
                            //  Multiply by reproductive population & ev effect
                            tempF *= nRF * (1.0d + ev[evR] * Math.sqrt(rVP / nRF));
                            tempF = Math.max(0.0d, tempF); // Check and remove negatives

                            //  Assign the pops
                            females[0] = Math.round(tempF);
                        }
                    }
                    //  Now temp has the age 0 set pre mortality

                    //  Aging, Mortality, Harvest & Supplement
                    if (gendered) {
                        for (int a = A-2; a >= 0; a--) {
                            females[a+1] = Math.max(0, Math.round(femalesOld[a] * (mortality[0][a] + mortalitySD[0][a] * ev[evM]) + delta[0][a]));
                            males[a+1] = Math.max(0, Math.round(malesOld[a] * (mortality[1][a] + mortalitySD[1][a] * ev[evM]) + delta[1][a]));
                        }
                    } else {
                        for (int a = A-2; a >= 0; a--) {
                            females[a+1] = Math.max(0, Math.round(femalesOld[a] * (mortality[0][a] + mortalitySD[0][a] * ev[evM]) + delta[0][a]));
                        }
                    }

                    // Census
                    nF = nRF = 0;
                    for (int a = 0; a < rAgeF; a++) {
                        nF += females[a];
                    }
                    for (int a = rAgeF; a < A; a++) {
                        nF += females[a];
                        nRF += females[a];
                    }

                    if (gendered) {
                        nM = nRM = 0;
                        for (int a = 0; a < rAgeM; a++) {
                            nM += males[a];
                        }
                        for (int a = rAgeM; a < A; a++) {
                            nM += males[a];
                            nRM += males[a];
                        }
                    }

                    // Carrying Capacity
                    tempD = Math.max(0.0d, k+kSD*ev[evK]);
                    tempL = Math.round(tempD);
                    if (gendered) {
                        if (nF + nM > tempL) {
                            tempD = (nF + nM) / tempD;
                            for (int a = 0; a < A; a++) {
                                females[a] = Math.round(females[a] * tempD);
                                males[a] = Math.round(males[a] * tempD);
                            }

                            //  Redo census
                            nF = nRF = nM = nRM = 0;
                            for (int a = 0; a < rAgeF; a++) {
                                nF += females[a];
                            }
                            for (int a = rAgeF; a < A; a++) {
                                nF += females[a];
                                nRF += females[a];
                            }
                            for (int a = 0; a < rAgeM; a++) {
                                nM += males[a];
                            }
                            for (int a = rAgeM; a < A; a++) {
                                nM += males[a];
                                nRM += males[a];
                            }
                        }
                    } else {
                        if (nF > tempL) {
                            tempD = nF / tempD;
                        }
                        for (int a = 0; a < A; a++) {
                            females[a] = Math.round(females[a] * tempD);
                        }

                        //  Redo census
                        nF = nRF = 0;
                        for (int a = 0; a < rAgeF; a++) {
                            nF += females[a];
                        }
                        for (int a = rAgeF; a < A; a++) {
                            nF += females[a];
                            nRF += females[a];
                        }
                    }

                    //  Record data
                    mPopulationData[i][t] = tempPop;

                    //  Prepare for next step
                    //tempPopOld = tempPop; // May not need?
                    femalesOld = females;
                    malesOld = males;
                } // for t
                //  The run is now complete

                this.mI++;
            } // for i
            // Iterations now complete

            if(this.mI == I) this.mCompleted = true;

            return true;

        } else {
            //  This shouldn't happen
            return false;
        }
    }

    public long[][][][] getData(int first) {
        if (!mInitialized) {
            throw new IllegalStateException("Not initialized");
        }
        if (first < 0 || first >= mI) {
            throw new InvalidParameterException("Invalid entry: "+first);
        }

        int l = mI-first;
        long[][][][] output = new long[l][T][S][A];
        System.arraycopy(mPopulationData, first, output, 0, l);
        return output;
    }

    public long[][][][] getData(int first, int last) {
        if (!mInitialized) {
            throw new IllegalStateException("Not initialized");
        }
        if (first < 0 || first >= mI) {
            throw new InvalidParameterException("Invalid entry: "+first);
        }
        if (last <= first || last >= mI) {
            throw new InvalidParameterException("Invalid entry: "+last);
        }

        int l = last-first;
        long[][][][] output = new long[l][T][S][A];
        System.arraycopy(mPopulationData, first, output, 0, l);
        return output;
    }

    /**
     *
     *
     * @return success
     */
    public boolean complete() {
        return true;
    }






    /**
     * Iterations left.
     *
     * @return number of iterations to go.
     */
    public int iterationsLeft() {
        return I - mI;
    }

    /**
     * Iterations left.
     *
     * @return number of iterations to go.
     */
    public int iterationsCompleted() {
        return mI;
    }

    /**
     * Is initialized
     *
     * @return initialization state
     */
    public boolean isInitialized() {
        return mInitialized;
    }

    /**
     * Is finished
     *
     * @return completion state
     */
    public boolean isComplete() {
        return mCompleted;
    }


    public final static double TOLLERANCE = 1e-10; // Tolerance for probability validation

    //  Max / mins, possibly temporary, largely for efficiency reasons
    public final static int    N_RUNS_MAX               = 100000;
    public final static int    N_PERIODS_MAX            = 10000;
    public final static int    MAX_AGE_MAX              = 200;
    public final static int    MAX_LITTER_SIZE_MAX      = 100;
    public final static double RM_CORRELATION_MIN       = 0d;
    public final static long   INITIAL_POPULATION_MAX   = 10000000000L;
    public final static long   CARRYING_CAPACITY_MAX    = 1000000000000L;
    public final static double SD_CARRYING_CAPACITY_MAX = 10000000000d;
    public final static int    HARVEST_RATE_MAX         = 1000000000;
    public final static int    SUPPLEMENT_RATE_MAX      = 1000000000;

    //  Defaults
    public final static int        N_RUNS_DEFAULT               = 100;
    public final static int        N_PERIODS_DEFAULT          = 100;
    public final static int        REPORTING_INTERVAL_DEFAULT = 10;
    public final static int        MAX_AGE_DEFAULT            = 16;
    public final static int[]      REPRODUCTION_AGE_DEFAULT   = {4, 4};
    public final static boolean    IS_GENDERED_DEFAULT        = true;
    public final static int        MAX_LITTER_SIZE_DEFAULT    = 4;
    public final static double[]   LITTER_PROBABILITY_DEFAULT = {.5d, .1d, .2d, .15d, .05d};
    //public final static double     SD_REPRODUCTION_P_DEFAULT  = .1d;
    public final static double     SEX_RATIO_DEFAULT          = .5d;
    public final static double     RM_CORRELATION_DEFAULT     = .8d;
    public final static double[][] MORTALITY_DEFAULT          = {{.5d, .25d, .2d, .15d, .1d,
                                                                    .1d, .1d, .1d, .1d, .1d,
                                                                    .1d, .1d, .1d, .1d, .1d,
                                                                    .1d, 1.0d},
                                                                   {.5d, .25d, .2d, .15d, .1d,
                                                                    .1d, .1d, .1d, .1d, .1d,
                                                                    .1d, .1d, .1d, .1d, .1d,
                                                                    .1d, 1.0d}};
    public final static double[][] SD_MORTALITY_DEFAULT         = {{.01d, .01d, .01d, .01d, .01d,
                                                                    .01d, .01d, .01d, .01d, .01d,
                                                                    .01d, .01d, .01d, .01d, .01d,
                                                                    .01d, 1.0d},
                                                                   {.01d, .01d, .01d, .01d, .01d,
                                                                    .01d, .01d, .01d, .01d, .01d,
                                                                    .01d, .01d, .01d, .01d, .01d,
                                                                    .01d, 1.0d}};
    public final static long[][]   INITIAL_POPULATION_DEFAULT   = {{100L, 100L, 100L, 100L, 100L,
                                                                    100L, 100L, 100L, 100L, 100L,
                                                                    100L, 100L, 100L, 100L, 100L,
                                                                    100L, 100L},
                                                                   {100L, 100L, 100L, 100L, 100L,
                                                                    100L, 100L, 100L, 100L, 100L,
                                                                    100L, 100L, 100L, 100L, 100L,
                                                                    100L, 100L}};
    public final static long       CARRYING_CAPACITY_DEFAULT    = 1000L;
    public final static double     SD_CARRYING_CAPACITY_DEFAULT = 100.0d;
    public final static int        HARVEST_RATE_DEFAULT         = 0;
    public final static int        SUPPLEMENT_RATE_DEFAULT      = 0;

}
