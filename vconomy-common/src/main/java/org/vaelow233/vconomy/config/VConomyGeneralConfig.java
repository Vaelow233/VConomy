package org.vaelow233.vconomy.config;

import java.util.List;
import java.util.Map;

public class VConomyGeneralConfig {

    public String lang;

    public StorageConfig storage;
    public CommandConfig command;
    public HookConfig hook;

    public static class StorageConfig {
        public String type;
        public String host;
        public int port;
        public String database;
        public String username;
        public String password;
        public Map<String, String> properties;
    }

    public static class CommandConfig {
        public AliasesConfig aliases;

        public static class AliasesConfig {
            public List<String> vconomy;
            public List<String> balance;
            public List<String> balancetop;
            public List<String> pay;
        }
    }

    public static class HookConfig {
        public VaultHookConfig vault;

        public static class VaultHookConfig {
            public String type;
        }
    }
}
