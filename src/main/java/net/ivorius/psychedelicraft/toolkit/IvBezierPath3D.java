/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 *
 * You are free to:
 *
 * Share — copy and redistribute the material in any medium or format
 * Adapt — remix, transform, and build upon the material
 * The licensor cannot revoke these freedoms as long as you follow the license terms.
 *
 * Under the following terms:
 *
 * Attribution — You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 * NonCommercial — You may not use the material for commercial purposes, unless you have a permit by the creator.
 * ShareAlike — If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.
 * No additional restrictions — You may not apply legal terms or technological measures that legally restrict others from doing anything the license permits.
 **************************************************************************************************/

package net.ivorius.psychedelicraft.toolkit;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class IvBezierPath3D
{
    private List<IvBezierPoint3D> bezierPoints;

    private List<Double> cachedDistances;
    private double cachedFullDistance;
    private List<Double> cachedProgresses;

    private boolean isDirty;

    public IvBezierPath3D()
    {
        cachedProgresses = new ArrayList<Double>();
        cachedDistances = new ArrayList<Double>();

        setBezierPoints(new ArrayList<IvBezierPoint3D>());
    }

    public IvBezierPath3D(ArrayList<IvBezierPoint3D> bezierPoints)
    {
        cachedProgresses = new ArrayList<Double>();
        cachedDistances = new ArrayList<Double>();

        setBezierPoints(bezierPoints);
    }

    public void render(double lineWidth, double stepSize, double textureShift)
    {
        if (isDirty)
        {
            buildDistances();
        }

        Tessellator tessellator = Tessellator.instance;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        for (double progress = 0.0; progress < (1.0 + stepSize); progress += stepSize)
        {
            boolean isVeryFirst = progress == 0.0;
            boolean isVeryLast = progress >= 1.0;

            double totalProgress = Math.min(progress, 1.0);
            IvBezierPoint3DCachedStep cachedStep = getCachedStep(totalProgress);
            double[] position = getPosition(cachedStep);
            double[] pVector = getPVector(cachedStep, stepSize);

            double red = IvMathHelper.mix(cachedStep.point1.getRed(), cachedStep.point2.getRed(), cachedStep.progress);
            double green = IvMathHelper.mix(cachedStep.point1.getGreen(), cachedStep.point2.getGreen(), cachedStep.progress);
            double blue = IvMathHelper.mix(cachedStep.point1.getBlue(), cachedStep.point2.getBlue(), cachedStep.progress);
            double alpha = IvMathHelper.mix(cachedStep.point1.getAlpha(), cachedStep.point2.getAlpha(), cachedStep.progress);

            double textureX = totalProgress + textureShift;
            if (!isVeryFirst)
            {
                tessellator.setColorRGBA_F((float) red, (float) green, (float) blue, (float) alpha);
                tessellator.addVertexWithUV(position[0] - pVector[0] * lineWidth, position[1] - pVector[1] * lineWidth, position[2] - pVector[2] * lineWidth, textureX, 0);
                tessellator.addVertexWithUV(position[0] + pVector[0] * lineWidth, position[1] + pVector[1] * lineWidth, position[2] + pVector[2] * lineWidth, textureX, 1);
                tessellator.draw();
            }

            if (!isVeryLast)
            {
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_F((float) red, (float) green, (float) blue, (float) alpha);
                tessellator.addVertexWithUV(position[0] + pVector[0] * lineWidth, position[1] + pVector[1] * lineWidth, position[2] + pVector[2] * lineWidth, textureX, 1);
                tessellator.addVertexWithUV(position[0] - pVector[0] * lineWidth, position[1] - pVector[1] * lineWidth, position[2] - pVector[2] * lineWidth, textureX, 0);
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
    }

    public void renderText(String text, FontRenderer fontRenderer, boolean spreadToFill, double shift, boolean inwards)
    {
        this.renderText(text, fontRenderer, spreadToFill, shift, inwards, 0.0f, 1.0f);
    }

    public void renderText(String text, FontRenderer fontRenderer, boolean spreadToFill, double shift, boolean inwards, double capBottom, double capTop)
    {
        if (isDirty)
        {
            buildDistances();
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        String plainText = "";
        ArrayList<String> modifiers = new ArrayList<String>();
        modifiers.add("");

        for (int i = 0; i < text.length(); i++)
        {
            char character = text.charAt(i);

            if (character == '\u00A7' && i + 1 < text.length())
            {
                modifiers.set(modifiers.size() - 1, modifiers.get(modifiers.size() - 1) + text.substring(i, i + 2));
                i++;
            }
            else
            {
                plainText = plainText + character;
                modifiers.add(modifiers.get(modifiers.size() - 1));
            }
        }

        for (int i = 0; i < plainText.length(); i++)
        {
            int charIndex = inwards ? i : plainText.length() - i - 1;
            char character = plainText.charAt(charIndex);

            if (character != ' ')
            {
                double totalProgress = (spreadToFill ? ((double) i / (double) text.length()) : (i * 0.5)) + shift;
                double finalProgress = ((totalProgress % 1.0) + 1.0) % 1.0;

                if (finalProgress >= capBottom && finalProgress <= capTop)
                {
                    IvBezierPoint3DCachedStep cachedStep = getCachedStep(finalProgress);
                    double[] position = getPosition(cachedStep);
                    double[] rotation = getNaturalRotation(cachedStep, 0.01);

                    double red = IvMathHelper.mix(cachedStep.point1.getRed(), cachedStep.point2.getRed(), cachedStep.progress);
                    double green = IvMathHelper.mix(cachedStep.point1.getGreen(), cachedStep.point2.getGreen(), cachedStep.progress);
                    double blue = IvMathHelper.mix(cachedStep.point1.getBlue(), cachedStep.point2.getBlue(), cachedStep.progress);
                    double alpha = IvMathHelper.mix(cachedStep.point1.getAlpha(), cachedStep.point2.getAlpha(), cachedStep.progress);

                    double textSize = IvMathHelper.mix(cachedStep.point1.getFontSize(), cachedStep.point2.getFontSize(), cachedStep.progress);

                    GL11.glPushMatrix();
                    GL11.glTranslated(position[0], position[1], position[2]);
                    GL11.glScaled(-textSize / 12.0, -textSize / 12.0, -textSize / 12.0);
                    GL11.glRotatef((float) rotation[0] + (inwards ? 0.0f : 180.0f), 0.0f, 1.0f, 0.0f);
                    GL11.glRotatef((float) rotation[1], 1.0f, 0.0f, 0.0f);
                    fontRenderer.drawString(modifiers.get(charIndex) + character, 0, 0, ((int) (red * 255.0) << 16) + ((int) (green * 255.0) << 8) + ((int) (blue * 255.0)));
                    GL11.glPopMatrix();
                }
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
    }

    public void buildDistances()
    {
        isDirty = false;

        cachedFullDistance = 0.0;
        cachedDistances.clear();
        cachedProgresses.clear();

        IvBezierPoint3D previousPoint = null;
        for (IvBezierPoint3D bezierPoint : bezierPoints)
        {
            if (previousPoint != null)
            {
                double distance = 0.0;

                int samples = 50;
                for (int i = 0; i < samples; i++)
                {
                    double[] bezierFrom = previousPoint.getBezierDirectionPointFrom();
                    double[] bezierTo = bezierPoint.getBezierDirectionPointTo();

                    double[] position = IvMathHelper.cubicMix(previousPoint.position, bezierFrom, bezierTo, bezierPoint.position, (double) i / (double) samples);
                    double[] position1 = IvMathHelper.cubicMix(previousPoint.position, bezierFrom, bezierTo, bezierPoint.position, (double) (i + 1) / (double) samples);

                    distance += IvMathHelper.distance(position, position1);
                }

                cachedFullDistance += distance;
                cachedDistances.add(distance);
            }

            previousPoint = bezierPoint;
        }

        for (Double d : cachedDistances)
        {
            cachedProgresses.add(d / cachedFullDistance);
        }
    }

    public double getPathLengthInRange(int startIndex, int endIndex)
    {
        double maxProgress = 0.0;

        int arraySize = cachedProgresses.size();
        for (int i = startIndex; i < endIndex; i++)
        {
            maxProgress += cachedProgresses.get(i % arraySize);
        }

        return maxProgress;
    }

    public double getPathLength()
    {
        return cachedFullDistance;
    }

    public IvBezierPoint3DCachedStep getCachedStep(double progress)
    {
        progress = ((progress % 1.0) + 1.0) % 1.0;

        for (int i = 1; i < bezierPoints.size(); i++)
        {
            double distance = cachedProgresses.get(i - 1);

            progress -= distance;

            if (progress <= 0.0)
            {
                return new IvBezierPoint3DCachedStep(bezierPoints.get(i - 1), bezierPoints.get(i), (progress + distance) / distance, i - 1);
            }
        }

        int bezierPointsLength = bezierPoints.size();
        return new IvBezierPoint3DCachedStep(bezierPoints.get(bezierPointsLength - 2), bezierPoints.get(bezierPointsLength - 1), 1.0f, bezierPointsLength - 2);
    }

    public double[] getPosition(IvBezierPoint3DCachedStep cachedStep)
    {
        return cachedStep.getPosition();
    }

    public double[] getMotion(IvBezierPoint3DCachedStep cachedStep, IvBezierPoint3DCachedStep cachedStep1)
    {
        return IvMathHelper.difference(getPosition(cachedStep1), getPosition(cachedStep));
    }

    public double[] getPVector(IvBezierPoint3DCachedStep cachedStep, double stepSize)
    {
        double[] motion1 = getMotion(cachedStep, getCachedStep(cachedStep.progress + stepSize * 0.3));
        double[] motion2 = getMotion(cachedStep, getCachedStep(cachedStep.progress + stepSize * 0.6));
        double nextPositionProgress = getPathLengthInRange(0, cachedStep.point1Index + 1);

        IvBezierPoint3DCachedStep nextStep = getCachedStep(nextPositionProgress);
        double[] motionNext1 = getMotion(nextStep, getCachedStep(nextPositionProgress + stepSize * 0.3));
        double[] motionNext2 = getMotion(nextStep, getCachedStep(nextPositionProgress + stepSize * 0.6));

        double[] pVector = IvMathHelper.crossProduct(IvMathHelper.mix(motion1, motionNext1, cachedStep.progress), IvMathHelper.mix(motion2, motionNext2, cachedStep.progress));

        return pVector;
    }

    public double[] getNaturalRotation(IvBezierPoint3DCachedStep cachedStep, double stepSize)
    {
        double[] motion1 = getMotion(cachedStep, getCachedStep(cachedStep.progress + stepSize * 0.3));
        double nextPositionProgress = getPathLengthInRange(0, cachedStep.point1Index + 1);
        double[] motionNext1 = getMotion(getCachedStep(nextPositionProgress), getCachedStep(nextPositionProgress + stepSize * 0.3));

        double[] finalMotion = IvMathHelper.mix(motion1, motionNext1, cachedStep.progress);

        float rotationYaw = (float) (Math.atan2(finalMotion[0], finalMotion[2]) * 180.0D / Math.PI) + 90.0F;

        return new double[]{rotationYaw, 0.0};
    }

    public void markDirty()
    {
        isDirty = true;
    }

    public boolean isDirty()
    {
        return isDirty;
    }

    public void setBezierPoints(List<IvBezierPoint3D> points)
    {
        this.bezierPoints = points;
        markDirty();
    }

    public List getBezierPoints()
    {
        List l = new ArrayList(bezierPoints.size());
        l.addAll(bezierPoints);
        return l;
    }

    public void removeBezierPoint(IvBezierPoint3D point)
    {
        bezierPoints.remove(point);
        markDirty();
    }

    public void addBezierPoint(IvBezierPoint3D point)
    {
        bezierPoints.add(point);
        markDirty();
    }

    public void addBezierPoints(List<IvBezierPoint3D> points)
    {
        bezierPoints.addAll(points);
        markDirty();
    }
}
