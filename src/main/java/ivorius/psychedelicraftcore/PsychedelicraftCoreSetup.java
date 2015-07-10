package ivorius.psychedelicraftcore;

import cpw.mods.fml.relauncher.IFMLCallHook;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Map;

/**
 * Created by lukas on 10.07.15.
 */
public class PsychedelicraftCoreSetup implements IFMLCallHook
{
    private File configDir;

    @Override
    public void injectData(Map<String, Object> data)
    {
        configDir = new File((File) data.get("mcLocation"), "config");
    }

    @Override
    public Void call() throws Exception
    {
        Configuration config = new Configuration(new File(configDir, PsychedelicraftLoadingPlugin.MODID + ".cfg"));
        config.load();

        PsychedelicraftLoadingPlugin.debugGlErrorTraceDumps = config.getBoolean("debugGlErrorTraceDumps", Configuration.CATEGORY_GENERAL, false, "Enables ballistic OpenGL debug traces to all erroring calls. Only enable for extreme debugging.");

        config.save();

        return null;
    }
}
