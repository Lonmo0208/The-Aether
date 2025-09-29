package com.aetherteam.aether.client.renderer.entity.layers;

import com.aetherteam.aether.client.gui.screen.perks.MoaSkinsScreen;
import com.aetherteam.aether.client.renderer.entity.model.MoaModel;
import com.aetherteam.aether.entity.passive.Moa;
import com.aetherteam.aether.perk.data.ClientMoaSkinPerkData;
import com.aetherteam.aether.perk.types.MoaData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

import java.util.Map;
import java.util.UUID;

public class MoaEffectsLayer extends RenderLayer<Moa, MoaModel> {
    private final MoaModel model;

    public MoaEffectsLayer(RenderLayerParent<Moa, MoaModel> entityRenderer, MoaModel model) {
        super(entityRenderer);
        this.model = model;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Moa moa, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        UUID lastRiderUUID = moa.getLastRider();
        UUID moaUUID = moa.getMoaUUID();
        Map<UUID, MoaData> userSkinsData = ClientMoaSkinPerkData.INSTANCE.getClientPerkData();
        if (Minecraft.getInstance().screen instanceof MoaSkinsScreen moaSkinsScreen && moaSkinsScreen.getSelectedSkin() != null && moaSkinsScreen.getPreviewMoa() != null && moaSkinsScreen.getPreviewMoa().getMoaUUID() != null && moaSkinsScreen.getPreviewMoa().getMoaUUID().equals(moaUUID) && moaSkinsScreen.getSelectedSkin().getSubRenderer() != null) {
            moaSkinsScreen.getSelectedSkin().getSubRenderer().render(this.getParentModel(), this.model, poseStack, buffer, packedLight, moa, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        } else if (userSkinsData.containsKey(lastRiderUUID) && userSkinsData.get(lastRiderUUID).moaSkin() != null && userSkinsData.get(lastRiderUUID).moaUUID() != null && userSkinsData.get(lastRiderUUID).moaUUID().equals(moaUUID) && userSkinsData.get(lastRiderUUID).moaSkin().getSubRenderer() != null) {
            userSkinsData.get(lastRiderUUID).moaSkin().getSubRenderer().render(this.getParentModel(), this.model, poseStack, buffer, packedLight, moa, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }
}
