package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.api.magic.MageController;
import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import com.elmakers.mine.bukkit.api.spell.Spell;
import me.blackvein.quests.CustomRequirement;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class SpellCastRequirement extends CustomRequirement {
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
        this.addData("Spell");
        this.addData("Cast Count");
        this.addDescription("Spell", "The name of the Magic spell the player must have cast.");
        this.addDescription("Cast Count", "How many times the player must have cast the spell.");
    }

    @Override
    public boolean testRequirement(Player player, Map<String, Object> stringObjectMap) {
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
