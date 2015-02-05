package prayer;

import java.util.HashMap;

import prayer.common.PConfigHandler;
import prayer.common.PProxy;
import prayer.common.PrayerEntity;
import prayer.common.PrayerItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import static prayer.Prayers.*;

@Mod(modid = MODID, name = "Minecraft Prayers", version = VERSION)
public class Prayers
{
    public static final String MODID = "Prayers";
    public static final String VERSION = "0.1.4";

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
        proxy.registerRenderer();
        // TODO
        // GameRegistry.registerPlayerTracker(new PPlayerTracker());
        prayer = new PrayerItem();
        GameRegistry.registerItem(prayer, "prayer");
        // TODO
        LanguageRegistry.addName(prayer, "Prayer to Notch");
        EntityRegistry.registerModEntity(PrayerEntity.class, "Prayer", 1, instance, 32, 5, true);
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(prayer, 1, 0), Items.paper, "dyeBlue", "dyeBlue"));
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
