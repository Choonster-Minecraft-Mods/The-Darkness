package thatmartinguy.thedarkness.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import thatmartinguy.thedarkness.data.ReliquaryWorldData;
import thatmartinguy.thedarkness.item.ModItems;

public class ModShapedRecipe extends ShapedOreRecipe
{

	public ModShapedRecipe(ItemStack result, Object... recipe)
	{
		super(result, recipe);
	}

	public static void init()
	{
		GameRegistry.addRecipe(new ModShapedRecipe(new ItemStack(ModItems.itemReliquary), "SGS", "SNS", "SSS", 'S', ModItems.itemDarklingSkin, 'G', Blocks.GLASS, 'N', Items.NETHER_STAR));
		RecipeSorter.register("thedarkness:shaped", ModShapedRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		final ReliquaryWorldData worldData = ReliquaryWorldData.get(worldIn);
		return !worldData.isReliquaryCrafted() && super.matches(inv, worldIn);
	}
}
