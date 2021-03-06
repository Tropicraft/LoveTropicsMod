package com.lovetropics.minigames.common.core.game.behavior.instances;

import com.lovetropics.minigames.common.core.game.IActiveGame;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FillChestsByMarkerBehavior extends ChunkGeneratingBehavior {
	public static final Codec<FillChestsByMarkerBehavior> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.unboundedMap(Registry.BLOCK, ResourceLocation.CODEC).fieldOf("loot_tables").forGetter(c -> c.lootTableByMarker)
		).apply(instance, FillChestsByMarkerBehavior::new);
	});

	private final Map<Block, ResourceLocation> lootTableByMarker;

	public FillChestsByMarkerBehavior(Map<Block, ResourceLocation> lootTableByMarker) {
		this.lootTableByMarker = lootTableByMarker;
	}

	@Override
	protected void generateChunk(IActiveGame game, ServerWorld world, Chunk chunk) {
		List<BlockPos> chestPositions = new ArrayList<>();
		for (TileEntity entity : chunk.getTileEntityMap().values()) {
			if (entity instanceof ChestTileEntity) {
				chestPositions.add(entity.getPos());
			}
		}

		for (BlockPos pos : chestPositions) {
			BlockPos belowPos = pos.down();

			BlockState belowState = world.getBlockState(belowPos);
			ResourceLocation lootTable = getLootTableFor(belowState.getBlock());
			if (lootTable != null) {
				Direction facing = belowState.get(BlockStateProperties.HORIZONTAL_FACING);
				setChest(world, belowPos, lootTable, facing);

				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
		}
	}

	private void setChest(ServerWorld world, BlockPos pos, ResourceLocation lootTable, Direction facing) {
		world.setBlockState(pos, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, facing));
		TileEntity chest = world.getTileEntity(pos);
		if (chest instanceof ChestTileEntity) {
			((ChestTileEntity) chest).setLootTable(lootTable, world.rand.nextLong());
		}
	}

	private ResourceLocation getLootTableFor(Block block) {
		return this.lootTableByMarker.get(block);
	}
}
