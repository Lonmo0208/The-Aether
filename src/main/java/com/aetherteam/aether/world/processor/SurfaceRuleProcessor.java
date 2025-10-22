package com.aetherteam.aether.world.processor;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.AetherTags;
import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.mixin.mixins.common.accessor.ChunkAccessAccessor;
import com.aetherteam.aether.world.BlockLogicUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

/**
 * This processor is used to replace grass blocks in the Aether with blocks determined by the biome's surface rules.
 */
public class SurfaceRuleProcessor extends StructureProcessor {
    public static final SurfaceRuleProcessor INSTANCE = new SurfaceRuleProcessor();

    public static final MapCodec<SurfaceRuleProcessor> CODEC = MapCodec.unit(SurfaceRuleProcessor.INSTANCE);

    /**
     * Warning for "deprecation" is suppressed because using {@link CarvingContext#topMaterial(Function, ChunkAccess, BlockPos, boolean)} is necessary.
     */
    @Nullable
    @Override
    @SuppressWarnings("deprecation")

    public StructureTemplate.StructureBlockInfo process(@NotNull LevelReader level, @NotNull BlockPos origin, @NotNull BlockPos centerBottom, StructureTemplate.@NotNull StructureBlockInfo originalBlockInfo, StructureTemplate.@NotNull StructureBlockInfo modifiedBlockInfo, @NotNull StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        if (level instanceof WorldGenLevel worldGenLevel) {
            // If the processor is running outside the center chunk, return immediately.
            if (worldGenLevel instanceof WorldGenRegion region && BlockLogicUtil.isOutOfBounds(modifiedBlockInfo.pos(), region.getCenter())) {
                return modifiedBlockInfo;
            }

            if (worldGenLevel instanceof WorldGenRegion region) {
                int chunkX = modifiedBlockInfo.pos().getX() >> 4;
                int chunkZ = modifiedBlockInfo.pos().getZ() >> 4;

                if (!region.hasChunk(chunkX, chunkZ)) {
                    return modifiedBlockInfo;
                }

                ChunkAccess chunk = region.getChunk(chunkX, chunkZ);
                if (chunk == null) {
                    return modifiedBlockInfo;
                }
            }

            if (worldGenLevel.getChunkSource() instanceof ServerChunkCache serverChunkCache) {
                if (serverChunkCache.getGenerator() instanceof NoiseBasedChunkGenerator noiseBasedChunkGenerator) {
                    NoiseGeneratorSettings settingsHolder = noiseBasedChunkGenerator.generatorSettings().value();
                    SurfaceRules.RuleSource surfaceRule = settingsHolder.surfaceRule();

                    try {
                        if (!worldGenLevel.hasChunk(modifiedBlockInfo.pos().getX() >> 4, modifiedBlockInfo.pos().getZ() >> 4)) {
                            return modifiedBlockInfo;
                        }

                        ChunkAccess chunkAccess = worldGenLevel.getChunk(modifiedBlockInfo.pos());
                        NoiseChunk noisechunk = ((ChunkAccessAccessor) chunkAccess).aether$getNoiseChunk();

                        if (noisechunk != null) {
                            CarvingContext carvingcontext = new CarvingContext(noiseBasedChunkGenerator, worldGenLevel.registryAccess(), chunkAccess.getHeightAccessorForGeneration(), noisechunk, serverChunkCache.randomState(), surfaceRule);
                            Optional<BlockState> state = carvingcontext.topMaterial(worldGenLevel.getBiomeManager()::getNoiseBiomeAtPosition, chunkAccess, modifiedBlockInfo.pos(), false);
                            if (state.isPresent()) {
                                if (modifiedBlockInfo.state().is(AetherTags.Blocks.AETHER_DIRT) && !modifiedBlockInfo.state().is(AetherBlocks.AETHER_DIRT.get()) && state.get().is(AetherTags.Blocks.AETHER_DIRT)) {
                                    return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), state.get(), null);
                                }
                            }
                        }
                    } catch (IllegalStateException e) {
                        Aether.LOGGER.debug("Failed to process surface rule at {}: {}", modifiedBlockInfo.pos(), e.getMessage());
                        return modifiedBlockInfo;
                    } catch (Exception e) {
                        Aether.LOGGER.debug("Unexpected error during surface rule processing at {}: {}", modifiedBlockInfo.pos(), e.getMessage());
                        return modifiedBlockInfo;
                    }
                }
            }
        }
        return modifiedBlockInfo;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return AetherStructureProcessors.SURFACE_RULE.get();
    }
}
