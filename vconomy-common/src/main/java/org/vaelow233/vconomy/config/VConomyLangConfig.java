package org.vaelow233.vconomy.config;

import java.util.List;

public class VConomyLangConfig {

    public Help help;

    public String noPermission;
    public String invalidArguments;
    public String sqlException;
    public String invalidBalanceType;
    public String invalidBalance;
    public String onlyPlayerCanExecute;
    public String operationSuccessful;
    public String balanceShow;
    public String othersBalanceShow;
    public String playerNotFound;
    public String notEnoughBalance;
    public String cannotPayYourself;
    public String exception;
    public String reloading;

    public BalanceTop balanceTop;

    public static class Help {
        public List<String> admin;
        public List<String> player;
    }

    public static class BalanceTop {
        public String title;
        public String line;
        public String footer;
    }
}
