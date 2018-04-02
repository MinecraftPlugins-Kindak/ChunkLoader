package se.kindak.chunkloader.playerhandler.util;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import se.kindak.chunkloader.playerhandler.OwnerHandler;
import se.kindak.kindaklib.language.CC;

import java.util.UUID;

public class Cooldown extends BukkitRunnable {
    private int minutes;
    private UUID player;

    public Cooldown(UUID player) {
        this.minutes = OwnerHandler.getInstance().getCooldowns().get(player);
        this.player = player;
    }

    @Override
    public void run() {
        if (minutes == 0) {
            if (Bukkit.getPlayer(player).isOnline())
                Bukkit.getPlayer(player).sendMessage(CC.GREEN + "Your cooldown has expired!");
            cancel();
            OwnerHandler.getInstance().getCooldowns().remove(player);
            return;
        }

        if (minutes == 1) {
            if (Bukkit.getPlayer(player).isOnline())
                Bukkit.getPlayer(player).sendMessage(CC.GREEN + "You have 1 minute left on your cooldown!");
        }

        minutes--;
        OwnerHandler.getInstance().getCooldowns().put(player, minutes);

    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

}
