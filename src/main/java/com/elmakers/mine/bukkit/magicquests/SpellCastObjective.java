package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.event.CastEvent;
import com.elmakers.mine.bukkit.api.spell.Spell;
import com.elmakers.mine.bukkit.api.spell.SpellResult;
import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Map;

public class SpellCastObjective extends CustomObjective {

    public SpellCastObjective() {
        this.setName("Magic Spell Cast");
        this.setAuthor("NathanWolf");
        this.setEnableCount(true);
        this.setShowCount(true);
        this.addData("Spell");
        this.setCountPrompt("Enter the number of times the player must cast the spell:");
        this.addDescription("Spell", "The name of the Magic spell the player must have cast.");
        this.setDisplay("Cast %Spell%: %count%");
    }

    @EventHandler
    public void onSpellCast(CastEvent event) {
        Player player = event.getMage().getPlayer();
        if (player == null) return;

        if (event.getSpellResult() != SpellResult.CAST) return;

        // This fails in a pretty bad way if the player is not on a quest currently
        // There isn't a super clean way to check for this state so we'll just catch
        // and ignore expections :\
        Quester quester = Quests.getInstance().getQuester(player.getUniqueId());
        if (quester == null) {
            return;
        }

        for (Quest quest : quester.currentQuests.keySet()) {
            Map<String, Object> map = null;
            try {
                map = getDatamap(player, this, quest);
            } catch (Exception ex) {
                map = null;
            }
            if (map == null) continue;

            String spellName = (String)map.get("Spell");
            if (spellName == null) return;

            Spell spell = event.getSpell();
            if (!spellName.equalsIgnoreCase(spell.getName()) && !spellName.equalsIgnoreCase(spell.getKey())) return;

            incrementObjective(player, this, 1, quest);
        }
    }
}
