/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.toolkit;

import com.google.common.base.Charsets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * Created by lukas on 17.02.14.
 */
public class IvShaderInstanceMC
{
    public static void trySettingUpShader(IvShaderInstance shaderInstance, ResourceLocation vertexShader, ResourceLocation fragmentShader, String utils)
    {
        String vShader = null;
        String fShader = null;

        IResource vShaderRes = null;
        IResource fShaderRes = null;

        try
        {
            vShaderRes = Minecraft.getMinecraft().getResourceManager().getResource(vertexShader);
            fShaderRes = Minecraft.getMinecraft().getResourceManager().getResource(fragmentShader);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (vShaderRes != null && fShaderRes != null)
        {
            try
            {
                vShader = IOUtils.toString(vShaderRes.getInputStream(), Charsets.UTF_8);
                fShader = IOUtils.toString(fShaderRes.getInputStream(), Charsets.UTF_8);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            if (vShader != null && fShader != null)
            {
                if (utils != null)
                {
                    vShader = addUtils(vShader, utils);
                    fShader = addUtils(fShader, utils);
                }

                shaderInstance.trySettingUpShader(vShader, fShader);
            }
        }
    }

    public static String addUtils(String shader, String utils)
    {
        int indexVersion = shader.indexOf("#version");
        if (indexVersion < 0)
            indexVersion = 0;

        int indexVersionNL = shader.indexOf("\n", indexVersion);

        return shader.substring(0, indexVersionNL + 1) + utils + shader.substring(indexVersionNL + 1);
    }
}
