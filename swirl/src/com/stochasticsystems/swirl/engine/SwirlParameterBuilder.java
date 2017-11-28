package com.stochasticsystems.swirl.engine;

//import org.jetbrains.annotations.Contract;

import java.io.PrintStream;
import java.util.Arrays;

/**
 * 
 * Created by Allan Stewart on 2017-11-02.
 */
public class SwirlParameterBuilder {
    public static final String TAG = "SwirlParameterBuilder";
    private SwirlParameterBundle mLastReturnedParameters = null;
    private boolean              mLastValid              = false;
    private boolean              mIsValid                = false; // Thread safe?

    //  Run parameters
    private int        mNRuns;
    private int        mNPeriods;
    private int        mReportingInterval;
    private int        mMaxAge;
    private int[]      mReproductionAge;
    private boolean    mIsGendered;
    private int        mMaxLitterSize;
    private double[]   mLitterProbability;
    //private double     mReproductionPercentSD;
    private double     mSexRatio; // males to females
    private double     mRMCorrelation;
    private double[][] mMortality;
    private double[][] mSDMortality;
    private long[][]   mInitialPopulation;
    private long       mCarryingCapacity;
    private double     mSDCarryingCapacity;
    private int        mHarvestRate;
    private int        mSupplementRate;

    public SwirlParameterBuilder() {
        mLastReturnedParameters = null;
        mLastValid = false;
        mIsValid = false;
        mNRuns = SwirlEngine.N_RUNS_DEFAULT;
        mNPeriods = SwirlEngine.N_PERIODS_DEFAULT;
        mReportingInterval = SwirlEngine.REPORTING_INTERVAL_DEFAULT;
        mMaxAge = SwirlEngine.MAX_AGE_DEFAULT;
        mReproductionAge = null;
        mIsGendered = true;
        mMaxLitterSize = SwirlEngine.MAX_LITTER_SIZE_DEFAULT;
        mLitterProbability = null;
        //mReproductionPercentSD = SwirlEngine.SD_REPRODUCTION_P_DEFAULT;
        mSexRatio = SwirlEngine.SEX_RATIO_DEFAULT;
        mRMCorrelation = SwirlEngine.RM_CORRELATION_DEFAULT;
        mMortality = null;
        mSDMortality = null;
        mInitialPopulation = null;
        mCarryingCapacity = SwirlEngine.CARRYING_CAPACITY_DEFAULT;
        mSDCarryingCapacity = SwirlEngine.SD_CARRYING_CAPACITY_DEFAULT;
        mHarvestRate = SwirlEngine.HARVEST_RATE_DEFAULT;
        mSupplementRate = SwirlEngine.SUPPLEMENT_RATE_DEFAULT;
    }

    public SwirlParameterBuilder(SwirlParameterBundle initialParameters) {
        mLastReturnedParameters = initialParameters;
        mLastValid = true; // unsure
        mIsValid = false; // Maybe could be true?
        mNRuns = initialParameters.mNRuns;
        mNPeriods = initialParameters.mNPeriods;
        mReportingInterval = initialParameters.mReportingInterval;
        mMaxAge = initialParameters.mMaxAge;
        mReproductionAge = initialParameters.mReproductionAge;
        mIsGendered = initialParameters.mIsGendered;
        mMaxLitterSize = initialParameters.mMaxLitterSize;
        mLitterProbability = initialParameters.mLitterProbability;
        //mReproductionPercentSD = initialParameters.mReproductionPercentSD;
        mSexRatio = initialParameters.mSexRatio;
        mRMCorrelation = initialParameters.mRMCorrelation;
        mMortality = initialParameters.mMortality;
        mSDMortality = initialParameters.mSDMortality;
        mInitialPopulation = initialParameters.mInitialPopulation;
        mCarryingCapacity = initialParameters.mCarryingCapacity;
        mSDCarryingCapacity = initialParameters.mSDCarryingCapacity;
        mHarvestRate = initialParameters.mHarvestRate;
        mSupplementRate = initialParameters.mSupplementRate;
        validate();
    }

    public boolean set(SwirlParameterBundle parameters) {
        mLastReturnedParameters = parameters;
        mLastValid = true; // unsure
        mIsValid = false; // Maybe could be true?
        mNRuns = parameters.mNRuns;
        mNPeriods = parameters.mNPeriods;
        mReportingInterval = parameters.mReportingInterval;
        mMaxAge = parameters.mMaxAge;
        mReproductionAge = parameters.mReproductionAge;
        mIsGendered = parameters.mIsGendered;
        mMaxLitterSize = parameters.mMaxLitterSize;
        mLitterProbability = parameters.mLitterProbability;
        //mReproductionPercentSD = initialParameters.mReproductionPercentSD;
        mSexRatio = parameters.mSexRatio;
        mRMCorrelation = parameters.mRMCorrelation;
        mMortality = parameters.mMortality;
        mSDMortality = parameters.mSDMortality;
        mInitialPopulation = parameters.mInitialPopulation;
        mCarryingCapacity = parameters.mCarryingCapacity;
        mSDCarryingCapacity = parameters.mSDCarryingCapacity;
        mHarvestRate = parameters.mHarvestRate;
        mSupplementRate = parameters.mSupplementRate;
        return validate();
    }

    public boolean setDefaults() {
        return set(SwirlParameterBundle.getDefault());
    }


    public void clear() {
        mLastReturnedParameters = null;
        mLastValid = false;
        mIsValid = false;
        mNRuns = SwirlEngine.N_RUNS_DEFAULT;
        mNPeriods = SwirlEngine.N_PERIODS_DEFAULT;
        mReportingInterval = SwirlEngine.REPORTING_INTERVAL_DEFAULT;
        mMaxAge = SwirlEngine.MAX_AGE_DEFAULT;
        mReproductionAge = null;
        mIsGendered = true;
        mMaxLitterSize = SwirlEngine.MAX_LITTER_SIZE_DEFAULT;
        mLitterProbability = null;
        //mReproductionPercentSD = SwirlEngine.SD_REPRODUCTION_P_DEFAULT;
        mSexRatio = SwirlEngine.SEX_RATIO_DEFAULT;
        mRMCorrelation = SwirlEngine.RM_CORRELATION_DEFAULT;
        mMortality = null;
        mSDMortality = null;
        mInitialPopulation = null;
        mCarryingCapacity = SwirlEngine.CARRYING_CAPACITY_DEFAULT;
        mSDCarryingCapacity = SwirlEngine.SD_CARRYING_CAPACITY_DEFAULT;
        mHarvestRate = SwirlEngine.HARVEST_RATE_DEFAULT;
        mSupplementRate = SwirlEngine.SUPPLEMENT_RATE_DEFAULT;
    }

    public SwirlParameterBundle build() {
        validate();
        if (!mIsValid) return null;
        if (mLastValid) return mLastReturnedParameters;
        mLastReturnedParameters =
                new SwirlParameterBundle(mNRuns, mNPeriods, mReportingInterval, mMaxAge,
                                         mReproductionAge, mIsGendered, mMaxLitterSize,
                                         mLitterProbability, /*mReproductionPercentSD,*/ mSexRatio,
                                         mRMCorrelation,
                                         mMortality, mSDMortality, mInitialPopulation,
                                         mCarryingCapacity, mSDCarryingCapacity,
                                         mHarvestRate, mSupplementRate, mIsValid);
        mLastValid = true;
        return mLastReturnedParameters;
    }

    private boolean validate() {
        mIsValid = isValidNRuns(mNRuns) && isValidNPeriods(mNPeriods)
                   && isValidReportingIntervalF(mReportingInterval, mNPeriods)
                   && isValidMaxAge(mMaxAge)
                   && isValidReproductionAgeF(mReproductionAge, mIsGendered, mMaxAge)
                   && isValidMaxLitterSize(mMaxLitterSize)
                   && isValidLitterProbabilityF(mLitterProbability, mMaxLitterSize)
                   //&& isValidReproductionPercentSD(mReproductionPercentSD)
                   && isValidSexRatio(mSexRatio, mIsGendered)
                   && isValidRMCorrelation(mRMCorrelation)
                   && isValidMortalityF(mMortality, mIsGendered, mMaxAge)
                   && isValidSDMortalityF(mSDMortality, mIsGendered, mMaxAge)
                   && isValidInitialPopulationF(mInitialPopulation, mIsGendered, mMaxAge)
                   && isValidCarryingCapacity(mCarryingCapacity)
                   && isValidSDCarryingCapacity(mSDCarryingCapacity)
                   && isValidHarvestRate(mHarvestRate)
                   && isValidSupplementRate(mSupplementRate);

        return mIsValid;
    }

    public boolean isValid() {
        return mIsValid || validate();
    }

    public long validationCode() {
        long vCode = (isValidSupplementRate(mSupplementRate) ? 0 : 1); //
        vCode = vCode << 1 | (isValidHarvestRate(mHarvestRate) ? 0 : 1); //
        vCode = vCode << 1 | (isValidSDCarryingCapacity(mSDCarryingCapacity) ? 0 : 1); //
        vCode = vCode << 1 | (isValidCarryingCapacity(mCarryingCapacity)? 0 : 1);
        vCode = vCode << 1 | (isValidInitialPopulationF(mInitialPopulation, mIsGendered, mMaxAge) ? 0 : 1);
        vCode = vCode << 1 | (isValidSDMortalityF(mSDMortality, mIsGendered, mMaxAge) ? 0 : 1);
        vCode = vCode << 1 | (isValidMortalityF(mMortality, mIsGendered, mMaxAge) ? 0 : 1);
        vCode = vCode << 1 | (isValidRMCorrelation(mRMCorrelation) ? 0 : 1);
        vCode = vCode << 1 | (isValidSexRatio(mSexRatio, mIsGendered) ? 0 : 1);
        vCode = vCode << 1 | (isValidLitterProbabilityF(mLitterProbability, mMaxLitterSize) ? 0 : 1);
        vCode = vCode << 1 | (isValidMaxLitterSize(mMaxLitterSize) ? 0 : 1);
        vCode = vCode << 1 | (isValidReproductionAgeF(mReproductionAge, mIsGendered, mMaxAge) ? 0 : 1);
        vCode = vCode << 1 | (isValidMaxAge(mMaxAge) ? 0 : 1);
        vCode = vCode << 1 | (isValidReportingIntervalF(mReportingInterval, mNPeriods) ? 0 : 1); // 4
        vCode = vCode << 1 | (isValidNPeriods(mNPeriods) ? 0 : 1); // 2
        vCode = vCode << 1 | (isValidNRuns(mNRuns) ? 0 : 1); // 1

        return vCode;
    }

    /**
     * @return number of failed tests.
     */
    public static int test(PrintStream err) { // Likely should be private
        int failed = 0;

        try {
            SwirlParameterBuilder b = new SwirlParameterBuilder();

            //  NRuns
            if (b.setNRuns(-1)) {
                failed++;
                err.println("Invalid NRuns set...");
            }
            if (!b.setNRuns(SwirlEngine.N_RUNS_DEFAULT)) {
                failed++;
                err.println("Default NRuns invalid?");
            }

            //  NPeriods
            if (b.setNPeriods(-1)) {
                failed++;
                err.println("Invalid NPeriods set...");
            }
            if (!b.setNPeriods(SwirlEngine.N_PERIODS_DEFAULT)) {
                failed++;
                err.println("Default NPeriods invalid?");
            }

            //  ReportingInterval
            if (b.setReportingInterval(-1)) {
                failed++;
                err.println("Invalid ReportingInterval set...");
            }
            if (!b.setReportingInterval(SwirlEngine.REPORTING_INTERVAL_DEFAULT)) {
                failed++;
                err.println("Default ReportingInterval invalid?");
            }

            //  MaxAge
            if (b.setMaxAge(-1)) {
                failed++;
                err.println("Invalid MaxAge set...");
            }
            if (!b.setMaxAge(SwirlEngine.MAX_AGE_DEFAULT)) {
                failed++;
                err.println("Default MaxAge invalid?");
            }

            //  ReproductionAge
            if (b.setReproductionAge(new int[]{-1})) {
                failed++;
                err.println("Invalid ReproductionAge set...");
            }
            if (!b.setReproductionAge(SwirlEngine.REPRODUCTION_AGE_DEFAULT)) {
                failed++;
                err.println("Default ReproductionAge invalid?");
            }

            //  IsGendered
            b.setGendered(SwirlEngine.IS_GENDERED_DEFAULT);
            if (b.isGendered() != SwirlEngine.IS_GENDERED_DEFAULT) {
                failed++;
                err.println("Default IsGendered not set?");
            }

            //  MaxLitterSize
            if (b.setMaxLitterSize(-1)) {
                failed++;
                err.println("Invalid MaxLitterSize set...");
            }
            if (!b.setMaxLitterSize(SwirlEngine.MAX_LITTER_SIZE_DEFAULT)) {
                failed++;
                err.println("Default MaxLitterSize invalid?");
            }

            //  LitterProbability
            if (b.setLitterProbability(new double[]{-1d})) {
                failed++;
                err.println("Invalid LitterProbability set...");
            }
            if (!b.setLitterProbability(SwirlEngine.LITTER_PROBABILITY_DEFAULT)) {
                failed++;
                err.println("Default LitterProbability invalid?");
            }

            //  ReproductionProbabilitySD
            /*if (b.setReproductionPercentSD(-1d)) {
                failed++;
                err.println("Invalid ReproductionProbabilitySD set...");
            }
            if (!b.setReproductionPercentSD(SwirlEngine.SD_REPRODUCTION_P_DEFAULT)) {
                failed++;
                err.println("Default ReproductionProbabilitySD invalid?");
            }//*/

            //  SexRatio
            if (b.setSexRatio(-1d)) {
                failed++;
                err.println("Invalid SexRatio set...");
            }
            if (!b.setSexRatio(SwirlEngine.SEX_RATIO_DEFAULT)) {
                failed++;
                err.println("Default SexRatio invalid?");
            }

            //  RMCorrelation
            if (b.setRMCorrelation(-1d)) {
                failed++;
                err.println("Invalid RMCorrelation set...");
            }
            if (!b.setRMCorrelation(SwirlEngine.RM_CORRELATION_DEFAULT)) {
                failed++;
                err.println("Default RMCorrelation invalid?");
            }

            //  Mortality
            if (b.setMortality(new double[][]{{-1d}})) {
                failed++;
                err.println("Invalid Mortality set...");
            }
            if (!b.setMortality(SwirlEngine.MORTALITY_DEFAULT)) {
                failed++;
                err.println("Default Mortality invalid?");
            }

            //  SDMortality
            if (b.setSDMortality(new double[][]{{-1d}})) {
                failed++;
                err.println("Invalid SDMortality set...");
            }
            if (!b.setSDMortality(SwirlEngine.SD_MORTALITY_DEFAULT)) {
                failed++;
                err.println("Default SDMortality invalid?");
            }

            //  InitialPopulation
            if (b.setInitialPopulation(new long[][]{{-1L}})) {
                failed++;
                err.println("Invalid InitialPopulation set...");
            }
            if (!b.setInitialPopulation(SwirlEngine.INITIAL_POPULATION_DEFAULT)) {
                failed++;
                err.println("Default InitialPopulation invalid?");
            }

            //  CarryingCapacity
            if (b.setCarryingCapacity(-1L)) {
                failed++;
                err.println("Invalid CarryingCapacity set...");
            }
            if (!b.setCarryingCapacity(SwirlEngine.CARRYING_CAPACITY_DEFAULT)) {
                failed++;
                err.println("Default CarryingCapacity invalid?");
            }

            //  SDCarryingCapacity
            if (b.setSDCarryingCapacity(-1L)) {
                failed++;
                err.println("Invalid SDCarryingCapacity set...");
            }
            if (!b.setSDCarryingCapacity(SwirlEngine.SD_CARRYING_CAPACITY_DEFAULT)) {
                failed++;
                err.println("Default SDCarryingCapacity invalid?");
            }

            //  HarvestRate
            if (b.setHarvestRate(-1)) {
                failed++;
                err.println("Invalid HarvestRate set...");
            }
            if (!b.setHarvestRate(SwirlEngine.HARVEST_RATE_DEFAULT)) {
                failed++;
                err.println("Default HarvestRate invalid?");
            }

            //  SupplementRate
            if (b.setSupplementRate(-1)) {
                failed++;
                err.println("Invalid SupplementRate set...");
            }
            if (!b.setSupplementRate(SwirlEngine.SUPPLEMENT_RATE_DEFAULT)) {
                failed++;
                err.println("Default SupplementRate invalid?");
            }



            //  ReproductionAge //
            if (!b.setReproductionAge(SwirlParameterBuilder.makeReproductionAge(SwirlEngine.IS_GENDERED_DEFAULT, 4))) {
                failed++;
                err.println("Make ReproductionAge 1 invalid?");
            }
            if (!b.setReproductionAge(SwirlParameterBuilder.makeReproductionAge(SwirlEngine.IS_GENDERED_DEFAULT, 2, 4))) {
                failed++;
                err.println("Make ReproductionAge 2 invalid?");
            }

            //  LitterProbability //
            b.setMaxAge(5);
            if (b.setLitterProbability(makeLitterProbability(5, .2,.3,.4,.5,.1,.1))) {
                failed++;
                err.println("Make Bad LitterProbability valid?");
            }
            b.setMaxAge(4);
            if (!b.setLitterProbability(makeLitterProbability(4, .2,.2,.2,.2,.2))) {
                failed++;
                err.println("Make LitterProbability 1 invalid?");
            }
            b.setMaxAge(4);
            if (!b.setLitterProbability(makeLitterProbability(4, .2,.3,.1))) {
                failed++;
                err.println("Make LitterProbability 2 invalid?");
            }

            //  Mortality //
            b.setMaxAge(SwirlEngine.MAX_AGE_DEFAULT);
            if (!b.setMortality(makeMortality(SwirlEngine.IS_GENDERED_DEFAULT, SwirlEngine.MAX_AGE_DEFAULT, .5d, .25d, .2d, .15d, .1d))) {
                failed++;
                err.println("Make Mortality 1 invalid?");
            }
            if (!b.setMortality(makeMortality(SwirlEngine.IS_GENDERED_DEFAULT, SwirlEngine.MAX_AGE_DEFAULT, new double[][] {{.5d, .25d, .2d, .15d, .1d}, {.5d, .15d, .2d, .15d, .1d}}))) {
                failed++;
                err.println("Make Mortality 2 invalid?");
            }

            //  SDMortality //
            if (!b.setSDMortality(makeSDMortality(1000, SwirlEngine.MORTALITY_DEFAULT))) {
                failed++;
                err.println("Make SDMortality invalid?");
            }

            //  InitialPopulation //
            if (!b.setInitialPopulation(
                    makeInitialPopulation(SwirlEngine.IS_GENDERED_DEFAULT,
                                          SwirlEngine.MAX_AGE_DEFAULT,
                                          SwirlEngine.REPRODUCTION_AGE_DEFAULT,
                                          SwirlEngine.SEX_RATIO_DEFAULT, 10000,
                                          SwirlEngine.LITTER_PROBABILITY_DEFAULT,
                                          SwirlEngine.MORTALITY_DEFAULT))) {
                failed++;
                err.println("Make InitialPopulation invalid?");
            }


            b.setDefaults();



            //  ReproductionAge // //
            if (!b.extrapolateReproductionAge(4)) {
                failed++;
                err.println("Extrapolate ReproductionAge invalid?");
            }

            //  LitterProbability // //
            if (!b.extrapolateLitterProbability(.1,.2,.1)) {
                failed++;
                err.println("Extrapolate LitterProbability invalid?");
            }

            //  Mortality // //
            if (!b.extrapolateMortality(.2,.1)) {
                failed++;
                err.println("Extrapolate Mortality 1 invalid?");
            }
            if (!b.extrapolateMortality(new double[][] {{.5d, .25d, .2d, .15d, .1d}, {.5d, .15d, .2d, .15d, .1d}})) {
                failed++;
                err.println("Extrapolate Mortality 2 invalid?");
            }

            //  SDMortality // //
            if (!b.extrapolateSDMortality()) {
                failed++;
                err.println("Extrapolate SDMortality invalid?");
            }

            //  InitialPopulation // //
            if (!b.extrapolateInitialPopulation(1000000)) {
                failed++;
                err.println("Extrapolate InitialPopulation invalid?");
            }


            if (!b.setDefaults() || !b.isValid()) {
                failed++;
                err.println("Defaults invalid? "+(b.validationCode()));
            }

            SwirlParameterBundle defaults = b.build();

            if (defaults == null || !defaults.equals(SwirlParameterBundle.getDefault())) {
                failed++;
                err.println("Default not returned?");
            }

            String defaultAsString = b.toDelimitedString("\n");

            b.clear();

            b.fromDelimitedString(defaultAsString, "\n");

            if (!b.isValid() || !b.equals(SwirlParameterBundle.getDefault())) {
                failed++;
                err.println("Default not read from save?");
            }



        } catch (Exception e) {
            err.println(e);
            e.printStackTrace(err);
            // TODO: Make this consistent
        }

        return failed;
    }

    public static void main(String[] args) {
        // TODO: test stuff
        System.err.println("Failed: "+test(System.err));
    }

    //  Getters, setters, validators, etc.

    public int getNRuns() {
        return mNRuns;
    }

    public boolean setNRuns(int nRuns) {
        if (isViableNRuns(nRuns)) {
            mIsValid = false; // Overkill?
            mNRuns = nRuns;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter.
     *
     * @param nRuns
     *
     * @return if the parameter is valid or not
     */
    //@Contract(pure = true)
    public static boolean isValidNRuns(int nRuns) {
        return nRuns >= SwirlEngine.N_RUNS_MIN && nRuns <= SwirlEngine.N_RUNS_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param nRuns
     *
     * @return if the parameter is possibly viable
     */
    //@Contract(pure = true)
    public static boolean isViableNRuns(int nRuns) {
        return isValidNRuns(nRuns);
    }

    public int getNPeriods() {
        return mNPeriods;
    }

    public boolean setNPeriods(int nPeriods) {
        if (isViableNPeriods(nPeriods)) {
            mIsValid = false; // Overkill?
            mLastValid = false;
            mNPeriods = nPeriods;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter.
     *
     * @param nPeriods
     *
     * @return
     */
    //@Contract(pure = true)
    public static boolean isValidNPeriods(int nPeriods) {
        return nPeriods >= SwirlEngine.N_PERIODS_MIN && nPeriods <= SwirlEngine.N_PERIODS_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param nPeriods
     *
     * @return if the parameter is possibly viable
     */
    //@Contract(pure = true)
    public static boolean isViableNPeriods(int nPeriods) {
        return isValidNPeriods(nPeriods);
    }

    public int getReportingInterval() {
        return mReportingInterval;
    }

    public boolean setReportingInterval(int reportingInterval) {
        //if (isValidReportingIntervalF(reportingInterval, mNPeriods)) {
        if (isViableReportingInterval(reportingInterval)) {
            mIsValid = false;
            mLastValid = false;
            mReportingInterval = reportingInterval;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter, given the secondary parameter.
     *
     * @param reportingInterval
     * @param nPeriods
     *
     * @return
     */
    public static boolean isValidReportingInterval(int reportingInterval, int nPeriods) {
        return isValidNPeriods(nPeriods) &&
               isValidReportingIntervalF(reportingInterval, nPeriods);
    }

    /**
     * Returns if the parameter is valid, assuming the validity of the dependent (secondary)
     * parameter.
     *
     * @param reportingInterval
     * @param nPeriods
     *
     * @return
     */
    //@Contract(pure = true)
    static boolean isValidReportingIntervalF(int reportingInterval, int nPeriods) {
        return reportingInterval > 0 && reportingInterval <= nPeriods &&
               nPeriods % reportingInterval == 0;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param reportingInterval
     *
     * @return if the parameter is possibly viable
     */
    //@Contract(pure = true)
    public static boolean isViableReportingInterval(int reportingInterval) {
        return reportingInterval >= SwirlEngine.N_PERIODS_MIN && reportingInterval <= SwirlEngine.N_PERIODS_MAX;
    }

    public int getMaxAge() {
        return mMaxAge;
    }

    public boolean setMaxAge(int maxAge) {
        if (isViableMaxAge(maxAge)) {
            mIsValid = false;
            mLastValid = false;
            mMaxAge = maxAge;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter.
     *
     * @param maxAge
     *
     * @return
     */
    //@Contract(pure = true)
    public static boolean isValidMaxAge(int maxAge) {
        return maxAge >= SwirlEngine.MAX_AGE_MIN && maxAge <= SwirlEngine.MAX_AGE_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param maxAge
     *
     * @return if the parameter is possibly viable
     */
    //@Contract(pure = true)
    public static boolean isViableMaxAge(int maxAge) {
        return isValidMaxAge(maxAge);
    }

    public int[] getReproductionAge() {
        return mReproductionAge.clone();
    }

    public boolean setReproductionAge(int[] reproductionAge) {
        //if (isValidReproductionAgeF(reproductionAge, mIsGendered, mMaxAge)) {
        if (isViableReproductionAge(reproductionAge)) {
            mIsValid = false; // Overkill?
            mLastValid = false;
            mReproductionAge = reproductionAge;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter, given the secondary parameters.
     *
     * @param reproductionAge
     * @param gendered
     * @param maxAge
     *
     * @return
     */
    public static boolean isValidReproductionAge(int[] reproductionAge, boolean gendered,
                                                 int maxAge) {
        return isValidMaxAge(maxAge) && validGArray(reproductionAge, gendered) &&
               isValidReproductionAgeF(reproductionAge, gendered, maxAge);
    }

    /**
     * Returns if the parameter is valid, assuming the validity of all parameters.
     *
     * @param reproductionAge
     * @param gendered
     * @param maxAge
     *
     * @return
     */
    //@Contract(pure = true)
    static boolean isValidReproductionAgeF(int[] reproductionAge, boolean gendered,
                                           int maxAge) {
        return viableGDim(reproductionAge) && isValidReproductionAgeEntry(reproductionAge[0], maxAge) &&
               (!gendered || isValidReproductionAgeEntry(reproductionAge[1], maxAge));
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param reproductionAge
     *
     * @return if the parameter is possibly viable
     */
    public static boolean isViableReproductionAge(int[] reproductionAge) {
        return viableGDim(reproductionAge) &&
               isValidReproductionAgeF(reproductionAge, toGenders(reproductionAge),
                                       SwirlEngine.MAX_AGE_MAX);
    }

    /**
     * Returns the validity of the given parameter, given the secondary parameter.
     *
     * @param reproductionAge
     * @param maxAge
     *
     * @return
     */
    //@Contract(pure = true)
    public static boolean isValidReproductionAgeEntry(int reproductionAge, int maxAge) {
        return reproductionAge >= 0 && reproductionAge < maxAge;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param reproductionAge
     *
     * @return if the parameter is possibly viable
     */
    //@Contract(pure = true)
    public static boolean isViableReproductionAgeEntry(int reproductionAge) {
        return isValidReproductionAgeEntry(reproductionAge, SwirlEngine.MAX_AGE_MAX);
    }

    public boolean isGendered() {
        return mIsGendered;
    }

    public void setGendered(boolean gendered) {
        mIsValid = false;
        mLastValid = false;
        mIsGendered = gendered;
    }

    public int getMaxLitterSize() {
        return mMaxLitterSize;
    }

    public boolean setMaxLitterSize(int maxLitterSize) {
        if (isViableMaxLitterSize(maxLitterSize)) {
            mIsValid = false;
            mLastValid = false;
            mMaxLitterSize = maxLitterSize;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter.
     *
     * @param maxLitterSize
     *
     * @return
     */
    //@Contract(pure = true)
    public static boolean isValidMaxLitterSize(int maxLitterSize) {
        return maxLitterSize >= SwirlEngine.MAX_LITTER_SIZE_MIN && maxLitterSize <= SwirlEngine.MAX_LITTER_SIZE_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param maxLitterSize
     *
     * @return if the parameter is possibly viable
     */
    //@Contract(pure = true)
    public static boolean isViableMaxLitterSize(int maxLitterSize) {
        return isValidMaxLitterSize(maxLitterSize);
    }

    public double[] getLitterProbability() {
        return mLitterProbability.clone();
    }

    public boolean setLitterProbability(double[] litterProbability) {
        //if (isValidLitterProbabilityF(litterProbability, mMaxLitterSize)) {
        if (isViableLitterProbability(litterProbability)) {
            mIsValid = false; // Overkill?
            mLastValid = false;
            mLitterProbability = litterProbability;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter, given the secondary parameter.
     *
     * @param litterProbability
     * @param maxLitterSize
     *
     * @return
     */
    public static boolean isValidLitterProbability(double[] litterProbability,
                                                   int maxLitterSize) {
        return isValidMaxLitterSize(maxLitterSize) &&
               validLDim(litterProbability, maxLitterSize + 1) &&
               isValidLitterProbabilityF(litterProbability, maxLitterSize);
    }

    /**
     * Returns if the parameter is valid, assuming the validity of the dependent (secondary)
     * parameter.
     *
     * @param litterProbability
     * @param maxLitterSize
     *
     * @return
     */
    static boolean isValidLitterProbabilityF(double[] litterProbability, int maxLitterSize) {
        double d = 0.0d, p;
        for (int i = maxLitterSize; i > 0; i--) {
            p = litterProbability[i];
            if (!validProb(p)) return false;
            d += p;
        }
        return realProb(d) && Math.abs(litterProbability[0] + d - 1.0d) < SwirlEngine.TOLLERANCE;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param litterProbability
     *
     * @return if the parameter is possibly viable
     */
    public static boolean isViableLitterProbability(double[] litterProbability) {
        return viableLDim(litterProbability, SwirlEngine.MAX_LITTER_SIZE_MAX + 1) &&
               isValidLitterProbabilityF(litterProbability, litterProbability.length - 1);
    }

    /*public double getReproductionPercentSD() {
        return mReproductionPercentSD;
    }

    public boolean setReproductionPercentSD(double reproductionPercentSD) {
        if (isViableReproductionPercentSD(reproductionPercentSD)) {
            mIsValid = false; // Overkill?
            mLastValid = false;
            mReproductionPercentSD = reproductionPercentSD;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter.
     *
     * @param reproductionPercentSD
     *
     * @return
     *
    public static boolean isValidReproductionPercentSD(double reproductionPercentSD) {
        return validProb(reproductionPercentSD);
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param reproductionPercentSD
     *
     * @return if the parameter is possibly viable
     *
    //@Contract(pure = true)
    public static boolean isViableReproductionPercentSD(double reproductionPercentSD) {
        return validProb(reproductionPercentSD);
    }//*/

    public double getSexRatio() {
        return mSexRatio;
    }

    public boolean setSexRatio(double sexRatio) {
        if (isViableSexRatio(sexRatio)) {
            mIsValid = false; // Overkill?
            mLastValid = false;
            mSexRatio = sexRatio;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter, given the secondary parameter.
     *
     * @param sexRatio
     * @param gendered
     *
     * @return
     */
    public static boolean isValidSexRatio(double sexRatio, boolean gendered) {
        return (!gendered && validProb(sexRatio) || gendered && realProb(sexRatio));
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param sexRatio
     *
     * @return if the parameter is possibly viable
     */
    //@Contract(pure = true)
    public static boolean isViableSexRatio(double sexRatio) {
        return validProb(sexRatio);
    }

    public double getRMCorrelation() {
        return mRMCorrelation;
    }

    public boolean setRMCorrelation(double rmCorrelation) {
        if (isViableRMCorrelation(rmCorrelation)) {
            mIsValid = false; // Overkill?
            mLastValid = false;
            mRMCorrelation = rmCorrelation;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter.
     *
     * @param rmCorrelation
     *
     * @return
     */
    //@Contract(pure = true)
    public static boolean isValidRMCorrelation(double rmCorrelation) {
        return rmCorrelation >= SwirlEngine.RM_CORRELATION_MIN && rmCorrelation <= SwirlEngine.RM_CORRELATION_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param rmCorrelation
     *
     * @return if the parameter is possibly viable
     */
    //@Contract(pure = true)
    public static boolean isViableRMCorrelation(double rmCorrelation) {
        return isValidRMCorrelation(rmCorrelation);
    }

    public double[][] getMortality() {
        return mMortality.clone();
    }

    public boolean setMortality(double[][] mortality) {
        //if (isValidMortalityF(mortality, mIsGendered, mMaxAge)) {
        if (isViableMortality(mortality)) {
            mIsValid = false; // Overkill?
            mLastValid = false;
            mMortality = mortality;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter, given the secondary parameters.
     *
     * @param mortality
     * @param gendered
     * @param maxAge
     *
     * @return
     */
    public static boolean isValidMortality(double[][] mortality, boolean gendered, int maxAge) {
        return isValidMaxAge(maxAge) && validGArray(mortality, gendered) &&
               isValidMortalityF(mortality, gendered, maxAge);
    }

    /**
     * Returns if the parameter is valid, assuming the validity of the dependent (secondary)
     * parameters.
     *
     * @param mortality
     * @param gendered
     * @param maxAge
     *
     * @return
     */
    //@Contract(pure = true)
    static boolean isValidMortalityF(double[][] mortality, boolean gendered, int maxAge) {
        int size = maxAge + 1;
        boolean isValid =
                mortality[0].length == size && (!gendered || mortality[1].length == size);
        for (int i = 0; isValid && i < maxAge; i++) {
            isValid = validProb(mortality[0][i]) && (!gendered || validProb(mortality[1][i]));
        }
        isValid &= mortality[0][maxAge] == 1.0d && (!gendered || mortality[1][maxAge] == 1.0d);
        return isValid;
    }


    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param mortality
     *
     * @return if the parameter is possibly viable
     */
    public static boolean isViableMortality(double[][] mortality) {
        return viableGDim(mortality) &&
               isValidMortalityF(mortality, toGenders(mortality), mortality[0].length - 1);
    }

    public double[][] getSDMortality() {
        return mSDMortality.clone();
    }

    public boolean setSDMortality(double[][] sdMortality) {
        //if (isValidSDMortalityF(sdMortality, mIsGendered, mMaxAge)) {
        if (isViableSDMortality(sdMortality)) {
            mIsValid = false; // Overkill?
            mLastValid = false;
            mSDMortality = sdMortality;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter, given the secondary parameters.
     *
     * @param sdMortality
     * @param gendered
     * @param maxAge
     *
     * @return
     */
    public static boolean isValidSDMortality(double[][] sdMortality, boolean gendered,
                                             int maxAge) {
        return isValidMaxAge(maxAge) && validGArray(sdMortality, gendered) &&
               isValidSDMortalityF(sdMortality, gendered, maxAge);
    }

    /**
     * Returns if the parameter is valid, assuming the validity of the dependent (secondary)
     * parameters.
     *
     * @param sdMortality
     * @param gendered
     * @param maxAge
     *
     * @return
     */
    static boolean isValidSDMortalityF(double[][] sdMortality, boolean gendered, int maxAge) {
        // TODO: Is this valid? should there be a stronger check?
        int size = maxAge + 1;
        boolean isValid = sdMortality[0].length == size && (!gendered || sdMortality[1].length == size);
        for (int i = 0; isValid && i < maxAge; i++) {
            isValid =
                    validProb(sdMortality[0][i]) && (!gendered || validProb(sdMortality[1][i]));
        }
        isValid &=
                sdMortality[0][maxAge] == 1.0d && (!gendered || sdMortality[1][maxAge] == 1.0d);
        return isValid;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param sdMortality
     *
     * @return if the parameter is possibly viable
     */
    public static boolean isViableSDMortality(double[][] sdMortality) {
        return viableGDim(sdMortality) &&
               isValidSDMortalityF(sdMortality, toGenders(sdMortality),
                                   sdMortality[0].length - 1);
    }

    public long[][] getInitialPopulation() {
        return mInitialPopulation.clone();
    }

    public boolean setInitialPopulation(long[][] initialPopulation) {
        //if (isValidInitialPopulationF(initialPopulation, mIsGendered, mMaxAge)) {
        if (isViableInitialPopulation(initialPopulation)) {
            mIsValid = false; // Overkill?
            mLastValid = false;
            mInitialPopulation = initialPopulation;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter, given the secondary parameters.
     *
     * @param initialPopulation
     * @param gendered
     * @param maxAge
     *
     * @return
     */
    public static boolean isValidInitialPopulation(long[][] initialPopulation, boolean gendered,
                                                   int maxAge) {
        return isValidMaxAge(maxAge) && validGArray(initialPopulation, gendered) &&
               isValidInitialPopulationF(initialPopulation, gendered, maxAge);
    }

    /**
     * Returns if the parameter is valid, assuming the validity of the dependent (secondary)
     * parameters.
     *
     * @param initialPopulation
     * @param gendered
     * @param maxAge
     *
     * @return
     */
    //@Contract(pure = true)
    static boolean isValidInitialPopulationF(long[][] initialPopulation, boolean gendered,
                                             int maxAge) {
        int size = maxAge + 1;
        boolean isValid = initialPopulation[0].length == size &&
                          (!gendered || initialPopulation[1].length == size);
        long l = 0l;
        for (int i = 0; isValid && i < size; i++) {
            isValid = isValidInitialPopulationEntry(l += initialPopulation[0][i]) &&
                      (!gendered ||
                       isValidInitialPopulationEntry(l += initialPopulation[1][i]));
        }
        return isValid && isValidInitialPopulationEntry(l);
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param initialPopulation
     *
     * @return
     */
    public static boolean isViableInitialPopulation(long[][] initialPopulation) {
        return viableGDim(initialPopulation) &&
               isValidInitialPopulationF(initialPopulation, toGenders(initialPopulation.length),
                                         initialPopulation[0].length - 1);
    }

    /**
     * Returns the validity of the given parameter.
     *
     * @param initialPopulation
     *
     * @return
     */
    //@Contract(pure = true)
    public static boolean isValidInitialPopulationEntry(long initialPopulation) {
        return initialPopulation >= SwirlEngine.INITIAL_POPULATION_MIN && initialPopulation <= SwirlEngine.INITIAL_POPULATION_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param initialPopulation
     *
     * @return
     */
    //@Contract(pure = true)
    public static boolean isViableInitialPopulationEntry(long initialPopulation) {
        return isValidInitialPopulationEntry(initialPopulation);
    }

    public long getCarryingCapacity() {
        return mCarryingCapacity;
    }

    public boolean setCarryingCapacity(long carryingCapacity) {
        if (isViableCarryingCapacity(carryingCapacity)) {
            mIsValid = false; // Overkill?
            mLastValid = false;
            mCarryingCapacity = carryingCapacity;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter.
     *
     * @param carryingCapacity
     *
     * @return
     */
    //@Contract(pure = true)
    public static boolean isValidCarryingCapacity(long carryingCapacity) {
        return carryingCapacity >= SwirlEngine.CARRYING_CAPACITY_MIN && carryingCapacity <= SwirlEngine.CARRYING_CAPACITY_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param carryingCapacity
     *
     * @return
     */
    //@Contract(pure = true)
    public static boolean isViableCarryingCapacity(long carryingCapacity) {
        return isValidCarryingCapacity(carryingCapacity);
    }

    public double getSDCarryingCapacity() {
        return mSDCarryingCapacity;
    }

    public boolean setSDCarryingCapacity(double sdCarryingCapacity) {
        if (isViableSDCarryingCapacity(sdCarryingCapacity)) {
            mIsValid = false; // Overkill?
            mLastValid = false;
            mSDCarryingCapacity = sdCarryingCapacity;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter.
     *
     * @param sdCarryingCapacity
     *
     * @return
     */
    //@Contract(pure = true)
    public static boolean isValidSDCarryingCapacity(double sdCarryingCapacity) {
        return sdCarryingCapacity >= SwirlEngine.SD_CARRYING_CAPACITY_MIN && sdCarryingCapacity <= SwirlEngine.SD_CARRYING_CAPACITY_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param sdCarryingCapacity
     *
     * @return
     */
    //@Contract(pure = true)
    public static boolean isViableSDCarryingCapacity(double sdCarryingCapacity) {
        return isValidSDCarryingCapacity(sdCarryingCapacity);
    }

    public int getHarvestRate() {
        return mHarvestRate;
    }

    public boolean setHarvestRate(int harvestRate) {
        if (isViableHarvestRate(harvestRate)) {
            mIsValid = false; // Overkill?
            mLastValid = false;
            mHarvestRate = harvestRate;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter.
     *
     * @param harvestRate
     *
     * @return
     */
    //@Contract(pure = true)
    public static boolean isValidHarvestRate(int harvestRate) {
        return harvestRate >= SwirlEngine.HARVEST_RATE_MIN && harvestRate <= SwirlEngine.HARVEST_RATE_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param harvestRate
     *
     * @return
     */
    //@Contract(pure = true)
    public static boolean isViableHarvestRate(int harvestRate) {
        return isValidHarvestRate(harvestRate);
    }

    public int getSupplementRate() {
        return mSupplementRate;
    }

    public boolean setSupplementRate(int supplementRate) {
        if (isViableSupplementRate(supplementRate)) {
            mIsValid = false; // Overkill?
            mLastValid = false;
            mSupplementRate = supplementRate;
            return true;
        }
        return false;
    }

    /**
     * Returns the validity of the given parameter.
     *
     * @param supplementRate
     *
     * @return
     */
    //@Contract(pure = true)
    public static boolean isValidSupplementRate(int supplementRate) {
        return supplementRate >= SwirlEngine.SUPPLEMENT_RATE_MIN && supplementRate <= SwirlEngine.SUPPLEMENT_RATE_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param supplementRate
     *
     * @return
     */
    //@Contract(pure = true)
    public static boolean isViableSupplementRate(int supplementRate) {
        return isValidSupplementRate(supplementRate);
    }

    //  Fill helpers

    /**
     * Generates a valid Reproduction Age array filled with the integer arguments, repeating the first one if there are insufficient.
     *
     * If all genders have the same age, only one int argument is necesssary.
     *
     * @param gendered
     * @param ages
     *
     * @return
     */
    public static int[] makeReproductionAge(boolean gendered, int... ages) {
        int g = genderedDim(gendered);

        for (int a : ages) if (!isViableReproductionAgeEntry(a)) return null;

        if (g <= ages.length) return Arrays.copyOf(ages, g);
        if (gendered) return new int[] {ages[0], ages[0]};
        return new int[] {ages[0]};
    }

    /**
     * Generates a Litter Probability array consistent with the given Max Litter Size, either filled with the provided arguments, or using the provided arguments for the non-zero litter sizes, repeating the last entry if necessary, and calculating the no litter size to match.
     *
     * To generate a flat probability spectrum, only one probability needs to be provided.
     *
     * @param maxLitterSize
     * @param probabilities
     *
     * @return
     */
    public static double[] makeLitterProbability(int maxLitterSize, double... probabilities) {
        if (isViableMaxLitterSize(maxLitterSize)) {

            double d = 0.0d;

            for (double p : probabilities) if (!validProb(p)) return null; else d += p;


            if (maxLitterSize < probabilities.length) {
                return Arrays.copyOf(probabilities, maxLitterSize+1);
            } else if (maxLitterSize == probabilities.length) {
                double[] litterProbabilities = new double[maxLitterSize+1];
                System.arraycopy(probabilities, 0, litterProbabilities, 1, maxLitterSize);
                litterProbabilities[0] = 1.0d - d;
                return (validProb(litterProbabilities[0])) ? litterProbabilities : null;
            } else {
                double r = probabilities[probabilities.length-1];
                double[] litterProbabilities = new double[maxLitterSize+1];
                System.arraycopy(probabilities, 0, litterProbabilities, 1, probabilities.length);
                for (int i = litterProbabilities.length+1; i <= maxLitterSize; i++) {
                    litterProbabilities[i] = r;
                    d+= r;
                }
                litterProbabilities[0] = 1.0d - d;
                return (validProb(litterProbabilities[0])) ? litterProbabilities : null;
            }
        } else {
            return null;
        }
    }

    /**
     * If there are sufficient parameters it will do the first possible of the following:
     * fill the whole array with the provided mortalities
     * fill the whole array, not counting the Max Age entry, with the provided mortalities
     * do one of the above, making the genders identical
     * fill the array with the given mortalities, repeating the last entry, making the genders identical
     *
     * Thus for identical genders, provide the first few entries, up to what gets repeated for all other adults.
     *
     * @param gendered
     * @param maxAge
     * @param mortalities
     *
     * @return
     */
    public static double[][] makeMortality(boolean gendered, int maxAge, double... mortalities) {
        int g = genderedDim(gendered);
        int size = maxAge+1;

        for (double m : mortalities) if (!validProb(m)) return null;

        double[][] temp;

        if (size <= mortalities.length) {
            if (gendered) {
                if (size * 2 <= mortalities.length) {
                    // Check?
                    temp = new double[][]{Arrays.copyOf(mortalities, size),
                                          Arrays.copyOfRange(mortalities, size, size * 2 - 1)};
                } else if (maxAge * 2 <= mortalities.length) {
                    temp = new double[g][size];
                    System.arraycopy(mortalities, 0, temp[0], 0, maxAge);
                    if (gendered) {
                        System.arraycopy(mortalities, maxAge, temp[1], 0, maxAge);
                    }
                } else {
                    temp = new double[][]{Arrays.copyOf(mortalities, size), Arrays.copyOf(mortalities, size)};
                }
            } else {
                temp = new double[][]{Arrays.copyOf(mortalities, size)};
            }
        } else if (maxAge == mortalities.length) {
            temp = new double[g][size];
            System.arraycopy(mortalities, 0, temp[0], 0, maxAge);
            if (gendered) {
                System.arraycopy(mortalities, 0, temp[1], 0, maxAge);
            }
        } else {
            double r = mortalities[mortalities.length-1];
            temp = new double[g][size];
            System.arraycopy(mortalities, 0, temp[0], 0, mortalities.length);
            if (gendered) {
                System.arraycopy(mortalities, 0, temp[1], 0, mortalities.length);
            }
            for (int i = mortalities.length; i < size; i++) {
                temp[0][i] = r;
                if (gendered) temp[1][i] = r;
            }
        }
        temp[0][maxAge] = 1.0d;
        if (gendered) temp[1][maxAge] = 1.0d;
        return temp;
    }

    /**
     * Fills the created mortality array with the given mortalities, repeating the last entry as necessary.
     *
     * @param gendered
     * @param maxAge
     * @param mortalities
     *
     * @return
     */
    public static double[][] makeMortality(boolean gendered, int maxAge, double[][] mortalities) {
        if (!validGArray(mortalities, gendered)) return null;

        int g = genderedDim(gendered);
        int size = maxAge+1;

        for (double[] ms : mortalities) for (double m : ms) if (!validProb(m)) return null;

        double[][] temp;

        if (size <= mortalities[0].length && (!gendered || size <= mortalities[1].length)) {
            if (gendered) {
                temp = new double[][]{Arrays.copyOf(mortalities[0], size),
                                      Arrays.copyOf(mortalities[1], size)};
            } else {
                temp = new double[][]{Arrays.copyOf(mortalities[0], size)};
            }
        } else {
            double r = mortalities[0][mortalities[0].length-1];
            temp = new double[g][size];
            System.arraycopy(mortalities[0], 0, temp[0], 0, mortalities[0].length);
            for (int i = mortalities[0].length; i < size; i++) temp[0][i] = r;
            if (gendered) {
                r = mortalities[1][mortalities[1].length-1];
                System.arraycopy(mortalities[1], 0, temp[1], 0, mortalities[1].length);
                for (int i = mortalities[1].length; i < size; i++) temp[1][i] = r;
            }
        }

        temp[0][maxAge] = 1.0d;
        if (gendered) temp[1][maxAge] = 1.0d;
        return temp;
    }

    /**
     * If there are sufficient parameters it will do the first possible of the following:
     * fill the whole array with the provided mortality sds
     * fill the whole array, not counting the Max Age entry, with the provided mortality sds
     * do one of the above, making the genders identical
     * fill the array with the given mortality sds, repeating the last entry, making the genders identical
     *
     * Thus for identical genders, provide the first few entries, up to what gets repeated for all other adults.
     *
     * @param gendered
     * @param maxAge
     * @param sds
     *
     * @return
     */
    public static double[][] makeSDMortality(boolean gendered, int maxAge, double... sds) {
        int g = genderedDim(gendered);
        int size = maxAge+1;

        for (double sd : sds) if (!validProb(sd)) return null;

        double[][] temp;

        if (size <= sds.length) {
            if (gendered) {
                if (size * 2 <= sds.length) {
                    // Check?
                    temp = new double[][]{Arrays.copyOf(sds, size),
                                          Arrays.copyOfRange(sds, size, size * 2 - 1)};
                } else if (maxAge * 2 <= sds.length) {
                    temp = new double[g][size];
                    System.arraycopy(sds, 0, temp[0], 0, maxAge);
                    if (gendered) {
                        System.arraycopy(sds, maxAge, temp[1], 0, maxAge);
                    }
                } else {
                    temp = new double[][]{Arrays.copyOf(sds, size), Arrays.copyOf(sds, size)};
                }
            } else {
                temp = new double[][]{Arrays.copyOf(sds, size)};
            }
        } else if (maxAge == sds.length) {
            temp = new double[g][size];
            System.arraycopy(sds, 0, temp[0], 0, maxAge);
            if (gendered) {
                System.arraycopy(sds, 0, temp[1], 0, maxAge);
            }
        } else {
            double r = sds[sds.length-1];
            temp = new double[g][size];
            System.arraycopy(sds, 0, temp[0], 0, sds.length);
            if (gendered) {
                System.arraycopy(sds, 0, temp[1], 0, sds.length);
            }
            for (int i = sds.length; i < size; i++) {
                temp[0][i] = r;
                if (gendered) temp[1][i] = r;
            }
        }
        temp[0][maxAge] = 1.0d;
        if (gendered) temp[1][maxAge] = 1.0d;
        return temp;
    }

    /**
     * Fills the created mortality sd array with the given mortality sds, repeating the last entry as necessary.
     *
     * @param gendered
     * @param maxAge
     * @param sds
     *
     * @return
     */
    public static double[][] makeSDMortality(boolean gendered, int maxAge, double[][] sds) {
        if (!validGArray(sds, gendered)) return null;

        int g = genderedDim(gendered);
        int size = maxAge+1;

        for (double[] sub : sds) for (double sd : sub) if (!validProb(sd)) return null;

        double[][] temp;

        if (size <= sds[0].length && (!gendered || size <= sds[1].length)) {
            if (gendered) {
                temp = new double[][]{Arrays.copyOf(sds[0], size),
                                      Arrays.copyOf(sds[1], size)};
            } else {
                temp = new double[][]{Arrays.copyOf(sds[0], size)};
            }
        } else {
            double r = sds[0][sds[0].length-1];
            temp = new double[g][size];
            System.arraycopy(sds[0], 0, temp[0], 0, sds[0].length);
            for (int i = sds[0].length; i < size; i++) temp[0][i] = r;
            if (gendered) {
                r = sds[1][sds[1].length-1];
                System.arraycopy(sds[1], 0, temp[1], 0, sds[1].length);
                for (int i = sds[1].length; i < size; i++) temp[1][i] = r;
            }
        }

        temp[0][maxAge] = 1.0d;
        if (gendered) temp[1][maxAge] = 1.0d;
        return temp;
    }

    /**
     * Creates a mortality sd array from a mortality array using statistics.
     *
     * @param averagePop
     * @param mortalities
     *
     * @return
     */
    public static double[][] makeSDMortality(long averagePop, double[][] mortalities) {
        if (!isViableMortality(mortalities)) return null;
        int g = mortalities.length;
        int size = mortalities[0].length;

        double[][] temp = new double[g][size];

        for (int i = 0; i < g; i++) {
            for (int j = 0; j < size-1; j++) {
                temp[i][j] = Math.sqrt(mortalities[i][j]*(1.0d-mortalities[i][j])/averagePop);
            }
        }

        temp[0][size-1] = 1.0d;
        if (temp.length > 1) temp[1][size-1] = 1.0d;
        return temp;
    }

    /**
     * Uses the provided parameters to generate a stable population distribution.
     *
     * @param gendered
     * @param maxAge
     * @param reproductionAge
     * @param sexRatio
     * @param initialPopulation
     * @param litterProbabilities
     * @param mortality
     *
     * @return
     */
    public static long[][] makeInitialPopulation(boolean gendered, int maxAge, int[] reproductionAge, double sexRatio, long initialPopulation, double[] litterProbabilities, double[][] mortality) {
        if (!(isValidSexRatio(sexRatio, gendered) ||
              isValidReproductionAge(reproductionAge, gendered, maxAge) ||
              isValidMortality(mortality, gendered, maxAge) ||
              isViableLitterProbability(litterProbabilities))) {
            return null;
        }

        int g = genderedDim(gendered);
        int size = maxAge+1;
        double[][] temp = new double[g][size];
        double sr = (gendered) ? sexRatio : 1.0d;
        double sr2 = 1.0 - sr;

        double d = 0.0;
        for (int i = 1; i < litterProbabilities.length; i++) {
            d += i*litterProbabilities[i];
        }

        double tempF = 1.0d, tempM = 1.0d;
        double tempFS = 0.0d, tempMS  = 0.0d;
        double tempFRS = 0.0d;
        long tempL = initialPopulation;
        int a;

        for (a = 1; a < reproductionAge[0]; a++) {
            temp[0][a] = tempF;
            tempFS += tempF;
            tempF *= 1.0d - mortality[0][a];
        }
        for (; a < size; a++) {
            temp[0][a] = tempF;
            tempFS += tempF;
            tempFRS += tempF;
            tempF *= 1.0d - mortality[0][a];
        }
        d *= tempFRS / tempFS; // Normalize by reproductive ratio
        temp[0][0] = d*sr2;

        if (gendered) {
            temp[0][0] = d*sr2;
            for (a = 1; a < size; a++) {
                temp[1][a] = tempM;
                tempMS += tempM;
                tempM *= 1.0d - mortality[1][a];
            }
            temp[1][0] = d*sr;

            tempF = tempL*sr2/(tempFS+temp[0][0]); // Normalize to initialPop
            tempM = tempL*sr/(tempMS+temp[1][0]);
            for (a = 0; a < size; a++) { // Normalize
                temp[0][a] *= tempF;
                temp[1][a] *= tempM;
            }
        } else {
            temp[0][0] = d;
            tempF = tempL/(tempFS+temp[0][0]);
            for (a = 1; a < size; a++) { // Normalize
                temp[0][a] *= tempF;
            }
        }

        long[][] pops = new long[g][size];

        for (int i = 0; i < g; i++) for (a = 0; a < size; a++) pops[i][a] = Math.round(temp[i][a]);

        return pops;
    }

    //  Extrapolation helpers

    /**
     * Generates a valid Reproduction Age array filled with the integer arguments, repeating the first one if there are insufficient.
     * Uses the current stored gender state.
     *
     * If all genders have the same age, only one int argument is necesssary.
     *
     * @param ages
     *
     * @return
     */
    public boolean extrapolateReproductionAge(int... ages) {
        return setReproductionAge(makeReproductionAge(mIsGendered, ages));
    }

    /**
     * Generates a Litter Probability array consistent with the current Max Litter Size, either filled with the provided arguments, or using the provided arguments for the non-zero litter sizes, repeating the last entry if necessary, and calculating the no litter size to match.
     *
     * To generate a flat probability spectrum, only one probability needs to be provided.
     *
     * @param probabilities
     *
     * @return
     */
    public boolean extrapolateLitterProbability(double... probabilities) {
        return setLitterProbability(makeLitterProbability(mMaxLitterSize, probabilities));
    }

    /**
     * If there are sufficient parameters it will do the first possible of the following:
     * fill the whole array with the provided mortalities
     * fill the whole array, not counting the Max Age entry, with the provided mortalities
     * do one of the above, making the genders identical
     * fill the array with the given mortalities, repeating the last entry, making the genders identical
     *
     * Thus for identical genders, provide the first few entries, up to what gets repeated for all other adults.
     *
     * Uses the current gendered state and max age.
     *
     * @param mortalities
     *
     * @return
     */
    public boolean extrapolateMortality(double... mortalities) {
        return setMortality(makeMortality(mIsGendered, mMaxAge, mortalities));
    }

    /**
     * Fills the created mortality array with the given mortalities, repeating the last entry as necessary.
     *
     * @param mortalities
     *
     * @return
     */
    public boolean extrapolateMortality(double[][] mortalities) {
        return setMortality(makeMortality(mIsGendered, mMaxAge, mortalities));
    }

    /**
     * If there are sufficient parameters it will do the first possible of the following:
     * fill the whole array with the provided mortality sds
     * fill the whole array, not counting the Max Age entry, with the provided mortality sds
     * do one of the above, making the genders identical
     * fill the array with the given mortality sds, repeating the last entry, making the genders identical
     *
     * Thus for identical genders, provide the first few entries, up to what gets repeated for all other adults.
     *
     * Uses the current gendered state and max age.
     *
     * @param mortalities
     *
     * @return
     */
    public boolean extrapolateSDMortality(double... mortalities) {
        return setSDMortality(makeSDMortality(mIsGendered, mMaxAge, mortalities));
    }

    /**
     * Fills the created sd mortality array with the given sd mortalities, repeating the last entry as necessary.
     *
     * @param mortalities
     *
     * @return
     */
    public boolean extrapolateSDMortality(double[][] mortalities) {
        return setSDMortality(makeSDMortality(mIsGendered, mMaxAge, mortalities));
    }

    /**
     * Creates a mortality sd array from a mortality array using statistics.
     *
     * @param initialPopulation
     * @param maxAge
     *
     * @return
     */
    public boolean extrapolateSDMortality(long initialPopulation, int maxAge) {
        return setSDMortality(makeSDMortality(initialPopulation/maxAge, mMortality));
    }

    /**
     * Creates a mortality sd array from a mortality array using statistics.
     *
     * @param averagePopulation
     *
     * @return
     */
    public boolean extrapolateSDMortality(long averagePopulation) {
        return setSDMortality(makeSDMortality(averagePopulation, mMortality));
    }

    /**
     * Creates a mortality sd array from a mortality array using statistics.
     *
     * Uses the inital populations
     *
     * @return
     */
    public boolean extrapolateSDMortality() {
        long avg = 0l;
        int n = 0;

        for (long[] ls : mInitialPopulation) for (long l : ls) {
            avg += l;
            n++;
        }

        avg = avg / n;

        return setSDMortality(makeSDMortality(avg, mMortality));
    }

    /**
     * Uses the provided parameters to generate a stable population distribution.
     *
     * Uses:
     *  gendered
     *  max age
     *  reproduction age
     *  sex ratio
     *  litter probabilities
     *  mortality matrix
     *
     * @param initialPopulation
     *
     * @return
     */
    public boolean extrapolateInitialPopulation(long initialPopulation) {
        return setInitialPopulation(makeInitialPopulation(mIsGendered, mMaxAge, mReproductionAge,
                                                          mSexRatio, initialPopulation,
                                                          mLitterProbability, mMortality));
    }

    //  Check helpers

    static int genderedDim(boolean gendered) {
        return ((gendered) ? 2 : 1);
    }

    //@Contract(pure = true)
    static boolean validProb(double p) {
        return p >= 0.0d && p <= 1.0d;
    }

    //@Contract(pure = true)
    static boolean realProb(double p) {
        return p > 0.0d && p < 1.0d;
    }

    //@Contract(value = "null, _ -> false", pure = true)
    static boolean validGArray(int[] array, boolean gendered) {
        return array != null && array.length == genderedDim(gendered);
    }

    //@Contract(value = "null, _ -> false", pure = true)
    static boolean validGArray(int[][] array, boolean gendered) {
        return array != null && array.length == genderedDim(gendered);
    }

    //@Contract(value = "null, _ -> false", pure = true)
    static boolean validGArray(long[] array, boolean gendered) {
        return array != null && array.length == genderedDim(gendered);
    }

    //@Contract(value = "null, _ -> false", pure = true)
    static boolean validGArray(long[][] array, boolean gendered) {
        return array != null && array.length == genderedDim(gendered);
    }

    //@Contract(value = "null, _ -> false", pure = true)
    static boolean validGArray(double[] array, boolean gendered) {
        return array != null && array.length == genderedDim(gendered);
    }

    //@Contract(value = "null, _ -> false", pure = true)
    static boolean validGArray(double[][] array, boolean gendered) {
        return array != null && array.length == genderedDim(gendered);
    }

    //@Contract(pure = true)
    static boolean viableGDim(int firstDimension) {
        return firstDimension == 1 || firstDimension == 2;
    }

    //@Contract(value = "null -> false", pure = true)
    static boolean viableGDim(int[] array) {
        return array != null && (array.length == 1 || array.length == 2);
    }

    //@Contract(value = "null -> false", pure = true)
    static boolean viableGDim(int[][] array) {
        return array != null && (array.length == 1 || array.length == 2);
    }

    //@Contract(value = "null -> false", pure = true)
    static boolean viableGDim(long[] array) {
        return array != null && (array.length == 1 || array.length == 2);
    }

    //@Contract(value = "null -> false", pure = true)
    static boolean viableGDim(long[][] array) {
        return array != null && (array.length == 1 || array.length == 2);
    }

    //@Contract(value = "null -> false", pure = true)
    static boolean viableGDim(double[] array) {
        return array != null && (array.length == 1 || array.length == 2);
    }

    //@Contract(value = "null -> false", pure = true)
    static boolean viableGDim(double[][] array) {
        return array != null && (array.length == 1 || array.length == 2);
    }

    //@Contract(value = "null, _ -> false", pure = true)
    static boolean validLDim(double[] array, int length) {
        return array != null && array.length == length;
    }

    //@Contract(value = "null, _ -> false", pure = true)
    static boolean viableLDim(double[] array, int length) {
        return array != null && array.length <= length;
    }

    //@Contract(pure = true)
    static boolean toGenders(int firstDimension) {
        return firstDimension == 2;
    }

    //@Contract(pure = true)
    static boolean toGenders(int[] array) {
        return array.length == 2;
    }

    //@Contract(pure = true)
    static boolean toGenders(int[][] array) {
        return array.length == 2;
    }

    //@Contract(pure = true)
    static boolean toGenders(long[] array) {
        return array.length == 2;
    }

    //@Contract(pure = true)
    static boolean toGenders(long[][] array) {
        return array.length == 2;
    }

    //@Contract(pure = true)
    static boolean toGenders(double[] array) {
        return array.length == 2;
    }

    //@Contract(pure = true)
    static boolean toGenders(double[][] array) {
        return array.length == 2;
    }




    /**
     * Encodes the parameters as a string, using the provided delimiter.
     * Using = [ ] or , may make the string unreadable by fromDelimitedString.
     *
     * @param delimiter The text that separates the output parameters
     *
     * @return The string
     */
    public String toDelimitedString(String delimiter) {
        StringBuilder output = new StringBuilder();

        output.append("NRuns=").append(mNRuns).append(delimiter)
              .append("NPeriods=").append(mNPeriods).append(delimiter)
              .append("ReportingInterval=").append(mReportingInterval).append(delimiter)
              .append("MaxAge=").append(mMaxAge);

        if (mReproductionAge != null) {
            output.append(delimiter)
                  .append("ReproductionAge=").append(Arrays.toString(mReproductionAge));
        }

        output.append(delimiter)
              .append("IsGendered=").append(mIsGendered).append(delimiter)
              .append("MaxLitterSize=").append(mMaxLitterSize);

        if (mLitterProbability != null) {
            output.append(delimiter)
                  .append("LitterProbability=").append(Arrays.toString(mLitterProbability));
        }

        output.append(delimiter)
              .append("SexRatio=").append(mSexRatio).append(delimiter)
              .append("RMCorrelation=").append(mRMCorrelation);

        if (mMortality != null) {
            output.append(delimiter)
                  .append("Mortality=").append(Arrays.toString(mMortality));
        }

        if (mSDMortality != null) {
            output.append(delimiter)
                  .append("SDMortality=").append(Arrays.toString(mSDMortality));
        }

        if (mInitialPopulation != null) {
            output.append(delimiter)
                  .append("InitialPopulation=").append(Arrays.toString(mInitialPopulation));
        }

        output.append(delimiter)
              .append("CarryingCapacity=").append(mCarryingCapacity).append(delimiter)
              .append("SDCarryingCapacity=").append(mSDCarryingCapacity).append(delimiter)
              .append("HarvestRate=").append(mHarvestRate).append(delimiter)
              .append("SupplementRate=").append(mSupplementRate);

        return output.toString();
    }

    /**
     * Creates a parameter bundle by attempting to read the provided input as if it were produced by
     * toDelimitedString using the provided delimiter. Using = [ ] or , may make the string
     * unreadable by fromDelimitedString.
     *
     * @param input     The string to parse
     * @param delimiter The delimiter used to separate the parameters
     *
     * @return
     */
    public void fromDelimitedString(String input, String delimiter) {
        //if (delimiter.equals("="))

        int        nRuns              = mNRuns;
        int        nPeriods           = mNPeriods;
        int        reportingInterval  = mReportingInterval;
        int        maxAge             = mMaxAge;
        int[]      reproductionAge    = mReproductionAge;
        boolean    isGendered         = mIsGendered;
        int        maxLitterSize      = mMaxLitterSize;
        double[]   litterProbability  = mLitterProbability;
        double     sexRatio           = mSexRatio;
        double     rmCorrelation      = mRMCorrelation;
        double[][] mortality          = mMortality;
        double[][] sdMortality        = mSDMortality;
        long[][]   initialPopulation  = mInitialPopulation;
        long       carryingCapacity   = mCarryingCapacity;
        double     sdCarryingCapacity = mSDCarryingCapacity;
        int        harvestRate        = mHarvestRate;
        int        supplementRate     = mSupplementRate;

        String[] terms = input.split(delimiter);
        String[] split;
        String   lower, after, temp;
        int      delim;

        for (String term : terms)
            try {
                lower = term.toLowerCase();
                delim = term.indexOf('=');

                if (delim > 0) {
                    after = term.substring(delim);

                    if (lower.startsWith("nruns")) {
                        try {
                            nRuns = Integer.parseInt(after);
                        } catch (Exception ignored) {}
                    } else if (lower.startsWith("nperiods")) {
                        try {
                            nPeriods = Integer.parseInt(after);
                        } catch (Exception ignored) {}
                    } else if (lower.startsWith("reportinginterval")) {
                        try {
                            reportingInterval = Integer.parseInt(after);
                        } catch (Exception ignored) {}
                    } else if (lower.startsWith("maxage")) {
                        try {
                            maxAge = Integer.parseInt(after);
                        } catch (Exception ignored) {}
                    } else if (lower.startsWith("reproductionage")) {
                        try {
                            if (after.startsWith("[")) {
                                temp = after.substring(1);
                                int[] ints;

                                int first = temp.indexOf(", ");
                                if (first > 0) {
                                    ints = new int[2];
                                    ints[0] = Integer.parseInt(temp.substring(0, first));
                                    ints[1] = Integer.parseInt(
                                            temp.substring(first + 2, temp.indexOf(']')));
                                } else {
                                    ints = new int[1];
                                    ints[0] = Integer.parseInt(temp.substring(0, first));
                                }
                                reproductionAge = ints;
                            }
                        } catch (Exception ignored) {}
                    } else if (lower.startsWith("isgendered")) {
                        try {
                            isGendered = Boolean.parseBoolean(after);
                        } catch (Exception ignored) {}
                    } else if (lower.startsWith("maxlittersize")) {
                        try {
                            maxLitterSize = Integer.parseInt(after);
                        } catch (Exception ignored) {}
                    } else if (lower.startsWith("litterprobability")) {
                        try {
                            if (after.startsWith("[")) {
                                temp = after.substring(1, after.length() - 1);

                                split = temp.split(", ");

                                double[] doubles = new double[split.length];

                                for (int i = 0; i < split.length; i++) {
                                    doubles[i] = Double.parseDouble(split[i]);
                                }

                                litterProbability = doubles;
                            }
                        } catch (Exception ignored) {}
                    } else if (lower.startsWith("sexratio")) {
                        try {
                            sexRatio = Double.parseDouble(after);
                        } catch (Exception ignored) {}
                    } else if (lower.startsWith("mortality")) {
                        try {
                            if (after.startsWith("[")) {
                                temp = after.substring(1, after.indexOf(']'));
                                int g = (after.indexOf('[', 2) > 0) ? 2 : 1;

                                split = temp.split(", ");

                                double[][] doubles = new double[g][split.length];

                                for (int i = 0; i < split.length; i++) {
                                    doubles[0][i] = Double.parseDouble(split[i]);
                                }

                                if (g > 1) {
                                    temp = after.substring(after.indexOf('[', 2),
                                            temp.length() - 1);

                                    split = temp.split(", ");

                                    if (split.length == doubles[0].length) {
                                        for (int i = 0; i < split.length; i++) {
                                            doubles[1][i] = Double.parseDouble(split[i]);
                                        }
                                    } else {
                                        doubles = null;
                                    }
                                }

                                mortality = doubles;
                            }
                        } catch (Exception ignored) {}
                    } else if (lower.startsWith("sdmortality")) {
                        try {
                            if (after.startsWith("[")) {
                                temp = after.substring(1, after.indexOf(']'));
                                int g = (after.indexOf('[', 2) > 0) ? 2 : 1;

                                split = temp.split(", ");

                                double[][] doubles = new double[g][split.length];

                                for (int i = 0; i < split.length; i++) {
                                    doubles[0][i] = Double.parseDouble(split[i]);
                                }

                                if (g > 1) {
                                    temp = after.substring(after.indexOf('[', 2),
                                            temp.length() - 1);

                                    split = temp.split(", ");

                                    if (split.length == doubles[0].length) {
                                        for (int i = 0; i < split.length; i++) {
                                            doubles[1][i] = Double.parseDouble(split[i]);
                                        }
                                    } else {
                                        doubles = null;
                                    }
                                }

                                sdMortality = doubles;
                            }
                        } catch (Exception ignored) {}
                    } else if (lower.startsWith("initialpopulation")) {
                        try {
                            if (after.startsWith("[")) {
                                temp = after.substring(1, after.indexOf(']'));
                                int g = (after.indexOf('[', 2) > 0) ? 2 : 1;

                                split = temp.split(", ");

                                long[][] longs = new long[g][split.length];

                                for (int i = 0; i < split.length; i++) {
                                    longs[0][i] = Long.parseLong(split[i]);
                                }

                                if (g > 1) {
                                    temp = after.substring(after.indexOf('[', 2),
                                            temp.length() - 1);

                                    split = temp.split(", ");

                                    if (split.length == longs[0].length) {
                                        for (int i = 0; i < split.length; i++) {
                                            longs[1][i] = Long.parseLong(split[i]);
                                        }
                                    } else {
                                        longs = null;
                                    }
                                }

                                initialPopulation = longs;
                            }
                        } catch (Exception ignored) {}
                    } else if (lower.startsWith("carryingcapacity")) {
                        try {
                            carryingCapacity = Long.parseLong(after);
                        } catch (Exception ignored) {}
                    } else if (lower.startsWith("sdcarryingcapacity")) {
                        try {
                            sdCarryingCapacity = Double.parseDouble(after);
                        } catch (Exception ignored) {}
                    } else if (lower.startsWith("harvestrate")) {
                        try {
                            harvestRate = Integer.parseInt(after);
                        } catch (Exception ignored) {}
                    } else if (lower.startsWith("supplementrate")) {
                        try {
                            supplementRate = Integer.parseInt(after);
                        } catch (Exception ignored) {}
                    }
                }
            } catch (Exception ignored) {}

        mLastValid = false;

        mNRuns = nRuns;
        mNPeriods = nPeriods;
        mReportingInterval = reportingInterval;
        mMaxAge = maxAge;
        mReproductionAge = reproductionAge;
        mIsGendered = isGendered;
        mMaxLitterSize = maxLitterSize;
        mLitterProbability = litterProbability;
        mSexRatio = sexRatio;
        mRMCorrelation = rmCorrelation;
        mMortality = mortality;
        mSDMortality = sdMortality;
        mInitialPopulation = initialPopulation;
        mCarryingCapacity = carryingCapacity;
        mSDCarryingCapacity = sdCarryingCapacity;
        mHarvestRate = harvestRate;
        mSupplementRate = supplementRate;
    }

    public boolean equals(SwirlParameterBundle o) {
        SwirlParameterBundle that = o;

        if (mNRuns != that.mNRuns) return false;
        if (mNPeriods != that.mNPeriods) return false;
        if (mReportingInterval != that.mReportingInterval) return false;
        if (mMaxAge != that.mMaxAge) return false;
        if (mIsGendered != that.mIsGendered) return false;
        if (mMaxLitterSize != that.mMaxLitterSize) return false;
        if (Double.compare(that.mSexRatio, mSexRatio) != 0) return false;
        if (Double.compare(that.mRMCorrelation, mRMCorrelation) != 0) return false;
        if (mCarryingCapacity != that.mCarryingCapacity) return false;
        if (Double.compare(that.mSDCarryingCapacity, mSDCarryingCapacity) != 0) return false;
        if (mHarvestRate != that.mHarvestRate) return false;
        if (mSupplementRate != that.mSupplementRate) return false;
        if (!Arrays.equals(mReproductionAge, that.mReproductionAge)) return false;
        if (!Arrays.equals(mLitterProbability, that.mLitterProbability)) return false;
        if (!Arrays.deepEquals(mMortality, that.mMortality)) return false;
        if (!Arrays.deepEquals(mSDMortality, that.mSDMortality)) return false;
        return Arrays.deepEquals(mInitialPopulation, that.mInitialPopulation);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SwirlParameterBuilder that = (SwirlParameterBuilder) o;

        if (mNRuns != that.mNRuns) return false;
        if (mNPeriods != that.mNPeriods) return false;
        if (mReportingInterval != that.mReportingInterval) return false;
        if (mMaxAge != that.mMaxAge) return false;
        if (mIsGendered != that.mIsGendered) return false;
        if (mMaxLitterSize != that.mMaxLitterSize) return false;
        if (Double.compare(that.mSexRatio, mSexRatio) != 0) return false;
        if (Double.compare(that.mRMCorrelation, mRMCorrelation) != 0) return false;
        if (mCarryingCapacity != that.mCarryingCapacity) return false;
        if (Double.compare(that.mSDCarryingCapacity, mSDCarryingCapacity) != 0) return false;
        if (mHarvestRate != that.mHarvestRate) return false;
        if (mSupplementRate != that.mSupplementRate) return false;
        if (!Arrays.equals(mReproductionAge, that.mReproductionAge)) return false;
        if (!Arrays.equals(mLitterProbability, that.mLitterProbability)) return false;
        if (!Arrays.deepEquals(mMortality, that.mMortality)) return false;
        if (!Arrays.deepEquals(mSDMortality, that.mSDMortality)) return false;
        return Arrays.deepEquals(mInitialPopulation, that.mInitialPopulation);

    }
}
