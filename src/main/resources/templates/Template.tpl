package rythmengine.compiled;

import java.util.Map;
import java.lang.StringBuffer;
import org.rythmengine.internal.*;
import org.rythmengine.exceptions.RythmTemplateRuntimeException;
import org.rythmengine.template.TemplateBase;


@@IMPORTS@@

public final class @@CLASSNAME@@ extends TemplateBase {
    private final StringBuffer sb;
@@VARS@@
    public @@CLASSNAME@@(final Map<String, Object> args) {
        if (args == null) {
            throw new RythmTemplateRuntimeException("args cannot be null");
        }
        this.sb = new StringBuffer();
@@CONSTRUCTOR@@
    }

    @Override
    public String execute(final IHttpContext context) {
@@FLOW@@
    }

    private void emitAt() {
        sb.append("@");
    }

@@FUNCTIONS@@
}
/*
@@METADATA@@
*/
