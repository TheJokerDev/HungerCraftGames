package me.thejokerdev.hungercraftgames.utils;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.player.SPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class MicUtils implements VoicechatPlugin {

    public Permission BROADCAST_PERMISSION = new Permission("hungergames.broadcast", PermissionDefault.OP);
    private Main plugin;

    public MicUtils(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPluginId() {
        return plugin.PLUGIN_ID;
    }

    @Override
    public void initialize(VoicechatApi api) {
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophone);
    }

    /**
     * This method is called whenever a player sends audio to the server via the voice chat.
     *
     * @param event the microphone packet event
     */
    private void onMicrophone(MicrophonePacketEvent event) {
        // The connection might be null if the event is caused by other means
        if (event.getSenderConnection() == null) {
            return;
        }
        // Cast the generic player object of the voice chat API to an actual bukkit player
        // This object should always be a bukkit player object on bukkit based servers
        if (!(event.getSenderConnection().getPlayer().getPlayer() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getSenderConnection().getPlayer().getPlayer();
        SPlayer p = plugin.getPlayerManager().getPlayer(player);

        // Check if the player has the broadcast permission
        if (!player.hasPermission(BROADCAST_PERMISSION)) {
            if (plugin.muted) {
                event.cancel();
            }
            return;
        }

        if (!p.isBroadcast()){
            return;
        }

        // Cancel the actual microphone packet event that people in that group or close by don't hear the broadcaster twice
        event.cancel();

        VoicechatServerApi api = event.getVoicechat();

        // Iterating over every player on the server
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            // Don't send the audio to the player that is broadcasting
            if (onlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                continue;
            }
            VoicechatConnection connection = api.getConnectionOf(onlinePlayer.getUniqueId());
            // Check if the player is actually connected to the voice chat
            if (connection == null) {
                continue;
            }
            // Send a static audio packet of the microphone data to the connection of each player
            api.sendStaticSoundPacketTo(connection, event.getPacket().toStaticSoundPacket());
        }
    }
}
