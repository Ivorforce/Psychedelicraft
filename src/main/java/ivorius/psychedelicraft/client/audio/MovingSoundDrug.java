package ivorius.psychedelicraft.client.audio;

import ivorius.psychedelicraft.entities.drugs.DrugProperties;
import ivorius.psychedelicraft.entities.drugs.DrugMusicManager;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by lukas on 22.11.14.
 */
public class MovingSoundDrug extends MovingSound
{
    private final Entity entity;
    private final DrugProperties drugProperties;
    private String drugName;

    public MovingSoundDrug(ResourceLocation resourceLocation, Entity entity, DrugProperties drugProperties, String drugName)
    {
        super(resourceLocation);
        this.entity = entity;
        this.drugProperties = drugProperties;
        this.drugName = drugName;
    }

    @Override
    public void update()
    {
        if (this.entity.isDead)
        {
            this.donePlaying = true;
        }
        else
        {
            this.xPosF = (float) this.entity.posX;
            this.yPosF = (float) this.entity.posY;
            this.zPosF = (float) this.entity.posZ;

            DrugMusicManager musicManager = drugProperties.musicManager;
            if (drugName.equals(musicManager.getActiveDrug()))
                volume = musicManager.getVolume();
            else
                volume = 0.0f;
        }
    }
}
