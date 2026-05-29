package org.vaelow233.vconomy.config;

import org.vaelow233.vconomy.util.YamlUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ConfigProvider {
    void saveResource(String path);

    void setGeneralConfig(VConomyGeneralConfig generalConfig);

    VConomyGeneralConfig getGeneralConfig();

    void setLangConfig(VConomyLangConfig langConfig);

    VConomyLangConfig getLangConfig();

    void setEconomyConfigs(List<VConomyEconomyConfig> economyConfigs);

    List<VConomyEconomyConfig> getEconomyConfigs();

    default Optional<VConomyEconomyConfig> getEconomyConfigByName(String balanceType) {
        return getEconomyConfigs()
                .stream()
                .filter(economyConfig -> economyConfig.name.equals(balanceType))
                .findFirst();
    }

    default List<String> getEconomyTypes() {
        return getEconomyConfigs().stream().map(economyConfig -> economyConfig.name).collect(Collectors.toList());
    }

    default void loadConfig(Path directory) throws IOException {
        Path configPath = directory.resolve("config.yml");
        if (!Files.exists(configPath)) {
            saveResource("config.yml");
        }
        InputStream inputStream = Files.newInputStream(configPath.toFile().toPath());
        VConomyGeneralConfig generalConfig = YamlUtil.loadAs(inputStream, VConomyGeneralConfig.class);
        if (generalConfig.storage.properties == null) {
            generalConfig.storage.properties = new LinkedHashMap<>();
        }
        setGeneralConfig(generalConfig);
        Path langPath = directory.resolve("lang").resolve(generalConfig.lang + ".yml");
        if (!Files.exists(langPath)) {
            saveResource("lang/en_us.yml");
        }
        if (!Files.exists(langPath)) {
            throw new IllegalAccessError("Language file not found (lang/" + generalConfig.lang + ".yml)");
        }
        VConomyLangConfig langConfig = YamlUtil.loadAs(Files.newInputStream(langPath), VConomyLangConfig.class);
        setLangConfig(langConfig);
        Path economyPath = directory.resolve("economy");
        if (!Files.exists(economyPath)) {
            saveResource("economy/coin.yml");
        }
        List<VConomyEconomyConfig> configs = new ArrayList<>();
        File[] economyFiles = economyPath.toFile().listFiles();
        if (economyFiles == null) {
            throw new IOException("Failed to get the files of economy directory: " + economyPath);
        }
        for (File file : economyFiles) {
            if (file.isFile()) {
                VConomyEconomyConfig economyConfig = YamlUtil.loadAs(
                        Files.newInputStream(file.toPath()), VConomyEconomyConfig.class);
                configs.add(economyConfig);
            }
        }
        setEconomyConfigs(configs);
    }
}
