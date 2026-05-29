package org.vaelow233.vconomy.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class HttpUtil {
    public static void downloadIfNotExists(String url, Path to, String filename) throws IOException {
        Path target = to.resolve(filename);
        if (Files.exists(target)) {
            return;
        }

        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        try {
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Failed to download " + url + " (" + responseCode + "): "
                        + connection.getResponseMessage());
            }

            Files.createDirectories(to);
            try (InputStream inputStream = connection.getInputStream()) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            connection.disconnect();
        }
    }
}
