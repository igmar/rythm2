package rythmengine.compiled;

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
    public String execute(IHttpContext context) {
@@FLOW@@
    }

    private void emitAt() {
        sb.append("@");
    }

@@FUNCTIONS@@
}
