package @@PACKAGE@@;

import java.util.Map;
import java.lang.StringBuffer;
import org.rythmengine.IHttpContext;
import org.rythmengine.template.TemplateBase;
import org.rythmengine.exceptions.RythmTemplateRuntimeException;
import org.rythmengine.template.TemplateBase;


@@IMPORTS@@

public final class @@CLASSNAME@@ extends TemplateBase {
    private final StringBuffer sb;
@@VARS@@
    public @@CLASSNAME@@(final IHttpContext httpContext, final Map<String, Object> args) {
        super(httpContext, args);
        this.sb = new StringBuffer();
@@CONSTRUCTOR@@
    }

    @Override
    public String execute() {
@@FLOW@@
        return this.sb.toString();
    }

    private void emitAt() {
        sb.append("@");
    }

@@FUNCTIONS@@
}
/*
@@METADATA@@
*/
