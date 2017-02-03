package org.rythmengine;

import org.apache.commons.io.IOUtils;
import org.rythmengine.conf.RythmConfiguration;
import org.rythmengine.conf.RythmEngineMode;
import org.rythmengine.internal.parser.RythmParser;
import org.rythmengine.internal.parser.TemplateParser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

public class TestBase {
    public InputStream loadTemplate(String template) {
        InputStream tpl = this.getClass().getClassLoader().getResourceAsStream(template);
        if (tpl == null) {
            return null;
        }

        return tpl;
    }

    public RythmParser createParser(InputStream is) {
        try {
            final TemplateParser templateParser = new TemplateParser("unittests", new TestGenerator(), new TestResourceLoader(), is);
            Field field = templateParser.getClass().getDeclaredField("parser");
            field.setAccessible(true);
            return (RythmParser) field.get(templateParser);
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(String.format("Exception : %s", e));
            return null;
        }
    }

    public String loadFile(String file) {
        InputStream tpl = this.getClass().getClassLoader().getResourceAsStream(file);
        if (tpl == null) {
            return "FILE_NOT_FOUND";
        }

        try {
            String content = IOUtils.toString(tpl, "UTF-8");
            return content.replace("\n", "").replace("\r", "");
        } catch (IOException e) {
            return "FILE_NOT_FOUND";
        }
    }

    public String loadFileRaw(String file) {
        InputStream tpl = this.getClass().getClassLoader().getResourceAsStream(file);
        if (tpl == null) {
            return "FILE_NOT_FOUND";
        }

        try {
            return IOUtils.toString(tpl, "UTF-8");
        } catch (IOException e) {
            return "FILE_NOT_FOUND";
        }
    }

    public RythmConfiguration createRythmConfiguration() {
        RythmConfiguration configuration = new RythmConfiguration.Builder()
                .engineMode(RythmEngineMode.DEV)
                .homeDir("/tmp")
                .tempDir("/tmp")
                .writeEnabled(true)
                .build();
        return configuration;
    }

    
}
