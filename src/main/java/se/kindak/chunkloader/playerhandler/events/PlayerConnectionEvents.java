package se.kindak.chunkloader.playerhandler.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import se.kindak.chunkloader.ChunkLoader;
import se.kindak.chunkloader.playerhandler.Owner;
import se.kindak.chunkloader.playerhandler.OwnerHandler;

public class PlayerConnectionEvents implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Owner owner = new Owner(player.getUniqueId(), ChunkLoader.getInstance().getPlayers());
        OwnerHandler.getInstance().getOwners().add(owner);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Owner owner = OwnerHandler.getInstance().getOwner(player);
        ChunkLoader.getInstance().getPlayers().set("Players." + owner.getPlayerUUID() + ".Rank", owner.getRank().getLevel());
        OwnerHandler.getInstance().getOwners().remove(owner);
    }
}
