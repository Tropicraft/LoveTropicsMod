package com.lovetropics.minigames.common.minigames.behaviours.instances.survive_the_tide;

import com.lovetropics.minigames.common.minigames.IMinigameInstance;
import com.lovetropics.minigames.common.minigames.behaviours.instances.ChunkGeneratingBehavior;
import com.mojang.datafixers.Dynamic;
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
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class PlaceSttChestsMinigameBehavior extends ChunkGeneratingBehavior {
	private static final ResourceLocation MISC_LOOT = new ResourceLocation("lt20", "stt2/misc_type");
	private static final ResourceLocation FOOD_LOOT = new ResourceLocation("lt20", "stt2/food_type");
	private static final ResourceLocation MILITARY_LOOT = new ResourceLocation("lt20", "stt2/military_type");
	private static final ResourceLocation EQUIPMENT_LOOT = new ResourceLocation("lt20", "stt2/equipment_type");

	public static <T> PlaceSttChestsMinigameBehavior parse(Dynamic<T> root) {
		return new PlaceSttChestsMinigameBehavior();
	}

	@Override
	protected void generateChunk(IMinigameInstance minigame, ServerWorld world, Chunk chunk) {
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
		if (block == Blocks.CYAN_GLAZED_TERRACOTTA) {
			return MISC_LOOT;
		} else if (block == Blocks.GREEN_GLAZED_TERRACOTTA) {
			return FOOD_LOOT;
		} else if (block == Blocks.RED_GLAZED_TERRACOTTA) {
			return MILITARY_LOOT;
		} else if (block == Blocks.ORANGE_GLAZED_TERRACOTTA) {
			return EQUIPMENT_LOOT;
		}
		return null;
	}
}