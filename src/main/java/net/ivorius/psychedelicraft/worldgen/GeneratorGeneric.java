package net.ivorius.psychedelicraft.worldgen;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by lukas on 11.03.14.
 */
public class GeneratorGeneric implements IWorldGenerator
{
    public WorldGenerator generator;
    public List<Entry> biomeEntries;

    public GeneratorGeneric(WorldGenerator worldGen, Entry... biomeEntries)
    {
        this.generator = worldGen;

        this.biomeEntries = Arrays.asList(biomeEntries);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        BiomeGenBase biomeGen = world.getBiomeGenForCoords(chunkX * 16 + 8, chunkZ * 16 + 8);

        int generate = 0;

        for (int i = 0; i < biomeEntries.size(); i++)
        {
            generate += this.biomeEntries.get(i).numberGenerated(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider, biomeGen);
        }

        for (int i = 0; i < generate; i++)
        {
            int genX = chunkX * 16 + random.nextInt(16) + 8;
            int genZ = chunkZ * 16 + random.nextInt(16) + 8;
            int genY = world.getHeightValue(genX, genZ);

            generator.generate(world, random, genX, genY, genZ);
        }
    }

    public static Entry[] simpleEntryList(float chance, int number, BiomeGenBase... biomes)
    {
        Entry[] list = new Entry[biomes.length];

        for (int i = 0; i < list.length; i++)
        {
            list[i] = new EntryDefault(biomes[i], chance, number);
        }

        return list;
    }

    public static interface Entry
    {
        public int numberGenerated(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider, BiomeGenBase biomeGen);
    }

    public static class EntryDefault implements Entry
    {
        private BiomeGenBase biome;
        private float chance;
        public int number;

        public EntryDefault(BiomeGenBase biome, float chance)
        {
            this(biome, chance, 1);
        }

        public EntryDefault(BiomeGenBase biome, float chance, int number)
        {
            this.biome = biome;
            this.chance = chance;
            this.number = number;
        }

        public int numberGenerated(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider, BiomeGenBase biomeGen)
        {
            return (getBiome() == biomeGen && random.nextFloat() < getChance()) ? number : 0;
        }

        public float getChance()
        {
            return chance;
        }

        public BiomeGenBase getBiome()
        {
            return biome;
        }
    }
}
