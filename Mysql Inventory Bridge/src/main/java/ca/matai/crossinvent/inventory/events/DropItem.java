package ca.matai.crossinvent.inventory.events;

import ca.matai.crossinvent.inventory.Inv;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItem implements Listener {
	
	private Inv pd;
	
	public DropItem(Inv pd) {
		this.pd = pd;
	}
	
	@EventHandler
	public void onItemDrop(final PlayerDropItemEvent event) {
		if (!pd.getInventoryDataHandler().isSyncComplete(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

}
