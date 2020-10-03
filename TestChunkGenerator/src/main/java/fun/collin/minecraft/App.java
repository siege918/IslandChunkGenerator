package fun.collin.minecraft;

import org.bukkit.plugin.java.JavaPlugin;

import nl.rutgerkok.worldgeneratorapi.event.WorldGeneratorInitEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Hello world!
 *
 */
public class App extends JavaPlugin implements Listener
{
    @Override
	public void onEnable() {
        getLogger().info("onEnable has been invoked!");
        getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		getLogger().info("onDisable has been invoked!");
    }
    
    @EventHandler
    public void onWorldGeneratorInit(WorldGeneratorInitEvent event) {
        String worldName = event.getWorldGenerator().getWorld().getName();
        if (!worldName.equals("world")) {
            getLogger().info("Not loading terrain generator for world " + worldName);
            return;
        }

        
        getLogger().info("Loading terrain generator for world " + worldName);

        long seed = event.getWorldGenerator().getWorld().getSeed();
        getLogger().info("Got vanilla biome generator");
        event.getWorldGenerator().setBiomeGenerator(new CustomBiomeGenerator(seed));
        getLogger().info("Created custom biome generator");
    }
}
