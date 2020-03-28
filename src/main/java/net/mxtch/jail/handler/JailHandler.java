package net.mxtch.jail.handler;

import net.mxtch.jail.Jail;
import net.mxtch.jail.JailPlayer;
import net.mxtch.jail.JailPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class JailHandler implements Listener {
    private Jail jail;
    private JailPlugin jailPlugin;

    public JailHandler(Jail jail, JailPlugin jailPlugin) {
        this.jail = jail;
        this.jailPlugin = jailPlugin;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        if (jail.isPunished(event.getPlayer()))
            if (!event.getMessage().equalsIgnoreCase("/jail"))
                event.setCancelled(true);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (jail.isPunished(event.getPlayer())) {
            if (jailPlugin.getConfig().getBoolean("jail_settings.punish-chat")) {
                for (JailPlayer player : jail.getPunishedPlayers()) {
                    player.getPlayer().sendMessage(jailPlugin.getConfig().getString("jail_settings.chat-format")
                            .replace("{username}", event.getPlayer().getName())
                            .replace("{message}", event.getMessage())
                    );
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        JailPlayer jailPlayer;
        Block block = event.getBlock();

        if (jail.isPunished(event.getPlayer())){
            jailPlayer = jail.getPunishedPlayer(event.getPlayer());
            if (block.getType() == Material.OBSIDIAN){
                if (!jailPlayer.isEndlessPunishmentTime()){
                    if (jailPlayer.getDurationPunishment() == 1){
                        jail.removePunishPlayer(jailPlayer);
                    } else {
                        jailPlayer.updateDurationPunishment(jailPlayer.getDurationPunishment() - 1);
                        jailPlayer.getPlayer().sendMessage(jailPlugin.getConfig().getString("punish-info").replace("{duration}", Long.toString(jailPlayer.getDurationPunishment())));
                    }
                    event.setCancelled(true);
                }
            } else
                event.setCancelled(true);
        }
    }
}
