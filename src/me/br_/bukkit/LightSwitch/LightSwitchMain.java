package me.br_.bukkit.LightSwitch;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LightSwitchMain extends JavaPlugin {
	public LightSwitchBlockListener bL;

	@Override
	public void onDisable() {
		bL.save();
		bL = null;
		System.out.println("[LightSwitch] Disabled.");
	}

	@Override
	public void onEnable() {
		System.out.println("[LightSwitch] Enabled.");
		bL = new LightSwitchBlockListener();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.REDSTONE_CHANGE, bL, Event.Priority.Normal,
				this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, bL, Event.Priority.Normal,
				this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, bL, Event.Priority.Normal,
				this);
	}
}
