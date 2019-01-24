package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.api.magic.MageController;
import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import me.blackvein.quests.CustomRequirement;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class ActiveWandRequirement extends CustomRequirement {
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
    public boolean testRequirement(Player player, Map<String, Object> stringObjectMap) {
        MagicAPI api = getAPI(player.getServer());
        MageController controller = api.getController();
        if (controller.isMage(player)) {
            Mage mage = controller.getMage(player);
            return (mage.getActiveWand() != null);
        }

        return false;
    }
}
