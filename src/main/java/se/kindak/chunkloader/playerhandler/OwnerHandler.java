package se.kindak.chunkloader.playerhandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import se.kindak.chunkloader.ChunkLoader;
import se.kindak.chunkloader.loader.ChunkLoaderNpc;
import se.kindak.chunkloader.playerhandler.util.Cooldown;
import se.kindak.chunkloader.playerhandler.util.Rank;
import se.kindak.chunkloader.util.Settings;
import se.kindak.kindaklib.item.ItemBuilder;
import se.kindak.kindaklib.language.CC;

import java.util.*;

public class OwnerHandler {
    private static OwnerHandler instance;
    private List<Rank> ranks;
    private Set<Owner> owners;
    private Map<UUID, Integer> cooldowns;


    public OwnerHandler() {
        this.ranks = new ArrayList<>();
        this.owners = new HashSet<>();
        this.cooldowns = new HashMap<>();
    }

    public static OwnerHandler getInstance() {
        if (instance == null)
            instance = new OwnerHandler();
        return instance;
    }

    public void setPlayerOnCooldown(ChunkLoaderNpc chunkLoaderNpc) {
        this.cooldowns.put(chunkLoaderNpc.getOwnerUUID(), chunkLoaderNpc.getRank().getCooldown());
        new Cooldown(chunkLoaderNpc.getOwnerUUID()).runTaskTimer(ChunkLoader.getInstance(), 20 * 60, 20 * 60);
    }

    public Map<UUID, Integer> getCooldowns() {
        return cooldowns;
    }

    public void setCooldowns(Map<UUID, Integer> cooldowns) {
        this.cooldowns = cooldowns;
    }

    public Rank getRank(String rankName) {
        try {
            return (Rank) ranks.stream().filter(o1 -> o1.getName().equalsIgnoreCase(rankName)).toArray()[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public Rank getRank(int level) {
        try {
            return (Rank) ranks.stream().filter(o1 -> o1.getLevel() == level).toArray()[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public Owner getOwner(Player player) {
        try {
            return (Owner) owners.stream().filter(o1 -> o1.getPlayer() == player).toArray()[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public void setRanks(List<Rank> ranks) {
        this.ranks = ranks;
    }

    public Set<Owner> getOwners() {
        return owners;
    }

    public void setOwners(Set<Owner> owners) {
        this.owners = owners;
    }

    public void showUpgradeInventory(Owner player) {

        Inventory inventory = Bukkit.createInventory(null, 9 * 6, Settings.getInstance().getUpgradeInventoryTitle());

        ItemBuilder glass = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 4, CC.GREEN + CC.MAGIC + CC.BOLD + "123", new ArrayList<>());

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, glass.build());
        }

        int level = player.getRank().getLevel();
        ItemBuilder rankIcon = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE));
        int x = 0;
        for (Rank rank : ranks) {
            int rankLevel = rank.getLevel();
            if (level > rankLevel)
                rankIcon.getItemStack().setDurability((short) 5);
            else if (level < rankLevel)
                rankIcon.getItemStack().setDurability((short) 14);

            rankIcon.setName(rank.getName());

            ArrayList<String> lore = new ArrayList<>();
            Settings.getInstance()
                    .getUpgradeLore()
                    .forEach(s -> lore.add(ChatColor.translateAlternateColorCodes('&', s.
                            replace("%level%", rank.getLevel() + "").
                            replace("%cooldown%", rank.getCooldown() + "").
                            replace("%minutes%", rank.getMinutes() + "").
                            replace("%radius%", rank.getRadius() + "").
                            replace("%price%", rank.getPrice() + ""))));
            rankIcon.getItemMeta().setLore(lore);
            inventory.setItem(x, rankIcon.build());
            x++;
        }

        player.getPlayer().openInventory(inventory);
    }
}
