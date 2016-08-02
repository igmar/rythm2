package org.rythmengine.internal.compiler;

import org.junit.Test;
import org.rythmengine.RythmEngine;
import org.rythmengine.TestBase;
import org.rythmengine.conf.RythmConfiguration;
import org.rythmengine.conf.RythmEngineMode;

/**
 * Created by igmar on 22/07/16.
 */
public class CompilerTest extends TestBase {
    @Test
    public void simpleCompile() {
        RythmConfiguration configuration = createRythmConfiguration();
        RythmEngine engine = new RythmEngine(configuration);
    }
}
