package se.kindak.chunkloader.playerhandler;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import se.kindak.chunkloader.loader.ChunkLoaderHandler;
import se.kindak.chunkloader.loader.ChunkLoaderNpc;
import se.kindak.chunkloader.playerhandler.util.Rank;
import se.kindak.kindaklib.playerdata.PlayerData;

import java.util.UUID;

public class Owner extends PlayerData {
    private ChunkLoaderNpc chunkLoaderNpc;
    private Rank rank;

    public Owner(UUID playerUUID, FileConfiguration configuration) {
        super(playerUUID);
        rank = OwnerHandler.getInstance().getRank(configuration.getInt("Players." + playerUUID + ".Rank"));
        if (rank == null)
            rank = OwnerHandler.getInstance().getRanks().get(0);
        initializeChunkloaderNpc();
    }

    private void initializeChunkloaderNpc() {
        ChunkLoaderNpc chunkLoaderNpc = ChunkLoaderHandler.instance().getNPC(this);

        if (chunkLoaderNpc == null)
            chunkLoaderNpc = new ChunkLoaderNpc(this);

        this.setChunkLoaderNpc(chunkLoaderNpc);
    }

    public boolean spawnNPC(Location location) {
        if (!canSpawn())
            return false;
        if (chunkLoaderNpc.getNpc() != null)
            chunkLoaderNpc.kill();

        chunkLoaderNpc.spawn(location);
        return true;
    }

    public boolean canSpawn() {
        if (OwnerHandler.getInstance().getCooldowns().containsKey(this.getPlayerUUID()) && OwnerHandler.getInstance().getCooldowns().get(this.getPlayerUUID()) > 0)
            return false;
        if (chunkLoaderNpc.getNpc() != null)
            return false;

        return true;
    }

    public ChunkLoaderNpc getChunkLoaderNpc() {
        return chunkLoaderNpc;
    }

    private void setChunkLoaderNpc(ChunkLoaderNpc chunkLoaderNpc) {
        this.chunkLoaderNpc = chunkLoaderNpc;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }
}
