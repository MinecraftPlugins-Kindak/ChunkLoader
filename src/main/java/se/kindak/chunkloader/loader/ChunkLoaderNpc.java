package se.kindak.chunkloader.loader;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import se.kindak.chunkloader.ChunkLoader;
import se.kindak.chunkloader.loader.util.Lifespan;
import se.kindak.chunkloader.playerhandler.Owner;
import se.kindak.chunkloader.playerhandler.util.Rank;
import se.kindak.chunkloader.util.Settings;
import se.kindak.kindaklib.location.Distance;

import java.util.UUID;

public class ChunkLoaderNpc {
    private Lifespan lifespan;
    private ArmorStand npc;
    private Location location;
    private UUID ownerUUID;
    private Rank rank;
    private int timeLeft;

    public ChunkLoaderNpc(Owner owner) {
        this.rank = owner.getRank();
        this.ownerUUID = owner.getPlayerUUID();
        this.timeLeft = owner.getRank().getMinutes();
        this.location = null;
        this.npc = null;
    }

    public ChunkLoaderNpc(int timeLeft, Rank rank, UUID ownerUUID) {
        this.rank = rank;
        this.timeLeft = timeLeft;
        this.ownerUUID = ownerUUID;
        this.npc = null;
        this.location = null;
    }


    public void spawn(Location location) {
        this.location = location;
        location.getChunk().load();
        this.npc = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        Distance.getBlocksInRadius(location, rank.getRadius()).forEach(block -> {
            if (block.getType() == Material.MOB_SPAWNER) {
                CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
                updateCreatureSpawner(creatureSpawner, -1);
            }
        });
        ChunkLoaderHandler.instance().getAliveChunkLoaderNpcs().add(this);
        lifespan = new Lifespan(this);
        lifespan.runTaskTimer(ChunkLoader.getInstance(), 20 * 60, 20 * 60);
        loadEssentials();
        updateNPC();
    }

    public void kill() {
        this.npc.remove();
        this.lifespan.cancel();
        Distance.getBlocksInRadius(location, rank.getRadius()).forEach(block -> {
            if (block.getType() == Material.MOB_SPAWNER) {
                CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
                updateCreatureSpawner(creatureSpawner, 16);
            }
        });
        ChunkLoaderHandler.instance().getAliveChunkLoaderNpcs().remove(this);
        this.timeLeft = rank.getMinutes();
        this.location = null;
        this.npc = null;
    }

    private void loadEssentials() {
        npc.setInvulnerable(true);
        npc.setGravity(false);
        npc.setVisible(false);
        npc.setCustomNameVisible(true);
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void updateNPC() {
        String ownerName = Bukkit.getPlayer(ownerUUID) != null ? Bukkit.getPlayer(ownerUUID).getName() : Bukkit.getOfflinePlayer(ownerUUID).getName();
        npc.setCustomName(Settings.getInstance().getNpcName()
                .replace("%owner%", ownerName)
                .replace("%time%", timeLeft + ""));
    }

    public ArmorStand getNpc() {
        return npc;
    }

    public void setNpc(ArmorStand npc) {
        this.npc = npc;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    private void updateCreatureSpawner(CreatureSpawner creatureSpawner, int range) {
        creatureSpawner.setRequiredPlayerRange(range);
        creatureSpawner.update(true);
    }
}
