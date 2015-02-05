package prayer.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import prayer.PrayerTimer;
import prayer.Prayers;
import prayer.common.PPacketHandler.PrayersPacket;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class PEventHandler
{
    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event)
    {
        EntityPlayer player = event.player;
        PrayerTimer timer = Prayers.getTimer(player.getCommandSenderName());
        NBTTagCompound tags = player.getEntityData();
        if (!tags.hasKey("Prayers"))
        {
            tags.setTag("Prayers", new NBTTagCompound());
        }
        if (tags.getCompoundTag("Prayers").hasKey("NotchCooldown"))
        {
            timer.notch = tags.getCompoundTag("Prayers").getLong("NotchCooldown");
        }

        PPacketHandler.INSTANCE.sendTo(new PrayersPacket(timer.notch), (EntityPlayerMP) player);
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerLoggedOutEvent event)
    {
        EntityPlayer player = event.player;
        PrayerTimer timer = Prayers.getTimer(player.getCommandSenderName());
        NBTTagCompound tags = player.getEntityData();
        if (!tags.hasKey("Prayers"))
        {
            tags.setTag("Prayers", new NBTTagCompound());
        }
        tags.getCompoundTag("Prayers").setLong("NotchCooldown", timer.notch);

        Prayers.logoutPlayer(player.getCommandSenderName());
        PPacketHandler.INSTANCE.sendTo(new PrayersPacket(), (EntityPlayerMP) player);
    }
}
