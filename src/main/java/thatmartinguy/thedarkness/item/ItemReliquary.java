package thatmartinguy.thedarkness.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import thatmartinguy.thedarkness.TheDarkness;
import thatmartinguy.thedarkness.data.ReliquaryWorldData;
import thatmartinguy.thedarkness.potion.ModPotionEffects;
import thatmartinguy.thedarkness.util.Reference;

public class ItemReliquary extends ItemFood
{
	private boolean isCounting;
	
	public ItemReliquary(String unlocalizedName, String registryName, int amount, boolean isWolfFood)
	{
		this(amount, isWolfFood);
		this.setAlwaysEdible();
		this.setCreativeTab(TheDarkness.tabDarkness);
		this.setUnlocalizedName(unlocalizedName);
		this.setRegistryName(registryName);
		this.isCounting = false;
		this.setMaxStackSize(1);
	}

	public ItemReliquary(int amount, boolean isWolfFood)
	{
		super(amount, isWolfFood);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.DRINK;
	}

	@Override
	public void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
	{
		if (worldIn.isRemote)
		{
			player.addChatMessage(new TextComponentString(ChatFormatting.DARK_PURPLE + "" + ChatFormatting.ITALIC + "I consume you..."));
			worldIn.playSound(player.posX, player.posY, player.posZ, SoundEvents.BLOCK_PORTAL_TRIGGER, SoundCategory.MASTER, 1, 1, false);
		}
		if (!worldIn.isRemote)
		{
			int timeUntilDay = 18000 - (int) worldIn.getWorldTime();
			//Set time to noon
			worldIn.setWorldTime(6000);
			//Add blindness
			player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 1200));
			//Add slowness
			player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, timeUntilDay));
			//Add mining fatigue
			player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, timeUntilDay));
			//Add nausea
			player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200));
			//Add weakness
			player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, timeUntilDay));
			//Add hidden lightning countdown effect
			player.addPotionEffect(new PotionEffect(ModPotionEffects.effectReliquary, timeUntilDay + 6000));
		}
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		System.out.println("item created");
		final ReliquaryWorldData worldData = ReliquaryWorldData.get(worldIn);
		worldData.setReliquaryCrafted(true);
	}
	
	@Override
	public Item setUnlocalizedName(String unlocalizedName)
	{
		return super.setUnlocalizedName(Reference.MOD_ID + ":" + unlocalizedName);
	}
}
