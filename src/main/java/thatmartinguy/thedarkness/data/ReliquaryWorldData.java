package thatmartinguy.thedarkness.data;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import thatmartinguy.thedarkness.TheDarkness;
import thatmartinguy.thedarkness.network.ReliquaryMessage;
import thatmartinguy.thedarkness.util.Reference;

public class ReliquaryWorldData extends WorldSavedData
{
	private static final String IDENTIFIER = Reference.MOD_ID + "reliquary";

	private boolean isReliquaryCrafted;
	private World world;
	
	private static ReliquaryWorldData instance;
	
	public ReliquaryWorldData()
	{
		super(IDENTIFIER);
	}

	public ReliquaryWorldData(String name)
	{
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		isReliquaryCrafted = compound.getBoolean("isReliquaryCrafted");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setBoolean("isReliquaryCrafted", isReliquaryCrafted);
		return compound;
	}
	
	public void setReliquaryCrafted(boolean isReliquaryCrafted)
	{
		this.isReliquaryCrafted = isReliquaryCrafted;
		
		if(!world.isRemote)
		{
			TheDarkness.NETWORK.sendToAll(new ReliquaryMessage(this.isReliquaryCrafted));
		}
		this.markDirty();
	}

	public boolean isReliquaryCrafted()
	{
		return isReliquaryCrafted;
	}
	
	public static ReliquaryWorldData get(World world)
	{
		ReliquaryWorldData reliquaryWorldData;
			
		if(!world.isRemote && instance != null)
		{
			reliquaryWorldData = instance;
		}
		else
		{
			reliquaryWorldData = (ReliquaryWorldData) world.loadItemData(ReliquaryWorldData.class, IDENTIFIER);
			if(reliquaryWorldData == null)
			{
				reliquaryWorldData = new ReliquaryWorldData();
			}
			
			if(reliquaryWorldData.world == null)
			{
				reliquaryWorldData.world = world;
			}
		}
		world.setItemData(IDENTIFIER, reliquaryWorldData);
		
		return reliquaryWorldData;
	}
	
	public static void clearInstance()
	{
		instance = null;
	}
	
	@Mod.EventBusSubscriber
	private static class EventHandler
	{
		private static void sendToPlayer(EntityPlayerMP player)
		{
			final ReliquaryWorldData reliquaryWorldData = ReliquaryWorldData.get(player.getEntityWorld());
			TheDarkness.NETWORK.sendTo(new ReliquaryMessage(reliquaryWorldData.isReliquaryCrafted), player);
		}
		
		@SubscribeEvent
		public static void playerLoggedInEvent(PlayerLoggedInEvent event)
		{
			if(event.player instanceof EntityPlayerMP)
			{
				sendToPlayer((EntityPlayerMP) event.player);
			}
		}
		
		@SubscribeEvent
		public static void playerChangedDimensionEvent(PlayerChangedDimensionEvent event)
		{
			if(event.player instanceof EntityPlayerMP)
			{
				sendToPlayer((EntityPlayerMP) event.player);
			}
		}
	}
}
