package prayer.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class PrayerEntity extends Entity
{
    public EntityPlayer player;
    public int count;
    public float turnAngle;
    public int befUseCount;

    public PrayerEntity(World par1World)
    {
        super(par1World);
        setSize(2.0F, 2.0F);
        this.yOffset = 0.0F;
    }

    public PrayerEntity(World par1World, EntityPlayer entityPlayer)
    {
        this(par1World);
        setSize(2.0F, 2.0F);
        this.yOffset = 0.0F;
        this.player = entityPlayer;
        this.prevPosX = entityPlayer.posX;
        this.prevPosY = entityPlayer.posY;
        this.prevPosZ = entityPlayer.posZ;
        setPosition(entityPlayer.posX, entityPlayer.posY + 1.4D, entityPlayer.posZ);
        setRotation(entityPlayer.rotationYaw, 0.0F);
        this.count = 0;
        this.befUseCount = 0;
        this.turnAngle = 0.0F;
    }

    protected void entityInit()
    {
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public boolean canBeCollidedWith()
    {
        return false;
    }

    public void updatePosition()
    {
        this.prevPosX = this.player.posX;
        this.prevPosY = this.player.posY;
        this.prevPosZ = this.player.posZ;
        this.posX = (this.player.posX - Math.sin(this.rotationYaw / 180.0F * 3.141593F) * 0.5D);
        this.posY = (this.player.posY + 1.4D);
        this.posZ = (this.player.posZ + Math.cos(this.rotationYaw / 180.0F * 3.141593F) * 0.5D);
        setPosition(this.posX, this.posY, this.posZ);
    }

    public void onUpdate()
    {
        if ((!this.worldObj.isRemote) && ((this.player == null) || (this.player.isDead)))
        {
            setDead();
        }
        if (this.player != null)
        {
            if (!this.player.isUsingItem())
            {
                setDead();
            }
            else
            {
                updatePosition();
                this.rotationYaw = this.player.rotationYaw;
                this.rotationPitch = 0.0F;
            }
        }
        this.count += 1;
        if (this.count > 114)
        {
            this.count = 114;
        }
        else if (this.count > 50)
        {
            this.turnAngle += 2.88F;
        }
        if (this.rotationYaw > 180.0F)
        {
            this.rotationYaw -= 360.0F;
        }
        if (this.rotationYaw < -180.0F)
        {
            this.rotationYaw += 360.0F;
        }
        if (this.rotationPitch > 180.0F)
        {
            this.rotationPitch -= 360.0F;
        }
        if (this.rotationPitch < -180.0F)
        {
            this.rotationPitch += 360.0F;
        }
    }

    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
    }

    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    }
}