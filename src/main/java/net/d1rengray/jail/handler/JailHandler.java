package net.d1rengray.jail.handler;

import net.d1rengray.jail.Jail;
import net.d1rengray.jail.JailPlayer;
import net.d1rengray.jail.JailPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class JailHandler implements Listener {
    private Jail jail;
    private JailPlugin jailPlugin;

    public JailHandler(Jail jail, JailPlugin jailPlugin) {
        this.jail = jail;
        this.jailPlugin = jailPlugin;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        List<String> allowed_commands = jailPlugin.getConfig().getStringList("jail_settings.allowed_commands");
        String[] command = event.getMessage().split(" ");

        if (jail.isPunished(event.getPlayer())){
            if (!allowed_commands.contains(command[0])){
                event.setCancelled(true);
            }
        }
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
        String material = jailPlugin.getConfig().getString("jail_settings.material");

        if (jail.isPunished(event.getPlayer())){
            jailPlayer = jail.getPunishedPlayer(event.getPlayer());
            if (block.getType() == Material.valueOf(material)){
                if (!jailPlayer.isEndlessPunishmentTime()){
                    if (jailPlayer.getDurationPunishment() == 1){
                        jail.removePunishPlayer(jailPlayer);
                    } else {
                        jailPlayer.updateDurationPunishment(jailPlayer.getDurationPunishment() - 1);
                        jailPlayer.getPlayer().sendMessage(jailPlugin.getMessages().getString("punish-info").replace("{duration}", Long.toString(jailPlayer.getDurationPunishment())));
                    }
                    event.setCancelled(true);
                }
            } else
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if (jail.isPunished(player)){
            player.teleport(jail.getJailArea());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player){
            Player player = (Player) event.getDamager();
            if (jail.isPunished(player)){
                event.setCancelled(true);
            }
        }
    }
}
