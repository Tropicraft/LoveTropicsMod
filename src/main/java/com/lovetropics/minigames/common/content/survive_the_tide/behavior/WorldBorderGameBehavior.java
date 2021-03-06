package com.lovetropics.minigames.common.content.survive_the_tide.behavior;

import com.lovetropics.minigames.common.core.game.GameException;
import com.lovetropics.minigames.common.core.game.IActiveGame;
import com.lovetropics.minigames.common.core.game.behavior.IGameBehavior;
import com.lovetropics.minigames.common.core.game.behavior.event.EventRegistrar;
import com.lovetropics.minigames.common.core.game.behavior.event.GameLifecycleEvents;
import com.lovetropics.minigames.common.core.game.util.GameBossBar;
import com.lovetropics.minigames.common.core.map.MapRegion;
import com.lovetropics.minigames.common.util.MoreCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class WorldBorderGameBehavior implements IGameBehavior
{
	public static final Codec<WorldBorderGameBehavior> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				MoreCodecs.TEXT.fieldOf("name").forGetter(c -> c.name),
				Codec.STRING.fieldOf("world_border_center").forGetter(c -> c.worldBorderCenterKey),
				MoreCodecs.TEXT.fieldOf("collapse_message").forGetter(c -> c.collapseMessage),
				Codec.LONG.fieldOf("ticks_until_start").forGetter(c -> c.ticksUntilStart),
				Codec.LONG.fieldOf("delay_until_collapse").forGetter(c -> c.delayUntilCollapse),
				Codec.INT.fieldOf("particle_rate_delay").forGetter(c -> c.particleRateDelay),
				Codec.INT.fieldOf("particle_height").forGetter(c -> c.particleHeight),
				Codec.INT.fieldOf("damage_rate_delay").forGetter(c -> c.damageRateDelay),
				Codec.INT.fieldOf("damage_amount").forGetter(c -> c.damageAmount),
				ParticleTypes.CODEC.optionalFieldOf("border_particle", ParticleTypes.EXPLOSION).forGetter(c -> c.borderParticle)
		).apply(instance, WorldBorderGameBehavior::new);
	});

	private final ITextComponent name;
	private final String worldBorderCenterKey;
	private final ITextComponent collapseMessage;
	private final long ticksUntilStart;
	private final long delayUntilCollapse;
	private final int particleRateDelay;
	private final int particleHeight;
	private final int damageRateDelay;
	private final int damageAmount;
	private final IParticleData borderParticle;

	private BlockPos worldBorderCenter = BlockPos.ZERO;

	private boolean borderCollapseMessageSent = false;
	private final GameBossBar bossBar;

	public WorldBorderGameBehavior(final ITextComponent name, final String worldBorderCenterKey, final ITextComponent collapseMessage, final long ticksUntilStart,
								   final long delayUntilCollapse, final int particleRateDelay, final int particleHeight, final int damageRateDelay, final int damageAmount, final IParticleData borderParticle) {
		this.name = name;
		this.worldBorderCenterKey = worldBorderCenterKey;
		this.collapseMessage = collapseMessage;
		this.ticksUntilStart = ticksUntilStart;
		this.delayUntilCollapse = delayUntilCollapse;
		this.particleRateDelay = particleRateDelay;
		this.particleHeight = particleHeight;
		this.damageRateDelay = damageRateDelay;
		this.damageAmount = damageAmount;
		this.borderParticle = borderParticle;

		this.bossBar = new GameBossBar(name, BossInfo.Color.WHITE, BossInfo.Overlay.PROGRESS);
	}

	@Override
	public void register(IActiveGame game, EventRegistrar events) throws GameException {
		List<MapRegion> regions = new ArrayList<>(game.getMapRegions().get(worldBorderCenterKey));

		if (!regions.isEmpty()) {
			MapRegion centerRegion = regions.get(game.getWorld().getRandom().nextInt(regions.size()));
			worldBorderCenter = centerRegion.getCenterBlock();
		} else {
			worldBorderCenter = BlockPos.ZERO;
		}

		events.listen(GameLifecycleEvents.STOP, this::onFinish);
		events.listen(GameLifecycleEvents.TICK, this::tickWorldBorder);
	}

	private void onFinish(final IActiveGame game) {
		borderCollapseMessageSent = false;
		bossBar.close();
	}

	// TODO: Clean up this mess
	private void tickWorldBorder(final IActiveGame game) {
		if (game.ticks() < ticksUntilStart) {
			return;
		}

		if (!borderCollapseMessageSent) {
			borderCollapseMessageSent = true;
			game.getAllPlayers().sendMessage(collapseMessage);
		}

		long ticksSinceStart = game.ticks() - ticksUntilStart;

		boolean isCollapsing = game.ticks() >= ticksUntilStart + delayUntilCollapse;
		float borderPercent = 0.01F;
		if (!isCollapsing) {
			borderPercent = 1F - ((float) (ticksSinceStart + 1) / (float) delayUntilCollapse);
			borderPercent = Math.max(borderPercent, 0.01F);
		}

		bossBar.setProgress(borderPercent);

		float maxRadius = 210;
		float currentRadius = maxRadius * borderPercent;

		//particle spawning
		if (game.ticks() % particleRateDelay == 0) {
			tickParticles(currentRadius, game.getWorld());
		}

		//player damage
		if (game.ticks() % damageRateDelay == 0) {
			tickPlayerDamage(game, isCollapsing, currentRadius);
		}

		//instance.getParticipants()
		//this.actionAllParticipants(instance, (p) -> p.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 10 * 20)));

		//world.addParticle(borderParticle, );
	}

	private void tickParticles(float currentRadius, ServerWorld world) {
		float amountPerCircle = 4 * currentRadius;
		float stepAmount = 360F / amountPerCircle;

		int yMin = -10;
		int yStepAmount = 5;

		int randSpawn = 30;

		for (float step = 0; step <= 360; step += stepAmount) {
			for (int yStep = yMin; yStep < particleHeight; yStep += yStepAmount) {
				if (world.rand.nextInt(randSpawn/*yMax - yMin*/) == 0) {
					float xVec = (float) -Math.sin(Math.toRadians(step)) * currentRadius;
					float zVec = (float) Math.cos(Math.toRadians(step)) * currentRadius;
					//world.addParticle(borderParticle, worldBorderCenter.getX() + xVec, worldBorderCenter.getY() + yStep, worldBorderCenter.getZ() + zVec, 0, 0, 0);
					//IParticleData data = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("heart"));
					world.spawnParticle(borderParticle, worldBorderCenter.getX() + xVec, worldBorderCenter.getY() + yStep, worldBorderCenter
							.getZ() + zVec, 1, 0, 0, 0, 1D);
				}

			}
		}

		//serverWorld.spawnParticle(borderParticle, worldBorderCenter.getX(), worldBorderCenter.getY(), worldBorderCenter.getZ(), 1, 0, 0, 0, 1D);
	}

	private void tickPlayerDamage(IActiveGame game, boolean isCollapsing, float currentRadius) {
		for (ServerPlayerEntity player : game.getParticipants()) {
			//ignore Y val, only do X Z dist compare
			double distanceSq = player.getDistanceSq(worldBorderCenter.getX(), player.getPosY(), worldBorderCenter.getZ());
			if (isCollapsing || !(currentRadius < 0.0 || distanceSq < currentRadius * currentRadius)) {
				player.attackEntityFrom(DamageSource.causeExplosionDamage((LivingEntity) null), damageAmount);
				player.addPotionEffect(new EffectInstance(Effects.NAUSEA, 40, 0));
			}

			//add boss bar info to everyone in dim if not already registered for it
			bossBar.addPlayer(player);
		}
	}
}
