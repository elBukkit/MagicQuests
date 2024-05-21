package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import me.pikamug.quests.module.BukkitCustomReward;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

public class MagicSkillPointReward extends BukkitCustomReward {

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
        this.setDisplay("%Amount% SP");
        this.addStringPrompt("Amount", "The amount of SP to give.", null);
    }

    @Override
    public void giveReward(UUID uuid, Map<String, Object> stringObjectMap) {
        Player player = Bukkit.getPlayer(uuid);
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
