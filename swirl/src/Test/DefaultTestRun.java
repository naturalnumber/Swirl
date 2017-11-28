package Test;

import com.stochasticsystems.swirl.engine.SwirlEngine;
import com.stochasticsystems.swirl.engine.SwirlOutputBundle;
import com.stochasticsystems.swirl.engine.SwirlParameterBuilder;
import com.stochasticsystems.swirl.engine.SwirlParameterBundle;

import java.util.Arrays;

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
            int by = 10, last = 0;
            for (int i = 1; i <= defaults.getNRuns()/by; i++) {
                System.out.print("i = "+i);
                engine.iterate(by, null);
                System.out.print(" I = "+engine.iterationsCompleted());
                long[][][][] temp = engine.getData(last);
                if (temp == null) System.err.println("?");
                System.out.println(" added: "+output.addData(temp));
                last = i*by;

                System.out.println(Arrays.toString(output.getSummaryMeans()));

                try {
                    System.in.read();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Do something
            }

            if (engine.iterationsLeft()>0) {
                engine.iterate(engine.iterationsLeft());
                System.out.println("I = "+engine.iterationsCompleted());
                output.addData(engine.getData(last));
                last = engine.iterationsCompleted();

                System.out.println(Arrays.toString(output.getSummaryMeans()));
                // Do something
            }

            engine.complete();

            // Do something
        }//*/

    }
}
