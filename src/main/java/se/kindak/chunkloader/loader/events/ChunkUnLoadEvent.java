package se.kindak.chunkloader.loader.events;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import se.kindak.chunkloader.loader.ChunkLoaderHandler;

public class ChunkUnLoadEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();

        if (ChunkLoaderHandler.instance().getNPC(chunk) != null)
            event.setCancelled(true);

    }
}
