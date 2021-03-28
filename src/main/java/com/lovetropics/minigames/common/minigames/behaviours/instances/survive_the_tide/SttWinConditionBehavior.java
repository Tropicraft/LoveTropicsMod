package com.lovetropics.minigames.common.minigames.behaviours.instances.survive_the_tide;

import com.lovetropics.minigames.common.minigames.IMinigameInstance;
import com.lovetropics.minigames.common.minigames.MinigameManager;
import com.lovetropics.minigames.common.minigames.PlayerSet;
import com.lovetropics.minigames.common.minigames.TemplatedText;
import com.lovetropics.minigames.common.minigames.behaviours.IMinigameBehavior;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;

public abstract class SttWinConditionBehavior implements IMinigameBehavior {
	protected boolean minigameEnded;
	private long minigameEndedTimer = 0;
	protected final long gameFinishTickDelay;
	private ITextComponent winner;
	protected final Long2ObjectMap<TemplatedText> scheduledGameFinishMessages;
	protected final boolean spawnLightningBoltsOnFinish;
	protected final int lightningBoltSpawnTickRate;

	public SttWinConditionBehavior(final long gameFinishTickDelay, final Long2ObjectMap<TemplatedText> scheduledGameFinishMessages, final boolean spawnLightningBoltsOnFinish, final int lightningBoltSpawnTickRate) {
		this.gameFinishTickDelay = gameFinishTickDelay;
		this.scheduledGameFinishMessages = scheduledGameFinishMessages;
		this.spawnLightningBoltsOnFinish = spawnLightningBoltsOnFinish;
		this.lightningBoltSpawnTickRate = lightningBoltSpawnTickRate;
	}

	protected final void triggerWin(ITextComponent winner) {
		this.winner = winner;
		this.minigameEnded = true;
		this.minigameEndedTimer = 0;
	}

	@Override
	public void worldUpdate(final IMinigameInstance minigame, ServerWorld world) {
		this.checkForGameEndCondition(minigame, world);
	}

	@Override
	public void onFinish(final IMinigameInstance minigame) {
		this.minigameEnded = false;
		this.minigameEndedTimer = 0;
	}

	private void checkForGameEndCondition(final IMinigameInstance minigame, final World world) {
		if (this.minigameEnded) {
			if (spawnLightningBoltsOnFinish) {
				spawnLightningBoltsEverywhere(minigame, world);
			}

			sendGameFinishMessages(minigame);

			if (this.minigameEndedTimer >= gameFinishTickDelay) {
				MinigameManager.getInstance().finish();
			}

			this.minigameEndedTimer++;
		}
	}

	private void spawnLightningBoltsEverywhere(IMinigameInstance minigame, final World world) {
		if (this.minigameEndedTimer % lightningBoltSpawnTickRate == 0) {
			PlayerSet participants = minigame.getParticipants();
			if (participants.isEmpty()) {
				return;
			}

			for (ServerPlayerEntity player : participants) {
				int xOffset = (7 + world.rand.nextInt(5)) * (world.rand.nextBoolean() ? 1 : -1);
				int zOffset = (7 + world.rand.nextInt(5)) * (world.rand.nextBoolean() ? 1 : -1);

				int posX = MathHelper.floor(player.getPosX()) + xOffset;
				int posZ = MathHelper.floor(player.getPosZ()) + zOffset;

				int posY = world.getHeight(Heightmap.Type.MOTION_BLOCKING, posX, posZ);

				LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
				lightning.forceSetPosition(posX, posY, posZ);
				lightning.setEffectOnly(true);

				world.addEntity(lightning);
			}
		}
	}

	private void sendGameFinishMessages(final IMinigameInstance minigame) {
		if (scheduledGameFinishMessages.containsKey(minigameEndedTimer)) {
			final TemplatedText message = scheduledGameFinishMessages.get(minigameEndedTimer);
			minigame.getPlayers().sendMessage(message.apply(winner));
		}
	}
}
