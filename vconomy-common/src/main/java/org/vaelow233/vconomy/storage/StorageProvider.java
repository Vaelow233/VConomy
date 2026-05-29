package org.vaelow233.vconomy.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.vaelow233.vconomy.VConomyPlugin;
import org.vaelow233.vconomy.adapter.VLogger;
import org.vaelow233.vconomy.config.VConomyEconomyConfig;
import org.vaelow233.vconomy.config.VConomyGeneralConfig;
import org.vaelow233.vconomy.storage.executor.H2Executor;
import org.vaelow233.vconomy.storage.executor.MysqlExecutor;
import org.vaelow233.vconomy.storage.executor.PostgresqlExecutor;
import org.vaelow233.vconomy.storage.executor.SqliteExecutor;
import org.vaelow233.vconomy.util.HttpUtil;
import org.vaelow233.vconomy.util.ReflectionUtil;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;

public interface StorageProvider {
    VLogger getVConomyLogger();

    Storage getStorage();

    void setCustomClassLoader(URLClassLoader classLoader);

    void setStorage(Storage storage);

    URLClassLoader getCustomClassLoader();

    default void loadStorage(VConomyPlugin plugin) throws Exception {
        VConomyGeneralConfig.StorageConfig storageConfig = plugin.getGeneralConfig().storage;
        Path directory = plugin.getDataDirectory();
        List<VConomyEconomyConfig> economyConfigs = plugin.getEconomyConfigs();
        HikariConfig config = new HikariConfig();
        for (String key : storageConfig.properties.keySet()) {
            config.addDataSourceProperty(key, storageConfig.properties.get(key));
        }
        DataSource dataSource;
        switch (storageConfig.type) {
            case "mysql": {
                dataSource = loadMysqlStorage(directory, storageConfig);
                config.setDataSource(dataSource);
                setStorage(new Storage(new HikariDataSource(config), economyConfigs, new MysqlExecutor()));
                break;
            }
            case "h2": {
                dataSource = loadH2Storage(directory, storageConfig);
                config.setDataSource(dataSource);
                setStorage(new Storage(new HikariDataSource(config), economyConfigs, new H2Executor()));
                break;
            }
            case "sqlite": {
                dataSource = loadSqliteStorage(directory, storageConfig);
                config.setDataSource(dataSource);
                setStorage(new Storage(new HikariDataSource(config), economyConfigs, new SqliteExecutor()));
                break;
            }
            case "postgresql": {
                dataSource = loadPostgresqlStorage(directory, storageConfig);
                config.setDataSource(dataSource);
                setStorage(new Storage(new HikariDataSource(config), economyConfigs, new PostgresqlExecutor()));
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported storage type: " + storageConfig.type);
            }
        }
        getStorage().init();
    }

    default void closeStorage() throws IOException {
        Storage storage = getStorage();
        if (storage != null) {
            HikariDataSource dataSource = storage.dataSource;
            if (dataSource != null) {
                dataSource.close();
            }
        }
        if (getCustomClassLoader() != null) {
            getCustomClassLoader().close();
        }
    }

    default DataSource loadMysqlStorage(Path directory, VConomyGeneralConfig.StorageConfig storageConfig) throws Exception {
        Path libsDir = directory.resolve("libs");
        getVConomyLogger().info("Downloading MySQL Driver...");
        HttpUtil.downloadIfNotExists(
                "https://repo1.maven.org/maven2/com/google/protobuf/protobuf-java/4.31.1/protobuf-java-4.31.1.jar",
                libsDir,
                "protobuf-java-4.31.1.jar"
        );
        HttpUtil.downloadIfNotExists(
                "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.7.0/mysql-connector-j-9.7.0.jar",
                libsDir,
                "mysql-connector-j-9.7.0.jar"
        );
        getVConomyLogger().info("Loading MySQL Driver...");
        URLClassLoader classLoader = ReflectionUtil.loadJar(
                libsDir.resolve("protobuf-java-4.31.1.jar"),
                libsDir.resolve("mysql-connector-j-9.7.0.jar")
        );
        Object mysqlDataSource = ReflectionUtil.createNoArgInstance(classLoader, "com.mysql.cj.jdbc.MysqlDataSource");
        ReflectionUtil.invokeMethod(mysqlDataSource, "setUser", storageConfig.username);
        ReflectionUtil.invokeMethod(mysqlDataSource, "setPassword", storageConfig.password);
        ReflectionUtil.invokeMethod(mysqlDataSource, "setServerName", storageConfig.host);
        ReflectionUtil.invokeMethod(mysqlDataSource, "setPortNumber", storageConfig.port);
        ReflectionUtil.invokeMethod(mysqlDataSource, "setDatabaseName", storageConfig.database);
        setCustomClassLoader(classLoader);
        return (DataSource) mysqlDataSource;
    }

    default DataSource loadH2Storage(Path directory, VConomyGeneralConfig.StorageConfig storageConfig) throws Exception {
        Path libsDir = directory.resolve("libs");
        getVConomyLogger().info("Downloading H2 Driver...");
        HttpUtil.downloadIfNotExists(
                "https://repo1.maven.org/maven2/com/h2database/h2/2.4.240/h2-2.4.240.jar",
                libsDir,
                "h2-2.4.240.jar"
        );
        getVConomyLogger().info("Loading H2 Driver...");
        URLClassLoader classLoader = ReflectionUtil.loadJar(libsDir.resolve("h2-2.4.240.jar"));
        Object h2DataSource = ReflectionUtil.createNoArgInstance(classLoader, "org.h2.jdbcx.JdbcDataSource");
        ReflectionUtil.invokeMethod(h2DataSource, "setUser", storageConfig.username);
        ReflectionUtil.invokeMethod(h2DataSource, "setPassword", storageConfig.password);
        ReflectionUtil.invokeMethod(h2DataSource, "setUrl", "jdbc:h2:file:" + directory
                .resolve("vconomy").toAbsolutePath());
        setCustomClassLoader(classLoader);
        return (DataSource) h2DataSource;
    }

    default DataSource loadSqliteStorage(Path directory, VConomyGeneralConfig.StorageConfig storageConfig) throws Exception {
        Path libsDir = directory.resolve("libs");
        getVConomyLogger().info("Downloading SQLite Driver...");
        HttpUtil.downloadIfNotExists(
                "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/sqlite-jdbc-3.43.0.0.jar",
                libsDir,
                "sqlite-jdbc-3.43.0.0.jar"
        );
        getVConomyLogger().info("Loading SQLite Driver...");
        URLClassLoader classLoader = ReflectionUtil.loadJar(libsDir.resolve("sqlite-jdbc-3.43.0.0.jar"));
        Object sqliteDataSource = ReflectionUtil.createNoArgInstance(classLoader, "org.sqlite.SQLiteDataSource");
        ReflectionUtil.invokeMethod(sqliteDataSource, "setDatabaseName", storageConfig.database);
        ReflectionUtil.invokeMethod(sqliteDataSource, "setUrl", "jdbc:sqlite:" + directory
                .resolve("vconomy.db").toAbsolutePath());
        setCustomClassLoader(classLoader);
        return (DataSource) sqliteDataSource;
    }

    default DataSource loadPostgresqlStorage(Path directory, VConomyGeneralConfig.StorageConfig storageConfig) throws Exception {
        Path libsDir = directory.resolve("libs");
        getVConomyLogger().info("Downloading PostgreSQL Driver...");
        HttpUtil.downloadIfNotExists(
                "https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.11/postgresql-42.7.11.jar",
                libsDir,
                "postgresql-42.7.11.jar"
        );
        getVConomyLogger().info("Loading PostgreSQL Driver...");
        URLClassLoader classLoader = ReflectionUtil.loadJar(libsDir.resolve("postgresql-42.7.11.jar"));
        Object postgresqlDataSource = ReflectionUtil.createNoArgInstance(classLoader, "org.postgresql.ds.PGSimpleDataSource");
        ReflectionUtil.invokeMethod(postgresqlDataSource, "setUser", storageConfig.username);
        ReflectionUtil.invokeMethod(postgresqlDataSource, "setPassword", storageConfig.password);
        ReflectionUtil.invokeMethod(postgresqlDataSource, "setServerName", storageConfig.host);
        ReflectionUtil.invokeMethod(postgresqlDataSource, "setPortNumber", storageConfig.port);
        ReflectionUtil.invokeMethod(postgresqlDataSource, "setDatabaseName", storageConfig.database);
        setCustomClassLoader(classLoader);
        return (DataSource) postgresqlDataSource;
    }


}
