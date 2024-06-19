package idealindustrial;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import idealindustrial.commands.CommandFixMaterials;
import idealindustrial.commands.CommandFixQuests;
import idealindustrial.commands.DimTPCommand;
import idealindustrial.commands.ReloadRecipesCommand;
import idealindustrial.integration.ingameinfo.InGameInfoLoader;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

@Mod(modid = "iicore", name = "II_Core", version = "MC1710", useMetadata = false, dependencies = "after:gregtech")
public class II_Core {

    private static final String PACKAGES_URL = "https://api.github.com/orgs/IdealIndustrial/packages/maven/idealindustrial.gt-ii-edition/versions";
    private static final String ANY_TOKEN = "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"; //just any public token without write access
    private static final String version = "1.19.2";
    private String auto_version = "Dev_version";
    private String auto_last_version = null;

    public II_Core() {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);

    }

    @Mod.EventHandler
    public void onPreLoad(FMLPreInitializationEvent aEvent) {
        if (!checkEnvironment()) {
            CrashReport tCrashReport = new CrashReport("Wrong enviroment detected, please install BQfix for thermos: https://github.com/IdealIndustrial/Ideal-Industrial-Quests", new RuntimeException("no fix for better questing is detected"));
            throw new ReportedException(tCrashReport);
        }

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        if (Loader.isModLoaded("InGameInfoXML") && FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            new InGameInfoLoader().load();
        }
    }

    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {

    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void openGui(GuiOpenEvent event) {
        if (auto_last_version != null || !(event.gui instanceof net.minecraft.client.gui.GuiMainMenu || event.gui.getClass().getSimpleName().equals("GuiCustom"))) {
            return;
        }

        try {
            Properties p = new Properties();
            InputStream is = getClass().getResourceAsStream("/version.properties");
            if (is != null) {
                p.load(is);
                auto_version = p.getProperty("version", auto_version).replace("-SNAPSHOT", "");
            }

            //get the latest vesrion from github:
            HttpGet get = new HttpGet(PACKAGES_URL);
            HttpClient client = new DefaultHttpClient();
            get.setHeader("Authorization", "Bearer " + ANY_TOKEN);
            HttpResponse response = client.execute(get);
            String s = IOUtils.toString(response.getEntity().getContent());
            JsonArray root = new Gson().fromJson(s, JsonArray.class);
            auto_last_version = root.get(0).getAsJsonObject().get("name").getAsString().replace("-SNAPSHOT", "");
        } catch (Exception e) {
            auto_last_version = auto_version;
        }
        auto_version = "Current version: " + auto_version;
        auto_last_version = "Latest version: " + auto_last_version;
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void renderScreenPost(GuiScreenEvent.DrawScreenEvent.Post event) throws IOException {
        if (event.gui instanceof net.minecraft.client.gui.GuiMainMenu || event.gui.getClass().getSimpleName().equals("GuiCustom")) {
            FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
            event.gui.drawString(fr, auto_version, event.gui.width - fr.getStringWidth(auto_version), 0, Color.ORANGE.getRGB());
            event.gui.drawString(fr, auto_last_version, event.gui.width - fr.getStringWidth(auto_last_version), fr.FONT_HEIGHT + 2, Color.ORANGE.getRGB());
        }
    }

    private static boolean checkEnvironment() {
        try {
            Class.forName("thermos.Thermos");
        } catch (ClassNotFoundException e) {
            return true;
        }
        try {
            Class.forName("betterquesting.core.BetterQuesting");
        } catch (ClassNotFoundException e) {
            return true;
        }

        try {
            Class.forName("a.b.c.gambiarra.Plugin");
        } catch (ClassNotFoundException e) {

            try {
                Class.forName("com.juanmuscaria.playercontainerfix.FMLCoreMod");
            } catch (ClassNotFoundException f) {
                return false;
            }
        }
        return true;
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent aEvent) {
        aEvent.registerServerCommand(new CommandFixQuests());
        aEvent.registerServerCommand(new DimTPCommand());
        aEvent.registerServerCommand(new ReloadRecipesCommand());

        //  aEvent.registerServerCommand(new CommandFixMaterials());
    }

    @Mod.EventHandler
    private void serverAboutToStart(final FMLServerAboutToStartEvent evt) {
        File iiSaveDir = new File(DimensionManager.getCurrentSaveRootDirectory(), "IIM");
        if (!iiSaveDir.isDirectory() && !iiSaveDir.mkdir()) {
            throw new IllegalStateException("Cannot create IIM save folder");
        }
        try {
            CommandFixMaterials.loadWorld(iiSaveDir);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static String getVersion() {
        return version;
    }
}
