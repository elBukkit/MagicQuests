package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.api.magic.MageController;
import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import com.elmakers.mine.bukkit.api.spell.Spell;
import me.pikamug.quests.module.BukkitCustomRequirement;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

public class SpellCastRequirement extends BukkitCustomRequirement {
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

    public SpellCastRequirement() {
        this.setName("Has Cast Magic Spell");
        this.setAuthor("NathanWolf");
        this.addStringPrompt("Spell", "The KEY name of the Magic spell the player must have cast.", null);
        this.addStringPrompt("Cast Count", "How many times the player must have cast the spell.", null);
    }

    @Override
    public boolean testRequirement(UUID uuid, Map<String, Object> stringObjectMap) {
        Player player = Bukkit.getPlayer(uuid);
        MagicAPI api = getAPI(player.getServer());
        String spellKey = (String)stringObjectMap.get("Spell");
        if (api == null || spellKey == null) return false;

        MageController controller = api.getController();
        if (!controller.isMage(player)) return false;

        Mage mage = api.getMage(player);
        Spell spell = mage.getSpell(spellKey);
        if (spell == null) return false;

        int castCount = 1;
        String castCountString = (String)stringObjectMap.get("Cast Count");
        try {
            castCount = Integer.parseInt(castCountString);
        } catch (Exception ex) {

        }
        return spell.getCastCount() >= castCount;
    }
}
