package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.event.AddSpellEvent;
import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.api.spell.SpellTemplate;
import com.elmakers.mine.bukkit.api.wand.Wand;
import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Map;

public class AddSpellObjective extends CustomObjective {
    // .... why did Quests.getInstance need to be made non-static.. ??
    private static Quests quests = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");


    public AddSpellObjective() {
        this.setName("Add Magic Spell");
        this.setAuthor("NathanWolf");
        this.setShowCount(true);
        this.setCountPrompt("Enter the number of times the player must learn the spell:");
        this.addStringPrompt("Spell", "The name of the Magic spell the player must learn.", null);
        this.setDisplay("Learn %Spell%");
    }

    @EventHandler
    public void onAddSpell(AddSpellEvent event) {
        Mage mage = event.getMage();
        Player player = mage.getPlayer();
        if (player == null) return;

        // This fails in a pretty bad way if the player is not on a quest currently
        // There isn't a super clean way to check for this state so we'll just catch
        // and ignore expections :\
        Quester quester = quests.getQuester(player.getUniqueId());
        if (quester == null) {
            return;
        }
        boolean isDebug = mage.getDebugLevel() > 0;

        if (isDebug) mage.sendDebugMessage(ChatColor.BLUE + "Checking add spell objective for add of " + ChatColor.GOLD + event.getSpell().getName() +
                ChatColor.BLUE + " with " + ChatColor.YELLOW + quester.getCurrentQuests().size() +
                ChatColor.BLUE + " active quests", 5);

        SpellTemplate spell = event.getSpell();
        for (Quest quest : quester.getCurrentQuests().keySet()) {
            Map<String, Object> map = null;
            try {
                map = getDataForPlayer(player, this, quest);
            } catch (Exception ex) {
                map = null;
            }
            if (map == null) {
                if (isDebug) mage.sendDebugMessage(ChatColor.DARK_RED + "No active data for this objective for quest: " + ChatColor.GOLD + quest.getName(), 7);
                continue;
            }

            String spellName = (String)map.get("Spell");
            if (spellName == null) {
                if (isDebug) mage.sendDebugMessage(ChatColor.DARK_RED + "No active Spell data for this objective for quest: " + ChatColor.GOLD + quest.getName(), 7);
                continue;
            }

            if (isDebug) mage.sendDebugMessage(ChatColor.BLUE + "Checking : " + ChatColor.GOLD +
                    quest.getName() + ChatColor.BLUE + " looking for " + ChatColor.YELLOW + spellName, 7);

            if (!spellName.equalsIgnoreCase("any") && !spellName.equalsIgnoreCase(spell.getName()) && !spellName.equalsIgnoreCase(spell.getKey())) {
                // Check for wand already having spell
                Wand wand = event.getWand();
                if (wand == null) {
                    continue;
                }
                if (!wand.hasSpell(spellName.toLowerCase())) {
                    continue;
                }

                if (isDebug) mage.sendDebugMessage(ChatColor.BLUE + "Player already has " + ChatColor.YELLOW + spellName, 7);
            }

            if (isDebug) mage.sendDebugMessage(ChatColor.GREEN + "Incrementing objective for quest: " + ChatColor.GOLD + quest.getName(), 2);
            incrementObjective(player, this, 1, quest);
        }
    }
}
