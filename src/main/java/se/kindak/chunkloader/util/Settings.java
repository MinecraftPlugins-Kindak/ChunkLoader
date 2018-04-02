package se.kindak.chunkloader.util;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import se.kindak.chunkloader.ChunkLoader;

import java.util.List;

public class Settings {
    private static Settings instance;
    private String npcName;
    private String playerCantSpawnNpc, playerCantKillNpc, notEnoughMoney;
    private String playerSpawnNpc;
    private String playerKillNpc;
    private String upgradeBought;
    private String upgradeInventoryTitle;
    private String npcDeathMessage;
    private String cooldownMessage;
    private List<String> upgradeLore;
    private String upgradeToHigh;

    private String reloadMessage;


    public Settings(FileConfiguration configuration) {
        this.npcName = ChatColor.translateAlternateColorCodes('&', configuration.getString("Npc.Npc_Name"));
        this.npcDeathMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("Npc.Npc_Death_Message"));

        this.reloadMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("Lang.Reload_Message"));
        this.playerCantSpawnNpc = ChatColor.translateAlternateColorCodes('&', configuration.getString("Lang.Player_Cant_SpawnNPC"));
        this.playerSpawnNpc = ChatColor.translateAlternateColorCodes('&', configuration.getString("Lang.Player_SpawnNPC"));
        this.playerCantKillNpc = ChatColor.translateAlternateColorCodes('&', configuration.getString("Lang.Player_Cant_KillNpc"));
        this.playerKillNpc = ChatColor.translateAlternateColorCodes('&', configuration.getString("Lang.Player_KillNpc"));
        this.upgradeBought = ChatColor.translateAlternateColorCodes('&', configuration.getString("Lang.Player_Upgrade_Bought"));
        this.notEnoughMoney = ChatColor.translateAlternateColorCodes('&', configuration.getString("Lang.insufficient_Money"));
        this.cooldownMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("Lang.Cooldown_Message"));
        this.upgradeToHigh = ChatColor.translateAlternateColorCodes('&', configuration.getString("Lang.Upgrade_To_High"));

        this.upgradeLore = configuration.getStringList("Upgrade.Lore");
        this.upgradeInventoryTitle = ChatColor.translateAlternateColorCodes('&', configuration.getString("Upgrade.Title"));

    }

    public static Settings getInstance() {
        if (instance == null)
            instance = new Settings(ChunkLoader.getInstance().getSettings());
        return instance;
    }

    public String getNpcDeathMessage() {
        return npcDeathMessage;
    }

    public void setNpcDeathMessage(String npcDeathMessage) {
        this.npcDeathMessage = npcDeathMessage;
    }

    public String getNotEnoughMoney() {
        return notEnoughMoney;
    }

    public void setNotEnoughMoney(String notEnoughMoney) {
        this.notEnoughMoney = notEnoughMoney;
    }

    public String getReloadMessage() {
        return reloadMessage;
    }

    public void setReloadMessage(String reloadMessage) {
        this.reloadMessage = reloadMessage;
    }

    public String getUpgradeBought() {
        return upgradeBought;
    }

    public void setUpgradeBought(String upgradeBought) {
        this.upgradeBought = upgradeBought;
    }

    public String getNpcName() {
        return npcName;
    }

    public void setNpcName(String npcName) {
        this.npcName = npcName;
    }

    public String getPlayerCantSpawnNpc() {
        return playerCantSpawnNpc;
    }

    public void setPlayerCantSpawnNpc(String playerCantSpawnNpc) {
        this.playerCantSpawnNpc = playerCantSpawnNpc;
    }

    public String getPlayerCantKillNpc() {
        return playerCantKillNpc;
    }

    public void setPlayerCantKillNpc(String playerCantKillNpc) {
        this.playerCantKillNpc = playerCantKillNpc;
    }

    public String getPlayerSpawnNpc() {
        return playerSpawnNpc;
    }

    public void setPlayerSpawnNpc(String playerSpawnNpc) {
        this.playerSpawnNpc = playerSpawnNpc;
    }

    public String getCooldownMessage() {
        return cooldownMessage;
    }

    public void setCooldownMessage(String cooldownMessage) {
        this.cooldownMessage = cooldownMessage;
    }

    public static void setInstance(Settings instance) {
        Settings.instance = instance;
    }

    public List<String> getUpgradeLore() {
        return upgradeLore;
    }

    public void setUpgradeLore(List<String> upgradeLore) {
        this.upgradeLore = upgradeLore;
    }

    public String getUpgradeToHigh() {
        return upgradeToHigh;
    }

    public void setUpgradeToHigh(String upgradeToHigh) {
        this.upgradeToHigh = upgradeToHigh;
    }

    public String getPlayerKillNpc() {
        return playerKillNpc;
    }

    public void setPlayerKillNpc(String playerKillNpc) {
        this.playerKillNpc = playerKillNpc;
    }

    public String getUpgradeInventoryTitle() {
        return upgradeInventoryTitle;
    }

    public void setUpgradeInventoryTitle(String upgradeInventoryTitle) {
        this.upgradeInventoryTitle = upgradeInventoryTitle;
    }
}
