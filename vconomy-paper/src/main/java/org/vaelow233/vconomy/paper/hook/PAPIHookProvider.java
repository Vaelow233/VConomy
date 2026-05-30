package org.vaelow233.vconomy.paper.hook;

import org.vaelow233.vconomy.paper.hook.papi.VConomyPAPIExpansion;
import org.vaelow233.vconomy.paper.VConomyPaperPlugin;

public interface PAPIHookProvider {

    default void loadPAPIHook(VConomyPaperPlugin plugin) {
        new VConomyPAPIExpansion(plugin).register();
    }
}
