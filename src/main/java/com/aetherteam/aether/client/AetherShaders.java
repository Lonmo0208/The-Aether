package com.aetherteam.aether.client;

import com.aetherteam.aether.Aether;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

public class AetherShaders {
    private static ShaderInstance voidMoaShader;

    public static void registerShaders(RegisterShadersEvent event) {
        ResourceProvider resourceProvider = event.getResourceProvider();
        try {
            event.registerShader(new ShaderInstance(resourceProvider, ResourceLocation.fromNamespaceAndPath(Aether.MODID, "void_moa"), DefaultVertexFormat.NEW_ENTITY), instance -> voidMoaShader = instance);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ShaderInstance getVoidMoaShader() {
        return voidMoaShader;
    }
}
