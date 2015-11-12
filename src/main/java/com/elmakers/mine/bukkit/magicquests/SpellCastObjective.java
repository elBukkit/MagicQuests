package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.event.CastEvent;
import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.api.spell.Spell;
import com.elmakers.mine.bukkit.api.spell.SpellResult;
import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;
import org.bukkit.ChatColor;
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
        this.addDescription("Spell", "The name of the Magic spell the player must cast.");
        this.setDisplay("Cast %Spell%: %count%");
    }

    @EventHandler
    public void onSpellCast(CastEvent event) {
        Mage mage = event.getMage();

        mage.sendDebugMessage(ChatColor.GRAY + "Checking spell cast objective for cast of " + ChatColor.GOLD + event.getSpell().getName() +
                ChatColor.BLUE + " with result of " + ChatColor.YELLOW + event.getSpellResult());

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

        mage.sendDebugMessage(ChatColor.BLUE + "Checking spell cast objective for cast of " + ChatColor.GOLD + event.getSpell().getName() +
                ChatColor.BLUE + " with " + ChatColor.YELLOW + quester.currentQuests.size() +
                ChatColor.BLUE + " active quests", 5);

        for (Quest quest : quester.currentQuests.keySet()) {
            Map<String, Object> map = null;
            try {
                map = getDatamap(player, this, quest);
            } catch (Exception ex) {
                map = null;
            }
            if (map == null) {
                mage.sendDebugMessage("No active data for this objective for quest: " + ChatColor.GOLD + quest.getName(), 5);
                continue;
            }

            String spellName = (String)map.get("Spell");
            if (spellName == null) {
                mage.sendDebugMessage(ChatColor.RED + "No active data for this objective for quest: " + ChatColor.GOLD + quest.getName(), 5);
                return;
            }

            Spell spell = event.getSpell();
            if (!spellName.equalsIgnoreCase(spell.getName()) && !spellName.equalsIgnoreCase(spell.getKey())) break;

            mage.sendDebugMessage(ChatColor.GREEN + "Incrementing objective for quest: " + ChatColor.GOLD + quest.getName(), 5);
            incrementObjective(player, this, 1, quest);
        }
    }
}
