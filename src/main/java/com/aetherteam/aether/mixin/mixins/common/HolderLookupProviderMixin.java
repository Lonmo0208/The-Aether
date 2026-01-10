package com.aetherteam.aether.mixin.mixins.common;

import com.aetherteam.aetherfabric.pond.IHolderLookupProviderExtension;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(HolderLookup.Provider.class)
public interface HolderLookupProviderMixin {
    @Unique
    private HolderLookup.Provider self() {
        return (HolderLookup.Provider) this;
    }

    /**
     * Shortcut method to get a holder from a ResourceKey.
     *
     * @throws IllegalStateException if the registry or key is not found.
     */
    @Unique
    default <T> Holder<T> aetherFabric$holderOrThrow(ResourceKey<T> key) {
        System.out.println("DEBUG: aetherFabric$holderOrThrow called on class: " + this.getClass().getName());
        return this.self().lookupOrThrow(key.registryKey()).getOrThrow(key);
    }

    /**
     * Shortcut method to get an optional holder from a ResourceKey.
     */
    @Unique
    default <T> Optional<Holder.Reference<T>> aetherFabric$holder(ResourceKey<T> key) {
        System.out.println("DEBUG: aetherFabric$holder called on class: " + this.getClass().getName());
        Optional<? extends HolderLookup.RegistryLookup<T>> registry = this.self().lookup(key.registryKey());
        return registry.flatMap(tRegistryLookup -> tRegistryLookup.get(key));
    }
}
