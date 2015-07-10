package ivorius.psychedelicraftcore;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * Created by lukas on 10.07.15.
 */
public class PSErrors
{
    public static boolean inDirectDraw;

    public static void glBegan()
    {
        inDirectDraw = true;
    }

    public static void glEnded()
    {
        inDirectDraw = false;
    }

    public static void printGlErrors(String context)
    {
        if (!inDirectDraw)
        {
            int i;
            while ((i = GL11.glGetError()) != 0)
            {
                PsychedelicraftCoreContainer.logger.error("########## GL ERROR ##########");
                PsychedelicraftCoreContainer.logger.error("@" + context);
                PsychedelicraftCoreContainer.logger.error(i + ": " + GLU.gluErrorString(i));
                Thread.dumpStack();
            }
        }
    }
}
