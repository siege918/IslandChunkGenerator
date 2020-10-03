package fun.collin.minecraft;

import java.util.ArrayList;

import org.bukkit.block.Biome;

public class BiomeCache {
    CachedBiomeList[] cache;
    int index;

    public BiomeCache(int size) {
        cache = new CachedBiomeList[size];
    }

    public ArrayList<Biome> get(long id) {
        for (CachedBiomeList list : cache) {
            if (list != null && list.id == id) {
                return list.biomes;
            }
        }

        return null;
    }

    public void add(long id, ArrayList<Biome> biomes) {
        index++;
        if (index >= cache.length) {
            index = 0;
        }

        cache[index] = new CachedBiomeList(id, biomes);
    }
}
