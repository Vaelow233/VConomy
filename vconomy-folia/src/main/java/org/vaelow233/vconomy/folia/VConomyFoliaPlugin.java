package org.vaelow233.vconomy.folia;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.vaelow233.vconomy.adapter.VPlayer;
import org.vaelow233.vconomy.bukkit.VConomyBukkitPlugin;
import org.vaelow233.vconomy.bukkit.adapter.SpigotPlayer;

import java.util.List;
import java.util.stream.Collectors;

public class VConomyFoliaPlugin extends VConomyBukkitPlugin {

    private volatile List<VPlayer> onlinePlayersCache = List.of();
    private ScheduledTask cacheRefreshTask;

    @Override
    public void onEnable() {
        super.onEnable();
        startOnlinePlayersCache();
    }

    @Override
    public void onDisable() {
        stopOnlinePlayersCache();
        super.onDisable();
    }

    private void startOnlinePlayersCache() {
        onlinePlayersCache = getServer().getOnlinePlayers().stream()
                .map(SpigotPlayer::new)
                .collect(Collectors.toUnmodifiableList());

        cacheRefreshTask = Bukkit.getGlobalRegionScheduler().runAtFixedRate(
                this,
                task -> onlinePlayersCache = getServer().getOnlinePlayers().stream()
                        .map(SpigotPlayer::new)
                        .collect(Collectors.toUnmodifiableList()),
                20L, 20L
        );
    }

    private void stopOnlinePlayersCache() {
        if (cacheRefreshTask != null && !cacheRefreshTask.isCancelled()) {
            cacheRefreshTask.cancel();
        }
    }

    @Override
    public List<VPlayer> getOnlinePlayers() {
        return onlinePlayersCache;
    }
}
