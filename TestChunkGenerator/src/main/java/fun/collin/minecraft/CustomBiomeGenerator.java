package fun.collin.minecraft;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.bukkit.block.Biome;
import nl.rutgerkok.worldgeneratorapi.BiomeGenerator;

public class CustomBiomeGenerator implements BiomeGenerator  {

    private static final int ISLAND_SIZE = 32;
    private static final int OCEAN_SIZE = 6;

    private static Biome[] rareBiomes = new Biome[]{
        Biome.MUSHROOM_FIELDS,
        Biome.DEEP_OCEAN,
        Biome.FROZEN_OCEAN,
        Biome.DEEP_FROZEN_OCEAN,
        Biome.COLD_OCEAN,
        Biome.DEEP_COLD_OCEAN,
        Biome.LUKEWARM_OCEAN,
        Biome.DEEP_LUKEWARM_OCEAN,
        Biome.WARM_OCEAN,
        Biome.DEEP_WARM_OCEAN,
        Biome.THE_VOID,
        Biome.THE_END,
        Biome.END_BARRENS,
        Biome.END_HIGHLANDS,
        Biome.END_MIDLANDS,
        Biome.SMALL_END_ISLANDS,
        Biome.THE_VOID,
        Biome.NETHER_WASTES,
        Biome.WARPED_FOREST,
        Biome.SOUL_SAND_VALLEY,
        Biome.CRIMSON_FOREST,
    };
    private static Biome[] bannedBiomes = new Biome[]{
        Biome.OCEAN,
        Biome.MUSHROOM_FIELD_SHORE,
        Biome.RIVER,
        Biome.FROZEN_RIVER
    };

    private BiomeCache biomeCache = new BiomeCache(32);
    private long baseSeed;

    public CustomBiomeGenerator(long seed) {
        baseSeed = seed;
    }

    @Override
    public Biome getZoomedOutBiome(int X, int Y, int Z) {

        int x = X + (int)(baseSeed & Integer.MAX_VALUE);
        int z = Z + (int)(baseSeed & Integer.MAX_VALUE);

        int adjustedZ = Math.abs((z / (ISLAND_SIZE + OCEAN_SIZE)));

        if (adjustedZ % 2 == 0) {
            x += (OCEAN_SIZE + ISLAND_SIZE) / 2;
        }

        if (isIslandChunk(x, z)) {
            return generateIsland(x, z);
        } else {
            return generateBorder();
        }
    }

    private boolean isIslandChunk(int x, int z) {

        double adjustedX = Math.abs(x % (ISLAND_SIZE + OCEAN_SIZE));
        double adjustedZ = Math.abs(z % (ISLAND_SIZE + OCEAN_SIZE));

        double mid = (ISLAND_SIZE + OCEAN_SIZE) / 2;

        double xPart = adjustedX - mid;
        double zPart = adjustedZ - mid;

        double distance = Math.sqrt(xPart * xPart + zPart * zPart);

        return distance <= (ISLAND_SIZE / 2);
    }

    private Biome generateIsland(int x, int z) {
        ArrayList<Biome> biomes = new ArrayList<Biome>();
        Collections.addAll(biomes, Biome.values());

        int biomeListSize = biomes.size() - (rareBiomes.length - 1) - bannedBiomes.length;

        int gridSize = (int)Math.ceil(Math.sqrt(biomeListSize));

        long seed = ((x / (long)gridSize / (ISLAND_SIZE + OCEAN_SIZE)) * 10000000000L) + (z / (long)gridSize / (ISLAND_SIZE + OCEAN_SIZE));
        seed += baseSeed;
        
        // check biome cache
        ArrayList<Biome> cachedBiomeList = biomeCache.get(seed);

        if (cachedBiomeList != null) {
            biomes = cachedBiomeList;
        } else {
            Collections.shuffle(biomes, new Random(seed));
            cleanUpBiomes(biomes);
            biomeCache.add(seed, biomes);
        }

        int adjustedX = Math.abs((x / (ISLAND_SIZE + OCEAN_SIZE))) % gridSize;
        int adjustedZ = Math.abs((z / (ISLAND_SIZE + OCEAN_SIZE))) % gridSize;

        int gridLocation =  (adjustedZ * gridSize) + adjustedX;

        return biomes.get(gridLocation % biomes.size());        
    }

    private void cleanUpBiomes(ArrayList<Biome> biomes) {
        int rareBiomesToRemove = rareBiomes.length - 1;
        int bannedBiomesToRemove = bannedBiomes.length;

        int i = 0;

        while (i < biomes.size()) {
            boolean biomeRemoved = false;
            Biome currentBiome = biomes.get(i);

            if (rareBiomesToRemove > 0) {
                for (Biome rareBiome : rareBiomes) {
                    if (currentBiome == rareBiome) {
                        biomes.remove(i);
                        biomeRemoved = true;
                        rareBiomesToRemove--;
                        break;
                    }
                }
            }

            if (bannedBiomesToRemove > 0 && !biomeRemoved) {
                for (Biome bannedBiome : bannedBiomes) {
                    if (currentBiome == bannedBiome) {
                        biomes.remove(i);
                        biomeRemoved = true;
                        bannedBiomesToRemove--;
                        break;
                    }
                }
            }

            if (!biomeRemoved) {
                i++;
            }
        }
    }

    private Biome generateBorder() {
        return Biome.OCEAN;
    }
}