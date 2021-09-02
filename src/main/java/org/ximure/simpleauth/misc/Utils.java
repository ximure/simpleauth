package org.ximure.simpleauth.misc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import static org.ximure.simpleauth.SimpleAuth.MESSAGES_YAML;

public class Utils {

    /**
     * Via this method you can retrieve all args length (in symbols, without spaces)
     * @param args  Which args list to count symbols from
     * @return      number of total symbols in all args
     */
    public int getArgsLength(String[] args) {
        int argsLength = 0;
        for (String arg : args) {
            argsLength += arg.length();
        }
        return argsLength;
    }

    /**
     * This method allow retrieving whole string in which there are all args
     * @param args          args to retrieve strings from
     * @param skipFirstArg  skip first arg or not
     * @return              string with all args
     */
    public String getAllArgsString(String[] args, boolean skipFirstArg) {
        StringBuilder builder = new StringBuilder();
        String lastArg = args[args.length - 1];
        for (String arg : args) {
            if (skipFirstArg) {
                skipFirstArg = false;
                continue;
            }
            if (arg.equals(lastArg)) {
                builder.append(arg);
                break;
            }
            builder.append(arg).append(" ");
        }
        return builder.toString();
    }

    /**
     * This method allow retrieving string with stringName from messages.yml config
     * @param stringName    which string to retrieve. All strings are located under /plugins/SimpleAuth/messages.yml
     * @return              string which has been accessed. Null if this string does not exist
     */
    public String getStringFromYml(String stringName) {
        Yaml yaml = new Yaml();
        try {
            InputStream stringsFile = new FileInputStream(MESSAGES_YAML);
            Map<String, Object> data = yaml.load(stringsFile);
            return data.get(stringName).toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method creates a default messages.yml with all messages which plugin uses to display in-game messages
     * @return  true if file has been created, false otherwise
     */
    public Boolean createYmlTemplate() {
        if (!MESSAGES_YAML.exists()) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("SimpleAuth");
            assert plugin != null;
            plugin.saveResource("messages.yml", false);
        }
        return MESSAGES_YAML.exists();
    }
}
