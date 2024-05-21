package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import me.pikamug.quests.module.BukkitCustomReward;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

public class MagicItemReward extends BukkitCustomReward {

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

    public MagicItemReward() {
        this.setName("Magic Item");
        this.setAuthor("NathanWolf");
        this.setDisplay("Magic Item");
        this.addStringPrompt("Item", "The name of the Magic item (wand, spell, upgrade) to give to the player.", null);
    }

    @Override
    public void giveReward(UUID uuid, Map<String, Object> stringObjectMap) {
        Player player = Bukkit.getPlayer(uuid);
        MagicAPI api = getAPI(player.getServer());
        String itemKey = (String)stringObjectMap.get("Item");
        if (itemKey != null) {
            api.giveItemToPlayer(player, api.createItem(itemKey));
        } else {
            Bukkit.getLogger().warning("Invalid item given as Quests reward: " + itemKey);
        }
    }
}
