package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import org.bukkit.entity.Player;

import java.util.Map;

public class MagicSpellReward extends MagicReward {

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
