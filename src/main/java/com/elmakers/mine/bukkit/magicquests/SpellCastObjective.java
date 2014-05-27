package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.event.CastEvent;
import com.elmakers.mine.bukkit.api.spell.Spell;
import me.blackvein.quests.CustomObjective;
import org.bukkit.Bukkit;
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
        this.setCountPrompt("Enter the number of times the player must cast the spell:");
        this.addDescription("Spell", "The name of the Magic spell the player must have cast.");
        this.setDisplay("Cast %Spell%: %count%");
    }

    @EventHandler
    public void onSpellCast(CastEvent event) {
        Player player = event.getMage().getPlayer();
        if (player == null) return;

        Map<String, Object> map = getDatamap(player, this);
        String spellName = (String)map.get("Spell");
        if (spellName == null) return;

        Spell spell = event.getSpell();
        if (!spellName.equalsIgnoreCase(spell.getName()) && !spellName.equalsIgnoreCase(spell.getKey())) return;

        incrementObjective(player, this, 1);
    }
}
