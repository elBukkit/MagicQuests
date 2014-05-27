package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import com.elmakers.mine.bukkit.api.wand.Wand;
import org.bukkit.entity.Player;

import java.util.Map;

public class MagicBrushReward extends MagicReward {

    public MagicBrushReward() {
        this.setName("Magic Brush");
        this.setAuthor("NathanWolf");
        this.setRewardName("Material Brush");
        this.addData("Brush");
        this.addDescription("Brush", "The key name of the Magic material brush to give to the player.");
    }

    @Override
    public void giveReward(Player player, Map<String, Object> stringObjectMap) {
        MagicAPI api = getAPI(player.getServer());
        String brushKey = (String)stringObjectMap.get("Brush");
        if (brushKey != null) {
            api.giveItemToPlayer(player, api.createBrushItem(brushKey));
        }
    }
}
