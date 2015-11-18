package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.api.magic.MageController;
import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import com.elmakers.mine.bukkit.api.wand.Wand;
import com.elmakers.mine.bukkit.api.wand.WandUpgradePath;
import me.blackvein.quests.CustomRequirement;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;

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
        this.setName("Has Wand Path");
        this.setAuthor("NathanWolf");
        this.addData("Wand");
        this.addData("Path");
        this.addDescription("Wand", "The template to look at (normally 'default')");
        this.addDescription("Path", "What path the player must be on.");
    }

    @Override
    public boolean testRequirement(Player player, Map<String, Object> stringObjectMap) {
        MagicAPI api = getAPI(player.getServer());
        String baseTemplate = (String)stringObjectMap.get("Wand");
        if (baseTemplate == null || baseTemplate.isEmpty()) {
            baseTemplate = "default";
        }
        String pathKey = (String)stringObjectMap.get("Path");
        if (api == null || pathKey == null) return false;

        MageController controller = api.getController();
        if (!controller.isMage(player)) return false;

        Mage mage = api.getMage(player);

        WandUpgradePath path = null;
        if (baseTemplate != null)
        {
            Wand boundWand = mage.getBoundWand(baseTemplate);
            if (boundWand != null) {
                path = boundWand.getPath();
            }
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
