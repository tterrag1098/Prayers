package prayer.common;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class PConfigHandler
{
    public static Configuration config;

    public static int prayerDelay = 10000;
    public static int[] minTimes = { 0, 12000 };
    public static int[] midTimes = { 3000, 9000 };
    public static int[] maxTimes = { 5000, 7000 };

    public static void loadConfig(File file)
    {
        config = new Configuration(file);

        prayerDelay = config.getInt("prayerDelay", Configuration.CATEGORY_GENERAL, prayerDelay, 0, Integer.MAX_VALUE,
                "The cooldown time (in ticks) between prayers.");

        minTimes = config.get(Configuration.CATEGORY_GENERAL, "minTimes", minTimes,
                "The times of day (in ticks) between which you will receive minimum benefit from prayers.").getIntList();

        midTimes = config.get(Configuration.CATEGORY_GENERAL, "midTimes", midTimes,
                "The times of day (in ticks) between which you will receive medium benefit from prayers.").getIntList();

        maxTimes = config.get(Configuration.CATEGORY_GENERAL, "maxTimes", maxTimes,
                "The times of day (in ticks) between which you will receive maxiumum benefit from prayers.").getIntList();

        assert minTimes.length == 2 && midTimes.length == 2 && maxTimes.length == 2 : "All time lists must be of length 2.";

        config.save();
    }
}
