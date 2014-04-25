/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.toolkit;

import java.util.Random;

public class IvStringHelper
{
    public static String cheeseString(String string, float effect, long seed)
    {
        return cheeseString(string, effect, new Random(seed));
    }

    public static String cheeseString(String string, float effect, Random rand)
    {
        if (effect <= 0.0f)
        {
            return string;
        }

        StringBuilder builder = new StringBuilder(string.length());

        for (int i = 0; i < string.length(); i++)
        {
            if (rand.nextFloat() <= effect)
            {
                builder.append(' ');
            }
            else
            {
                builder.append(string.charAt(i));
            }
        }

        return builder.toString();
    }

    public static int countOccurrences(String haystack, char needle)
    {
        int count = 0;

        for (int i = 0; i < haystack.length(); i++)
        {
            if (haystack.charAt(i) == needle)
            {
                count++;
            }
        }

        return count;
    }
}
