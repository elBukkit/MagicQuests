package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.api.magic.MageController;
import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import com.elmakers.mine.bukkit.api.spell.Spell;
import com.elmakers.mine.bukkit.api.wand.Wand;
import me.pikamug.quests.module.BukkitCustomRequirement;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

public class ActiveSpellRequirement extends BukkitCustomRequirement {
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

    public ActiveSpellRequirement() {
        this.setName("Has Active Spell");
        this.setAuthor("NathanWolf");
        this.addStringPrompt("Spell", "The KEY name of the spell a player must have active", null);
    }

    @Override
    public boolean testRequirement(UUID uuid, Map<String, Object> stringObjectMap) {
        Player player = Bukkit.getPlayer(uuid);
        MagicAPI api = getAPI(player.getServer());
        MageController controller = api.getController();
        String spellKey = (String)stringObjectMap.get("Spell");
        if (spellKey != null && controller.isMage(player)) {
            Mage mage = controller.getMage(player);
            Wand wand = mage.getActiveWand();
            if (wand != null) {
                Spell spell = wand.getActiveSpell();
                return (spell != null && (spell.getKey().equalsIgnoreCase(spellKey)) || spell.getName().equalsIgnoreCase(spellKey));
            }
        }

        return false;
    }
}
