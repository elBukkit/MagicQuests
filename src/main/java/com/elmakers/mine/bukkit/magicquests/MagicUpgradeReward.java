package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import com.elmakers.mine.bukkit.api.wand.Wand;
import me.blackvein.quests.CustomReward;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class MagicUpgradeReward extends CustomReward {

    // This boilerplate cannot be abstracted due to the class-scanning that Quests does
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

    public MagicUpgradeReward() {
        this.setName("Magic Upgrade");
        this.setAuthor("NathanWolf");
        this.setRewardName("Wand Upgrade");
        this.addStringPrompt("Wand", "The key name of the Magic wand or upgrade item to use.", null);
    }

    @Override
    public void giveReward(Player player, Map<String, Object> stringObjectMap) {
        MagicAPI api = getAPI(player.getServer());
        String wandKey = (String)stringObjectMap.get("Wand");
        if (wandKey != null) {
            Wand wand = api.createUpgrade(wandKey);
            if (wand != null) {
                api.giveItemToPlayer(player, wand.getItem());
            } else {
                Bukkit.getLogger().warning("Invalid upgrade given as Quests reward: " + wandKey);
            }
        }
    }
}
