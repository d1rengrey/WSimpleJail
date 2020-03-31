package net.d1rengray.jail;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class JailPlayer {
    private Player punisher;
    private Player player;
    private String reason;
    private long durationPunishment;
    private Location lastLocation;
    private boolean endlessPunishmentTime;
    private ItemStack[] inventoryContents;

    public Player getPlayer() {
        return player;
    }

    public Player getPunisher() {
        return punisher;
    }

    public String getReason() {
        return reason;
    }

    public long getDurationPunishment() {
        return durationPunishment;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public ItemStack[] getInventoryContents(){
        return inventoryContents;
    }

    public boolean isEndlessPunishmentTime() {
        return endlessPunishmentTime;
    }

    public void updateDurationPunishment(long durationPunishment) {
        this.durationPunishment = durationPunishment;
    }

    public static class Builder {
        private JailPlayer jailPlayer;

        public Builder(){
            this.jailPlayer = new JailPlayer();
        }

        public Builder setReason(String reason) {
            this.jailPlayer.reason = reason.replace("&", "§");
            return this;
        }

        public Builder setDurationPunishment(long durationPunishment) {
            this.jailPlayer.durationPunishment = durationPunishment;
            this.jailPlayer.endlessPunishmentTime = durationPunishment == 0;
            return this;
        }

        public Builder setPlayer(Player player) {
            this.jailPlayer.player = player;
            this.jailPlayer.lastLocation = player.getLocation();
            this.jailPlayer.inventoryContents = player.getInventory().getContents();
            return this;
        }

        public Builder setPunisher(Player player) {
            this.jailPlayer.punisher = player;
            return this;
        }

        public JailPlayer build() {
            return jailPlayer;
        }
    }
}
