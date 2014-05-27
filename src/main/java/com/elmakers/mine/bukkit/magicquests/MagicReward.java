package com.elmakers.mine.bukkit.magicquests;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import me.blackvein.quests.CustomReward;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

public abstract class MagicReward extends CustomReward {
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
}
