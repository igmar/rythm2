package org.rythmengine.template;

import org.rythmengine.internal.IHttpContext;

/**
 * Created by igmar on 31/07/16.
 */
public abstract class TemplateBase {
    public abstract String execute(IHttpContext context);
}
