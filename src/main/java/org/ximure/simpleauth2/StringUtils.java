package org.ximure.simpleauth2;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class StringUtils {

    public String getString(String stringName) {
        try {
            InputStream stringsFile = new FileInputStream("./plugins/SimpleAuth2/messages.yml");
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(stringsFile);
            return data.get(stringName).toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
