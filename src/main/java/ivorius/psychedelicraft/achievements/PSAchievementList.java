package ivorius.psychedelicraft.achievements;

import ivorius.psychedelicraft.Psychedelicraft;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukas on 18.05.15.
 */
public class PSAchievementList
{
    public static AchievementPage page;

    public static void init()
    {
        List<Achievement> achievements = new ArrayList<>();

        page = new AchievementPage(Psychedelicraft.NAME, achievements.toArray(new Achievement[achievements.size()]));
        AchievementPage.registerAchievementPage(page);
    }
}
