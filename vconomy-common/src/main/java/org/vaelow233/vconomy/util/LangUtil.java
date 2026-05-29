package org.vaelow233.vconomy.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LangUtil {

    public static String formatFromConfig(String message) {
        return message.replace('&', '§');
    }

    public static String formatFromConfig(String message, Map<String, Object> map) {
        String formattedMessage = message.replace('&', '§');
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            formattedMessage = formattedMessage.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        return formattedMessage;
    }

    public static String formatFromConfig(List<String> messages) {
        return messages.stream().map(LangUtil::formatFromConfig).collect(Collectors.joining("\n"));
    }
}
