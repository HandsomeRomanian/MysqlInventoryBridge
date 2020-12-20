package ca.matai.crossinvent.inventory.events;

import ca.matai.crossinvent.inventory.Inv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerQuit implements Listener {
	
	private Inv inv;
	
	public PlayerQuit(Inv inv) {
		this.inv = inv;
	}
	
	@EventHandler
	public void onDisconnect(final PlayerQuitEvent event) {
		if (!Inv.isDisabling) {
			Bukkit.getScheduler().runTaskLaterAsynchronously(inv, new Runnable() {

				@Override
				public void run() {
					if (event.getPlayer() != null) {
						Player p = event.getPlayer();
						ItemStack[] inventory = inv.getInventoryDataHandler().getInventory(p);
						ItemStack[] armor = inv.getInventoryDataHandler().getArmor(p);
						ItemStack[] echest = p.getEnderChest().getContents();
						inv.getInventoryDataHandler().onDataSaveFunction(p, true, "true", inventory, armor,echest);
					}
				}
				
			}, 2L);
		}
	}

}
