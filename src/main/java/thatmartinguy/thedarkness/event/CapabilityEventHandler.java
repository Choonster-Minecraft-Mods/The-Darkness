package thatmartinguy.thedarkness.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thatmartinguy.thedarkness.data.capability.PlayerHostProvider;
import thatmartinguy.thedarkness.util.Reference;

public class CapabilityEventHandler
{
	public static final ResourceLocation IS_HOST = new ResourceLocation(Reference.MOD_ID, "isHost");
	
	//Attach player capabilities
	@SubscribeEvent
	public void attachPlayerCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event)
	{
		if(event.getObject() instanceof EntityPlayer)
		{
			event.addCapability(IS_HOST, new PlayerHostProvider());
			System.out.println("Attached PlayerHostCapability");
		}
	}
}
