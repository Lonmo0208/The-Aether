package com.aetherteam.aether.item.combat;

import com.aetherteam.aether.entity.projectile.dart.AbstractDart;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class DartItem extends Item implements ProjectileItem {
    public DartItem(Properties properties) {
        super(properties);
    }

    /**
     * Creates Dart with setup using shooter entity.
     *
     * @param level   {@link Level} of shooter entity.
     * @param shooter {@link LivingEntity} shooting dart.
     * @return {@link AbstractDart} entity created from dart entity type.
     */
    public abstract AbstractDart createDart(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack firedFromWeapon);

    public boolean isInfinite(ItemStack ammo, ItemStack weapon, LivingEntity livingEntity) {
        // 修复1：使用标准 API 而不是扩展方法
        var registryAccess = livingEntity.level().registryAccess();
        var enchantmentLookup = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);
        var infinityHolder = enchantmentLookup.getOrThrow(Enchantments.INFINITY);

        // 修复2：使用标准方法获取附魔等级
        // 方法1：使用 EnchantmentHelper.getItemEnchantmentLevel
        int infinityLevel = EnchantmentHelper.getItemEnchantmentLevel(infinityHolder, weapon);

        // 方法2：或者使用 ItemStack 的 getEnchantmentLevel 方法（如果可用）
        // int infinityLevel = weapon.getEnchantmentLevel(infinityHolder);

        // 方法3：或者使用 EnchantmentHelper.getTagEnchantmentLevel（如果可用）
        // int infinityLevel = EnchantmentHelper.getTagEnchantmentLevel(weapon, infinityHolder);

        return infinityLevel > 0;
    }
}
