package org.rythmengine.internal.generator;


import org.rythmengine.internal.hash.sha1.SHA1;

public final class GeneratedTemplateSource {
    final private String source;
    final private String canonicalName;
    final private String hash;

    public GeneratedTemplateSource(final String canonicalName, final String source) {
        this.source = source;
        this.canonicalName = canonicalName;
        this.hash = SHA1.sha1Hex(source);
    }

    public String getSource() {
        return source;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public String hash() {
        return hash;
    }
}
