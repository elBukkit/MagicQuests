package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.event.CraftWandEvent;
import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.api.wand.Wand;

import me.pikamug.quests.Quests;
import me.pikamug.quests.module.BukkitCustomObjective;
import me.pikamug.quests.player.Quester;
import me.pikamug.quests.quests.Quest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Map;

public class CraftWandObjective extends BukkitCustomObjective {
    private static Quests quests = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");

    public CraftWandObjective() {
        this.setName("Craft Magic Wand");
        this.setAuthor("NathanWolf");
        this.setShowCount(true);
        this.setCountPrompt("Enter the number of times the player must craft the item:");
        this.addStringPrompt("Wand", "The name of the Magic wand the player must craft.", null);
        this.setDisplay("Craft %Wand%: %count%");
    }

    @EventHandler
    public void onCraftWand(CraftWandEvent event) {
        Mage mage = event.getMage();

        Player player = event.getMage().getPlayer();
        if (player == null) return;

        Quester quester = quests.getQuester(player.getUniqueId());
        if (quester == null) {
            return;
        }

        Wand wand = event.getWand();
        boolean isDebug = mage.getDebugLevel() > 0;

        if (isDebug) mage.sendDebugMessage(ChatColor.BLUE + "Checking spell cast objective for crafting of " + ChatColor.GOLD + wand.getName() +
                ChatColor.BLUE + " with " + ChatColor.YELLOW + quester.getCurrentQuests().size() +
                ChatColor.BLUE + " active quests", 5);

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

            String wandName = (String)map.get("Wand");
            if (wandName == null) {
                if (isDebug) mage.sendDebugMessage(ChatColor.DARK_RED + "No active Spell data for this objective for quest: " + ChatColor.GOLD + quest.getName(), 7);
                continue;
            }

            if (isDebug) mage.sendDebugMessage(ChatColor.BLUE + "Checking : " + ChatColor.GOLD +
                    quest.getName() + ChatColor.BLUE + " looking for " + ChatColor.YELLOW + wandName, 7);

            if (!wandName.equalsIgnoreCase(wand.getName()) && !wandName.equalsIgnoreCase(wand.getTemplateKey())) continue;

            if (isDebug) mage.sendDebugMessage(ChatColor.GREEN + "Incrementing objective for quest: " + ChatColor.GOLD + quest.getName(), 2);
            incrementObjective(player.getUniqueId(), this, quest, 1);
        }
    }
}
