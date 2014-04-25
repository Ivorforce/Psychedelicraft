/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package net.ivorius.psychedelicraft.toolkit;

public class IvChatLine
{
    public int delay;
    public String lineString;

    public IvChatLine(int par1, String par2Str)
    {
        this.lineString = par2Str;
        this.delay = par1;
    }
}
