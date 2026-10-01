package com.midnightsmp.midnightstaff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MidnightStaff extends JavaPlugin implements Listener, CommandExecutor {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        if (getCommand("sc") != null) {
            getCommand("sc").setExecutor(this);
        }
        getLogger().info("MidnightStaff (Ranks & Staff Chat) enabled!");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String prefix = getRankPrefix(player);
        if (!prefix.isEmpty()) {
            event.setFormat(prefix + " §f" + player.getName() + "§7: §f" + event.getMessage());
        }
    }

    private String getRankPrefix(Player p) {
        if (p.hasPermission("staff.owner") || p.isOp()) return "§4[OWNER]";
        if (p.hasPermission("staff.admin")) return "§c[ADMIN]";
        if (p.hasPermission("staff.mod")) return "§2[MOD]";
        if (p.hasPermission("staff.helper")) return "§b[HELPER]";
        return "";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("sc")) {
            if (!sender.hasPermission("staff.chat")) {
                sender.sendMessage("§cYou do not have permission to use staff chat!");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage("§cUsage: /sc <message>");
                return true;
            }

            String msg = String.join(" ", args);
            String staffMsg = "§8[§cStaffChat§8] §e" + sender.getName() + "§7: §f" + msg;

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("staff.chat")) {
                    p.sendMessage(staffMsg);
                }
            }
            return true;
        }
        return false;
    }
}
