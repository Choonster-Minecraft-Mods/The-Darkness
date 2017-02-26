package thatmartinguy.thedarkness.data.capability;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import thatmartinguy.thedarkness.TheDarkness;
import thatmartinguy.thedarkness.network.PlayerHostMessage;
import thatmartinguy.thedarkness.util.CapabilityUtils;
import thatmartinguy.thedarkness.util.Reference;

public class PlayerHostProvider implements ICapabilitySerializable<NBTBase>
{
	@CapabilityInject(IPlayerHostCapability.class)
	public static final Capability<IPlayerHostCapability> PLAYER_HOST_CAPABILITY = null;

	private static final ResourceLocation IDENTIFIER = new ResourceLocation(Reference.MOD_ID, "isHost");

	private final IPlayerHostCapability instance = PLAYER_HOST_CAPABILITY.getDefaultInstance();

	public static void register()
	{
		CapabilityManager.INSTANCE.register(IPlayerHostCapability.class, new Capability.IStorage<IPlayerHostCapability>()
				{
					@Override
					public NBTBase writeNBT(Capability<IPlayerHostCapability> capability, IPlayerHostCapability instance, EnumFacing side)
					{
						NBTTagCompound nbt = new NBTTagCompound();
						nbt.setBoolean("isHost", instance.isHost());
						return nbt;
					}

					@Override
					public void readNBT(Capability<IPlayerHostCapability> capability, IPlayerHostCapability instance, EnumFacing side, NBTBase nbt)
					{
						NBTTagCompound compound = (NBTTagCompound) nbt;
						instance.setHost(compound.getBoolean("isHost"));
					}
				}, 
				PlayerHostCapability::new);
	}
	
	@Nullable
	public static IPlayerHostCapability getHostCapability(EntityPlayer player)
	{
		return CapabilityUtils.getCapability(player, PLAYER_HOST_CAPABILITY, null);
	}
	
	private static void sendToPlayer(PlayerHostCapability capability, EntityPlayerMP player, boolean isHost)
	{
		TheDarkness.NETWORK.sendTo(new PlayerHostMessage(capability.isHost()), player);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == PLAYER_HOST_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == PLAYER_HOST_CAPABILITY)
		{
			return PLAYER_HOST_CAPABILITY.cast(instance);
		}
		
		return null;
	}

	@Override
	public NBTBase serializeNBT()
	{
		return PLAYER_HOST_CAPABILITY.getStorage().writeNBT(PLAYER_HOST_CAPABILITY, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt)
	{
		PLAYER_HOST_CAPABILITY.getStorage().readNBT(PLAYER_HOST_CAPABILITY, this.instance, null, nbt);
	}
}
