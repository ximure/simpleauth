package org.ximure.simpleauth2;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import static org.ximure.simpleauth2.SimpleAuth2.MESSAGES_YAML;

public class MessagesUtils {
    Yaml yaml = new Yaml();

    public String getString(String stringName) {
        try {
            InputStream stringsFile = new FileInputStream(MESSAGES_YAML);
            Map<String, Object> data = yaml.load(stringsFile);
            return data.get(stringName).toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean createTemplate() {
        // TODO: yaml template creating
        return true;
    }
}
