package com.lovetropics.minigames.common.dimension.biome;

import com.lovetropics.minigames.Constants;
import com.lovetropics.minigames.common.dimension.biome.minigames.SurviveTheTideBiome;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class TropicraftBiomes {
    
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, Constants.MODID);

    public static final RegistryObject<Biome> SURVIVE_THE_TIDE = register("island_royale", SurviveTheTideBiome::new);
   // public static final Biome TROPICS_LAKE = new TropicsLakeBiome();

    private static final <T extends Biome> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return BIOMES.register(name, sup);
    }

    public static void addFeatures() {
        for (Biome b : ForgeRegistries.BIOMES.getValues()) {
        }
    }
}
