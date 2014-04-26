package net.ivorius.psychedelicraftcore.toolkit;

import java.util.Hashtable;

/**
 * Created by lukas on 25.02.14.
 */
public class IvDevRemapper
{
    public static Hashtable<String, String> fakeMappings = new Hashtable<String, String>();

    private static boolean isSetUp;

    // TODO Read actual DEV files
    public static void setUp()
    {
        isSetUp = true;
    }

    public boolean isSetUp()
    {
        return isSetUp;
    }

    // Hurr fake and gay
    public static String getSRGName(String name)
    {
        String mapping = fakeMappings.get(name);

        return mapping != null ? mapping : name;
    }
}
