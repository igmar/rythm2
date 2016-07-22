package org.rythmengine.internal.compiler;

import org.rythmengine.internal.IHttpContext;

public final class CompiledTemplate {
    private String source;
    private String hash;

    public String path() {
        return "";
    }

    public String hash() {
        return hash;
    }

    public String execute(IHttpContext context) {
        return "";
    }
}
