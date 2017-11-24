package com.stochasticsystems.swirl;

import java.util.Arrays;

/**
 * Created by androiddev on 2017-11-02.
 */
public class SwirlParameterBundle {
    private final static SwirlParameterBundle sDefault = new SwirlParameterBundle(
         SwirlEngine.N_RUNS_DEFAULT,
         SwirlEngine.N_PERIODS_DEFAULT,
         SwirlEngine.REPORTING_INTERVAL_DEFAULT,
         SwirlEngine.MAX_AGE_DEFAULT,
         SwirlEngine.REPRODUCTION_AGE_DEFAULT,
         SwirlEngine.IS_GENDERED_DEFAULT,
         SwirlEngine.MAX_LITTER_SIZE_DEFAULT,
         SwirlEngine.LITTER_PROBABILITY_DEFAULT,
         //SwirlEngine.SD_REPRODUCTION_P_DEFAULT,
         SwirlEngine.SEX_RATIO_DEFAULT,
         SwirlEngine.RM_CORRELATION_DEFAULT,
         SwirlEngine.MORTALITY_DEFAULT,
         SwirlEngine.SD_MORTALITY_DEFAULT,
         SwirlEngine.INITIAL_POPULATION_DEFAULT,
         SwirlEngine.CARRYING_CAPACITY_DEFAULT,
         SwirlEngine.SD_CARRYING_CAPACITY_DEFAULT,
         SwirlEngine.HARVEST_RATE_DEFAULT,
         SwirlEngine.SUPPLEMENT_RATE_DEFAULT);

    final int        mNRuns;
    final int        mNPeriods;
    final int        mReportingInterval;
    final int        mMaxAge;
    final int[]      mReproductionAge; // By sex
    final boolean    mIsGendered;
    final int        mMaxLitterSize;
    final double[]   mLitterProbability; // By litter size
    //final double     mReproductionPercentSD;
    final double     mSexRatio;
    final double     mRMCorrelation;
    final double[][] mMortality; // By sex, age
    final double[][] mSDMortality; // By sex, age
    final long[][]   mInitialPopulation; // By sex, age
    final long       mCarryingCapacity;
    final double     mSDCarryingCapacity;
    final int        mHarvestRate;
    final int        mSupplementRate;

    // Too much?
    private final boolean mIsValid;

    private SwirlParameterBundle(int nRuns, int nPeriods, int reportingInterval, int maxAge,
                                 int[] reproductionAge, boolean isGendered, int maxLitterSize,
                                 double[] litterProbability, //double reproductionPercentSD,
                                 double sexRatio, double rmCorrelation,
                                 double[][] mortality, double[][] sdMortality,
                                 long[][] initialPopulation, long carryingCapacity,
                                 double sdCarryingCapacity, int harvestRate, int supplementRate) {
        mNRuns = nRuns;
        mNPeriods = nPeriods;
        mReportingInterval = reportingInterval;
        mMaxAge = maxAge;
        mReproductionAge = reproductionAge;
        mIsGendered = isGendered;
        mMaxLitterSize = maxLitterSize;
        mLitterProbability = litterProbability;
        //mReproductionPercentSD = reproductionPercentSD;
        mSexRatio = sexRatio;
        mRMCorrelation = rmCorrelation;
        mMortality = mortality;
        mSDMortality = sdMortality;
        mInitialPopulation = initialPopulation;
        mCarryingCapacity = carryingCapacity;
        mSDCarryingCapacity = sdCarryingCapacity;
        mHarvestRate = harvestRate;
        mSupplementRate = supplementRate;
        mIsValid = isValid();
    }

    /**
     * Use with caution.
     *
     * @param nRuns
     * @param nPeriods
     * @param reportingInterval
     * @param maxAge
     * @param reproductionAge
     * @param isGendered
     * @param maxLitterSize
     * @param litterProbability
     * @param sexRatio
     * @param rmCorrelation
     * @param mortality
     * @param sdMortality
     * @param initialPopulation
     * @param carryingCapacity
     * @param sdCarryingCapacity
     * @param harvestRate
     * @param supplementRate
     * @param isValid
     */
    SwirlParameterBundle(int nRuns, int nPeriods, int reportingInterval, int maxAge,
                         int[] reproductionAge, boolean isGendered, int maxLitterSize,
                         double[] litterProbability, //double reproductionPercentSD,
                         double sexRatio, double rmCorrelation,
                         double[][] mortality, double[][] sdMortality,
                         long[][] initialPopulation, long carryingCapacity,
                         double sdCarryingCapacity, int harvestRate, int supplementRate,
                         boolean isValid) {
        mNRuns = nRuns;
        mNPeriods = nPeriods;
        mReportingInterval = reportingInterval;
        mMaxAge = maxAge;
        mReproductionAge = reproductionAge;
        mIsGendered = isGendered;
        mMaxLitterSize = maxLitterSize;
        mLitterProbability = litterProbability;
        //mReproductionPercentSD = reproductionPercentSD;
        mSexRatio = sexRatio;
        mRMCorrelation = rmCorrelation;
        mMortality = mortality;
        mSDMortality = sdMortality;
        mInitialPopulation = initialPopulation;
        mCarryingCapacity = carryingCapacity;
        mSDCarryingCapacity = sdCarryingCapacity;
        mHarvestRate = harvestRate;
        mSupplementRate = supplementRate;
        mIsValid = isValid;
    }

    //  Getters

    public int getNRuns() {
        return mNRuns;
    }

    public int getNPeriods() {
        return mNPeriods;
    }

    public int getReportingInterval() {
        return mReportingInterval;
    }

    public int getMaxAge() {
        return mMaxAge;
    }

    public int[] getReproductionAge() {
        return mReproductionAge;
    }

    public boolean isGendered() {
        return mIsGendered;
    }

    public int getMaxLitterSize() {
        return mMaxLitterSize;
    }

    public double[] getLitterProbability() {
        return mLitterProbability;
    }

    //public double getReproductionPercentSD() {
    //    return mReproductionPercentSD;
    //}

    public double getSexRatio() {
        return mSexRatio;
    }

    public double getRMCorrelation() {
        return mRMCorrelation;
    }

    public double[][] getMortality() {
        return mMortality;
    }

    public double[][] getSDMortality() {
        return mSDMortality;
    }

    public long[][] getInitialPopulation() {
        return mInitialPopulation;
    }

    public long getCarryingCapacity() {
        return mCarryingCapacity;
    }

    public double getSDCarryingCapacity() {
        return mSDCarryingCapacity;
    }

    public int getHarvestRate() {
        return mHarvestRate;
    }

    public int getSupplementRate() {
        return mSupplementRate;
    }

    public static SwirlParameterBundle getDefault() {
        return sDefault;
    }

    /**
     * Pair with SwirlParameterBuilder
     *
     * @return
     */
    private boolean isValid() {
        boolean isValid = SwirlParameterBuilder.isValidNRuns(mNRuns) &&
                          SwirlParameterBuilder.isValidNPeriods(mNPeriods)
                          && SwirlParameterBuilder
                                     .isValidReportingIntervalF(mReportingInterval, mNPeriods)
                          && SwirlParameterBuilder.isValidMaxAge(mMaxAge)
                          && SwirlParameterBuilder
                                     .isValidReproductionAgeF(mReproductionAge, mIsGendered, mMaxAge)
                          && SwirlParameterBuilder.isValidMaxLitterSize(mMaxLitterSize)
                          && SwirlParameterBuilder
                                     .isValidLitterProbabilityF(mLitterProbability, mMaxLitterSize)
                          && SwirlParameterBuilder
                                     .isValidSexRatio(mSexRatio, mIsGendered)
                          && SwirlParameterBuilder.isValidRMCorrelation(mRMCorrelation)
                          && SwirlParameterBuilder
                                     .isValidMortalityF(mMortality, mIsGendered, mMaxAge)
                          && SwirlParameterBuilder
                                     .isValidSDMortalityF(mSDMortality, mIsGendered, mMaxAge)
                          && SwirlParameterBuilder
                                     .isValidInitialPopulationF(mInitialPopulation, mIsGendered, mMaxAge)
                          && SwirlParameterBuilder
                                     .isValidCarryingCapacity(mCarryingCapacity)
                          &&
                          SwirlParameterBuilder
                                  .isValidSDCarryingCapacity(mSDCarryingCapacity)
                          && SwirlParameterBuilder.isValidHarvestRate(mHarvestRate)
                          && SwirlParameterBuilder
                                     .isValidSupplementRate(mSupplementRate);

        return isValid;
    }

    //  Other methods

    @Override
    public String toString() {
        // TODO: make a sensible version.
        return "SwirlParameterBundle{" +
               "mNRuns=" + mNRuns +
               ", mNPeriods=" + mNPeriods +
               ", mReportingInterval=" + mReportingInterval +
               ", mMaxAge=" + mMaxAge +
               ", mReproductionAge=" + Arrays.toString(mReproductionAge) +
               ", mIsGendered=" + mIsGendered +
               ", mMaxLitterSize=" + mMaxLitterSize +
               ", mLitterProbability=" + Arrays.toString(mLitterProbability) +
               ", mSexRatio=" + mSexRatio +
               ", mRMCorrelation=" + mRMCorrelation +
               ", mMortality=" + Arrays.toString(mMortality) +
               ", mSDMortality=" + Arrays.toString(mSDMortality) +
               ", mInitialPopulation=" + Arrays.toString(mInitialPopulation) +
               ", mCarryingCapacity=" + mCarryingCapacity +
               ", mSDCarryingCapacity=" + mSDCarryingCapacity +
               ", mHarvestRate=" + mHarvestRate +
               ", mSupplementRate=" + mSupplementRate +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SwirlParameterBundle that = (SwirlParameterBundle) o;

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
    public int hashCode() {
        int  result;
        long temp;
        result = mNRuns;
        result = 31 * result + mNPeriods;
        result = 31 * result + mReportingInterval;
        result = 31 * result + mMaxAge;
        result = 31 * result + Arrays.hashCode(mReproductionAge);
        result = 31 * result + (mIsGendered ? 1 : 0);
        result = 31 * result + mMaxLitterSize;
        result = 31 * result + Arrays.hashCode(mLitterProbability);
        temp = Double.doubleToLongBits(mSexRatio);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(mRMCorrelation);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + Arrays.deepHashCode(mMortality);
        result = 31 * result + Arrays.deepHashCode(mSDMortality);
        result = 31 * result + Arrays.deepHashCode(mInitialPopulation);
        result = 31 * result + (int) (mCarryingCapacity ^ (mCarryingCapacity >>> 32));
        temp = Double.doubleToLongBits(mSDCarryingCapacity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + mHarvestRate;
        result = 31 * result + mSupplementRate;
        return result;
    }
}
