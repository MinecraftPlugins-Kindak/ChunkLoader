package se.kindak.chunkloader.loader;

import org.bukkit.Chunk;
import se.kindak.chunkloader.playerhandler.Owner;

import java.util.HashSet;
import java.util.Set;

public class ChunkLoaderHandler {
    private static ChunkLoaderHandler instance;
    private Set<ChunkLoaderNpc> aliveChunkLoaderNpcs;

    public ChunkLoaderHandler() {
        this.aliveChunkLoaderNpcs = new HashSet<>();
    }

    public static ChunkLoaderHandler instance() {
        if (instance == null)
            instance = new ChunkLoaderHandler();
        return instance;
    }


    public ChunkLoaderNpc getNPC(Chunk chunk) {
        try {
            return (ChunkLoaderNpc) aliveChunkLoaderNpcs.stream().filter(npc -> npc.getLocation().getChunk() == chunk).toArray()[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public ChunkLoaderNpc getNPC(Owner owner) {
        for (ChunkLoaderNpc npc : aliveChunkLoaderNpcs) {
            if (npc.getOwnerUUID().toString().equalsIgnoreCase(owner.getPlayerUUID().toString()))
                return npc;

        }
        return null;
    }

    public Set<ChunkLoaderNpc> getAliveChunkLoaderNpcs() {
        return aliveChunkLoaderNpcs;
    }

    public void setAliveChunkLoaderNpcs(Set<ChunkLoaderNpc> aliveChunkLoaderNpcs) {
        this.aliveChunkLoaderNpcs = aliveChunkLoaderNpcs;
    }
}
