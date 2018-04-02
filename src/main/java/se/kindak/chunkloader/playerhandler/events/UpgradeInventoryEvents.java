package se.kindak.chunkloader.playerhandler.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import se.kindak.chunkloader.ChunkLoader;
import se.kindak.chunkloader.playerhandler.Owner;
import se.kindak.chunkloader.playerhandler.OwnerHandler;
import se.kindak.chunkloader.playerhandler.util.Rank;
import se.kindak.chunkloader.util.Settings;

public class UpgradeInventoryEvents implements Listener {


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null)
            return;
        if (event.getCurrentItem() == null)
            return;
        if (!inventory.getTitle().equalsIgnoreCase(Settings.getInstance().getUpgradeInventoryTitle()))
            return;

        ItemStack clickedItem = event.getCurrentItem();
        Owner owner = OwnerHandler.getInstance().getOwner((Player) event.getWhoClicked());
        if (clickedItem.getDurability() == (short) 14) {
            Rank rank = OwnerHandler.getInstance().getRank(clickedItem.getItemMeta().getDisplayName());

            if (ChunkLoader.getEco().getBalance(owner.getPlayer()) < rank.getPrice()) {
                owner.getPlayer().sendMessage(Settings.getInstance().getNotEnoughMoney()
                        .replace("%rank%", rank.getName())
                        .replace("%price%", rank.getPrice() + "")
                        .replace("%currentmoney%", ChunkLoader.getEco().getBalance(owner.getPlayer()) + ""));
                event.setCancelled(true);
                ((Player) event.getWhoClicked()).updateInventory();
                event.getWhoClicked().closeInventory();
                return;
            }
            if (owner.getRank().getLevel() + 1 != rank.getLevel()) {
                owner.getPlayer().sendMessage(Settings.getInstance().getUpgradeToHigh());
                event.setCancelled(true);
                ((Player) event.getWhoClicked()).updateInventory();
                event.getWhoClicked().closeInventory();
                return;
            }

            owner.setRank(rank);
            owner.getPlayer().sendMessage(Settings.getInstance().getUpgradeBought()
                    .replace("%rank%", rank.getName())
                    .replace("%price%", rank.getPrice() + "")
                    .replace("%currentmoney%", ChunkLoader.getEco().getBalance(owner.getPlayer()) + ""));
            event.getWhoClicked().closeInventory();
        }
        event.setCancelled(true);
        ((Player) event.getWhoClicked()).updateInventory();
    }
}
