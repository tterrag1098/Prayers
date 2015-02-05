package prayer.client;

import prayer.common.PProxy;
import prayer.common.PrayerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class PClientProxy extends PProxy
{
    public void registerRenderer()
    {
        RenderingRegistry.registerEntityRenderingHandler(PrayerEntity.class, new PrayerEntityRenderer());
    }
    
    @Override
    public EntityPlayer getClientPlayer()
    {
        return Minecraft.getMinecraft().thePlayer;
    }
}
