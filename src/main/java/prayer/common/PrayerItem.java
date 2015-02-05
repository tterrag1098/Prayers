package prayer.common;

import java.util.List;

import prayer.PrayerTimer;
import prayer.Prayers;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import static prayer.common.PConfigHandler.*;

public class PrayerItem extends ItemBow
{
    public PrayerItem()
    {
        super();
        this.maxStackSize = 1;
        setCreativeTab(CreativeTabs.tabMisc);
        setUnlocalizedName("prayers.prayer");
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon("prayer:sutra");
    }

    NBTTagCompound initializeData(ItemStack itemstack, EntityPlayer player)
    {
        if (!itemstack.hasTagCompound())
        {
            NBTTagCompound tags = new NBTTagCompound();
            NBTTagCompound prayerTag = new NBTTagCompound();
            tags.setTag("Prayers", prayerTag);
            itemstack.setTagCompound(tags);
        }
        NBTTagCompound tags = itemstack.getTagCompound();
        NBTTagCompound prayerTag = tags.getCompoundTag("Prayers");
        if (prayerTag == null)
        {
            prayerTag = new NBTTagCompound();
            tags.setTag("Prayers", prayerTag);
        }
        return prayerTag;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityPlayer)
    {
        boolean canPray = true;
        PrayerTimer timer = Prayers.getTimer(entityPlayer.getCommandSenderName());
        if (world.getWorldInfo().getWorldTotalTime() < timer.notch)
        {
            canPray = false;
        }
        if (canPray)
        {
            entityPlayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
            PrayerEntity sutra = new PrayerEntity(world, entityPlayer);
            if (!world.isRemote)
            {
                world.spawnEntityInWorld(sutra);
            }
        }
        return itemstack;
    }

    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer player, int usedTime)
    {
        int time = getMaxItemUseDuration(itemstack) - usedTime;
        if (time < 110)
        {
            return;
        }
        PrayerTimer timer = Prayers.getTimer(player.getCommandSenderName());
        if (!player.capabilities.isCreativeMode)
        {
            timer.notch = (world.getWorldInfo().getWorldTotalTime() + prayerDelay + 2000);
        }
        if (!world.isRemote)
        {
            int effect = 0;
            long worldTime = world.getWorldTime() % 24000L;

            if (worldTime >= minTimes[0] && worldTime <= minTimes[1])
            {
                effect++;
            }
            if (worldTime >= midTimes[0] && worldTime <= midTimes[1])
            {
                effect++;
            }
            if (worldTime >= maxTimes[0] && worldTime <= maxTimes[1])
            {
                effect++;
            }

            if (effect != 0)
            {
                int length = Item.itemRand.nextInt(effect + 1);
                length += effect;
                player.addPotionEffect(new PotionEffect(Potion.regeneration.id, length * 25, 1));
            }
        }
        if (!player.capabilities.isCreativeMode)
        {
            itemstack.stackSize -= 1;
            if (itemstack.stackSize <= 0)
            {
                player.destroyCurrentEquippedItem();
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        PrayerTimer timer = Prayers.getTimer(player.getCommandSenderName());
        long time = timer.notch;
        long totalTime = player.worldObj.getWorldInfo().getWorldTotalTime();
        if (time > totalTime)
        {
            long offset = time - totalTime;
            list.add(I18n.format("prayers.tooltip.timer", formatTime(offset)));
        }
        else
        {
            list.add(I18n.format("prayers.tooltip.ready"));
        }
    }

    String formatTime(long time)
    {
        long s = time / 20L;
        long seconds = s % 60L;
        long minutes = s / 60L;
        return I18n.format("prayers.tooltip.timeformat", minutes, seconds);
    }

    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.block;
    }
}