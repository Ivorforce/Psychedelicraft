/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities;

import ivorius.psychedelicraft.ivToolkit.IvMathHelper;
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
        float zero = drugHelper.getDrugValue("Zero");
        if (alcohol > 0.0f || zero > 0.0f)
            return distortIncomingMessage(message, random, alcohol, zero);

        return message;
    }

    public String distortIncomingMessage(String message, Random random, float alcohol, float zero)
    {
        StringBuilder builder = new StringBuilder();

        float randomCaseChance = IvMathHelper.zeroToOne(alcohol, 0.3f, 1.0f) * 0.06f + IvMathHelper.zeroToOne(zero, 0.0f, 0.3f);
        float randomLetterChance = IvMathHelper.zeroToOne(alcohol, 0.5f, 1.0f) * 0.015f;
        float sToShChance = IvMathHelper.zeroToOne(alcohol, 0.2f, 0.6f);
        float longShChance = alcohol * 0.8f;
        float hicChance = IvMathHelper.zeroToOne(alcohol, 0.5f, 1.0f) * 0.04f;
        float rewindChance = IvMathHelper.zeroToOne(alcohol, 0.4f, 0.9f) * 0.03f;
        float longCharChance = IvMathHelper.zeroToOne(alcohol, 0.3f, 1.0f) * 0.025f;

        float oneZeroChance = IvMathHelper.zeroToOne(zero, 0.6f, 0.95f);
        float randomCharChance = IvMathHelper.zeroToOne(zero, 0.2f, 0.95f);

        for (int i = 0; i < message.length(); i++)
        {
            char origChar = message.charAt(i);
            char curChar = origChar;

            if (random.nextFloat() < oneZeroChance)
            {
                curChar = random.nextBoolean() ? '0' : '1';
            }
            else if (random.nextFloat() < randomCharChance)
            {
                curChar = (char) (' ' + random.nextInt(('~' - ' ' + 1)));
            }
            else if (random.nextFloat() < randomLetterChance)
            {
                curChar = (char) ((random.nextBoolean() ? 'a' : 'A') + random.nextInt(26));
            }
            else if (random.nextFloat() < randomCaseChance)
            {
                if (random.nextBoolean())
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
            }

            if ((curChar == 's' || curChar == 'S') && random.nextFloat() < sToShChance)
            {
                builder.append(curChar).append(random.nextFloat() < longShChance ? "hh" : "h");
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
}
