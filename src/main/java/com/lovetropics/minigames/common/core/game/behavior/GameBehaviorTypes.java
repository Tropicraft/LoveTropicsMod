package com.lovetropics.minigames.common.core.game.behavior;

import com.lovetropics.minigames.Constants;
import com.lovetropics.minigames.common.content.survive_the_tide.behavior.*;
import com.lovetropics.minigames.common.core.game.behavior.instances.*;
import com.lovetropics.minigames.common.content.build_competition.PollFinalistsBehavior;
import com.lovetropics.minigames.common.content.conservation_exploration.ConservationExplorationBehavior;
import com.lovetropics.minigames.common.core.game.behavior.instances.command.*;
import com.lovetropics.minigames.common.core.game.behavior.instances.donation.*;
import com.lovetropics.minigames.common.core.game.behavior.instances.statistics.*;
import com.lovetropics.minigames.common.core.game.behavior.instances.tweak.*;
import com.lovetropics.minigames.common.content.trash_dive.PlaceTrashBehavior;
import com.lovetropics.minigames.common.content.trash_dive.TrashCollectionBehavior;
import com.mojang.serialization.Codec;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class GameBehaviorTypes {
	public static final DeferredRegister<GameBehaviorType<?>> REGISTER = DeferredRegister.create(GameBehaviorType.wildcardType(), Constants.MODID);
	public static final Supplier<IForgeRegistry<GameBehaviorType<?>>> REGISTRY;

	public static final RegistryObject<GameBehaviorType<PositionPlayersGameBehavior>> POSITION_PLAYERS;
	public static final RegistryObject<GameBehaviorType<SurviveTheTideWeatherBehavior>> WEATHER_EVENTS;
	public static final RegistryObject<GameBehaviorType<TimedGameBehavior>> TIMED;
	public static final RegistryObject<GameBehaviorType<RespawnSpectatorGameBehavior>> RESPAWN_SPECTATOR;
	public static final RegistryObject<GameBehaviorType<CommandEventsBehavior>> COMMANDS;
	public static final RegistryObject<GameBehaviorType<IsolatePlayerStateBehavior>> ISOLATE_PLAYER_STATE;
	public static final RegistryObject<GameBehaviorType<SetGameTypesBehavior>> SET_GAME_TYPES;
	public static final RegistryObject<GameBehaviorType<PhasesGameBehavior>> PHASES;
	public static final RegistryObject<GameBehaviorType<RisingTidesGameBehavior>> RISING_TIDES;
	public static final RegistryObject<GameBehaviorType<ScheduledMessagesBehavior>> SCHEDULED_MESSAGES;
	public static final RegistryObject<GameBehaviorType<WorldBorderGameBehavior>> WORLD_BORDER;
	public static final RegistryObject<GameBehaviorType<SttIndividualsWinConditionBehavior>> SURVIVE_THE_TIDE_INDIVIDUALS_WIN_CONDITION;
	public static final RegistryObject<GameBehaviorType<SttTeamsWinConditionBehavior>> SURVIVE_THE_TIDE_TEAMS_WIN_CONDITION;
	public static final RegistryObject<GameBehaviorType<FireworksOnDeathBehavior>> FIREWORKS_ON_DEATH;
	public static final RegistryObject<GameBehaviorType<SurviveTheTideRulesetBehavior>> SURVIVE_THE_TIDE_RULESET;
	public static final RegistryObject<GameBehaviorType<BindControlsBehavior>> BIND_CONTROLS;
	public static final RegistryObject<GameBehaviorType<CancelPlayerDamageBehavior>> CANCEL_PLAYER_DAMAGE;
	public static final RegistryObject<GameBehaviorType<ConservationExplorationBehavior>> CONSERVATION_EXPLORATION;
	public static final RegistryObject<GameBehaviorType<SetGameRulesBehavior>> SET_GAME_RULES;
	public static final RegistryObject<GameBehaviorType<PlaceTrashBehavior>> PLACE_TRASH;
	public static final RegistryObject<GameBehaviorType<TrashCollectionBehavior>> TRASH_COLLECTION;
	public static final RegistryObject<GameBehaviorType<TeamsBehavior>> TEAMS;
	public static final RegistryObject<GameBehaviorType<SpectatorChaseBehavior>> SPECTATOR_CHASE;
	public static final RegistryObject<GameBehaviorType<ForceLoadRegionBehavior>> FORCE_LOAD_REGION;
	public static final RegistryObject<GameBehaviorType<DeleteBlocksBehavior>> DELETE_BLOCKS;
	public static final RegistryObject<GameBehaviorType<EliminatePlayerControlBehavior>> ELIMINATE_PLAYER_CONTROL;
	public static final RegistryObject<GameBehaviorType<PlaceSttChestsGameBehavior>> PLACE_STT_CHESTS;
	public static final RegistryObject<GameBehaviorType<GenerateEntitiesBehavior>> GENERATE_ENTITIES;
	public static final RegistryObject<GameBehaviorType<WeatherControlsBehavior>> WEATHER_CONTROLS;
	public static final RegistryObject<GameBehaviorType<RunCommandInRegionBehavior>> RUN_COMMAND_IN_REGION;
	public static final RegistryObject<GameBehaviorType<TntAutoFuseBehavior>> TNT_AUTO_FUSE;
	public static final RegistryObject<GameBehaviorType<DisableHungerBehavior>> DISABLE_HUNGER;
	public static final RegistryObject<GameBehaviorType<DisableTntDestructionBehavior>> DISABLE_TNT_BLOCK_DESTRUCTION;
	public static final RegistryObject<GameBehaviorType<RevealPlayersBehavior>> REVEAL_PLAYERS;
	public static final RegistryObject<GameBehaviorType<SetMaxHealthBehavior>> SET_MAX_HEALTH;

	public static final RegistryObject<GameBehaviorType<BindObjectiveToStatisticBehavior>> BIND_OBJECTIVE_TO_STATISTIC;
	public static final RegistryObject<GameBehaviorType<PlaceByStatisticBehavior>> PLACE_BY_STATISTIC;
	public static final RegistryObject<GameBehaviorType<PlaceByDeathOrderBehavior>> PLACE_BY_DEATH_ORDER;
	public static final RegistryObject<GameBehaviorType<CampingTrackerBehavior>> CAMPING_TRACKER;
	public static final RegistryObject<GameBehaviorType<CauseOfDeathTrackerBehavior>> CAUSE_OF_DEATH_TRACKER;
	public static final RegistryObject<GameBehaviorType<KillsTrackerBehavior>> KILLS_TRACKER;
	public static final RegistryObject<GameBehaviorType<TimeSurvivedTrackerBehavior>> TIME_SURVIVED_TRACKER;
	public static final RegistryObject<GameBehaviorType<DamageTrackerBehavior>> DAMAGE_TRACKER;
	public static final RegistryObject<GameBehaviorType<BlocksBrokenTrackerBehavior>> BLOCKS_BROKEN_TRACKER;

	public static final RegistryObject<GameBehaviorType<DisplayLeaderboardOnFinishBehavior<?>>> DISPLAY_LEADERBOARD_ON_FINISH;
	public static final RegistryObject<GameBehaviorType<LootPackageBehavior>> LOOT_PACKAGE;
	public static final RegistryObject<GameBehaviorType<EffectPackageBehavior>> EFFECT_PACKAGE;
	public static final RegistryObject<GameBehaviorType<SwapPlayersPackageBehavior>> SWAP_PLAYERS_PACKAGE;
	public static final RegistryObject<GameBehaviorType<SpawnEntityAtPlayerPackageBehavior>> SPAWN_ENTITY_AT_PLAYER_PACKAGE;
	public static final RegistryObject<GameBehaviorType<SpawnEntityAtRegionsPackageBehavior>> SPAWN_ENTITY_AT_REGIONS_PACKAGE;
	public static final RegistryObject<GameBehaviorType<SpawnEntitiesAroundPlayersPackageBehavior>> SPAWN_ENTITIES_AROUND_PLAYERS_PACKAGE;
	public static final RegistryObject<GameBehaviorType<SpawnEntitiesAtRegionsOverTimePackageBehavior>> SPAWN_ENTITIES_AT_REGIONS_OVER_TIME_PACKAGE;
	public static final RegistryObject<GameBehaviorType<SetBlockAtPlayerPackageBehavior>> SET_BLOCK_AT_PLAYER_PACKAGE;
	public static final RegistryObject<GameBehaviorType<ForcedPlayerHeadPackageBehavior>> FORCED_PLAYER_HEAD_PACKAGE;
	public static final RegistryObject<GameBehaviorType<PufferfishPackageBehavior>> PUFFERFISH_PACKAGE;
	public static final RegistryObject<GameBehaviorType<ShootProjectilesAroundAllPlayersPackageBehavior>> SHOOT_PROJECTILES_AT_ALL_PLAYERS;
	public static final RegistryObject<GameBehaviorType<ShootProjectilesAroundPlayerPackageBehavior>> SHOOT_PROJECTILES_AT_PLAYER;

	public static final RegistryObject<GameBehaviorType<PollFinalistsBehavior>> POLL_FINALISTS;

	public static <T extends IGameBehavior> RegistryObject<GameBehaviorType<T>> register(final String name, final Codec<T> codec) {
		return REGISTER.register(name, () -> new GameBehaviorType<>(codec));
	}

	static {
		REGISTRY = REGISTER.makeRegistry("minigame_behaviours", () -> {
			return new RegistryBuilder<GameBehaviorType<?>>()
					.disableSync()
					.disableSaving();
		});

		POSITION_PLAYERS = register("position_players", PositionPlayersGameBehavior.CODEC);
		WEATHER_EVENTS = register("weather_events", SurviveTheTideWeatherBehavior.CODEC);
		TIMED = register("timed", TimedGameBehavior.CODEC);
		RESPAWN_SPECTATOR = register("respawn_spectator", RespawnSpectatorGameBehavior.CODEC);
		COMMANDS = register("commands", CommandEventsBehavior.CODEC);
		ISOLATE_PLAYER_STATE = register("isolate_player_state", IsolatePlayerStateBehavior.CODEC);
		SET_GAME_TYPES = register("set_game_types", SetGameTypesBehavior.CODEC);
		PHASES = register("phases", PhasesGameBehavior.CODEC);
		RISING_TIDES = register("rising_tides", RisingTidesGameBehavior.CODEC);
		SCHEDULED_MESSAGES = register("scheduled_messages", ScheduledMessagesBehavior.CODEC);
		WORLD_BORDER = register("world_border", WorldBorderGameBehavior.CODEC);
		SURVIVE_THE_TIDE_INDIVIDUALS_WIN_CONDITION = register("survive_the_tide_individuals_win_condition", SttIndividualsWinConditionBehavior.CODEC);
		SURVIVE_THE_TIDE_TEAMS_WIN_CONDITION = register("survive_the_tide_teams_win_condition", SttTeamsWinConditionBehavior.CODEC);
		FIREWORKS_ON_DEATH = register("fireworks_on_death", FireworksOnDeathBehavior.CODEC);
		SURVIVE_THE_TIDE_RULESET = register("survive_the_tide_ruleset", SurviveTheTideRulesetBehavior.CODEC);
		BIND_CONTROLS = register("bind_controls", BindControlsBehavior.CODEC);
		CANCEL_PLAYER_DAMAGE = register("cancel_player_damage", CancelPlayerDamageBehavior.CODEC);
		SET_GAME_RULES = register("set_game_rules", SetGameRulesBehavior.CODEC);
		TEAMS = register("teams", TeamsBehavior.CODEC);
		CONSERVATION_EXPLORATION = register("conservation_exploration", ConservationExplorationBehavior.CODEC);
		PLACE_TRASH = register("place_trash", PlaceTrashBehavior.CODEC);
		TRASH_COLLECTION = register("trash_collection", TrashCollectionBehavior.CODEC);
		SPECTATOR_CHASE = register("spectator_chase", SpectatorChaseBehavior.CODEC);
		FORCE_LOAD_REGION = register("force_load_region", ForceLoadRegionBehavior.CODEC);
		DELETE_BLOCKS = register("delete_blocks", DeleteBlocksBehavior.CODEC);
		ELIMINATE_PLAYER_CONTROL = register("eliminate_player_control", EliminatePlayerControlBehavior.CODEC);
		PLACE_STT_CHESTS = register("place_stt_chests", PlaceSttChestsGameBehavior.CODEC);
		GENERATE_ENTITIES = register("generate_entities", GenerateEntitiesBehavior.CODEC);
		WEATHER_CONTROLS = register("weather_controls", WeatherControlsBehavior.CODEC);
		RUN_COMMAND_IN_REGION = register("run_command_in_region", RunCommandInRegionBehavior.CODEC);
        TNT_AUTO_FUSE = register("tnt_auto_fuse", TntAutoFuseBehavior.CODEC);
		DISABLE_HUNGER = register("disable_hunger", DisableHungerBehavior.CODEC);
		DISABLE_TNT_BLOCK_DESTRUCTION = register("disable_tnt_block_destruction", DisableTntDestructionBehavior.CODEC);
		REVEAL_PLAYERS = register("reveal_players", RevealPlayersBehavior.CODEC);
		SET_MAX_HEALTH = register("set_max_health", SetMaxHealthBehavior.CODEC);

		BIND_OBJECTIVE_TO_STATISTIC = register("bind_objective_to_statistic", BindObjectiveToStatisticBehavior.CODEC);
		PLACE_BY_STATISTIC = register("place_by_statistic", PlaceByStatisticBehavior.CODEC);
		PLACE_BY_DEATH_ORDER = register("place_by_death_order", PlaceByDeathOrderBehavior.CODEC);
		CAMPING_TRACKER = register("camping_tracker", CampingTrackerBehavior.CODEC);
		CAUSE_OF_DEATH_TRACKER = register("cause_of_death_tracker", CauseOfDeathTrackerBehavior.CODEC);
		KILLS_TRACKER = register("kills_tracker", KillsTrackerBehavior.CODEC);
		TIME_SURVIVED_TRACKER = register("time_survived_tracker", TimeSurvivedTrackerBehavior.CODEC);
		DAMAGE_TRACKER = register("damage_tracker", DamageTrackerBehavior.CODEC);
		BLOCKS_BROKEN_TRACKER = register("blocks_broken_tracker", BlocksBrokenTrackerBehavior.CODEC);

		DISPLAY_LEADERBOARD_ON_FINISH = register("display_leaderboard_on_finish", DisplayLeaderboardOnFinishBehavior.CODEC);
		LOOT_PACKAGE = register("loot_package", LootPackageBehavior.CODEC);
		EFFECT_PACKAGE = register("effect_package", EffectPackageBehavior.CODEC);
		SWAP_PLAYERS_PACKAGE = register("swap_players_package", SwapPlayersPackageBehavior.CODEC);
		SPAWN_ENTITY_AT_PLAYER_PACKAGE = register("spawn_entity_at_player_package", SpawnEntityAtPlayerPackageBehavior.CODEC);
		SPAWN_ENTITY_AT_REGIONS_PACKAGE = register("spawn_entity_at_regions_package", SpawnEntityAtRegionsPackageBehavior.CODEC);
		SPAWN_ENTITIES_AROUND_PLAYERS_PACKAGE = register("spawn_entities_around_players_package", SpawnEntitiesAroundPlayersPackageBehavior.CODEC);
		SPAWN_ENTITIES_AT_REGIONS_OVER_TIME_PACKAGE = register("spawn_entities_at_regions_over_time_package", SpawnEntitiesAtRegionsOverTimePackageBehavior.CODEC);
		SET_BLOCK_AT_PLAYER_PACKAGE = register("set_block_at_player_package", SetBlockAtPlayerPackageBehavior.CODEC);
		FORCED_PLAYER_HEAD_PACKAGE = register("forced_player_head_package", ForcedPlayerHeadPackageBehavior.CODEC);
		PUFFERFISH_PACKAGE = register("pufferfish_package", PufferfishPackageBehavior.CODEC);
		SHOOT_PROJECTILES_AT_ALL_PLAYERS = register("shoot_projectiles_at_all_players", ShootProjectilesAroundAllPlayersPackageBehavior.CODEC);
		SHOOT_PROJECTILES_AT_PLAYER = register("shoot_projectiles_at_player", ShootProjectilesAroundPlayerPackageBehavior.CODEC);

		POLL_FINALISTS = register("poll_finalists", PollFinalistsBehavior.CODEC);
	}
}