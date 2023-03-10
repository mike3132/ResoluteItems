package net.resolutemc.resoluteitems.Talismans.EventManager;

import net.resolutemc.resoluteitems.Main;
import net.resolutemc.resoluteitems.MessageManager.ChatMessages;
import net.resolutemc.resoluteitems.Talismans.HashSets.PoseidonTalismanSet;
import net.resolutemc.resoluteitems.Talismans.HashSets.SunTalismanSet;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PoseidonTalismanEvents implements Listener {

    NamespacedKey key = new NamespacedKey(Main.plugin, "Poseidon-Talisman-Key");

    @EventHandler
    public void onWalk(PlayerMoveEvent pme) {
        Player player = pme.getPlayer();
        if (PoseidonTalismanSet.getPoseidonTalismanPlayers().contains(player.getUniqueId())) {
            player.sendMessage("You are in the Poseidon set");
        }
    }

    // Everything below is general talisman events.

    @EventHandler
    public void onPlayerHold(PlayerSwapHandItemsEvent ph) {
        Player player = ph.getPlayer();

        if (player.getInventory().getItemInMainHand().getType().equals(Material.PLAYER_HEAD)) {
            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                if (!PoseidonTalismanSet.getPoseidonTalismanPlayers().contains(player.getUniqueId())) {
                    PoseidonTalismanSet.onTalismanEnable(player);
                }
            }
        }

        if (player.getInventory().getItemInOffHand().getType().equals(Material.PLAYER_HEAD)) {
            if (player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                if (PoseidonTalismanSet.getPoseidonTalismanPlayers().contains(player.getUniqueId())) {
                    PoseidonTalismanSet.onTalismanDisable(player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent ice) {
        Player player = (Player) ice.getWhoClicked();
        ItemStack item = ice.getCurrentItem();
        int slotNumber = ice.getSlot();
        ItemStack cursor = ice.getCursor();

        if (slotNumber != 40) return;
        if (cursor.getType().equals(Material.PLAYER_HEAD)) {
            if (!item.getType().equals(Material.PLAYER_HEAD)) return;
            if (item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                if (PoseidonTalismanSet.getPoseidonTalismanPlayers().contains(player.getUniqueId())) {
                    PoseidonTalismanSet.onTalismanDisable(player);
                }
            }
            return;
        }

        if (cursor.getType().equals(Material.PLAYER_HEAD)) {
            if (cursor.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                if (!PoseidonTalismanSet.getPoseidonTalismanPlayers().contains(player.getUniqueId())) {
                    PoseidonTalismanSet.onTalismanEnable(player);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent bpe) {
        Player player = bpe.getPlayer();
        Block block = bpe.getBlock();

        if (block.getType().equals(Material.PLAYER_HEAD)) {
            if (PoseidonTalismanSet.getPoseidonTalismanPlayers().contains(player.getUniqueId())) {
                if (!player.getInventory().getItemInOffHand().getType().equals(Material.PLAYER_HEAD)) return;
                if (player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    ChatMessages.sendPlayerMessage(player, "Player-Cannot-Place-Talisman-Placeholder");
                    bpe.setCancelled(true);
                }
            }

            if (!player.getInventory().getItemInMainHand().getType().equals(Material.PLAYER_HEAD)) return;
            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                ChatMessages.sendPlayerMessage(player, "Player-Cannot-Place-Talisman-Placeholder");
                bpe.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void playerClickAnvil(InventoryClickEvent ice) {
        Player player = (Player) ice.getWhoClicked();
        Inventory blockInventory = ice.getClickedInventory();
        ItemStack item = ice.getCurrentItem();

        if (blockInventory == null) return;
        if (blockInventory.getType().equals(InventoryType.ANVIL)) {
            if (ice.getSlot() == 2) {
                if (item.getItemMeta() == null) return;
                if (item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    ice.setCancelled(true);
                    ChatMessages.sendPlayerMessage(player, "Anvil-disabled-message");
                    player.playSound(player, Sound.BLOCK_ANVIL_DESTROY, 1, 1);
                }
            }
        }
    }
}
