package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import me.blackvein.quests.CustomReward;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class MagicReward extends CustomReward {
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

    public MagicReward() {
        this.setName("Magic Loot");
        this.setAuthor("NathanWolf");
        this.setRewardName("Magic Loot");
        this.addData("Item");
        this.addDescription("Item", "The name of the Magic item (wand, spell, upgrade) to give to the player.");
    }

    @Override
    public void giveReward(Player player, Map<String, Object> stringObjectMap) {
        MagicAPI api = getAPI(player.getServer());
        String itemKey = (String)stringObjectMap.get("Item");
        if (itemKey != null) {
            api.giveItemToPlayer(player, api.createItem(itemKey));
        }
    }
}
