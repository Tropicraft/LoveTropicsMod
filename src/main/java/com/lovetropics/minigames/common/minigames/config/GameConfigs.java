package com.lovetropics.minigames.common.minigames.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.resources.IResource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class GameConfigs {
    private static final Logger LOGGER = LogManager.getLogger(GameConfigs.class);

    private static final Map<ResourceLocation, GameConfig> GAME_CONFIGS = new HashMap<>();

    private static final JsonParser PARSER = new JsonParser();

    @Nullable
    public static GameConfig byId(ResourceLocation id) {
        return GAME_CONFIGS.get(id);
    }

    public static void init(MinecraftServer server) {
        server.getResourceManager().addReloadListener((stage, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> {
            return CompletableFuture.supplyAsync(() -> {
                GAME_CONFIGS.clear();

                Collection<ResourceLocation> paths = resourceManager.getAllResourceLocations("games", file -> file.endsWith(".json"));
                for (ResourceLocation path : paths) {
                    try (IResource resource = resourceManager.getResource(path)) {
                        GameConfig config = loadConfig(path, resource);
                        GAME_CONFIGS.put(config.id, config);
                    } catch (IOException e) {
                        LOGGER.error("Failed to load game config at {}", path, e);
                    }
                }

                return null;
            }, backgroundExecutor);
        });
    }

    private static GameConfig loadConfig(ResourceLocation path, IResource resource) throws IOException {
        try (InputStream input = resource.getInputStream()) {
            JsonElement json = PARSER.parse(new BufferedReader(new InputStreamReader(input)));
            Dynamic<JsonElement> dynamic = new Dynamic<>(JsonOps.INSTANCE, json);
            return GameConfig.deserialize(getIdFromPath(path), dynamic);
        }
    }

    private static ResourceLocation getIdFromPath(ResourceLocation location) {
        String path = location.getPath();
        String name = path.substring("games/".length(), path.length() - ".json".length());
        return new ResourceLocation(location.getNamespace(), name);
    }
}