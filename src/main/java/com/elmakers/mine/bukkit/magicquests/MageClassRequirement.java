package com.elmakers.mine.bukkit.magicquests;

import java.util.Map;
import java.util.UUID;

import me.pikamug.quests.module.BukkitCustomRequirement;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.api.magic.MagicAPI;

public class MageClassRequirement extends BukkitCustomRequirement {
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

    public MageClassRequirement() {
        this.setName("Has Mage Class");
        this.setAuthor("NathanWolf");
        this.addStringPrompt("Class", "The KEY name of the class (or a list of classes) that the player must be a member of (at least one).", "");
    }

    @Override
    public boolean testRequirement(UUID uuid, Map<String, Object> stringObjectMap) {
        Player player = Bukkit.getPlayer(uuid);
        MagicAPI api = getAPI(player.getServer());
        Mage mage = api.getController().getMage(player);
        String classKeyList = (String)stringObjectMap.get("Class");
        String[] classKeys = StringUtils.split(classKeyList, ",");
        for (String classKey : classKeys) {
            if (mage != null && mage.hasClassUnlocked(classKey)) {
                return false;
            }
        }

        return false;
    }
}
