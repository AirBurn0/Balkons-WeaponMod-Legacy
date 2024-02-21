package ckathode.weaponmod.render;

import net.minecraft.client.model.*;
import net.minecraft.entity.*;

public class ModelCannonBarrel extends ModelBase
{
    public ModelRenderer swiwel_front;
    public ModelRenderer swivel_back;
    public ModelRenderer swivel_main;
    public ModelRenderer axis;
    public ModelRenderer seal;
    public ModelRenderer handcrap;
    public ModelRenderer fronttip;
    public ModelRenderer backtip;
    
    public ModelCannonBarrel() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        (this.swiwel_front = new ModelRenderer(this, 12, 12)).addBox(-2.0f, -2.0f, -11.0f, 4, 4, 2);
        this.swiwel_front.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.swiwel_front.setTextureSize(32, 32);
        this.swiwel_front.mirror = true;
        this.setRotation(this.swiwel_front, 0.0f, 0.0f, 0.0f);
        (this.swivel_back = new ModelRenderer(this, 12, 0)).addBox(-2.0f, -0.5f, -2.0f, 4, 8, 4);
        this.swivel_back.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.swivel_back.setTextureSize(32, 32);
        this.swivel_back.mirror = true;
        this.setRotation(this.swivel_back, 1.570796f, 0.0f, 0.0f);
        (this.swivel_main = new ModelRenderer(this, 0, 0)).addBox(-1.5f, -11.5f, -1.5f, 3, 20, 3);
        this.swivel_main.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.swivel_main.setTextureSize(32, 32);
        this.swivel_main.mirror = true;
        this.setRotation(this.swivel_main, 1.570796f, 0.0f, 0.0f);
        (this.axis = new ModelRenderer(this, 12, 18)).addBox(-4.0f, -0.5f, -0.5f, 8, 1, 1);
        this.axis.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.axis.setTextureSize(32, 32);
        this.axis.mirror = true;
        this.setRotation(this.axis, 1.570796f, 0.0f, 0.0f);
        (this.seal = new ModelRenderer(this, 9, 0)).addBox(-1.0f, -2.5f, 5.5f, 2, 1, 1);
        this.seal.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.seal.setTextureSize(32, 32);
        this.seal.mirror = true;
        this.setRotation(this.seal, 0.0f, 0.0f, 0.0f);
        (this.handcrap = new ModelRenderer(this, 28, 0)).addBox(-0.5f, 8.5f, -0.5f, 1, 7, 1);
        this.handcrap.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.handcrap.setTextureSize(32, 32);
        this.handcrap.mirror = true;
        this.setRotation(this.handcrap, 1.570796f, 0.0f, 0.0f);
        (this.fronttip = new ModelRenderer(this, 24, 12)).addBox(-1.0f, 9.0f, -1.0f, 2, 1, 2);
        this.fronttip.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.fronttip.setTextureSize(32, 32);
        this.fronttip.mirror = true;
        this.setRotation(this.fronttip, 1.570796f, 0.0f, 0.0f);
        (this.backtip = new ModelRenderer(this, 24, 12)).addBox(-1.0f, 15.5f, -1.0f, 2, 1, 2);
        this.backtip.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.backtip.setTextureSize(32, 32);
        this.backtip.mirror = true;
        this.setRotation(this.backtip, 1.570796f, 0.0f, 0.0f);
    }
    
    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3, final float f4, final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.swiwel_front.render(f5);
        this.swivel_back.render(f5);
        this.swivel_main.render(f5);
        this.axis.render(f5);
        this.seal.render(f5);
        this.handcrap.render(f5);
        this.fronttip.render(f5);
        this.backtip.render(f5);
    }
    
    public void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
    
    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4, final float f5, final Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}
