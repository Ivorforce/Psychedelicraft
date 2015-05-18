package ivorius.psychedelicraft.achievements;

import ivorius.psychedelicraft.Psychedelicraft;
import ivorius.psychedelicraft.fluids.PSFluids;
import ivorius.psychedelicraft.items.PSItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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

    public static Achievement madeMashTub;
    public static Achievement madeDistillery;

    public static Achievement hopCones;
    public static Achievement beerWash;
    public static Achievement drankBeer;
    public static Achievement drankMatureBeer;

    public static Achievement grapes;
    public static Achievement grapeWash;
    public static Achievement drankWine;
    public static Achievement drankWineBrandy;

    public static Achievement sugarcaneWash;
    public static Achievement drankBasi;
    public static Achievement drankRum;

    public static void init()
    {
        List<Achievement> achievements = new ArrayList<>();

        madeMashTub = new Achievement("achievement.madeMashTub", "madeMashTub", 0, 0, new ItemStack(PSItems.itemMashTub), null).registerStat();
        achievements.add(madeMashTub);

        madeDistillery = new Achievement("achievement.madeDistillery", "madeDistillery", 2, 0, new ItemStack(PSItems.itemDistillery), madeMashTub).registerStat();
        achievements.add(madeDistillery);

        hopCones = new Achievement("achievement.hopCones", "hopCones", -2, 0, new ItemStack(PSItems.hopCones), madeMashTub).registerStat();
        achievements.add(hopCones);
        beerWash = new Achievement("achievement.beerWash", "beerWash", -4, 0, new ItemStack(Items.wheat), hopCones).registerStat();
        achievements.add(beerWash);
        drankBeer = new Achievement("achievement.drankBeer", "drankBeer", -6, 0, PSFluids.filledStack(PSItems.woodenMug, PSFluids.alcWheatHop.fermentedFluidStack(1, 0, 0)), beerWash).registerStat();
        achievements.add(drankBeer);
        drankMatureBeer = new Achievement("achievement.drankMatureBeer", "drankMatureBeer", -8, 0, PSFluids.filledStack(PSItems.woodenMug, PSFluids.alcWheatHop.fermentedFluidStack(1, 0, 7)), drankBeer).registerStat();
        achievements.add(drankMatureBeer);

        grapes = new Achievement("achievement.grapes", "grapes", 1, 2, new ItemStack(PSItems.wineGrapes), madeMashTub).registerStat();
        achievements.add(grapes);
        grapeWash = new Achievement("achievement.grapeWash", "grapeWash", 1, 4, new ItemStack(PSItems.wineGrapes), grapes).registerStat();
        achievements.add(grapeWash);
        drankWine = new Achievement("achievement.drankWine", "drankWine", 2, 5, PSFluids.filledStack(PSItems.woodenMug, PSFluids.alcRedGrapes.fermentedFluidStack(1, 0, 7)), grapeWash).registerStat();
        achievements.add(drankWine);
        drankWineBrandy = new Achievement("achievement.drankWineBrandy", "drankWineBrandy", 0, 5, PSFluids.filledStack(PSItems.woodenMug, PSFluids.alcRedGrapes.fermentedFluidStack(1, 1, 7)), grapeWash).registerStat();
        achievements.add(drankWineBrandy);

        sugarcaneWash = new Achievement("achievement.sugarcaneWash", "sugarcaneWash", 1, -2, new ItemStack(Items.reeds), madeMashTub).registerStat();
        achievements.add(sugarcaneWash);
        drankBasi = new Achievement("achievement.drankBasi", "drankBasi", 2, -3, PSFluids.filledStack(PSItems.woodenMug, PSFluids.alcSugarCane.fermentedFluidStack(1, 0, 7)), sugarcaneWash).registerStat();
        achievements.add(drankBasi);
        drankRum = new Achievement("achievement.drankRum", "drankRum", 0, -3, PSFluids.filledStack(PSItems.woodenMug, PSFluids.alcSugarCane.fermentedFluidStack(1, 1, 7)), sugarcaneWash).registerStat();
        achievements.add(drankRum);

        page = new AchievementPage(Psychedelicraft.NAME, achievements.toArray(new Achievement[achievements.size()]));
        AchievementPage.registerAchievementPage(page);
    }
}
