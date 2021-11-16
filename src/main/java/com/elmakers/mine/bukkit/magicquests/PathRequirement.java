package com.elmakers.mine.bukkit.magicquests;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.api.magic.MageClass;
import com.elmakers.mine.bukkit.api.magic.MageController;
import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import com.elmakers.mine.bukkit.api.magic.ProgressionPath;
import com.elmakers.mine.bukkit.api.wand.Wand;

import me.blackvein.quests.CustomRequirement;

public class PathRequirement extends CustomRequirement {
    private static MagicAPI api;

    protected static MagicAPI getAPI(Server server) {
        if (api == null) {
            Plugin magicPlugin = server.getPluginManager().getPlugin("Magic");
            if (magicPlugin != null && magicPlugin instanceof MagicAPI) {
                api = (MagicAPI)magicPlugin;
            }
        }

        return api;
    }

    public PathRequirement() {
        this.setName("Has Path");
        this.setAuthor("NathanWolf");
        this.addStringPrompt("Wand", "A specific wand template to look for", "");
        this.addStringPrompt("Class", "A specific class to look for", "");
        this.addStringPrompt("Path", "What path the player must be on.", null);
    }

    @Override
    public String getName() {
        String name = super.getName();
        Object path = getData().get("Path");
        if (path != null) {
            name = name + ": " + path;
        }
        return name;
    }

    @Override
    public boolean testRequirement(Player player, Map<String, Object> stringObjectMap) {
        MagicAPI api = getAPI(player.getServer());
        String pathKey = (String)stringObjectMap.get("Path");
        if (api == null || pathKey == null) return false;

        MageController controller = api.getController();
        Mage mage = controller.getRegisteredMage(player);
        if (mage == null) return false;

        ProgressionPath path = null;

        // Look for a specific wand first
        String baseTemplate = (String)stringObjectMap.get("Wand");
        String baseClass = (String)stringObjectMap.get("Class");
        if (baseTemplate != null && !baseTemplate.isEmpty()) {
            Wand boundWand = mage.getBoundWand(baseTemplate);
            if (boundWand != null) {
                path = boundWand.getPath();
            }
        } else if (baseClass != null && !baseClass.isEmpty()) {
            MageClass mageClass = mage.getClass(baseClass);
            if (mageClass != null) {
                path = mageClass.getPath();
            }
        } else {
            path = mage.getActiveProperties().getPath();
        }

        boolean isDebug = mage.getDebugLevel() > 0;
        if (path == null) {
            if (isDebug) mage.sendDebugMessage(ChatColor.RED + "Has no " + ChatColor.YELLOW + baseTemplate + ChatColor.DARK_RED +" wand", 7);
            return false;
        }

        boolean hasPath = path.hasPath(pathKey);
        if (isDebug) mage.sendDebugMessage(ChatColor.BLUE + "Checking wand path " + ChatColor.GOLD + path.getKey() +
                ChatColor.BLUE +" has path " + ChatColor.YELLOW + pathKey + "? " +
                (hasPath ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"), 7);
        return hasPath;
    }
}
