package se.kindak.chunkloader.loader.util;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import se.kindak.chunkloader.loader.ChunkLoaderNpc;
import se.kindak.chunkloader.playerhandler.OwnerHandler;
import se.kindak.chunkloader.util.Settings;

public class Lifespan extends BukkitRunnable {
    private ChunkLoaderNpc chunkLoaderNpc;

    public Lifespan(ChunkLoaderNpc chunkLoaderNpc) {
        this.chunkLoaderNpc = chunkLoaderNpc;
    }

    @Override
    public void run() {
        chunkLoaderNpc.setTimeLeft(chunkLoaderNpc.getTimeLeft() - 1);
        chunkLoaderNpc.updateNPC();

        if(chunkLoaderNpc.getNpc().isDead())
            chunkLoaderNpc.spawn(chunkLoaderNpc.getLocation());


        if (chunkLoaderNpc.getTimeLeft() == 0) {
            chunkLoaderNpc.kill();
            if (Bukkit.getPlayer(chunkLoaderNpc.getOwnerUUID()) != null)
                Bukkit.getPlayer(chunkLoaderNpc.getOwnerUUID()).sendMessage(Settings.getInstance().getNpcDeathMessage());
            OwnerHandler.getInstance().setPlayerOnCooldown(chunkLoaderNpc);
            this.cancel();
        }
    }


    public ChunkLoaderNpc getChunkLoaderNpc() {
        return chunkLoaderNpc;
    }

    public void setChunkLoaderNpc(ChunkLoaderNpc chunkLoaderNpc) {
        this.chunkLoaderNpc = chunkLoaderNpc;
    }
}
