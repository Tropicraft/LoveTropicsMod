package com.lovetropics.minigames.common.core.game.behavior.instances.donation;

import com.lovetropics.minigames.common.core.game.GameException;
import com.lovetropics.minigames.common.core.game.IActiveGame;
import com.lovetropics.minigames.common.core.game.behavior.IGameBehavior;
import com.lovetropics.minigames.common.core.game.behavior.event.EventRegistrar;
import com.lovetropics.minigames.common.core.game.behavior.event.GamePackageEvents;
import com.lovetropics.minigames.common.util.MoreCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;

public final class SetBlockAtPlayerPackageBehavior implements IGameBehavior {
	public static final Codec<SetBlockAtPlayerPackageBehavior> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				MoreCodecs.BLOCK_STATE_PROVIDER.fieldOf("block").forGetter(c -> c.block)
		).apply(instance, SetBlockAtPlayerPackageBehavior::new);
	});

	private final BlockStateProvider block;

	public SetBlockAtPlayerPackageBehavior(BlockStateProvider block) {
		this.block = block;
	}

	@Override
	public void register(IActiveGame registerGame, EventRegistrar events) throws GameException {
		events.listen(GamePackageEvents.APPLY_PACKAGE, (game, player, sendingPlayer) -> {
			BlockPos pos = player.getPosition();
			BlockState state = block.getBlockState(player.world.rand, pos);
			player.world.setBlockState(pos, state);
		});
	}
}
