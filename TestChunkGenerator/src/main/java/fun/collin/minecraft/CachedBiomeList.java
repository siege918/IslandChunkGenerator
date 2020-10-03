package fun.collin.minecraft;

import java.util.ArrayList;

import org.bukkit.block.Biome;

public class CachedBiomeList {
    public long id;
    public ArrayList<Biome> biomes;

    public CachedBiomeList(long id, ArrayList<Biome> biomes) {
        this.id = id;
        this.biomes = biomes;
    }
}
