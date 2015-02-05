package prayer.common;

import prayer.PrayerTimer;
import prayer.Prayers;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PPacketHandler
{
    public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Prayers.MODID);

    public static void init()
    {
        INSTANCE.registerMessage(PrayersPacketHandler.class, PrayersPacket.class, 9, Side.CLIENT);
    }

    public static class PrayersPacket implements IMessage
    {
        // logs out the client player
        public PrayersPacket()
        {
            this.logout = true;
        }

        private long cooldown;
        private boolean logout;

        // updates the player's notch cooldown
        public PrayersPacket(long cooldown)
        {
            this.cooldown = cooldown;
        }

        @Override
        public void toBytes(ByteBuf buf)
        {
            buf.writeBoolean(logout);
            if (!logout)
            {
                buf.writeLong(cooldown);
            }
        }

        @Override
        public void fromBytes(ByteBuf buf)
        {
            this.logout = buf.readBoolean();
            if (!logout)
            {
                this.cooldown = buf.readLong();
            }
        }
    }

    public static class PrayersPacketHandler implements IMessageHandler<PrayersPacket, IMessage>
    {
        @Override
        public IMessage onMessage(PrayersPacket message, MessageContext ctx)
        {
            if (message.logout)
            {
                Prayers.logoutPlayer(Prayers.proxy.getClientPlayer().getCommandSenderName());
            }
            else
            {
                PrayerTimer timer = Prayers.getTimer(Prayers.proxy.getClientPlayer().getCommandSenderName());
                timer.notch = message.cooldown;
            }
            return null;
        }
    }
}
