package se.kindak.chunkloader;

import co.aikar.commands.BukkitCommandManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import se.kindak.chunkloader.commands.PlayerCommands;
import se.kindak.chunkloader.loader.ChunkLoaderHandler;
import se.kindak.chunkloader.loader.ChunkLoaderNpc;
import se.kindak.chunkloader.loader.events.ChunkUnLoadEvent;
import se.kindak.chunkloader.playerhandler.Owner;
import se.kindak.chunkloader.playerhandler.OwnerHandler;
import se.kindak.chunkloader.playerhandler.events.PlayerConnectionEvents;
import se.kindak.chunkloader.playerhandler.events.UpgradeInventoryEvents;
import se.kindak.chunkloader.playerhandler.util.Rank;
import se.kindak.chunkloader.util.Settings;
import se.kindak.kindaklib.language.CC;
import se.kindak.kindaklib.location.LocationFormater;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.UUID;

public final class ChunkLoader extends JavaPlugin {
    private static ChunkLoader instance;
    private static Economy eco = null;

    private File tempNPCF, playersF, settingsF, ranksF;
    private FileConfiguration tempNPC, players, settings, ranks;

    public static ChunkLoader getInstance() {
        return instance;
    }

    public static Economy getEco() {
        return eco;
    }

    public File getTempNPCF() {
        return tempNPCF;
    }

    public File getPlayersF() {
        return playersF;
    }

    public File getSettingsF() {
        return settingsF;
    }

    public FileConfiguration getTempNPC() {
        return tempNPC;
    }

    public FileConfiguration getPlayers() {
        return players;
    }

    public FileConfiguration getSettings() {
        return settings;
    }

    @Override
    public void onEnable() {
        instance = this;
        setupConfigs();
        setupRanks();
        setupEconomy();

        Settings.getInstance();

        loadEvents();
        loadCommands();
        loadNPCS();
        loadPlayers();
    }

    @Override
    public void onDisable() {
        saveTempNpcs();
        savePlayers();
        saveConfigs();
    }

    private void saveConfigs() {
        try {
            this.players.save(playersF);
            this.tempNPC.save(tempNPCF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupRanks() {
        for (String key : ranks.getKeys(false)) {
            Rank rank = new Rank(ranks, key, Integer.parseInt(key));
            OwnerHandler.getInstance().getRanks().add(rank);
        }
        OwnerHandler.getInstance().getRanks().sort(Comparator.comparing(rank -> rank.getLevel()));
    }

    public void log(String msg) {
        String prefix = CC.D_GRAY + CC.BOLD + "ChunkLoader: ";
        Bukkit.getConsoleSender().sendMessage(prefix + msg);
    }

    private void loadEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ChunkUnLoadEvent(), this);
        pm.registerEvents(new UpgradeInventoryEvents(), this);
        pm.registerEvents(new PlayerConnectionEvents(), this);
    }

    private void setupConfigs() {
        this.tempNPCF = new File(getDataFolder(), "TempNpc.yml");
        this.playersF = new File(getDataFolder(), "Players.yml");
        this.settingsF = new File(getDataFolder(), "Settings.yml");
        this.ranksF = new File(getDataFolder(), "Ranks.yml");


        if (!this.tempNPCF.exists()) {
            this.tempNPCF.getParentFile().mkdirs();
            saveResource("TempNpc.yml", false);
        }

        if (!this.ranksF.exists()) {
            this.ranksF.getParentFile().mkdirs();
            saveResource("Ranks.yml", false);
        }


        if (!this.playersF.exists()) {
            this.playersF.getParentFile().mkdirs();
            saveResource("Players.yml", false);
        }

        if (!this.settingsF.exists()) {
            this.settingsF.getParentFile().mkdirs();
            saveResource("Settings.yml", false);
        }

        this.tempNPC = new YamlConfiguration();
        this.players = new YamlConfiguration();
        this.settings = new YamlConfiguration();
        this.ranks = new YamlConfiguration();

        try {
            this.tempNPC.load(tempNPCF);
            this.players.load(playersF);
            this.ranks.load(ranksF);
            this.settings.load(settingsF);
        } catch (IOException | InvalidConfigurationException e) {
            log(e.getMessage());
        }
    }

    public void loadNPCS() {
        if (tempNPC.getKeys(false).isEmpty()) {
            return;
        }

        for (String key : tempNPC.getKeys(false)) {
            Location location = LocationFormater.format(tempNPC.getString(key + ".Location"));
            Rank rank = OwnerHandler.getInstance().getRank(tempNPC.getInt(key + ".Rank"));
            int timeleft = tempNPC.getInt(key + ".TimeLeft");
            UUID owner = UUID.fromString(tempNPC.getString(key + ".Owner"));

            ChunkLoaderNpc chunkLoaderNpc = new ChunkLoaderNpc(timeleft, rank, owner);
            chunkLoaderNpc.spawn(location);
        }
        System.out.print(ChunkLoaderHandler.instance().getAliveChunkLoaderNpcs().size());

        this.tempNPC = new YamlConfiguration();
    }

    private void saveTempNpcs() {
        if (ChunkLoaderHandler.instance().getAliveChunkLoaderNpcs().isEmpty())
            return;

        for (ChunkLoaderNpc chunkLoaderNpc : ChunkLoaderHandler.instance().getAliveChunkLoaderNpcs()) {
            tempNPC.set(chunkLoaderNpc.getOwnerUUID() + ".TimeLeft", chunkLoaderNpc.getTimeLeft());
            tempNPC.set(chunkLoaderNpc.getOwnerUUID() + ".Location", LocationFormater.format(chunkLoaderNpc.getLocation()));
            tempNPC.set(chunkLoaderNpc.getOwnerUUID() + ".Owner", chunkLoaderNpc.getOwnerUUID().toString());
            tempNPC.set(chunkLoaderNpc.getOwnerUUID() + ".Rank", chunkLoaderNpc.getRank().getLevel());
            chunkLoaderNpc.kill();
        }
    }

    private void savePlayers() {
        for (Owner owner : OwnerHandler.getInstance().getOwners())
            players.set("Players." + owner.getPlayerUUID() + ".Rank", owner.getRank().getLevel());
    }

    private void loadCommands() {
        BukkitCommandManager manager = new BukkitCommandManager(instance);
        manager.registerCommand(new PlayerCommands());
        manager.enableUnstableAPI("help");
    }

    private void loadPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Owner owner = new Owner(player.getUniqueId(), players);
            OwnerHandler.getInstance().getOwners().add(owner);
        }
    }

    public File getRanksF() {
        return ranksF;
    }

    public FileConfiguration getRanks() {
        return ranks;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }

    public void reloadConfigs() {
        try {
            this.ranks.load(ranksF);
            this.settings.load(settingsF);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        OwnerHandler.getInstance().getRanks().clear();
        setupRanks();
        Settings.setInstance(new Settings(this.settings));
    }
}
