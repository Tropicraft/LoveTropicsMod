package com.lovetropics.minigames.client.minigame;

import java.util.function.Supplier;

import com.lovetropics.minigames.common.minigames.MinigameStatus;
import com.lovetropics.minigames.common.minigames.ProtoMinigame;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

public class ClientMinigameMessage {

	private final ResourceLocation minigame;
	private final String unlocName;
	private final MinigameStatus status;

	public ClientMinigameMessage() {
		this(null, null, null);
	}

	public ClientMinigameMessage(ProtoMinigame minigame) {
		this(minigame.getDefinition().getID(),
			minigame.getDefinition().getUnlocalizedName(),
			minigame.getStatus());
	}

	private ClientMinigameMessage(ResourceLocation minigame, String unlocName, MinigameStatus status) {
		this.minigame = minigame;
		this.unlocName = unlocName;
		this.status = status;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeBoolean(minigame != null);
		if (minigame != null) {
			buffer.writeResourceLocation(minigame);
			buffer.writeString(unlocName, 200);
			buffer.writeEnumValue(status);
		}
	}

	public static ClientMinigameMessage decode(PacketBuffer buffer) {
		if (buffer.readBoolean()) {
			ResourceLocation minigame = buffer.readResourceLocation();
			String unlocName = buffer.readString(200);
			MinigameStatus status = buffer.readEnumValue(MinigameStatus.class);
			return new ClientMinigameMessage(minigame, unlocName, status);
		}
		return new ClientMinigameMessage(null, null, null);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			boolean wasJoined = ClientMinigameState.get().map(ClientMinigameState::isJoined).orElse(false);
			ClientMinigameState.set(minigame == null ? null : new ClientMinigameState(minigame, unlocName, status, wasJoined));
		});
		ctx.get().setPacketHandled(true);
	}
}