package com.aetherteam.aether.client;

import com.aetherteam.aether.Aether;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterRenderBuffersEvent;

public class AetherRenderTypes {
    public static final RenderType VOID_MOA = RenderType.create("aether:void_moa",
        DefaultVertexFormat.NEW_ENTITY,
        VertexFormat.Mode.QUADS,
        1536,
        false,
        false,
        RenderType.CompositeState.builder()
            .setShaderState(new RenderStateShard.ShaderStateShard(AetherShaders::getVoidMoaShader))
            .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
                .add(ResourceLocation.fromNamespaceAndPath(Aether.MODID, "textures/entity/mobs/moa/skins/void_moa/void_moa.png"), false, false)
//                .add(ItemRenderer.ENCHANTED_GLINT_ITEM, true, false)
                .add(TheEndPortalRenderer.END_SKY_LOCATION, false, false)
                .add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false)
                .build())
//            .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
            .setWriteMaskState(RenderType.COLOR_WRITE)
            .setCullState(RenderType.NO_CULL)
            .setDepthTestState(RenderType.EQUAL_DEPTH_TEST)
            .createCompositeState(false)
    );

    public static RenderType voidMoa() {
        return VOID_MOA;
    }

    public static void registerRenderBuffers(RegisterRenderBuffersEvent event) {
        event.registerRenderBuffer(voidMoa());
    }
}
