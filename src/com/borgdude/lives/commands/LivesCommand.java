package com.borgdude.lives.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.borgdude.lives.SimpleLives;

public class LivesCommand implements CommandExecutor{

    private SimpleLives plugin;

    public LivesCommand(SimpleLives plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            
            if(command.getName().equalsIgnoreCase("lives")) {
                if(args.length <= 0) {
                    int lives = plugin.getLivesConfig().getInt("players." + player.getUniqueId().toString() + ".lives");
                    String msg = "";
                    
                    if(lives > -1)
                        msg = plugin.getLanguageManager().getMessage("General.Player-Lives")
                                .replace("%lives%", String.valueOf(lives));
                    else
                        msg = plugin.getLanguageManager().getMessage("General.Player-Ignored");
                    
                    player.sendMessage(msg);
                    
                    return true;
                }
            }
            return false;
        }
        return true;
    }

}
