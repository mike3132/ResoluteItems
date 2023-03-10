package net.resolutemc.resoluteitems.Talismans.EventManager;

import net.resolutemc.resoluteitems.MessageManager.ActionBarMessages;
import net.resolutemc.resoluteitems.MessageManager.ChatMessages;
import net.resolutemc.resoluteitems.Main;
import net.resolutemc.resoluteitems.Talismans.HashSets.SunTalismanSet;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class SunTalismanEvents implements Listener {

    NamespacedKey key = new NamespacedKey(Main.plugin, "Sun-Talisman-Key");
    private static final Map<UUID, Integer> coolDown = new HashMap<>();


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent pme) {
        Player player = pme.getPlayer();
        World world = player.getWorld();
        Location origin = player.getLocation();

        if (player.getInventory().getItemInOffHand().getType().equals(Material.PLAYER_HEAD)) {
            if (player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                if (world.getTime() < 12000) return;
                int randomX = ThreadLocalRandom.current().nextInt(origin.getBlockX() - 5, origin.getBlockX() + 5);
                int randomZ = ThreadLocalRandom.current().nextInt(origin.getBlockZ() - 5, origin.getBlockZ() + 5);
                Block randomBlock = player.getWorld().getBlockAt(randomX, player.getLocation().getBlockY(), randomZ);
                if (randomBlock.getType() != Material.AIR && randomBlock.getType() != Material.LIGHT) return;
                player.sendBlockChange(randomBlock.getLocation(), Material.LIGHT.createBlockData());
            }
        }

        if (player.getInventory().getItemInMainHand().getType().equals(Material.PLAYER_HEAD)) {
            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                if (world.getTime() < 12000 && world.getTime() > 0) {
                    if (player.getGameMode() != GameMode.SURVIVAL) return;
                    if (player.getHealth() > 0) {
                        player.damage(1);
                        player.spawnParticle(Particle.LAVA, player.getLocation(), 10);
                    }
                }
            }
        }
    }

    // How long the solar flare lasts

    private static final int DEFAULTCOOLDOWN = 5;

    private static void setCoolDown(UUID player, int time) {
        if (time < 1) {
            coolDown.remove(player);
        } else {
            coolDown.put(player, time);
        }
    }

    private static int getCooldown(UUID player) {
        return coolDown.getOrDefault(player, 0);
    }

    private static void timerActivate(Player player) {
        int timeLeft = getCooldown(player.getUniqueId());
        if (timeLeft == 0) {
            ActionBarMessages.sendPlayerActionBar(player, "Solar-Flare-Activate");
            setCoolDown(player.getUniqueId(), DEFAULTCOOLDOWN);
            new BukkitRunnable() {
                @Override
                public void run() {
                    int timeLeft = getCooldown(player.getUniqueId());
                    setCoolDown(player.getUniqueId(), --timeLeft);
                    if (timeLeft == 0) {
                        this.cancel();
                        ActionBarMessages.sendPlayerActionBar(player, "Solar-Flare-Deactivate");
                        SunTalismanSet.removeBarPlayer(player);
                    }
                }
            }.runTaskTimer(Main.plugin, 20L, 20L);
        }
    }

    @EventHandler
    public void onPlayerCrouch(PlayerToggleSneakEvent pc) {
        Player player = pc.getPlayer();

        if (!player.getInventory().getItemInMainHand().getType().equals(Material.PLAYER_HEAD)) return;
        if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            if (player.isSneaking()) return;
            if (SunTalismanSet.getTalismanCooldown().containsKey(player.getUniqueId())) {
                pc.setCancelled(true);
                ActionBarMessages.sendPlayerActionBar(player, "Solar-Flare-Ability-On-Cooldown");
                return;
            }
            for (Entity nearby : player.getNearbyEntities(25, 25, 25)) {
                if (nearby instanceof ArmorStand) return;
                if (nearby instanceof LivingEntity) {
                    LivingEntity entity = (LivingEntity) nearby;
                    SunTalismanSet.onAiDisable(entity);
                }
            }
            timerActivate(player);
            SunTalismanSet.addBarPlayer(player);
            SunTalismanSet.addTalismanCooldown(player);
            SunTalismanSet.particleRun(player);
            player.playSound(player.getLocation(), Sound.BLOCK_BELL_RESONATE, 1, 1.6F);
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 2, 1.4F);
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, 1.5F);

            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1, false, false, false));
        }
    }


    // Everything below is general talisman events.

    @EventHandler
    public void onPlayerHold(PlayerSwapHandItemsEvent ph) {
        Player player = ph.getPlayer();

        if (player.getInventory().getItemInMainHand().getType().equals(Material.PLAYER_HEAD)) {
            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                if (!SunTalismanSet.getSunTalismanPlayers().contains(player.getUniqueId())) {
                    SunTalismanSet.onTalismanEnable(player);
                }
            }
        }

        if (player.getInventory().getItemInOffHand().getType().equals(Material.PLAYER_HEAD)) {
            if (player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                if (SunTalismanSet.getSunTalismanPlayers().contains(player.getUniqueId())) {
                    SunTalismanSet.onTalismanDisable(player);
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
                if (SunTalismanSet.getSunTalismanPlayers().contains(player.getUniqueId())) {
                    SunTalismanSet.onTalismanDisable(player);
                }
            }
            return;
        }

        if (cursor.getType().equals(Material.PLAYER_HEAD)) {
            if (cursor.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                if (!SunTalismanSet.getSunTalismanPlayers().contains(player.getUniqueId())) {
                    SunTalismanSet.onTalismanEnable(player);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent bpe) {
        Player player = bpe.getPlayer();
        Block block = bpe.getBlock();

        if (block.getType().equals(Material.PLAYER_HEAD)) {
            if (SunTalismanSet.getSunTalismanPlayers().contains(player.getUniqueId())) {
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
