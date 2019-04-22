package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.api.magic.MageClass;
import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import me.blackvein.quests.CustomReward;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class MagicSpellReward extends CustomReward {

    // This boilerplate cannot be abstracted due to the class-scanning that Quests does
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

    public MagicSpellReward() {
        this.setName("Magic Spell");
        this.setAuthor("NathanWolf");
        this.setRewardName("Spell");
        this.addStringPrompt("Spell", "The KEY name of the Magic spell to give to the player.", null);
        this.addStringPrompt("Class", "The KEY name of the class to apply the spell to. If not set will be given to the player as an item.", null);
    }

    @Override
    public void giveReward(Player player, Map<String, Object> stringObjectMap) {
        MagicAPI api = getAPI(player.getServer());
        String spellKey = (String)stringObjectMap.get("Spell");
        String classKey = (String)stringObjectMap.get("Class");
        if (spellKey != null) {
            if (classKey != null) {
                Mage mage = api.getController().getMage(player);
                if (mage != null) {
                    MageClass mageClass = mage.getClass(classKey);
                    if (mageClass != null) {
                        mageClass.addSpell(spellKey);
                    } else {
                        Bukkit.getLogger().warning("Player " + player.getName() + " wants a quest reward of spell " + spellKey + " but does not have class " + mageClass);
                    }
                }
            } else {
                ItemStack item = api.createSpellItem(spellKey);
                if (item != null) {
                    api.giveItemToPlayer(player, item);
                } else {
                    Bukkit.getLogger().warning("Invalid spell given as Quests reward: " + spellKey);
                }
            }
        }
    }
}
