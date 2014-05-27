package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import me.blackvein.quests.CustomReward;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class MagicSpellReward extends CustomReward {

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

    public MagicSpellReward() {
        this.setName("Magic Spell");
        this.setAuthor("NathanWolf");
        this.setRewardName("Spell");
        this.addData("Spell");
        this.addDescription("Spell", "The key name of the Magic spell to give to the player.");
    }

    @Override
    public void giveReward(Player player, Map<String, Object> stringObjectMap) {
        MagicAPI api = getAPI(player.getServer());
        String spellKey = (String)stringObjectMap.get("Spell");
        if (spellKey != null) {
            api.giveItemToPlayer(player, api.createSpellItem(spellKey));
        }
    }
}
