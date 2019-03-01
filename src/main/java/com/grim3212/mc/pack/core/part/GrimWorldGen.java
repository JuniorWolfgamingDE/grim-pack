package com.grim3212.mc.pack.core.part;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public abstract class GrimWorldGen implements IWorldGenerator {

	@Override
	/**
	 * Be doubly sure that each dimension type is actually the dimension we want
	 */
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (world.getDimension().getType() == DimensionType.NETHER) {
			generateNether(world, chunkProvider, random, chunkX, chunkZ);
			generateNether(world, random, chunkX * 16, chunkZ * 16);
		} else if (world.getDimension().getType() == DimensionType.OVERWORLD) {
			generateSurface(world, chunkProvider, random, chunkX, chunkZ);
			generateSurface(world, random, chunkX * 16, chunkZ * 16);
		} else if (world.getDimension().getType() == DimensionType.THE_END) {
			generateEnd(world, random, chunkX * 16, chunkZ * 16);
		} else {
			generateCustom(world.getDimension().getType(), world, random, chunkX * 16, chunkZ * 16);
		}
	}

	/**
	 * Generate on the surface
	 */
	protected void generateSurface(World world, IChunkProvider chunkProvider, Random random, int chunkX, int chunkZ) {
	}

	/**
	 * Generate on the surface
	 */
	protected void generateSurface(World world, Random random, int i, int j) {
	}

	/**
	 * Generate in the nether
	 */
	protected void generateNether(World world, IChunkProvider chunkProvider, Random random, int chunkX, int chunkZ) {
	}

	/**
	 * Generate in the nether
	 */
	protected void generateNether(World world, Random random, int i, int j) {
	}

	/**
	 * Generate in the end
	 */
	protected void generateEnd(World world, Random random, int i, int j) {
	}

	/**
	 * Generate in a custom dimension make sure to check that it is the right
	 * dimension id before generating
	 */
	protected void generateCustom(DimensionType dimension, World world, Random random, int i, int j) {
	}

}
