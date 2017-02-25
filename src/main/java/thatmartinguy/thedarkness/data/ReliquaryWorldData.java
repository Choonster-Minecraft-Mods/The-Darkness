package thatmartinguy.thedarkness.data;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import thatmartinguy.thedarkness.TheDarkness;
import thatmartinguy.thedarkness.network.ReliquaryMessage;
import thatmartinguy.thedarkness.reference.Reference;

public class ReliquaryWorldData extends WorldSavedData
{
	private static final String IDENTIFIER = Reference.MOD_ID + "reliquary";

	private static ReliquaryWorldData instance;

	private World world;
	private boolean isReliquaryCrafted;

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

		if (!world.isRemote)
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

		// If this is the server and the instance already exists, use it
		if (!world.isRemote && instance != null)
		{
			reliquaryWorldData = instance;
		}
		else
		{
			// Get the data from the MapStorage or create it it doesn't yet exist
			reliquaryWorldData = (ReliquaryWorldData) world.loadItemData(ReliquaryWorldData.class, IDENTIFIER);
			if (reliquaryWorldData == null)
			{
				reliquaryWorldData = new ReliquaryWorldData();
			}

			// Set the world field if the data was just created or loaded from disk
			if (reliquaryWorldData.world == null)
			{
				reliquaryWorldData.world = world;
			}

			// If this is the server, store the data in the instance field
			if (!world.isRemote)
			{
				instance = reliquaryWorldData;
			}
		}

		// Store the data in the MapStorage
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
		/**
		 * Send the reliquary crafted state to a player.
		 *
		 * @param player The player
		 */
		private static void sendToPlayer(EntityPlayerMP player)
		{
			final ReliquaryWorldData reliquaryWorldData = ReliquaryWorldData.get(player.getEntityWorld());
			TheDarkness.NETWORK.sendTo(new ReliquaryMessage(reliquaryWorldData.isReliquaryCrafted()), player);
		}

		/**
		 * Send the reliquary crafted state to a player when they log in.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
		{
			if (event.player instanceof EntityPlayerMP)
			{
				sendToPlayer((EntityPlayerMP) event.player);
			}
		}

		/**
		 * Send the reliquary crafted state to a player when they change dimension.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
		{
			if (event.player instanceof EntityPlayerMP)
			{
				sendToPlayer((EntityPlayerMP) event.player);
			}
		}
	}
}
