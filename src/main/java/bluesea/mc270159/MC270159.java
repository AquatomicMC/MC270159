package bluesea.mc270159;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MC270159 implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("MC270159");

    public static boolean record = false;
    public static boolean analyze = false;
    public static StringBuilder analyzeReport = new StringBuilder();

    @Override
    public void onInitialize() {
    }

    public static void saveInfo(String info, long time) {
        String sub = "";
        int subIndex = info.indexOf("- ");
        if (subIndex != -1) {
            sub = info.substring(0, subIndex + 2);
            info = info.substring(subIndex + 2);
        }
        if (analyze) {
            LOGGER.info("{}Time to save {}: {}ms", sub, info, time);
            String printInfo = String.format("%sTime to save %s: %sms", sub, info, time);
            analyzeReport.append("\n").append(printInfo);
        } else {
            LOGGER.info("{}Time to auto save {}: {}ms", sub, info, time);
        }
    }
}
