package Test;

import java.util.Arrays;

import Core.SwirlEngine;
import Core.SwirlOutputBundle;
import Core.SwirlParameterBuilder;
import Core.SwirlParameterBundle;

public class DefaultTestRun {
    public static void main(String... args) {
        //  Run test
        //int failed = SwirlParameterBuilder.test(System.err);

        //System.out.println("failed: "+failed);


        //  Run Defaults
        SwirlParameterBuilder spb = new SwirlParameterBuilder();
        spb.setDefaults();

        SwirlParameterBundle defaults = spb.build();

        SwirlOutputBundle output = new SwirlOutputBundle(defaults);

        SwirlEngine engine = new SwirlEngine(defaults);

        engine.initialize();

        if (engine.isInitialized()) {
            int last = 0;
            for (int i = 1; i < defaults.getNRuns()/10; i++) {
                System.out.print("i = "+i);
                engine.iterate(i*10);
                System.out.println(" I = "+engine.iterationsCompleted());
                output.addData(engine.getData(last));
                last = i*10;

                System.out.println(Arrays.toString(output.getPopulationMeans()[last][0]));
                System.out.println(Arrays.toString(output.getPopulationMeans()[last][1]));
                // Do something
            }

            if (engine.iterationsLeft()>0) {
                engine.iterate(engine.iterationsLeft());
                System.out.println("I = "+engine.iterationsCompleted());
                output.addData(engine.getData(last));
                last = engine.iterationsCompleted();

                System.out.println(Arrays.toString(output.getPopulationMeans()[last][0]));
                System.out.println(Arrays.toString(output.getPopulationMeans()[last][1]));
                // Do something
            }

            engine.complete();

            // Do something
        }//*/

    }
}
