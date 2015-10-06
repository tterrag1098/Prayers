package prayer;

import static prayer.Prayers.MODID;
import static prayer.Prayers.VERSION;

import java.util.HashMap;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import prayer.common.PConfigHandler;
import prayer.common.PEventHandler;
import prayer.common.PPacketHandler;
import prayer.common.PProxy;
import prayer.common.PrayerEntity;
import prayer.common.PrayerItem;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = MODID, name = "Minecraft Prayers", version = VERSION)
public class Prayers
{
    public static final String MODID = "Prayers";
    public static final String VERSION = "@VERSION@";

    @Mod.Instance("Prayers")
    public static Prayers instance;
    @SidedProxy(clientSide = "prayer.client.PClientProxy", serverSide = "prayer.common.PProxy")
    public static PProxy proxy;
    public static PrayerItem prayer;
    public static int prayerID;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        PConfigHandler.loadConfig(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        PPacketHandler.init();
        proxy.registerRenderer();
        prayer = new PrayerItem();
        GameRegistry.registerItem(prayer, "prayer");
        EntityRegistry.registerModEntity(PrayerEntity.class, "Prayer", 1, instance, 32, 5, true);
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(prayer, 1, 0), Items.paper, "dyeBlue", "dyeBlue"));
        FMLCommonHandler.instance().bus().register(new PEventHandler());
    }

    public static HashMap<String, PrayerTimer> prayers = new HashMap<String, PrayerTimer>();

    public static PrayerTimer getTimer(String username)
    {
        PrayerTimer timer = (PrayerTimer) prayers.get(username);
        if (timer == null)
        {
            timer = new PrayerTimer();
            prayers.put(username, timer);
        }
        return timer;
    }

    public static void logoutPlayer(String username)
    {
        prayers.remove(username);
    }
}
