package ca.matai.crossinvent.inventory.events;

import ca.matai.crossinvent.inventory.objects.SyncCompleteTask;
import ca.matai.crossinvent.inventory.Inv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
	
	private Inv inv;
	
	public PlayerJoin(Inv inv) {
		this.inv = inv;
	}
	
	@EventHandler
	public void onLogin(final PlayerJoinEvent event) {		
		if (!Inv.isDisabling) {
			final Player p = event.getPlayer();
			Bukkit.getScheduler().runTaskLaterAsynchronously(inv, new Runnable() {

				@Override
				public void run() {
					if (p != null) {
						if (p.isOnline()) {
							inv.getInventoryDataHandler().onJoinFunction(p);
							new SyncCompleteTask(inv, System.currentTimeMillis(), p).runTaskTimerAsynchronously(inv, 5L, 20L);
						}
					}
				}
				
			}, 5L);
		}
	}

}
