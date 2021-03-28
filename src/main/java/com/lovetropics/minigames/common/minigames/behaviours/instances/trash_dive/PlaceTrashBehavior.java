package com.lovetropics.minigames.common.minigames.behaviours.instances.trash_dive;

import com.lovetropics.minigames.common.block.LoveTropicsBlocks;
import com.lovetropics.minigames.common.block.TrashBlock;
import com.lovetropics.minigames.common.block.TrashBlock.Attachment;
import com.lovetropics.minigames.common.block.TrashType;
import com.lovetropics.minigames.common.minigames.IMinigameInstance;
import com.lovetropics.minigames.common.minigames.behaviours.IMinigameBehavior;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.resources.IResource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.Random;

public final class PlaceTrashBehavior implements IMinigameBehavior {
	public static final Codec<PlaceTrashBehavior> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				ResourceLocation.CODEC.fieldOf("positionData").forGetter(c -> c.positionData),
				Codec.INT.optionalFieldOf("centerY", 75).forGetter(c -> c.centerY),
				Codec.INT.optionalFieldOf("range", 50).forGetter(c -> c.range),
				Codec.INT.optionalFieldOf("density", 4).forGetter(c -> c.density)
		).apply(instance, PlaceTrashBehavior::new);
	});

	private static final Logger LOGGER = LogManager.getLogger(PlaceTrashBehavior.class);

	private final TrashType[] trashTypes = TrashType.values();

	private final ResourceLocation positionData;
	private final int centerY;
	private final int range;
	private final int density;

	private final LongSet trashBlocks = new LongOpenHashSet();

	public PlaceTrashBehavior(ResourceLocation positionData, int centerY, int range, int density) {
		this.positionData = positionData;
		this.centerY = centerY;
		this.range = range;
		this.density = density;
	}

	@Override
	public void onConstruct(IMinigameInstance minigame) {
		LongBuffer candidatePositions;
		try (IResource res = minigame.getServer().getDataPackRegistries().getResourceManager().getResource(positionData)) {
			InputStream in = res.getInputStream();
			final byte[] data = new byte[8];
			final ByteBuffer buf = ByteBuffer.allocate(in.available());
			candidatePositions = buf.asLongBuffer();
			while (in.read(data) > 0) {
				buf.put(data);
			}
			candidatePositions.position(0);
		} catch (Exception e) {
			LOGGER.error("Unexpected error reading position data:", e);
			return;
		}

		ServerWorld world = minigame.getWorld();
		Random random = world.rand;

		BlockPos.Mutable pos = new BlockPos.Mutable();
		while (candidatePositions.hasRemaining()) {
			pos.setPos(candidatePositions.get());
			pos.setY(centerY);
			int count = random.nextInt(density);
			for (int i = 0; i < count; i++) {
				// range defines 3 standard deviations from the mean (centerY)
				pos.move(Direction.UP, (int) ((random.nextGaussian() / 3) * range));
				tryPlaceTrash(world, pos);
				pos.setY(centerY);
			}
		}
	}

	private boolean tryPlaceTrash(ServerWorld world, BlockPos pos) {
		Random random = world.rand;
		if (world.getBlockState(pos).getBlock() == Blocks.WATER) {
			TrashType trashType = trashTypes[random.nextInt(trashTypes.length)];
			world.setBlockState(pos, LoveTropicsBlocks.TRASH.get(trashType).getDefaultState()
					.with(TrashBlock.WATERLOGGED, true)
					.with(TrashBlock.ATTACHMENT, Block.hasSolidSideOnTop(world, pos.down()) ? Attachment.FLOOR : Attachment.random(random))
					.with(TrashBlock.FACING, Direction.byHorizontalIndex(random.nextInt(4)))
			);

			trashBlocks.add(pos.toLong());
			return true;
		}

		return false;
	}

	public LongSet getTrashBlocks() {
		return trashBlocks;
	}
}
