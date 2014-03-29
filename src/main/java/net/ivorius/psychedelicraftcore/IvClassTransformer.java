package net.ivorius.psychedelicraftcore;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

/**
 * Created by lukas on 21.02.14.
 */
public abstract class IvClassTransformer
{
    public void printSubMethodError(String className, String methodID, String submethodID)
    {
        PsychedelicraftCoreContainer.logger.error("Could not patch " + className + "." + methodID + " (" + submethodID + ")!");
    }

    public static String getMethodDescriptor(Object returnValue, Object... params)
    {
        Type[] types = new Type[params.length];

        for (int i = 0; i < params.length; i++)
        {
            types[i] = getType(params[i]);
        }

        return Type.getMethodDescriptor(getType(returnValue), types);
    }

    private static Type getType(Object obj)
    {
        if (obj instanceof Type)
        {
            return (Type) obj;
        }
        else if (obj instanceof Class)
        {
            return Type.getType((Class) obj);
        }
        else if (obj instanceof String)
        {
            return Type.getObjectType((String) obj);
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    public static String getSRGDescriptor(String signature)
    {
        return FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(signature);
    }

    public static String getTypeDescriptor(Class clazz)
    {
        Type type = Type.getType(clazz);
        return type.getDescriptor();
    }

    public static String getSrgName(MethodInsnNode node)
    {
        return IvDevRemapper.getSRGName(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(node.owner, node.name, node.desc));
    }

    public static String getSrgName(String className, MethodNode node)
    {
        return IvDevRemapper.getSRGName(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(className, node.name, node.desc));
    }

    public static String getSrgName(FieldInsnNode node)
    {
        return IvDevRemapper.getSRGName(FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(node.owner, node.name, node.desc));
    }

    public static String getSrgClassName(String className)
    {
        return FMLDeobfuscatingRemapper.INSTANCE.map(className);
    }

    public static boolean isMethod(AbstractInsnNode node, int opCode, String srgMethodName, String owner, String signature)
    {
        if (!(node instanceof MethodInsnNode))
        {
            return false;
        }

        MethodInsnNode mNode = (MethodInsnNode) node;
        boolean nameEqual = srgMethodName == null || getSrgName(mNode).equals(srgMethodName);
        boolean ownerEqual = owner == null || getSrgClassName(mNode.owner).equals(owner);
        boolean signatureEqual = signature == null || getSRGDescriptor(mNode.desc).equals(signature);

        return opCode == mNode.getOpcode() && nameEqual && ownerEqual && signatureEqual;
    }

    public static boolean isField(AbstractInsnNode node, int opCode, String srgFieldName, String owner, Type type)
    {
        if (!(node instanceof FieldInsnNode))
        {
            return false;
        }

        FieldInsnNode fNode = (FieldInsnNode) node;
        boolean nameEqual = srgFieldName == null || getSrgName(fNode).equals(srgFieldName);
        boolean ownerEqual = owner == null || getSrgClassName(fNode.owner).equals(owner);
        boolean signatureEqual = type == null || type.equals(Type.getType(fNode.desc));

        return opCode == fNode.getOpcode() && nameEqual && ownerEqual && signatureEqual;
    }

    public byte[] transform(String actualClassName, String srgClassName, byte[] data, boolean obf)
    {
        ClassNode classNode = null;
        boolean didChange = false;

        try
        {
            classNode = new ClassNode();
            ClassReader classReader = new ClassReader(data);
            classReader.accept(classNode, 0);
        }
        catch (Exception ex)
        {
            PsychedelicraftCoreContainer.logger.error("Error patching class PRE " + actualClassName + " (" + srgClassName + ")!", ex);
            return data;
        }

        try
        {
            didChange = transform(actualClassName.replaceAll("\\.", "/"), classNode, obf);
        }
        catch (Exception ex)
        {
            PsychedelicraftCoreContainer.logger.error("Error patching class MAIN " + actualClassName + " (" + srgClassName + ")!", ex);
            return data;
        }

        if (didChange)
        {
            try
            {
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES); // Compute Frames can crash sometimes...
                classNode.accept(writer);
                return writer.toByteArray();
            }
            catch (Exception ex)
            {
                PsychedelicraftCoreContainer.logger.error("Error patching class POST " + actualClassName + " (" + srgClassName + ")!", ex);
                return data;
            }
        }

        return data;
    }

    public abstract boolean transform(String className, ClassNode classNode, boolean obf);
}
