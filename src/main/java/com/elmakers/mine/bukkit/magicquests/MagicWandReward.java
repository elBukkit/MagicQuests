package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import com.elmakers.mine.bukkit.api.wand.Wand;
import org.bukkit.entity.Player;

import java.util.Map;

public class MagicWandReward extends MagicReward {

    public MagicWandReward() {
        this.setName("Magic Wand");
        this.setAuthor("NathanWolf");
        this.setRewardName("Wand");
        this.addData("Wand");
        this.addDescription("Wand", "The key name of the Magic wand to give to the player.");
    }

    @Override
    public void giveReward(Player player, Map<String, Object> stringObjectMap) {
        MagicAPI api = getAPI(player.getServer());
        String wandKey = (String)stringObjectMap.get("Wand");
        if (wandKey != null) {
            Wand wand = api.createWand(wandKey);
            if (wand != null) {
                api.giveItemToPlayer(player, wand.getItem());
            }
        }
    }
}
