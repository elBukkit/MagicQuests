package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import org.bukkit.entity.Player;

import java.util.Map;

public class MagicItemReward extends MagicReward {

    public MagicItemReward() {
        this.setName("Magic Item");
        this.setAuthor("NathanWolf");
        this.setRewardName("Magic Item");
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
