package net.mxtch.jail;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Jail {
    private List<JailPlayer> punishedPlayers;
    private JailPlugin jailPlugin;
    private Location jailArea;

    public Jail(JailPlugin jailPlugin) {
        this.jailPlugin = jailPlugin;
        this.punishedPlayers = new ArrayList<>();
        loadJailLocation();
    }

    public void loadJailLocation(){
        String world = jailPlugin.getConfig().getString("jail_area.world");
        int x = jailPlugin.getConfig().getInt("jail_area.x");
        int y = jailPlugin.getConfig().getInt("jail_area.y");
        int z = jailPlugin.getConfig().getInt("jail_area.z");
        this.jailArea = new Location(Bukkit.getWorld(world), x, y, z);
    }

    public void punishPlayer(JailPlayer player) {
        for (JailPlayer jailPlayer : punishedPlayers){
            if (jailPlayer.getPlayer().getName().equals(player.getPlayer().getName())) {
                jailPlayer.updateDurationPunishment(jailPlayer.getDurationPunishment() + player.getDurationPunishment());
                player.getPlayer().sendMessage(jailPlugin.getMessages().getString("punishment-extended")
                        .replace("{player}", player.getPlayer().getName())
                        .replace("{punisher}", player.getPunisher().getName())
                        .replace("{duration}", String.valueOf(player.getDurationPunishment()))
                        .replace("{reason}", player.getReason())
                );
                return;
            }
        }
        punishedPlayers.add(player);
        player.getPlayer().teleport(jailArea);
        player.getPlayer().setGameMode(GameMode.SURVIVAL);
        player.getPlayer().getInventory().clear();

        player.getPlayer().sendMessage(jailPlugin.getMessages().getString("punish-message")
                .replace("{player}", player.getPlayer().getName())
                .replace("{punisher}", player.getPunisher().getName())
                .replace("{duration}", String.valueOf(player.getDurationPunishment()))
                .replace("{reason}", player.getReason())
        );
    }

    public boolean isPunished(Player player) {
        for (JailPlayer jailPlayer : punishedPlayers){
            if (jailPlayer.getPlayer().getName().equals(player.getPlayer().getName()))
                return true;
        }
        return false;
    }

    public JailPlayer getPunishedPlayer(Player player) {
        for (JailPlayer jailPlayer : punishedPlayers){
            if (jailPlayer.getPlayer().getName().equals(player.getPlayer().getName()))
                return jailPlayer;
        }
        return null;
    }

    public List<JailPlayer> getPunishedPlayers() {
        return punishedPlayers;
    }

    public Location getJailArea() {
        return jailArea;
    }

    public void removePunishPlayer(JailPlayer jailPlayer) {
        punishedPlayers.remove(jailPlayer);
        jailPlayer.getPlayer().sendMessage(jailPlugin.getMessages().getString("punish-free"));
        jailPlayer.getPlayer().teleport(jailPlayer.getLastLocation());
        jailPlayer.getPlayer().getInventory().setContents(jailPlayer.getInventoryContents());
    }

}
