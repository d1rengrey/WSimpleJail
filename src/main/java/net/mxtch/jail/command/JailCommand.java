package net.mxtch.jail.command;

import net.mxtch.jail.Jail;
import net.mxtch.jail.JailMessages;
import net.mxtch.jail.JailPlayer;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class JailCommand implements CommandExecutor {

    private Jail jail;

    public JailCommand(Jail jail) {
        this.jail = jail;
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Usage command in the console is prohibited");
            return true;
        } else {
            if (args.length < 1){
                if (jail.isPunished((Player) sender)){
                    JailPlayer jailPlayer = jail.getPunishedPlayer((Player) sender);
                    sender.sendMessage(JailMessages.getMessage("punish-message")
                            .replace("{player}", jailPlayer.getPlayer().getName())
                            .replace("{punisher}", jailPlayer.getPunisher().getName())
                            .replace("{duration}", String.valueOf(jailPlayer.getDurationPunishment()))
                            .replace("{reason}", jailPlayer.getReason()));
                } else {
                    sender.sendMessage(command.getUsage());
                }
            } else {
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
                } else {
                    sender.sendMessage("§c" + args[0] + " §foffline :(");
                }
            }
        }
        return true;
    }
}
