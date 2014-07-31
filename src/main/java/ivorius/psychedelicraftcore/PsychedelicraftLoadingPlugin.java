/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraftcore;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

/**
 * Created by lukas on 21.02.14.
 */
@IFMLLoadingPlugin.Name(value = PsychedelicraftLoadingPlugin.NAME)
@IFMLLoadingPlugin.MCVersion(value = "1.7.2")
@IFMLLoadingPlugin.TransformerExclusions(value = {"ivorius.psychedelicraftcore.", "ivorius.ivtoolkit.asm."})
@IFMLLoadingPlugin.SortingIndex(value = 0)
public class PsychedelicraftLoadingPlugin implements IFMLLoadingPlugin
{
    public static final String NAME = "Psychedelicraft Core";

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[]{PsychedelicraftClassTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass()
    {
        return "ivorius.psychedelicraftcore.PsychedelicraftCoreContainer";
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {

    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
