package rythmengine.compiled;

import org.rythmengine.template.TemplateBase;
@@IMPORTS@@


public final class @@CLASSNAME@@ extends TemplateBase {
    private StringBuffer sb;

    public @@CLASSNAME@@() {
        sb = new StringBuffer();
    }

    @Override
    public String execute(IHttpContext context) {
        @@FLOW@@
    }

    private void emitAt() {
        sb.append("@");
    }

    @@DRAINS@@
}
