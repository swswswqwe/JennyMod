package com.schnurritv.sexmod.girls;

import net.minecraft.client.renderer.entity.*;
import software.bernie.geckolib3.model.*;
import org.lwjgl.opengl.*;
import software.bernie.geckolib3.model.provider.data.*;
import software.bernie.shadowed.eliotlash.mclib.utils.*;
import net.minecraft.util.math.*;
import software.bernie.geckolib3.core.event.predicate.*;
import java.util.*;
import software.bernie.geckolib3.core.*;
import net.minecraft.client.*;
import software.bernie.geckolib3.renderers.geo.*;
import software.bernie.geckolib3.model.provider.*;
import net.minecraft.util.*;
import software.bernie.geckolib3.geo.render.built.*;
import java.awt.*;
import com.schnurritv.sexmod.util.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.entity.*;
import net.minecraft.client.renderer.*;

public class GirlRenderer<T extends GirlEntity> extends GeoEntityRenderer<T>
{
    double leashHeightOffset;
    
    public GirlRenderer(final RenderManager renderManager, final AnimatedGeoModel<T> model, final double leashHeightOffset) {
        super(renderManager, (AnimatedGeoModel)model);
        this.leashHeightOffset = leashHeightOffset;
    }
    
    public void doRender(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (entity.getLeashed()) {
            this.renderLeash(entity, x, y + this.leashHeightOffset, z, partialTicks);
        }
        if (entity.isForPreloading) {
            GL11.glScalef(0.1f, 0.1f, 0.1f);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GL11.glDisable(2896);
        final boolean shouldSit = entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit();
        final EntityModelData entityModelData = new EntityModelData();
        entityModelData.isSitting = shouldSit;
        entityModelData.isChild = entity.isChild();
        float f = Interpolations.lerpYaw(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
        final float f2 = Interpolations.lerpYaw(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
        float netHeadYaw = f2 - f;
        if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase) {
            final EntityLivingBase livingentity = (EntityLivingBase)entity.getRidingEntity();
            f = Interpolations.lerpYaw(livingentity.prevRenderYawOffset, livingentity.renderYawOffset, partialTicks);
            netHeadYaw = f2 - f;
            float f3 = MathHelper.wrapDegrees(netHeadYaw);
            if (f3 < -85.0f) {
                f3 = -85.0f;
            }
            if (f3 >= 85.0f) {
                f3 = 85.0f;
            }
            f = f2 - f3;
            if (f3 * f3 > 2500.0f) {
                f += f3 * 0.2f;
            }
            netHeadYaw = f2 - f;
        }
        final float headPitch = Interpolations.lerp(entity.prevRotationPitch, entity.rotationPitch, partialTicks);
        final float f4 = this.handleRotationFloat((EntityLivingBase)entity, partialTicks);
        this.applyRotations((EntityLivingBase)entity, f4, f, partialTicks);
        float limbSwingAmount = 0.0f;
        float limbSwing = 0.0f;
        if (!shouldSit && entity.isEntityAlive()) {
            limbSwingAmount = Interpolations.lerp(entity.prevLimbSwingAmount, entity.limbSwingAmount, partialTicks);
            limbSwing = entity.limbSwing - entity.limbSwingAmount * (1.0f - partialTicks);
            if (entity.isChild()) {
                limbSwing *= 3.0f;
            }
            if (limbSwingAmount > 1.0f) {
                limbSwingAmount = 1.0f;
            }
        }
        entityModelData.headPitch = -headPitch;
        entityModelData.netHeadYaw = -netHeadYaw;
        final AnimationEvent<T> predicate = (AnimationEvent<T>)new AnimationEvent((IAnimatable)entity, limbSwing, limbSwingAmount, partialTicks, limbSwingAmount <= -0.15f || limbSwingAmount >= 0.15f, (List)Collections.singletonList(entityModelData));
        final GeoModelProvider modelProvider = super.getGeoModelProvider();
        final ResourceLocation location = modelProvider.getModelLocation((Object)entity);
        final GeoModel model = modelProvider.getModel(location);
        if (modelProvider instanceof IAnimatableModel) {
            ((IAnimatableModel)modelProvider).setLivingAnimations((Object)entity, this.getUniqueID((Object)entity), (AnimationEvent)predicate);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.01f, 0.0f);
        Minecraft.getMinecraft().renderEngine.bindTexture(this.getEntityTexture((EntityLivingBase)entity));
        final Color renderColor = this.getRenderColor((Object)entity, partialTicks);
        final boolean flag = this.setDoRenderBrightness((EntityLivingBase)entity, partialTicks);
        this.render(model, (Object)entity, partialTicks, renderColor.getRed() / 255.0f, renderColor.getBlue() / 255.0f, renderColor.getGreen() / 255.0f, renderColor.getAlpha() / 255.0f);
        if (flag) {
            RenderHurtColor.unset();
        }
        GL11.glEnable(2896);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }
    
    protected void renderLeash(final GirlEntity entityLivingIn, double x, double y, double z, final float partialTicks) {
        final Entity entity = entityLivingIn.getLeashHolder();
        if (entity != null) {
            y -= (1.6 - entityLivingIn.height) * 0.5;
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            final double d0 = Reference.Lerp(entity.prevRotationYaw, entity.rotationYaw, partialTicks * 0.5f) * 0.01745329238474369;
            final double d2 = Reference.Lerp(entity.prevRotationPitch, entity.rotationPitch, partialTicks * 0.5f) * 0.01745329238474369;
            double d3 = Math.cos(d0);
            double d4 = Math.sin(d0);
            double d5 = Math.sin(d2);
            if (entity instanceof EntityHanging) {
                d3 = 0.0;
                d4 = 0.0;
                d5 = -1.0;
            }
            final double d6 = Math.cos(d2);
            final double d7 = Reference.Lerp(entity.prevPosX, entity.posX, partialTicks) - d3 * 0.7 - d4 * 0.5 * d6;
            final double d8 = Reference.Lerp(entity.prevPosY + entity.getEyeHeight() * 0.7, entity.posY + entity.getEyeHeight() * 0.7, partialTicks) - d5 * 0.5 - 0.25;
            final double d9 = Reference.Lerp(entity.prevPosZ, entity.posZ, partialTicks) - d4 * 0.7 + d3 * 0.5 * d6;
            final double d10 = Reference.Lerp(entityLivingIn.prevRenderYawOffset, entityLivingIn.renderYawOffset, partialTicks) * 0.01745329238474369 + 1.5707963267948966;
            d3 = Math.cos(d10) * entityLivingIn.width * 0.4;
            d4 = Math.sin(d10) * entityLivingIn.width * 0.4;
            final double d11 = Reference.Lerp(entityLivingIn.prevPosX, entityLivingIn.posX, partialTicks) + d3;
            final double d12 = Reference.Lerp(entityLivingIn.prevPosY, entityLivingIn.posY, partialTicks);
            final double d13 = Reference.Lerp(entityLivingIn.prevPosZ, entityLivingIn.posZ, partialTicks) + d4;
            x += d3;
            z += d4;
            final double d14 = (float)(d7 - d11);
            final double d15 = (float)(d8 - d12);
            final double d16 = (float)(d9 - d13);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
            for (int j = 0; j <= 24; ++j) {
                float f = 0.5f;
                float f2 = 0.4f;
                float f3 = 0.3f;
                if (j % 2 == 0) {
                    f *= 0.7f;
                    f2 *= 0.7f;
                    f3 *= 0.7f;
                }
                final float f4 = j / 24.0f;
                bufferbuilder.pos(x + d14 * f4 + 0.0, y + d15 * (f4 * f4 + f4) * 0.5 + ((24.0f - j) / 18.0f + 0.125f), z + d16 * f4).color(f, f2, f3, 1.0f).endVertex();
                bufferbuilder.pos(x + d14 * f4 + 0.025, y + d15 * (f4 * f4 + f4) * 0.5 + ((24.0f - j) / 18.0f + 0.125f) + 0.025, z + d16 * f4).color(f, f2, f3, 1.0f).endVertex();
            }
            tessellator.draw();
            bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
            for (int k = 0; k <= 24; ++k) {
                float f5 = 0.5f;
                float f6 = 0.4f;
                float f7 = 0.3f;
                if (k % 2 == 0) {
                    f5 *= 0.7f;
                    f6 *= 0.7f;
                    f7 *= 0.7f;
                }
                final float f8 = k / 24.0f;
                bufferbuilder.pos(x + d14 * f8 + 0.0, y + d15 * (f8 * f8 + f8) * 0.5 + ((24.0f - k) / 18.0f + 0.125f) + 0.025, z + d16 * f8).color(f5, f6, f7, 1.0f).endVertex();
                bufferbuilder.pos(x + d14 * f8 + 0.025, y + d15 * (f8 * f8 + f8) * 0.5 + ((24.0f - k) / 18.0f + 0.125f), z + d16 * f8 + 0.025).color(f5, f6, f7, 1.0f).endVertex();
            }
            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableCull();
        }
    }
}
