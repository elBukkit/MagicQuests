package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import com.elmakers.mine.bukkit.utility.ConfigurationUtils;
import me.blackvein.quests.CustomReward;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class SpellCastReward extends CustomReward {

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

    public SpellCastReward() {
        this.setName("Spell Cast");
        this.setAuthor("NathanWolf");
        this.setRewardName("Magical Event");
        this.addData("Spell");
        this.addDescription("Spell", "The KEY name of the Magic spell to cast as the player.");
        this.addData("Parameters");
        this.addDescription("Parameters", "Any parameters to pass to the spell on cast.");
    }

    @Override
    public void giveReward(Player player, Map<String, Object> stringObjectMap) {
        MagicAPI api = getAPI(player.getServer());
        String spellKey = (String)stringObjectMap.get("Spell");
        if (spellKey != null) {
            String[] extraParameters = StringUtils.split((String)stringObjectMap.get("Parameters"), ' ');
            ConfigurationSection parameters = null;
            if (extraParameters != null && extraParameters.length > 0) {
                parameters = new MemoryConfiguration();
                ConfigurationUtils.addParameters(extraParameters, parameters);
            }
            api.cast(spellKey, parameters, player, player);
        }
    }
}
