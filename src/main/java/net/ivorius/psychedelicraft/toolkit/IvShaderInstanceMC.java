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
    public static void trySettingUpShader(IvShaderInstance shaderInstance, ResourceLocation vertexShader, ResourceLocation fragmentShader)
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
                shaderInstance.trySettingUpShader(vShader, fShader);
            }
        }
    }
}
