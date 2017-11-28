package com.stochasticsystems.swirl.engine;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Stores the parameters used to run a simulation.
 * <p>
 * Created by Allan Stewart on 2017-11-02.
 */
public class SwirlParameterBundle implements Serializable, Cloneable {
    public static final String TAG = "SwirlParameterBundle";

    //  The parameters
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

    //  The validity
    private final boolean mIsValid;

    /**
     * The constructor used by the builder.
     *
     * @param nRuns              The number of times to run the simulation
     * @param nPeriods           The number of time steps to continue past the initial time
     * @param reportingInterval  Not used by the simulation
     * @param maxAge             The maximum age of an individual in the simulation
     * @param reproductionAge    The age(s) at which each gender becomes reproductive
     * @param isGendered         Whether the simulation tracks/uses two genders
     * @param maxLitterSize      The maximum simulated litter size
     * @param litterProbability  The probability of each litter size, indexed by litter size
     * @param sexRatio           The ratio of male to female births
     * @param rmCorrelation      The correlation between reproduction and mortality environmental
     *                           variation
     * @param mortality          The mortality rate by gender and age
     * @param sdMortality        The standard deviation of the provided mortality rates by gender
     *                           and age
     * @param initialPopulation  The initial population used to initialize each run by gender and
     *                           age
     * @param carryingCapacity   The carrying capacity used in the simulation
     * @param sdCarryingCapacity The standard deviation of the carrying capacity
     * @param harvestRate        The number of individuals removed from the population each year
     * @param supplementRate     The number of individuals added to the population each year
     */
    protected SwirlParameterBundle(int nRuns, int nPeriods, int reportingInterval, int maxAge,
                                   int[] reproductionAge, boolean isGendered, int maxLitterSize,
                                   double[] litterProbability, double sexRatio,
                                   double rmCorrelation,
                                   double[][] mortality, double[][] sdMortality,
                                   long[][] initialPopulation,
                                   long carryingCapacity, double sdCarryingCapacity,
                                   int harvestRate, int supplementRate) {
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
        mIsValid = validate();
    }

    /**
     * Creates a copy of the provided parameters.
     *
     * @param toCopy The parameters to copy
     */
    public SwirlParameterBundle(SwirlParameterBundle toCopy) {
        mNRuns = toCopy.getNRuns();
        mNPeriods = toCopy.getNPeriods();
        mReportingInterval = toCopy.getReportingInterval();
        mMaxAge = toCopy.getMaxAge();
        mReproductionAge = toCopy.getReproductionAge();
        mIsGendered = toCopy.isGendered();
        mMaxLitterSize = toCopy.getMaxLitterSize();
        mLitterProbability = toCopy.getLitterProbability();
        mSexRatio = toCopy.getSexRatio();
        mRMCorrelation = toCopy.getRMCorrelation();
        mMortality = toCopy.getMortality();
        mSDMortality = toCopy.getSDMortality();
        mInitialPopulation = toCopy.getInitialPopulation();
        mCarryingCapacity = toCopy.getCarryingCapacity();
        mSDCarryingCapacity = toCopy.getSDCarryingCapacity();
        mHarvestRate = toCopy.getHarvestRate();
        mSupplementRate = toCopy.getSupplementRate();
        mIsValid = validate();
    }

    /**
     * Creates a bundle with the exact parameters given, and forces the validity.
     * This is here because it was used in testing.
     * Use with caution.
     *
     * @param nRuns              The number of times to run the simulation
     * @param nPeriods           The number of time steps to continue past the initial time
     * @param reportingInterval  Not used by the simulation
     * @param maxAge             The maximum age of an individual in the simulation
     * @param reproductionAge    The age(s) at which each gender becomes reproductive
     * @param isGendered         Whether the simulation tracks/uses two genders
     * @param maxLitterSize      The maximum simulated litter size
     * @param litterProbability  The probability of each litter size, indexed by litter size
     * @param sexRatio           The ratio of male to female births
     * @param rmCorrelation      The correlation between reproduction and mortality environmental
     *                           variation
     * @param mortality          The mortality rate by gender and age
     * @param sdMortality        The standard deviation of the provided mortality rates by gender
     *                           and age
     * @param initialPopulation  The initial population used to initialize each run by gender and
     *                           age
     * @param carryingCapacity   The carrying capacity used in the simulation
     * @param sdCarryingCapacity The standard deviation of the carrying capacity
     * @param harvestRate        The number of individuals removed from the population each year
     * @param supplementRate     The number of individuals added to the population each year
     * @param isValid            The validity of these parameters
     */
    SwirlParameterBundle(int nRuns, int nPeriods, int reportingInterval, int maxAge,
                         int[] reproductionAge, boolean isGendered, int maxLitterSize,
                         double[] litterProbability,
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

    /**
     * @return The number of times to run the simulation
     */
    public int getNRuns() {
        return mNRuns;
    }

    /**
     * @return The number of time steps to continue past the initial time
     */
    public int getNPeriods() {
        return mNPeriods;
    }

    /**
     * @return Not used by the simulation
     */
    public int getReportingInterval() {
        return mReportingInterval;
    }

    /**
     * @return The maximum age of an individual in the simulation
     */
    public int getMaxAge() {
        return mMaxAge;
    }

    /**
     * @return The age(s) at which each gender becomes reproductive
     */
    public int[] getReproductionAge() {
        return mReproductionAge;
    }

    /**
     * @return Whether the simulation tracks/uses two genders
     */
    public boolean isGendered() {
        return mIsGendered;
    }

    /**
     * @return The maximum simulated litter size
     */
    public int getMaxLitterSize() {
        return mMaxLitterSize;
    }

    /**
     * @return The probability of each litter size, indexed by litter size
     */
    public double[] getLitterProbability() {
        return mLitterProbability;
    }

    /**
     * @return The ratio of male to female births
     */
    public double getSexRatio() {
        return mSexRatio;
    }

    /**
     * @return The correlation between reproduction and mortality environmental variation
     */
    public double getRMCorrelation() {
        return mRMCorrelation;
    }

    /**
     * @return The mortality rate by gender and age
     */
    public double[][] getMortality() {
        return mMortality;
    }

    /**
     * @return The standard deviation of the provided mortality rates by gender and age
     */
    public double[][] getSDMortality() {
        return mSDMortality;
    }

    /**
     * @return The initial population used to initialize each run by gender and age
     */
    public long[][] getInitialPopulation() {
        return mInitialPopulation;
    }

    /**
     * @return The carrying capacity used in the simulation
     */
    public long getCarryingCapacity() {
        return mCarryingCapacity;
    }

    /**
     * @return The standard deviation of the carrying capacity
     */
    public double getSDCarryingCapacity() {
        return mSDCarryingCapacity;
    }

    /**
     * @return The number of individuals removed from the population each year
     */
    public int getHarvestRate() {
        return mHarvestRate;
    }

    /**
     * @return The number of individuals added to the population each year
     */
    public int getSupplementRate() {
        return mSupplementRate;
    }

    /**
     * @return The default parameters
     */
    public static SwirlParameterBundle getDefault() {
        return sDefault;
    }

    /**
     * @return The validity of these parameters
     */
    public boolean isValid() {
        return mIsValid;
    }

    /**
     * Pair with SwirlParameterBuilder validation.
     *
     * @return The validity of these parameters
     */
    private boolean validate() {
        return SwirlParameterBuilder.isValidNRuns(mNRuns) &&
               SwirlParameterBuilder.isValidNPeriods(mNPeriods) &&
               SwirlParameterBuilder.isValidReportingIntervalF(mReportingInterval, mNPeriods) &&
               SwirlParameterBuilder.isValidMaxAge(mMaxAge) &&
               SwirlParameterBuilder.isValidReproductionAgeF(mReproductionAge, mIsGendered,
                                                             mMaxAge) &&
               SwirlParameterBuilder.isValidMaxLitterSize(mMaxLitterSize) &&
               SwirlParameterBuilder.isValidLitterProbabilityF(mLitterProbability,
                                                               mMaxLitterSize) &&
               SwirlParameterBuilder.isValidSexRatio(mSexRatio, mIsGendered) &&
               SwirlParameterBuilder.isValidRMCorrelation(mRMCorrelation) &&
               SwirlParameterBuilder.isValidMortalityF(mMortality, mIsGendered, mMaxAge) &&
               SwirlParameterBuilder.isValidSDMortalityF(mSDMortality, mIsGendered, mMaxAge) &&
               SwirlParameterBuilder.isValidInitialPopulationF(mInitialPopulation, mIsGendered,
                                                               mMaxAge) &&
               SwirlParameterBuilder.isValidCarryingCapacity(mCarryingCapacity) &&
               SwirlParameterBuilder.isValidSDCarryingCapacity(mSDCarryingCapacity) &&
               SwirlParameterBuilder.isValidHarvestRate(mHarvestRate) &&
               SwirlParameterBuilder.isValidSupplementRate(mSupplementRate);
    }

    //  Helper methods

    /**
     * @return The number of genders
     */
    public int nGenders() {
        return (mIsGendered) ? 2 : 1;
    }

    /**
     * @return The initial population of each gender, female first
     */
    public long[] totalInitialGenderes() {
        int  S       = nGenders();
        long total[] = new long[S];

        for (int g = 0; g < S; g++)
            for (long l : mInitialPopulation[g]) {
                total[g] += l;
            }

        return total;
    }

    /**
     * @return The total initial population
     */
    public long totalInitialPopulation() {
        int  S     = nGenders();
        long total = 0l;

        for (int g = 0; g < S; g++)
            for (long l : mInitialPopulation[g]) {
                total += l;
            }

        return total;
    }

    /**
     * @return The mean litter size
     */
    public double meanLitterSize() {
        double average = 0;

        for (int i = 1; i < mLitterProbability.length; i++) average += i * mLitterProbability[i];

        return average;
    }

    /**
     * The mean mortality rate.
     * Needs to be tested.
     *
     * @return The mean mortality rate
     */
    public double meanMortalityRate() { // TODO: check this
        double average = 0.0d, runningWeight = 1.0;

        if (mIsGendered) {
            runningWeight = 1.0;
            average = 0.0d;
            for (int j = 0; j < mMortality[0].length; j++) {
                average += mMortality[0][j] * runningWeight;
                runningWeight *= (1.0d - mMortality[0][j]);
            }
            average /= mMortality[0].length;

            runningWeight = 1.0;
            double averageM = 0.0d;
            for (int j = 0; j < mMortality[0].length; j++) {
                averageM += mMortality[0][j] * runningWeight;
                runningWeight *= (1.0d - mMortality[0][j]);
            }
            averageM /= mMortality[1].length;

            average = average * (1.0d - mSexRatio) + averageM * mSexRatio;
        } else {
            runningWeight = 1.0;
            average = 0.0d;
            for (int j = 0; j < mMortality[0].length; j++) {
                average += mMortality[0][j] * runningWeight;
                runningWeight *= (1.0d - mMortality[0][j]);
            }
            average /= mMortality[0].length;
        }

        return average;
    }

    //  Comparision outputs

    /**
     * Estimates the equivalent output of these parameters from the logistic equation.
     * Needs to be more thoroughly tested.
     *
     * @return The logistic output for each time step
     */
    public double[] logisticOutput() {
        long   P0 = totalInitialPopulation();
        double r  = meanLitterSize() * ((isGendered()) ? getSexRatio() : 1.0d);
        long   K  = getCarryingCapacity();
        int    T  = getNPeriods();

        double[] out = new double[T + 1];
        Arrays.fill(out, 0.0d);

        out[0] = P0;

        double e;
        for (int t = 1; t <= T; t++) {
            e = Math.exp(r * t);
            out[t] = (K * P0 * e) / (K + P0 * (e - 1.0d));
        }

        return out;
    }

    //  Other methods

    /**
     * Encodes the parameters as a string, using the provided delimiter.
     * Using = [ ] or , may make the string unreadable by fromDelimitedString.
     *
     * @param delimiter The text that separates the output parameters
     *
     * @return The string
     */
    public String toDelimitedString(String delimiter) {
        return "NRuns=" + mNRuns + delimiter +
               "NPeriods=" + mNPeriods + delimiter +
               "ReportingInterval=" + mReportingInterval + delimiter +
               "MaxAge=" + mMaxAge + delimiter +
               "ReproductionAge=" + Arrays.toString(mReproductionAge) + delimiter +
               "IsGendered=" + mIsGendered + delimiter +
               "MaxLitterSize=" + mMaxLitterSize + delimiter +
               "LitterProbability=" + Arrays.toString(mLitterProbability) + delimiter +
               "SexRatio=" + mSexRatio + delimiter +
               "RMCorrelation=" + mRMCorrelation + delimiter +
               "Mortality=" + Arrays.toString(mMortality) + delimiter +
               "SDMortality=" + Arrays.toString(mSDMortality) + delimiter +
               "InitialPopulation=" + Arrays.toString(mInitialPopulation) + delimiter +
               "CarryingCapacity=" + mCarryingCapacity + delimiter +
               "SDCarryingCapacity=" + mSDCarryingCapacity + delimiter +
               "HarvestRate=" + mHarvestRate + delimiter +
               "SupplementRate=" + mSupplementRate;
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
    public static SwirlParameterBundle fromDelimitedString(String input, String delimiter) {
        //if (delimiter.equals("="))

        int        nRuns              = SwirlEngine.N_RUNS_DEFAULT;
        int        nPeriods           = SwirlEngine.N_PERIODS_DEFAULT;
        int        reportingInterval  = SwirlEngine.REPORTING_INTERVAL_DEFAULT;
        int        maxAge             = SwirlEngine.MAX_AGE_DEFAULT;
        int[]      reproductionAge    = null;
        boolean    isGendered         = SwirlEngine.IS_GENDERED_DEFAULT;
        int        maxLitterSize      = SwirlEngine.MAX_LITTER_SIZE_DEFAULT;
        double[]   litterProbability  = null;
        double     sexRatio           = SwirlEngine.SEX_RATIO_DEFAULT;
        double     rmCorrelation      = SwirlEngine.RM_CORRELATION_DEFAULT;
        double[][] mortality          = null;
        double[][] sdMortality        = null;
        long[][]   initialPopulation  = null;
        long       carryingCapacity   = SwirlEngine.CARRYING_CAPACITY_DEFAULT;
        double     sdCarryingCapacity = SwirlEngine.SD_CARRYING_CAPACITY_DEFAULT;
        int        harvestRate        = SwirlEngine.HARVEST_RATE_DEFAULT;
        int        supplementRate     = SwirlEngine.SUPPLEMENT_RATE_DEFAULT;

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

        return new SwirlParameterBundle(nRuns, nPeriods, reportingInterval, maxAge, reproductionAge,
                                        isGendered, maxLitterSize,
                                        litterProbability, sexRatio, rmCorrelation, mortality,
                                        sdMortality, initialPopulation,
                                        carryingCapacity, sdCarryingCapacity, harvestRate,
                                        supplementRate);
    }

    @Override
    public String toString() {
        // TODO: make a sensible version.
        return "SwirlParameterBundle{" +
               "NRuns=" + mNRuns +
               ", NPeriods=" + mNPeriods +
               ", ReportingInterval=" + mReportingInterval +
               ", MaxAge=" + mMaxAge +
               ", ReproductionAge=" + Arrays.toString(mReproductionAge) +
               ", IsGendered=" + mIsGendered +
               ", MaxLitterSize=" + mMaxLitterSize +
               ", LitterProbability=" + Arrays.toString(mLitterProbability) +
               ", SexRatio=" + mSexRatio +
               ", RMCorrelation=" + mRMCorrelation +
               ", Mortality=" + Arrays.toString(mMortality[0]) +
               ((mIsGendered) ? Arrays.toString(mMortality[1]) : "") +
               ", SDMortality=" + Arrays.toString(mSDMortality[0]) +
               ((mIsGendered) ? Arrays.toString(mSDMortality[1]) : "") +
               ", InitialPopulation=" + Arrays.toString(mInitialPopulation[0]) +
               ((mIsGendered) ? Arrays.toString(mInitialPopulation[1]) : "") +
               ", CarryingCapacity=" + mCarryingCapacity +
               ", SDCarryingCapacity=" + mSDCarryingCapacity +
               ", HarvestRate=" + mHarvestRate +
               ", SupplementRate=" + mSupplementRate +
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

    private final static SwirlParameterBundle sDefault =
            new SwirlParameterBundle(SwirlEngine.N_RUNS_DEFAULT,
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

    /*
    public DoubleFunction<Double> asLogistic() {
        long P0 = totalInitialPopulation();
        double r = meanLitterSize() * ((mIsGendered) ? mSexRatio : 1.0d );
        long K = mCarryingCapacity;

        return (t) -> { double e = Math.exp(r*t); return (K * P0 * e) / (K + P0 * (e - 1.0d));}
    }

    private class LogisticProgression implements DoubleFunction<Double> {

        @Override
        public Double apply(double value) {
            return null;
        }
    }//*/
}
