package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.api.magic.MageController;
import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import me.pikamug.quests.module.BukkitCustomRequirement;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

public class ActiveWandRequirement extends BukkitCustomRequirement {
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

    public ActiveWandRequirement() {
        this.setName("Holding Magic Wand");
        this.setAuthor("NathanWolf");
    }

    @Override
    public boolean testRequirement(UUID uuid, Map<String, Object> stringObjectMap) {
        Player player = Bukkit.getPlayer(uuid);
        MagicAPI api = getAPI(player.getServer());
        MageController controller = api.getController();
        if (controller.isMage(player)) {
            Mage mage = controller.getMage(player);
            return (mage.getActiveWand() != null);
        }

        return false;
    }
}
