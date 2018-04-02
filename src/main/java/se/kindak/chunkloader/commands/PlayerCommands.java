package se.kindak.chunkloader.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import se.kindak.chunkloader.ChunkLoader;
import se.kindak.chunkloader.playerhandler.Owner;
import se.kindak.chunkloader.playerhandler.OwnerHandler;
import se.kindak.chunkloader.util.Settings;
import se.kindak.kindaklib.language.CC;

@CommandAlias("cl|chunkloader|cloader")
public class PlayerCommands extends BaseCommand {
    private Owner owner;

    @CatchUnknown
    @HelpCommand
    public static void onHelp(CommandHelp helpCommand) {
        helpCommand.showHelp();
    }

    @Subcommand("reload|rl")
    @CommandPermission("Chunkloader.reload")
    public void onReload(Player sender) {
        sender.sendMessage(Settings.getInstance().getReloadMessage());
        ChunkLoader.getInstance().reloadConfigs();
    }

    @Subcommand("cooldown|cd")
    public void showCooldown(Player sender) {
        Integer time = OwnerHandler.getInstance().getCooldowns().get(sender.getUniqueId());
        if (time == null)
            time = 0;
        sender.sendMessage(Settings.getInstance().getCooldownMessage().replace("%time%", time + ""));
    }

    @Subcommand("upgrade")
    public void onUpgrade() {
        OwnerHandler.getInstance().showUpgradeInventory(owner);
    }

    @Subcommand("spawn")
    public void onSpawn(Player sender) {
        if (!owner.canSpawn()) {
            owner.getPlayer().sendMessage(Settings.getInstance().getPlayerCantSpawnNpc());
            return;
        }
        owner.spawnNPC(sender.getLocation());
        owner.getPlayer().sendMessage(Settings.getInstance().getPlayerSpawnNpc());
    }

    @Subcommand("kill")
    public void onKill(Player sender) {
        if (owner.getChunkLoaderNpc().getNpc() == null) {
            owner.getPlayer().sendMessage(Settings.getInstance().getPlayerCantKillNpc());
            return;
        }
        owner.getChunkLoaderNpc().kill();
        owner.getPlayer().sendMessage(Settings.getInstance().getPlayerKillNpc());
        OwnerHandler.getInstance().setPlayerOnCooldown(owner.getChunkLoaderNpc());
    }

    @PreCommand
    public boolean preCommand() {
        if (!this.getCurrentCommandIssuer().isPlayer()) {
            this.getCurrentCommandIssuer().sendMessage(CC.RED + CC.BOLD + "You have to be player to issue thease commands!");
            return true;
        }
        owner = OwnerHandler.getInstance().getOwner(Bukkit.getPlayer(this.getCurrentCommandIssuer().getUniqueId()));
        return false;
    }

}
