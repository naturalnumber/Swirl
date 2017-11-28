package Test;

import com.stochasticsystems.swirl.engine.SwirlEngine;
import com.stochasticsystems.swirl.engine.SwirlOutputBundle;
import com.stochasticsystems.swirl.engine.SwirlParameterBuilder;
import com.stochasticsystems.swirl.engine.SwirlParameterBundle;
import com.sun.xml.internal.ws.api.pipe.Engine;

import java.util.Arrays;

public class SpeedTestRun {
    public static void main(String... args) {
        //  Run test
        //int failed = SwirlParameterBuilder.test(System.err);

        //System.out.println("failed: "+failed);

        String plot;


        System.out.println("Default run");

        //  Run Defaults
        SwirlParameterBuilder spb = new SwirlParameterBuilder();
        spb.setDefaults();

        SwirlParameterBundle defaults = spb.build();

        if (spb.validationCode() > 0) System.out.println("Default run validation: " + spb.validationCode());

        SwirlOutputBundle output = new SwirlOutputBundle(defaults);

        long start = System.currentTimeMillis();

        SwirlEngine engine = new SwirlEngine(defaults);

        long[][][][] temp = engine.completeResults();
        if (temp == null) System.err.println("No Data?");
        System.out.println("Data added: "+output.addData(temp));

        System.out.println("Time taken: "+(System.currentTimeMillis()-start));

        System.out.println(Arrays.toString(output.getSummaryMeans()));

        System.out.println(SwirlOutputBundle.toPlot(1, output.getSummaryMeans()));

        System.out.println("Extinction "+(output.getExtinctionPercent()*100)+"%");

        System.out.println();





        System.out.println("Long run");

        spb.setDefaults();

        spb.setNRuns(SwirlEngine.N_RUNS_MAX);

        if (spb.getNRuns() != SwirlEngine.N_RUNS_MAX) System.out.println("NRuns: " + spb.getNRuns());

        spb.setNPeriods(SwirlEngine.N_PERIODS_MAX);

        if (spb.getNPeriods() != SwirlEngine.N_PERIODS_MAX) System.out.println("NPeriods: " + spb.getNPeriods());

        SwirlParameterBundle longRun = spb.build();

        if (spb.validationCode() > 0) System.out.println("Long run validation: " + spb.validationCode());

        SwirlOutputBundle longOutput = new SwirlOutputBundle(longRun);

        start = System.currentTimeMillis();

        SwirlEngine longEngine = new SwirlEngine(longRun);

        long[][][][] longTemp = longEngine.completeResults();
        if (longTemp == null) System.err.println("No Data?");
        System.out.println("Data added: "+longOutput.addData(longTemp));

        System.out.println("Time taken: "+(System.currentTimeMillis()-start));

        System.out.println(Arrays.toString(longOutput.getSummaryMeans()));

        System.out.println(SwirlOutputBundle.toPlot(5, longOutput.getSummaryMeans()));

        System.out.println("Extinction "+(longOutput.getExtinctionPercent()*100)+"%");
        System.out.println("Validity Check: Iterations "+(longOutput.getPopulationData().length));
        System.out.println("Validity Check: Runs "+(longOutput.getPopulationData()[0].length));

        System.out.println();





        System.out.println("Variable run");

        spb.setDefaults();

        spb.setNRuns(SwirlEngine.N_RUNS_DEFAULT/10);

        if (spb.getNRuns() != SwirlEngine.N_RUNS_DEFAULT/10) System.out.println("NRuns: " + spb.getNRuns());

        spb.setNPeriods(SwirlEngine.N_PERIODS_MAX);

        if (spb.getNPeriods() != SwirlEngine.N_PERIODS_MAX) System.out.println("NPeriods: " + spb.getNPeriods());

        spb.setCarryingCapacity(SwirlEngine.CARRYING_CAPACITY_DEFAULT*2);

        if (spb.getCarryingCapacity() != SwirlEngine.CARRYING_CAPACITY_DEFAULT*2) System.out.println("CarryingCapacity: " + spb.getCarryingCapacity());

        spb.setSDCarryingCapacity(SwirlEngine.CARRYING_CAPACITY_DEFAULT/2);

        if (spb.getSDCarryingCapacity() != SwirlEngine.CARRYING_CAPACITY_DEFAULT/2) System.out.println("SDCarryingCapacity: " + spb.getSDCarryingCapacity());

        if (!spb.extrapolateInitialPopulation(2000)) System.out.println("Extrapolate failed...");

        if (spb.validationCode() > 0) System.out.println("Long run validation: " + spb.validationCode());

        SwirlParameterBundle variableRun = spb.build();

        if (variableRun == null) System.out.println("Bad parameters...");

        SwirlOutputBundle variableOutput = new SwirlOutputBundle(variableRun);

        start = System.currentTimeMillis();

        SwirlEngine variableEngine = new SwirlEngine(variableRun);

        long[][][][] variableTemp = variableEngine.completeResults();
        if (variableTemp == null) System.err.println("No Data?");

        System.out.println("Data added: "+variableOutput.addData(variableTemp));

        System.out.println("Time taken: "+(System.currentTimeMillis()-start));

        System.out.println(Arrays.toString(variableOutput.getSummaryMeans()));

        System.out.println(SwirlOutputBundle.toPlot(5, variableOutput.getSummaryMeans()));

        System.out.println("Extinction "+(variableOutput.getExtinctionPercent()*100)+"%");
        System.out.println("Validity Check: Iterations="+(variableOutput.getPopulationData().length));
        System.out.println("Validity Check: Runs="+(variableOutput.getPopulationData()[0].length));//*/

    }
}
