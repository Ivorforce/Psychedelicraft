/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities;

import net.minecraft.entity.Entity;

import java.util.Random;

/**
 * Created by lukas on 22.05.14.
 */
public class DrugMessageDistorter
{
    public String distortIncomingMessage(DrugHelper drugHelper, Entity entity, Random random, String message)
    {
        return message;
    }

    public String distortOutgoingMessage(DrugHelper drugHelper, Entity entity, Random random, String message)
    {
        if (message.indexOf("/") == 0)
        {
            return message;
        }

        float alcohol = drugHelper.getDrugValue("Alcohol");
        if (alcohol > 0.0f)
        {
            StringBuilder builder = new StringBuilder();

            float randomCaseChance = alcohol * 0.1f - 0.03f;
            float randomLetterChance = alcohol * 0.04f - 0.025f;
            float sToShChance = alcohol * 1.5f - 0.2f;
            float longShChance = alcohol * 0.8f;
            float hicChance = alcohol * 0.08f - 0.04f;
            float rewindChance = alcohol * 0.06f - 0.03f;
            float longCharChance = alcohol * 0.035f - 0.01f;

            for (int i = 0; i < message.length(); i++)
            {
                char origChar = message.charAt(i);
                char curChar = origChar;

                if (random.nextFloat() < randomLetterChance)
                {
                    curChar = (char) ((random.nextBoolean() ? 'a' : 'A') + random.nextInt(26));
                }
                else if (random.nextFloat() < randomCaseChance)
                {
                    if (Character.isUpperCase(curChar))
                    {
                        curChar = Character.toLowerCase(curChar);
                    }
                    else
                    {
                        curChar = Character.toUpperCase(curChar);
                    }
                }

                if ((curChar == 's' || curChar == 'S') && random.nextFloat() < sToShChance)
                {
                    builder.append(curChar + (random.nextFloat() < longShChance ? "hh" : "h"));
                }
                else
                {
                    builder.append(curChar);
                }

                if (random.nextFloat() < longCharChance)
                {
                    float moreChance = 0.6f * 2.0f;
                    do
                    {
                        moreChance *= 0.5f;
                        builder.append(curChar);
                    }
                    while (random.nextFloat() < moreChance);
                }

                if (random.nextFloat() < hicChance)
                {
                    builder.append("*hic*");
                }

                if (random.nextFloat() < rewindChance)
                {
                    builder.append("... ");
                    int wordsRewind = random.nextInt(5) + 1;
                    for (int j = 0; j < wordsRewind; j++)
                    {
                        i = message.lastIndexOf(" ", i - 1);
                    }

                    if (i < 0)
                    {
                        i = 0;
                    }
                }
            }

            return builder.toString();
        }

        return message;
    }
}
