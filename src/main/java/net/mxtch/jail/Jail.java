package net.mxtch.jail;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Jail {
    private List<JailPlayer> punishedPlayers;
    private JailPlugin jailPlugin;
    private Location jailArea;

    public Jail(JailPlugin jailPlugin) {
        this.jailPlugin = jailPlugin;
        this.punishedPlayers = new ArrayList<>();
        this.jailArea = new Location(Bukkit.getWorld("world"), 32,76,-176);
    }

    public boolean punishPlayer(JailPlayer player) {
        for (JailPlayer jailPlayer : punishedPlayers){
            if (jailPlayer.getPlayer().getName().equals(player.getPlayer().getName()))
                return false;
        }
        punishedPlayers.add(player);
        player.getPlayer().teleport(jailArea);

        player.getPlayer().sendMessage(JailMessages.getMessage("punish-message")
                .replace("{player}", player.getPlayer().getName())
                .replace("{punisher}", player.getPunisher().getName())
                .replace("{duration}", String.valueOf(player.getDurationPunishment()))
                .replace("{reason}", player.getReason())
        );
        return true;
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

    public void removePunishPlayer(JailPlayer jailPlayer) {
        punishedPlayers.remove(jailPlayer);
        jailPlayer.getPlayer().sendMessage("§dU are free");
        jailPlayer.getPlayer().teleport(jailPlayer.getLastLocation());
    }

    public void start(){
        new BukkitRunnable() {
            public void run() {
                if (!punishedPlayers.isEmpty()){
                    for (int i = 0; i < punishedPlayers.size(); i++){
                        if (punishedPlayers.get(i).getDurationPunishment() == 0) {
                            if (!punishedPlayers.get(i).isEndlessPunishmentTime()){
                                removePunishPlayer(punishedPlayers.get(i));
                            }
                        } else {
                            punishedPlayers.get(i).updateDurationPunishment(punishedPlayers.get(i).getDurationPunishment() - 1);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(jailPlugin, 20, 20);
    }

}
