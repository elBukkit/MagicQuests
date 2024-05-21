package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.event.SpellUpgradeEvent;
import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.api.spell.SpellTemplate;

import me.pikamug.quests.Quests;
import me.pikamug.quests.module.BukkitCustomObjective;
import me.pikamug.quests.player.Quester;
import me.pikamug.quests.quests.Quest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Map;

public class SpellUpgradeObjective extends BukkitCustomObjective {
    private static Quests quests = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");

    public SpellUpgradeObjective() {
        this.setName("Magic Spell Upgrade");
        this.setAuthor("NathanWolf");
        this.setShowCount(true);
        this.setCountPrompt("Enter the number of times the player must upgrade the spell:");
        this.addStringPrompt("Spell", "The name of the Magic spell the player must upgrade.", null);
        this.setDisplay("Upgrade %Spell%: %count%");
    }

    @EventHandler
    public void onSpellUpgrade(SpellUpgradeEvent event) {
        Mage mage = event.getMage();

        Player player = event.getMage().getPlayer();
        if (player == null) return;

        Quester quester = quests.getQuester(player.getUniqueId());
        if (quester == null) {
            return;
        }

        boolean isDebug = mage.getDebugLevel() > 0;

        if (isDebug) mage.sendDebugMessage(ChatColor.BLUE + "Checking spell cast objective for upgrade of " + ChatColor.GOLD + event.getOldSpell().getName() +
                ChatColor.BLUE + " with " + ChatColor.YELLOW + quester.getCurrentQuests().size() +
                ChatColor.BLUE + " active quests", 5);

        SpellTemplate spell = event.getOldSpell();
        for (Quest quest : quester.getCurrentQuests().keySet()) {
            Map<String, Object> map = null;
            try {
                map = getDataForPlayer(player.getUniqueId(), this, quest);
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

            if (!spellName.equalsIgnoreCase(spell.getName()) && !spellName.equalsIgnoreCase(spell.getKey())) continue;

            if (isDebug) mage.sendDebugMessage(ChatColor.GREEN + "Incrementing objective for quest: " + ChatColor.GOLD + quest.getName(), 2);
            incrementObjective(player.getUniqueId(), this, quest, 1);
        }
    }
}
