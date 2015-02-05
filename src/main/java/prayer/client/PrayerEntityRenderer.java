package prayer.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import prayer.common.PrayerEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PrayerEntityRenderer extends Render
{
    Minecraft mc = Minecraft.getMinecraft();

    public void doRenderMazinkyoukan(PrayerEntity prayer, double x, double y, double z, float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_NORMALIZE);

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_COLOR);
        GL11.glScalef(1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(getEntityTexture(prayer));

        Tessellator tessellator = Tessellator.instance;

        int count = prayer.count;

        float angle = prayer.rotationYaw - 57.0F;
        float py = -0.3F;
        float px = -MathHelper.sin(angle / 180.0F * 3.141593F) * 1.6F;
        double pz = MathHelper.cos(angle / 180.0F * 3.141593F) * 1.6D;
        float nextpx = -MathHelper.sin((angle + 1.0F) / 180.0F * 3.141593F) * 1.6F;
        double nextpz = MathHelper.cos((angle + 1.0F) / 180.0F * 3.141593F) * 1.6D;
        float widthY = 0.3F;

        float widthY2 = widthY + 0.1F;
        float px2min = -MathHelper.sin((angle - 2.0F) / 180.0F * 3.141593F) * 1.55F;
        float px2max = -MathHelper.sin((angle + 2.0F) / 180.0F * 3.141593F) * 1.65F;
        float pz2min = MathHelper.cos((angle - 2.0F) / 180.0F * 3.141593F) * 1.55F;
        float pz2max = MathHelper.cos((angle + 2.0F) / 180.0F * 3.141593F) * 1.65F;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.addVertexWithUV(px2min, py - widthY2, pz2min, 0.875D, 1.0D);
        tessellator.addVertexWithUV(px2max, py - widthY2, pz2max, 1.0D, 1.0D);
        tessellator.addVertexWithUV(px2max, py + widthY2, pz2max, 1.0D, 0.5D);
        tessellator.addVertexWithUV(px2min, py + widthY2, pz2min, 0.875D, 0.5D);
        tessellator.draw();

        float colorRev = 0.05235988F;
        float ticks = prayer.ticksExisted;
        GL11.glEnable(GL11.GL_BLEND);
        for (int i = 0; i < count; i++)
        {
            float color = (angle + ticks) * colorRev;
            float umin = i % 64 / 64.0F;
            float umax = umin + 0.015f;
            float vmin = i / 64 * 16.0F / 32.0F;
            float vmax = vmin + 0.5F;
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            tessellator.setColorRGBA_F(MathHelper.sin(color) + 0.5F, MathHelper.cos(color) + 100.0F, -MathHelper.sin(color), 0.8F);
            tessellator.addVertexWithUV(px, py - widthY, pz, umin, vmax);
            tessellator.addVertexWithUV(nextpx, py - widthY, nextpz, umax, vmax);
            tessellator.addVertexWithUV(nextpx, py + widthY, nextpz, umax, vmin);
            tessellator.addVertexWithUV(px, py + widthY, pz, umin, vmin);
            tessellator.draw();
            angle += 1.0F;
            px = nextpx;
            pz = nextpz;
            nextpx = -MathHelper.sin((angle + 1.0F) / 180.0F * 3.141593F) * 1.6F;
            nextpz = MathHelper.cos((angle + 1.0F) / 180.0F * 3.141593F) * 1.6D;
        }
        GL11.glDisable(GL11.GL_BLEND);
        px2min = -MathHelper.sin((angle - 2.0F) / 180.0F * 3.141593F) * 1.55F;
        px2max = -MathHelper.sin((angle + 2.0F) / 180.0F * 3.141593F) * 1.65F;
        pz2min = MathHelper.cos((angle - 2.0F) / 180.0F * 3.141593F) * 1.55F;
        pz2max = MathHelper.cos((angle + 2.0F) / 180.0F * 3.141593F) * 1.65F;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.addVertexWithUV(px2min, py - widthY2, pz2min, 1.0D, 1.0D);
        tessellator.addVertexWithUV(px2max, py - widthY2, pz2max, 0.875D, 1.0D);
        tessellator.addVertexWithUV(px2max, py + widthY2, pz2max, 0.875D, 0.5D);
        tessellator.addVertexWithUV(px2min, py + widthY2, pz2min, 1.0D, 0.5D);
        tessellator.draw();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glPopMatrix();
    }

    public void doRender(Entity entity, double x, double y, double z, float f, float f1)
    {
        doRenderMazinkyoukan((PrayerEntity) entity, x, y, z, f, f1);
    }

    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return new ResourceLocation("prayer", "textures/entity/notchsutra.png");
    }
}
