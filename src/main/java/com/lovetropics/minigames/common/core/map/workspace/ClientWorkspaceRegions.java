package com.lovetropics.minigames.common.core.map.workspace;

import com.lovetropics.minigames.common.core.map.MapRegion;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;
import java.util.Iterator;

public class ClientWorkspaceRegions implements Iterable<ClientWorkspaceRegions.Entry> {

	// Used when regions are hidden on the client
	public static ClientWorkspaceRegions noop() {
		
		return new ClientWorkspaceRegions() {
			
			@Override
			protected void add(Entry entry) {}
			
			@Override
			public void set(int id, MapRegion region) {}
		};
	}

	private final Int2ObjectMap<Entry> entries = new Int2ObjectOpenHashMap<>();

	public final void add(int id, String key, MapRegion region) {
		add(new Entry(id, key, region));
	}

	protected void add(Entry entry) {
		entries.put(entry.id, entry);
	}

	public void set(int id, @Nullable MapRegion region) {
		if (region != null) {
			ClientWorkspaceRegions.Entry entry = entries.get(id);
			if (entry != null) {
				entry.region = region;
			}
		} else {
			entries.remove(id);
		}
	}

	public boolean isEmpty() {
		return entries.isEmpty();
	}

	public static ClientWorkspaceRegions read(PacketBuffer buffer) {
		ClientWorkspaceRegions regions = new ClientWorkspaceRegions();

		int keyCount = buffer.readVarInt();

		for (int i = 0; i < keyCount; i++) {
			String key = buffer.readString(64);

			int regionCount = buffer.readVarInt();
			for (int j = 0; j < regionCount; j++) {
				int id = buffer.readVarInt();
				MapRegion region = MapRegion.read(buffer);
				regions.add(new Entry(id, key, region));
			}
		}

		return regions;
	}

	@Override
	public Iterator<Entry> iterator() {
		return entries.values().iterator();
	}

	public static class Entry {
		public final int id;
		public final String key;
		public MapRegion region;

		Entry(int id, String key, MapRegion region) {
			this.id = id;
			this.key = key;
			this.region = region;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;

			if (o instanceof Entry) {
				Entry entry = (Entry) o;
				return id == entry.id;
			}

			return false;
		}

		@Override
		public int hashCode() {
			return id;
		}
	}
}
