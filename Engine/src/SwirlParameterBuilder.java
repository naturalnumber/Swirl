package com.stochasticsystems.swirl;

import org.jetbrains.annotations.Contract;

import java.io.PrintStream;

/**
 * Created by androiddev on 2017-11-02.
 */
public class SwirlParameterBuilder {
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
        mLastValid = true;
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

    /**
     * @return number of failed tests.
     */
    static int test(PrintStream err) {
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
            if (!b.setSDCarryingCapacity(SwirlEngine.CARRYING_CAPACITY_DEFAULT)) {
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
            if (!b.setSupplementRate(SwirlEngine.HARVEST_RATE_DEFAULT)) {
                failed++;
                err.println("Default SupplementRate invalid?");
            }

            if (!b.isValid()) {
                failed++;
                err.println("Defaults invalid?");
            }

            SwirlParameterBundle defaults = b.build();

            if (!defaults.equals(SwirlParameterBundle.getDefault())) {
                failed++;
                err.println("Default not returned?");
            }

        } catch (Exception e) {
            err.println(e);
            // TODO: Make this consistent
        }

        return failed;
    }

    public static void main(String[] args) {
        // TODO: test stuff
        test(System.err);
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
    @org.jetbrains.annotations.Contract(pure = true)
    public static boolean isValidNRuns(int nRuns) {
        return nRuns > 0 && nRuns < SwirlEngine.N_RUNS_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param nRuns
     *
     * @return if the parameter is possibly viable
     */
    @Contract(pure = true)
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
    @Contract(pure = true)
    public static boolean isValidNPeriods(int nPeriods) {
        return nPeriods > 0 && nPeriods < SwirlEngine.N_PERIODS_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param nPeriods
     *
     * @return if the parameter is possibly viable
     */
    @Contract(pure = true)
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
    @Contract(pure = true)
    static boolean isValidReportingIntervalF(int reportingInterval, int nPeriods) {
        return reportingInterval > 0 && reportingInterval <= nPeriods &&
               reportingInterval % nPeriods == 0;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param reportingInterval
     *
     * @return if the parameter is possibly viable
     */
    @Contract(pure = true)
    public static boolean isViableReportingInterval(int reportingInterval) {
        return reportingInterval > 0 && reportingInterval <= SwirlEngine.N_PERIODS_MAX;
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
    @Contract(pure = true)
    public static boolean isValidMaxAge(int maxAge) {
        return maxAge > 0 && maxAge < SwirlEngine.MAX_AGE_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param maxAge
     *
     * @return if the parameter is possibly viable
     */
    @Contract(pure = true)
    public static boolean isViableMaxAge(int maxAge) {
        return isValidMaxAge(maxAge);
    }

    public int[] getReproductionAge() {
        return mReproductionAge;
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
    @Contract(pure = true)
    static boolean isValidReproductionAgeF(int[] reproductionAge, boolean gendered,
                                           int maxAge) {
        return isValidReproductionAgeEntry(reproductionAge[0], maxAge) &&
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
        return validGDim(reproductionAge) &&
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
    @Contract(pure = true)
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
    @Contract(pure = true)
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
    @Contract(pure = true)
    public static boolean isValidMaxLitterSize(int maxLitterSize) {
        return maxLitterSize > 0 && maxLitterSize < SwirlEngine.MAX_LITTER_SIZE_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param maxLitterSize
     *
     * @return if the parameter is possibly viable
     */
    @Contract(pure = true)
    public static boolean isViableMaxLitterSize(int maxLitterSize) {
        return isValidMaxLitterSize(maxLitterSize);
    }

    public double[] getLitterProbability() {
        return mLitterProbability;
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
        return validLDim(litterProbability, SwirlEngine.MAX_LITTER_SIZE_MAX + 1) &&
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
    @Contract(pure = true)
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
    @Contract(pure = true)
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
    @Contract(pure = true)
    public static boolean isValidRMCorrelation(double rmCorrelation) {
        return rmCorrelation >= SwirlEngine.RM_CORRELATION_MIN && rmCorrelation <= 1.0d;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param rmCorrelation
     *
     * @return if the parameter is possibly viable
     */
    @Contract(pure = true)
    public static boolean isViableRMCorrelation(double rmCorrelation) {
        return isValidRMCorrelation(rmCorrelation);
    }

    public double[][] getMortality() {
        return mMortality;
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
    @Contract(pure = true)
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
        return validGDim(mortality) &&
               isValidMortalityF(mortality, toGenders(mortality), mortality[0].length - 1);
    }

    public double[][] getSDMortality() {
        return mSDMortality;
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
        boolean isValid =
                sdMortality[0].length == size && (!gendered || sdMortality[1].length == size);
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
        return validGDim(sdMortality) &&
               isValidSDMortalityF(sdMortality, toGenders(sdMortality),
                                   sdMortality[0].length - 1);
    }

    public long[][] getInitialPopulation() {
        return mInitialPopulation;
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
    @Contract(pure = true)
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
        return validGDim(initialPopulation) &&
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
    @Contract(pure = true)
    public static boolean isValidInitialPopulationEntry(long initialPopulation) {
        return initialPopulation >= 0L && initialPopulation <= SwirlEngine.INITIAL_POPULATION_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param initialPopulation
     *
     * @return
     */
    @Contract(pure = true)
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
    @Contract(pure = true)
    public static boolean isValidCarryingCapacity(long carryingCapacity) {
        return carryingCapacity > 0L && carryingCapacity <= SwirlEngine.CARRYING_CAPACITY_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param carryingCapacity
     *
     * @return
     */
    @Contract(pure = true)
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
    @Contract(pure = true)
    public static boolean isValidSDCarryingCapacity(double sdCarryingCapacity) {
        return sdCarryingCapacity >= 0L && sdCarryingCapacity <= SwirlEngine.SD_CARRYING_CAPACITY_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param sdCarryingCapacity
     *
     * @return
     */
    @Contract(pure = true)
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
    @Contract(pure = true)
    public static boolean isValidHarvestRate(int harvestRate) {
        return harvestRate >= 0 && harvestRate <= SwirlEngine.HARVEST_RATE_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param harvestRate
     *
     * @return
     */
    @Contract(pure = true)
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
    @Contract(pure = true)
    public static boolean isValidSupplementRate(int supplementRate) {
        return supplementRate >= 0 && supplementRate <= SwirlEngine.HARVEST_RATE_MAX;
    }

    /**
     * Returns false if there is no way that the parameter can be valid, true otherwise. Does
     * not guarantee validity.
     *
     * @param supplementRate
     *
     * @return
     */
    @Contract(pure = true)
    public static boolean isViableSupplementRate(int supplementRate) {
        return isValidSupplementRate(supplementRate);
    }

    //  Check helpers

    @Contract(pure = true)
    static boolean validProb(double p) {
        return p >= 0.0d && p <= 1.0d;
    }

    @Contract(pure = true)
    static boolean realProb(double p) {
        return p > 0.0d && p < 1.0d;
    }

    @Contract(value = "null, _ -> false", pure = true)
    static boolean validGArray(int[] array, boolean gendered) {
        return array != null && array.length == ((gendered) ? 2 : 1);
    }

    @Contract(value = "null, _ -> false", pure = true)
    static boolean validGArray(int[][] array, boolean gendered) {
        return array != null && array.length == ((gendered) ? 2 : 1);
    }

    @Contract(value = "null, _ -> false", pure = true)
    static boolean validGArray(long[] array, boolean gendered) {
        return array != null && array.length == ((gendered) ? 2 : 1);
    }

    @Contract(value = "null, _ -> false", pure = true)
    static boolean validGArray(long[][] array, boolean gendered) {
        return array != null && array.length == ((gendered) ? 2 : 1);
    }

    @Contract(value = "null, _ -> false", pure = true)
    static boolean validGArray(double[] array, boolean gendered) {
        return array != null && array.length == ((gendered) ? 2 : 1);
    }

    @Contract(value = "null, _ -> false", pure = true)
    static boolean validGArray(double[][] array, boolean gendered) {
        return array != null && array.length == ((gendered) ? 2 : 1);
    }

    @Contract(pure = true)
    static boolean validGDim(int firstDimension) {
        return firstDimension == 1 || firstDimension == 2;
    }

    @Contract(value = "null -> false", pure = true)
    static boolean validGDim(int[] array) {
        return array != null && (array.length == 1 || array.length == 2);
    }

    @Contract(value = "null -> false", pure = true)
    static boolean validGDim(int[][] array) {
        return array != null && (array.length == 1 || array.length == 2);
    }

    @Contract(value = "null -> false", pure = true)
    static boolean validGDim(long[] array) {
        return array != null && (array.length == 1 || array.length == 2);
    }

    @Contract(value = "null -> false", pure = true)
    static boolean validGDim(long[][] array) {
        return array != null && (array.length == 1 || array.length == 2);
    }

    @Contract(value = "null -> false", pure = true)
    static boolean validGDim(double[] array) {
        return array != null && (array.length == 1 || array.length == 2);
    }

    @Contract(value = "null -> false", pure = true)
    static boolean validGDim(double[][] array) {
        return array != null && (array.length == 1 || array.length == 2);
    }

    @Contract(value = "null, _ -> false", pure = true)
    static boolean validLDim(double[] array, int length) {
        return array != null && array.length == length;
    }

    @Contract(pure = true)
    static boolean toGenders(int firstDimension) {
        return firstDimension == 2;
    }

    @Contract(pure = true)
    static boolean toGenders(int[] array) {
        return array.length == 2;
    }

    @Contract(pure = true)
    static boolean toGenders(int[][] array) {
        return array.length == 2;
    }

    @Contract(pure = true)
    static boolean toGenders(long[] array) {
        return array.length == 2;
    }

    @Contract(pure = true)
    static boolean toGenders(long[][] array) {
        return array.length == 2;
    }

    @Contract(pure = true)
    static boolean toGenders(double[] array) {
        return array.length == 2;
    }

    @Contract(pure = true)
    static boolean toGenders(double[][] array) {
        return array.length == 2;
    }
}
