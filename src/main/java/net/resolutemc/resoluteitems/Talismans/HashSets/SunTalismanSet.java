package net.resolutemc.resoluteitems.Talismans.HashSets;

import net.resolutemc.resoluteitems.Main;
import net.resolutemc.resoluteitems.MessageManager.ActionBarMessages;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class SunTalismanSet {

    private static final HashSet<UUID> SunTalismanPlayers = new HashSet<>();
    private static final HashSet<Entity> disabledMobs = new HashSet<>();
    private static final HashMap<UUID, Integer> coolDown = new HashMap<>();
    private static final BossBar bar = Bukkit.createBossBar(Main.chatColor("&e&lSolar Flare Active"), BarColor.YELLOW, BarStyle.SOLID);


    public static HashSet<UUID> getSunTalismanPlayers() {
        return SunTalismanPlayers;
    }

    public static void addSunTalismanPlayers(UUID player) {
        getSunTalismanPlayers().add(player);
    }

    public static void removeSunTalismanPlayers(UUID player) {
        getSunTalismanPlayers().remove(player);
    }

    public static void onTalismanEnable(Player player) {
        addSunTalismanPlayers(player.getUniqueId());
    }

    public static void onTalismanDisable(Player player) {
        removeSunTalismanPlayers(player.getUniqueId());
    }

    public static void onAiDisable(Entity entity) {
        disableAI(entity);
    }

    public static void addBarPlayer(Player player) {
        bar.addPlayer(player);
    }

    public static void removeBarPlayer(Player player) {
        bar.removePlayer(player);
    }

    public static void addTalismanCooldown(Player player) {
        timerActivate(player);
    }

    public static HashMap<UUID, Integer> getTalismanCooldown() {
        return coolDown;
    }

    private static final int DEFAULTCOOLDOWN = 10;

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
            setCoolDown(player.getUniqueId(), DEFAULTCOOLDOWN);
            new BukkitRunnable() {
                @Override
                public void run() {
                    int timeLeft = getCooldown(player.getUniqueId());
                    setCoolDown(player.getUniqueId(), --timeLeft);
                    if (timeLeft == 0) {
                        this.cancel();
                        ActionBarMessages.sendPlayerActionBar(player, "Solar-Flare-Ability-Refreshed");
                    }
                }
            }.runTaskTimer(Main.plugin, 20L, 20L);
        }
    }

    private static void disableAI(Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity mob = (LivingEntity) entity;
            mob.setAI(false);

            new BukkitRunnable() {
                @Override
                public void run() {
                    mob.setAI(true);
                    disabledMobs.remove(mob);
                }
            }.runTaskLater(Main.plugin, 100L);
        }
    }

    // Particles that appear around the player
    public static void particleRun(Player player) {
        new BukkitRunnable() {
            double t = Math.PI / 4;
            final Location location = player.getLocation();

            public void run() {
                t = t + 0.1 * Math.PI;
                for (double theta = 0; theta <= 2 * Math.PI; theta = theta + Math.PI / 64) {
                    double x = t * cos(theta);
                    double y = 2 * Math.exp(-0.1 * t) * sin(t) + 0.5;
                    double z = t * sin(theta);
                    location.add(x, y, z);
                    location.getWorld().spawnParticle(Particle.REDSTONE, location, 2, new Particle.DustOptions(Color.YELLOW, 1));
                    location.subtract(x, y, z);

                    theta = theta + Math.PI / 128;

                    x = t * cos(theta);
                    y = 2 * Math.exp(-0.1 * t) * sin(t) + 0.5;
                    z = t * sin(theta);
                    location.add(x, y, z);
                    location.getWorld().spawnParticle(Particle.REDSTONE, location, 2, new Particle.DustOptions(Color.ORANGE, 1));
                    location.subtract(x, y, z);
                }
                if (t > 25) {
                    this.cancel();
                }
            }

        }.runTaskTimer(Main.plugin, 0, 1);
    }
}
