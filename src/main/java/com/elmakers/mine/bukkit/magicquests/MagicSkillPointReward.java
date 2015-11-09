package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import me.blackvein.quests.CustomReward;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class MagicSkillPointReward extends CustomReward {

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

    public MagicSkillPointReward() {
        this.setName("Magic Skill Points");
        this.setAuthor("NathanWolf");
        this.setRewardName("SP");
        this.addData("Amount");
        this.addDescription("Amount", "The amount of SP to give.");
    }

    @Override
    public void giveReward(Player player, Map<String, Object> stringObjectMap) {
        MagicAPI api = getAPI(player.getServer());
        String amountString = (String)stringObjectMap.get("Amount");
        int amount = 0;
        try {
            amount = Integer.parseInt(amountString);
        } catch (Exception ex) {
            Bukkit.getLogger().warning("Invalid amount given as Quests reward: " + amountString);
        }
        if (amount > 0) {
            api.getMage(player).addSkillPoints(amount);
        }
    }
}
