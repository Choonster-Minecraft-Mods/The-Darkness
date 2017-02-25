package thatmartinguy.thedarkness;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import thatmartinguy.thedarkness.block.ModBlocks;
import thatmartinguy.thedarkness.command.CommandResetReliquaryCraftedState;
import thatmartinguy.thedarkness.crafting.ModCrafting;
import thatmartinguy.thedarkness.crafting.ModShapedRecipe;
import thatmartinguy.thedarkness.data.ReliquaryWorldData;
import thatmartinguy.thedarkness.event.CommonEventHandler;
import thatmartinguy.thedarkness.item.ModItems;
import thatmartinguy.thedarkness.network.ReliquaryMessage;
import thatmartinguy.thedarkness.potion.ModPotionEffects;
import thatmartinguy.thedarkness.proxy.IProxy;
import thatmartinguy.thedarkness.reference.Reference;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)

public class TheDarkness
{
	public static CreativeTabs tabDarkness = new TabDarkness("tabDarkness");
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_LOCATION, serverSide = Reference.SERVER_PROXY_LOCATION)
	public static IProxy proxy;
	
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		ModItems.init();
		ModPotionEffects.init();
		ModBlocks.init();
		
		proxy.preInit();
		
		int id = -1;
		NETWORK.registerMessage(ReliquaryMessageHandler.class, ReliquaryMessage.class, id++, Side.CLIENT);
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event)
	{
		ModCrafting.init();
		ModShapedRecipe.init();
		
		CommonEventHandler commonEventHandler = new CommonEventHandler();
		MinecraftForge.EVENT_BUS.register(commonEventHandler);
		proxy.init();
	}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}

	@EventHandler
	public static void serverStopped(FMLServerStoppedEvent event)
	{
		ReliquaryWorldData.clearInstance();
	}
}
