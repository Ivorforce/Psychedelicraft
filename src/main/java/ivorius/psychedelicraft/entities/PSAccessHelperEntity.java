package ivorius.psychedelicraft.entities;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.EntityLivingBase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by lukas on 12.12.14.
 */
public class PSAccessHelperEntity
{
    private static Method methodJump;

    public static void jump(EntityLivingBase entityLivingBase)
    {
        if (methodJump == null)
            methodJump = ReflectionHelper.findMethod(EntityLivingBase.class, entityLivingBase, new String[]{"jump", "func_70664_aZ"});

        try
        {
            methodJump.invoke(entityLivingBase);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }
}
