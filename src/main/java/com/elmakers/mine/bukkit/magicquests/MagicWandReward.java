package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import com.elmakers.mine.bukkit.api.wand.Wand;
import me.pikamug.quests.module.BukkitCustomReward;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

public class MagicWandReward extends BukkitCustomReward {

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

    public MagicWandReward() {
        this.setName("Magic Wand");
        this.setAuthor("NathanWolf");
        this.setDisplay("Wand");
        this.addStringPrompt("Wand", "The key name of the Magic wand to give to the player.", null);
    }

    @Override
    public void giveReward(UUID uuid, Map<String, Object> stringObjectMap) {
        Player player = Bukkit.getPlayer(uuid);
        MagicAPI api = getAPI(player.getServer());
        String wandKey = (String)stringObjectMap.get("Wand");
        if (wandKey != null) {
            Wand wand = api.createWand(wandKey);
            if (wand != null) {
                api.giveItemToPlayer(player, wand.getItem());
            } else {
                Bukkit.getLogger().warning("Invalid wand given as Quests reward: " + wandKey);
            }
        }
    }
}
