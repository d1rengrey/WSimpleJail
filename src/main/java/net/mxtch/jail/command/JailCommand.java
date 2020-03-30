package net.mxtch.jail.command;

import net.mxtch.jail.Jail;
import net.mxtch.jail.JailPlayer;
import net.mxtch.jail.JailPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JailCommand implements CommandExecutor {

    private Jail jail;
    private JailPlugin jailPlugin;

    public JailCommand(Jail jail, JailPlugin jailPlugin) {
        this.jail = jail;
        this.jailPlugin = jailPlugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Usage command in the console is prohibited");
            return true;
        } else {
            if (args.length < 1){
                if (jail.isPunished((Player) sender)){
                    JailPlayer jailPlayer = jail.getPunishedPlayer((Player) sender);
                    sender.sendMessage(jailPlugin.getMessages().getString("punish-message")
                            .replace("{player}", jailPlayer.getPlayer().getName())
                            .replace("{punisher}", jailPlayer.getPunisher().getName())
                            .replace("{duration}", String.valueOf(jailPlayer.getDurationPunishment()))
                            .replace("{reason}", jailPlayer.getReason()));
                } else {
                    sender.sendMessage(command.getUsage());
                }
            } else {
                if (args[0].equalsIgnoreCase("set") && args.length == 2){
                    if (args[1].equalsIgnoreCase("spawn")){
                        jailPlugin.getConfig().set("jail_area.world", ((Player) sender).getWorld().getName());
                        jailPlugin.getConfig().set("jail_area.x", ((Player) sender).getLocation().getBlockX());
                        jailPlugin.getConfig().set("jail_area.y", ((Player) sender).getLocation().getBlockY());
                        jailPlugin.getConfig().set("jail_area.z", ((Player) sender).getLocation().getBlockZ());
                        jailPlugin.saveConfig();
                        jail.loadJailLocation();
                        sender.sendMessage("§aSpawn point has been set");
                        return true;
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("remove") && args.length == 2){
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player != null){
                        if (jail.isPunished(player)){
                            JailPlayer jailPlayer = jail.getPunishedPlayer(player);
                            jail.removePunishPlayer(jailPlayer);
                            sender.sendMessage("§aPlayer successfully removed from jail");
                        } else {
                            sender.sendMessage("§cPlayer is not in jail");
                        }
                        return true;
                    }
                }

                Player player = Bukkit.getPlayer(args[0]);

                if (player != null){

                    long duration = 0;

                    StringBuilder message = new StringBuilder();

                    if (args.length > 1) {
                        if (args[1].matches("[0-9]+")){
                            duration = Long.parseLong(args[1]);
                        } else {
                            message.append(args[1]).append(" ");
                        }
                    }
                    for (int i = 2; i < args.length; i++){
                        message.append(args[i]).append(" ");
                    }
                    if (message.toString().length() == 0){
                        message.append("NaN");
                    }
                    jail.punishPlayer(
                            new JailPlayer.Builder()
                                    .setPlayer(player)
                                    .setDurationPunishment(duration)
                                    .setPunisher((Player) sender)
                                    .setReason(message.toString())
                                    .build()
                    );
                    sender.sendMessage(jailPlugin.getMessages().getString("punisher-message")
                            .replace("{player}", player.getName())
                            .replace("{punisher}", sender.getName())
                            .replace("{duration}", Long.toString(duration))
                            .replace("{reason}", message)
                    );
                } else {
                    sender.sendMessage("§c" + args[0] + " §foffline :(");
                }
            }
        }
        return true;
    }
}

