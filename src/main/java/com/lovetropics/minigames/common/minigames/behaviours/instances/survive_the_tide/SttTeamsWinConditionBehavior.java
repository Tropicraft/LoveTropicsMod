package com.lovetropics.minigames.common.minigames.behaviours.instances.survive_the_tide;

import com.lovetropics.minigames.common.MoreCodecs;
import com.lovetropics.minigames.common.minigames.IMinigameInstance;
import com.lovetropics.minigames.common.minigames.TemplatedText;
import com.lovetropics.minigames.common.minigames.behaviours.MinigameBehaviorTypes;
import com.lovetropics.minigames.common.minigames.behaviours.instances.TeamsBehavior;
import com.lovetropics.minigames.common.minigames.statistics.StatisticKey;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import javax.annotation.Nullable;
import java.util.Optional;

public class SttTeamsWinConditionBehavior extends SttWinConditionBehavior {
	public static final Codec<SttTeamsWinConditionBehavior> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.LONG.optionalFieldOf("game_finish_tick_delay", 0L).forGetter(c -> c.gameFinishTickDelay),
				MoreCodecs.long2Object(TemplatedText.CODEC).fieldOf("scheduled_game_finish_messages").forGetter(c -> c.scheduledGameFinishMessages),
				Codec.BOOL.optionalFieldOf("spawn_lightning_bolts_on_finish", false).forGetter(c -> c.spawnLightningBoltsOnFinish),
				Codec.INT.optionalFieldOf("lightning_bolt_spawn_tick_rate", 60).forGetter(c -> c.lightningBoltSpawnTickRate)
		).apply(instance, SttTeamsWinConditionBehavior::new);
	});

	public SttTeamsWinConditionBehavior(final long gameFinishTickDelay, final Long2ObjectMap<TemplatedText> scheduledGameFinishMessages, final boolean spawnLightningBoltsOnFinish, final int lightningBoltSpawnTickRate) {
		super(gameFinishTickDelay, scheduledGameFinishMessages, spawnLightningBoltsOnFinish, lightningBoltSpawnTickRate);
	}

	@Override
	public void onPlayerDeath(final IMinigameInstance minigame, ServerPlayerEntity player, LivingDeathEvent event) {
		if (minigameEnded) {
			return;
		}

		Optional<TeamsBehavior> teamsBehaviorOpt = minigame.getOneBehavior(MinigameBehaviorTypes.TEAMS.get());
		if (!teamsBehaviorOpt.isPresent()) {
			return;
		}

		TeamsBehavior teamsBehavior = teamsBehaviorOpt.get();

		TeamsBehavior.TeamKey playerTeam = teamsBehavior.getTeamForPlayer(player);
		if (teamsBehavior.getPlayersForTeam(playerTeam).isEmpty()) {
			TeamsBehavior.TeamKey lastTeam = getLastTeam(teamsBehavior);
			if (lastTeam != null) {
				triggerWin(new StringTextComponent(lastTeam.name).mergeStyle(lastTeam.text));
				minigame.getStatistics().getGlobal().set(StatisticKey.WINNING_TEAM, lastTeam);
			}
		}
	}

	@Nullable
	private TeamsBehavior.TeamKey getLastTeam(TeamsBehavior teamBehavior) {
		TeamsBehavior.TeamKey lastTeam = null;
		for (TeamsBehavior.TeamKey team : teamBehavior.getTeams()) {
			if (teamBehavior.getPlayersForTeam(team).isEmpty()) {
				continue;
			}

			if (lastTeam != null) {
				return null;
			} else {
				lastTeam = team;
			}
		}

		return lastTeam;
	}
}
