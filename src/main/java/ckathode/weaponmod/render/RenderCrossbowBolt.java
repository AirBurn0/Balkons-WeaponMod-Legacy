package ckathode.weaponmod.render;

import ckathode.weaponmod.WeaponModResources;
import ckathode.weaponmod.entity.projectile.EntityCrossbowBolt;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderCrossbowBolt extends Render<EntityCrossbowBolt> {
    public RenderCrossbowBolt(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(@Nonnull EntityCrossbowBolt entitybolt, double d, double d1,
                         double d2, float f, float f1) {
        bindEntityTexture(entitybolt);
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translated(d, d1, d2);
        GlStateManager.rotatef(entitybolt.prevRotationYaw + (entitybolt.rotationYaw - entitybolt.prevRotationYaw) * f1 - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotatef(entitybolt.prevRotationPitch + (entitybolt.rotationPitch - entitybolt.prevRotationPitch) * f1, 0.0f, 0.0f, 1.0f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        int i = 0;
        float f2 = 0.0f;
        float f3 = 0.5f;
        float f4 = 0.0f;
        float f5 = 0.15625f;
        float f6 = 0.0f;
        float f7 = 0.15625f;
        float f8 = 0.15625f;
        float f9 = 0.3125f;
        float f10 = 0.05625f;
        GlStateManager.enableRescaleNormal();
        float f11 = entitybolt.arrowShake - f1;
        if (f11 > 0.0f) {
            float f12 = -MathHelper.sin(f11 * 3.0f) * f11;
            GlStateManager.rotatef(f12, 0.0f, 0.0f, 1.0f);
        }
        GlStateManager.rotatef(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scalef(0.05625f, 0.05625f, 0.05625f);
        GlStateManager.translatef(-1.0f, 0.0f, 0.0f);
        if (renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(getTeamColor(entitybolt));
        }
        GlStateManager.normal3f(0.05625f, 0.0f, 0.0f);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(-5.0, -2.0, -2.0).tex(0.0, 0.15625).endVertex();
        vertexbuffer.pos(-5.0, -2.0, 2.0).tex(0.15625, 0.15625).endVertex();
        vertexbuffer.pos(-5.0, 2.0, 2.0).tex(0.15625, 0.3125).endVertex();
        vertexbuffer.pos(-5.0, 2.0, -2.0).tex(0.0, 0.3125).endVertex();
        tessellator.draw();
        GlStateManager.normal3f(-0.05625f, 0.0f, 0.0f);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(-5.0, 2.0, -2.0).tex(0.0, 0.15625).endVertex();
        vertexbuffer.pos(-5.0, 2.0, 2.0).tex(0.15625, 0.15625).endVertex();
        vertexbuffer.pos(-5.0, -2.0, 2.0).tex(0.15625, 0.3125).endVertex();
        vertexbuffer.pos(-5.0, -2.0, -2.0).tex(0.0, 0.3125).endVertex();
        tessellator.draw();
        for (int j = 0; j < 4; ++j) {
            GlStateManager.rotatef(90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.normal3f(0.0f, 0.0f, 0.05625f);
            vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            vertexbuffer.pos(-6.0, -2.0, 0.0).tex(0.0, 0.0).endVertex();
            vertexbuffer.pos(6.0, -2.0, 0.0).tex(0.5, 0.0).endVertex();
            vertexbuffer.pos(6.0, 2.0, 0.0).tex(0.5, 0.15625).endVertex();
            vertexbuffer.pos(-6.0, 2.0, 0.0).tex(0.0, 0.15625).endVertex();
            tessellator.draw();
        }
        if (renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        super.doRender(entitybolt, d, d1, d2, f, f1);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityCrossbowBolt entity) {
        return WeaponModResources.Textures.BOLT;
    }
}
