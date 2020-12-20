package ca.matai.crossinvent.inventory;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundHandler {
	
	private Inv pd;
	
	public SoundHandler(Inv pd) {
		this.pd = pd;
	}
	
	public void sendPlingSound(Player p) {
		if (!pd.getConfigHandler().getBoolean("General.disableSounds")) {
			if (Inv.is13Server) {
				//p.playSound(p.getLocation(), Sound.NOTE_BASS, 3F, 3);
			} else if (Inv.is19Server) {
				//p.playSound(p.getLocation(), Sound.valueOf("BLOCK_NOTE_PLING"), 3F, 3F);
			} else {
				//p.playSound(p.getLocation(), Sound.valueOf("NOTE_PLING"), 3F, 3F);
			}
		}
	}
	
	public void sendLevelUpSound(Player p) {
		if (!pd.getConfigHandler().getBoolean("General.disableSounds")) {
			if (Inv.is19Server) {
				//p.playSound(p.getLocation(), Sound.LEVEL_UP, 1F, 1);
			} else {
				//p.playSound(p.getLocation(), Sound.valueOf("LEVEL_UP"), 1, 1F);
			}
		}
	}
	
	public void sendArrowHit(Player p) {
		if (!pd.getConfigHandler().getBoolean("General.disableSounds")) {
			if (Inv.is19Server) {
				//p.playSound(p.getLocation(), Sound.ARROW_HIT, 3F, 3);
			} else {
				//p.playSound(p.getLocation(), Sound.valueOf("SUCCESSFUL_HIT"), 3F, 3F);
			}
		}
	}

}
