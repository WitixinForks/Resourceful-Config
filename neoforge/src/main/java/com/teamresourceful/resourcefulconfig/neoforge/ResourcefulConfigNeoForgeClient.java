package com.teamresourceful.resourcefulconfig.neoforge;

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen;
import com.teamresourceful.resourcefulconfig.common.config.Configurations;
import com.teamresourceful.resourcefulconfig.common.utils.ModUtils;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ResourcefulConfigNeoForgeClient {

    public static void onClientInit(ModContainer container) {
        container.registerExtensionPoint(
            IConfigScreenFactory.class,
            (client, parent) -> ResourcefulConfigScreen.getFactory(null).apply(parent)
        );
    }

    public static void onClientComplete() {
        for (String mod : Configurations.INSTANCE.getModIds()) {
            ModContainer container = ModList.get().getModContainerById(mod).orElse(null);
            if (container == null) {
                ModUtils.log("Could not find mod container for mod id '" + mod + "'. Skipping config screen registration.");
                continue;
            }
            if (container.getCustomExtension(IConfigScreenFactory.class).isPresent()) {
                ModUtils.debug("Mod '" + mod + "' already has a config screen factory. Skipping config screen registration.");
                continue;
            }
            container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (ignored, parent) -> ResourcefulConfigScreen.getFactory(mod).apply(parent)
            );
        }
    }
}