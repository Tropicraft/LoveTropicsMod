package com.lovetropics.minigames.common.core.diguise;

import com.lovetropics.minigames.Constants;
import com.lovetropics.minigames.LoveTropics;
import com.lovetropics.minigames.common.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public final class PlayerDisguise implements ICapabilityProvider {
	public static final Capability.IStorage<PlayerDisguise> STORAGE = new Capability.IStorage<PlayerDisguise>() {
		@Nullable
		@Override
		public INBT writeNBT(Capability<PlayerDisguise> capability, PlayerDisguise instance, Direction side) {
			return null;
		}

		@Override
		public void readNBT(Capability<PlayerDisguise> capability, PlayerDisguise instance, Direction side, INBT nbt) {
		}
	};

	private final LazyOptional<PlayerDisguise> instance = LazyOptional.of(() -> this);

	private final PlayerEntity player;
	private Entity disguiseEntity;

	PlayerDisguise(PlayerEntity player) {
		this.player = player;
	}

	@SubscribeEvent
	public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		if (entity instanceof PlayerEntity) {
			event.addCapability(Util.resource("player_disguise"), new PlayerDisguise((PlayerEntity) entity));
		}
	}

	public static LazyOptional<PlayerDisguise> get(PlayerEntity player) {
		return player.getCapability(LoveTropics.playerDisguiseCap());
	}

	@Nullable
	public static Entity getDisguiseEntity(PlayerEntity player) {
		PlayerDisguise disguise = get(player).orElse(null);
		if (disguise != null) {
			return disguise.getDisguiseEntity();
		} else {
			return null;
		}
	}

	public void setDisguiseEntity(@Nullable Entity disguiseEntity) {
		this.disguiseEntity = disguiseEntity;
		this.player.recalculateSize();
	}

	@Nullable
	public Entity getDisguiseEntity() {
		return this.disguiseEntity;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return LoveTropics.playerDisguiseCap().orEmpty(cap, instance);
	}
}
