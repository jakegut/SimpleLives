package com.borgdude.lives.events;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.borgdude.lives.SimpleLives;

public class LivesEvents implements Listener {
    private final SimpleLives plugin;
    
    public LivesEvents(SimpleLives plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        
        String exist = plugin.getLivesConfig().getString("players." + p.getUniqueId().toString() + ".lives");
        if(exist == null) {
            plugin.getLivesConfig().set("players." + p.getUniqueId().toString() + ".lives", 3);
            try {
                plugin.saveLivesConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        int lives = plugin.getLivesConfig().getInt("players." + p.getUniqueId().toString() + ".lives");
        p.sendMessage(plugin.getLanguageManager().getMessage("General.Individual-On-Join")
                .replace("%lives%", String.valueOf(lives)));
    }
}
