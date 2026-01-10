package com.aetherteam.aether.item.tools.abilities;

import com.aetherteam.aether.AetherTags;
import com.aetherteam.aether.block.AetherBlockStateProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface SkyrootTool {
    default ItemStack doubleDrops(Level level, ItemStack drop, @Nullable ItemStack tool, @Nullable BlockState state) {
        if (tool != null) {

            var registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            var silkTouchHolder = registry.getOrThrow(Enchantments.SILK_TOUCH);
            int silkTouchLevel = EnchantmentHelper.getItemEnchantmentLevel(silkTouchHolder, tool);

            if (silkTouchLevel == 0) {
                if (state != null && (state.getValue(AetherBlockStateProperties.DOUBLE_DROPS) || state.is(AetherTags.Blocks.DOUBLE_DROPS_OVERRIDE))) {
                    if (tool.isCorrectToolForDrops(state)) {
                        drop.setCount(2 * drop.getCount());
                    }
                }
            }
        }
        return drop;
    }
}
