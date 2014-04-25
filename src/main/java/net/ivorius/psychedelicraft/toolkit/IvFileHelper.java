/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.toolkit;

import java.io.*;
import java.util.ArrayList;

public class IvFileHelper
{
    public static void setContentsOfFile(OutputStream outputStream, byte[] contents)
    {
        try
        {
            outputStream.write(contents);
            outputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void setContentsOfFile(File file, byte[] contents)
    {
        FileOutputStream output = null;

        try
        {
            output = new FileOutputStream(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        setContentsOfFile(output, contents);
    }

    public static void setContentsOfFileAsString(File file, String contents)
    {
        char[] charArray = contents.toCharArray();

        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < byteArray.length; i++)
        {
            byteArray[i] = (byte) charArray[i];
        }

        setContentsOfFile(file, byteArray);
    }

    public static byte[] getContentsOfFile(InputStream inputStream)
    {
        ArrayList<Byte> array = new ArrayList<Byte>();

        try
        {
            while (true)
            {
                int i = inputStream.read();
                if (i >= 0)
                {
                    array.add(Byte.valueOf((byte) i));
                }
                else
                {
                    break;
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        byte[] byteArray = new byte[array.size()];
        for (int i = 0; i < array.size(); i++)
        {
            byteArray[i] = array.get(i);
        }

        return byteArray;
    }

    public static byte[] getContentsOfFile(File file)
    {
        FileInputStream input = null;

        try
        {
            input = new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return getContentsOfFile(input);
    }

    public static String getContentsOfFileAsString(File file)
    {
        return getStringFromByteArray(getContentsOfFile(file));
    }

    public static byte[] getContentsOfFileInJar(String path)
    {
        return getContentsOfFile(IvFileHelper.class.getResourceAsStream(path));
    }

    public static String getContentsOfFileInJarAsString(String path)
    {
        return getStringFromByteArray(getContentsOfFileInJar(path));
    }

    public static String getStringFromByteArray(byte[] byteArray)
    {
        char[] charArray = new char[byteArray.length];
        for (int i = 0; i < charArray.length; i++)
        {
            charArray[i] = (char) byteArray[i];
        }

        return String.valueOf(charArray);
    }
}
