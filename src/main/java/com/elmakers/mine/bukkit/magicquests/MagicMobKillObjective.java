package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.entity.EntityData;
import com.elmakers.mine.bukkit.api.event.MagicMobDeathEvent;
import com.elmakers.mine.bukkit.api.magic.Mage;

import me.pikamug.quests.Quests;
import me.pikamug.quests.module.BukkitCustomObjective;
import me.pikamug.quests.player.Quester;
import me.pikamug.quests.quests.Quest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Map;

public class MagicMobKillObjective extends BukkitCustomObjective {
    private static Quests quests = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");

    public MagicMobKillObjective() {
        this.setName("Kill Magic Mob");
        this.setAuthor("NathanWolf");
        this.setShowCount(true);
        this.setCountPrompt("Enter the number of times the player must kill this mob:");
        this.addStringPrompt("Mob", "The name or key of the Magic Mob the player must kill.", null);
        this.setDisplay("Kill %Mob%: %count%");
    }

    @EventHandler
    public void onMagicMobKill(MagicMobDeathEvent event) {
        Player player = event.getPlayer();
        Mage mage = event.getMage();
        if (player == null || mage == null) {
            return;
        }

        Quester quester = quests.getQuester(player.getUniqueId());
        if (quester == null) {
            return;
        }

        boolean isDebug = mage.getDebugLevel() > 0;

        EntityData entityData = event.getEntityData();
        
        if (isDebug) mage.sendDebugMessage(ChatColor.BLUE + "Checking mob kill objective for " + ChatColor.GOLD + entityData.describe() +
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

            String mobName = (String)map.get("Mob");
            if (mobName == null) {
                if (isDebug) mage.sendDebugMessage(ChatColor.DARK_RED + "No active Mob data for this objective for quest: " + ChatColor.GOLD + quest.getName(), 7);
                continue;
            }

            if (isDebug) mage.sendDebugMessage(ChatColor.BLUE + "Checking : " + ChatColor.GOLD +
                    quest.getName() + ChatColor.BLUE + " looking for " + ChatColor.YELLOW + mobName, 7);

            if (!mobName.equalsIgnoreCase(ChatColor.stripColor(entityData.getName())) && !mobName.equalsIgnoreCase(entityData.getKey())) continue;

            if (isDebug) mage.sendDebugMessage(ChatColor.GREEN + "Incrementing objective for quest: " + ChatColor.GOLD + quest.getName(), 2);
            incrementObjective(player.getUniqueId(), this, quest, 1);
        }
    }
}
