package net.resolutemc.resoluteitems.Talismans.HashSets;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class PoseidonTalismanSet {

    private static final HashSet<UUID> PoseidonTalismanPlayers = new HashSet<>();


    public static HashSet<UUID> getPoseidonTalismanPlayers() {
        return PoseidonTalismanPlayers;
    }

    public static void addPoseidonTalismanPlayers(UUID player) {
        getPoseidonTalismanPlayers().add(player);
    }

    public static void removePoseidonTalismanPlayers(UUID player) {
        getPoseidonTalismanPlayers().remove(player);
    }

    public static void onTalismanEnable(Player player) {
        addPoseidonTalismanPlayers(player.getUniqueId());
    }

    public static void onTalismanDisable(Player player) {
        removePoseidonTalismanPlayers(player.getUniqueId());
    }

}
