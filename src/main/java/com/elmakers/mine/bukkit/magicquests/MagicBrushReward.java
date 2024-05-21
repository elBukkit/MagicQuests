package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import me.pikamug.quests.module.BukkitCustomReward;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

public class MagicBrushReward extends BukkitCustomReward {

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

    public MagicBrushReward() {
        this.setName("Magic Brush");
        this.setAuthor("NathanWolf");
        this.setDisplay("Material Brush");
        this.addStringPrompt("Brush", "The key name of the Magic material brush to give to the player.", null);
    }

    @Override
    public void giveReward(UUID uuid, Map<String, Object> stringObjectMap) {
        Player player = Bukkit.getPlayer(uuid);
        MagicAPI api = getAPI(player.getServer());
        String brushKey = (String)stringObjectMap.get("Brush");
        if (brushKey != null) {
            ItemStack item = api.createBrushItem(brushKey);
            if (item != null) {
                api.giveItemToPlayer(player, item);
            } else {
                Bukkit.getLogger().warning("Invalid brush given as Quests reward: " + brushKey);
            }
        }
    }
}
