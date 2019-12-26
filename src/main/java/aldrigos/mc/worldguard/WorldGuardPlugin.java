package aldrigos.mc.worldguard;

import aldrigos.mc.worldguard.commands.*;
import aldrigos.mc.worldguard.listeners.*;
import cn.nukkit.plugin.PluginBase;

import java.util.HashMap;
import java.util.Map;

public class WorldGuardPlugin extends PluginBase {
    public final Map<Long, Cuboid> Selection= new HashMap<>();
    public RegionManager RegionManager;

    @Override
    public void onEnable(){
        RegionManager = new RegionManager(this);

        var pm = this.getServer().getPluginManager();
        pm.registerEvents(new InteractionListener(this), this);
        pm.registerEvents(new BlockListener(this), this);
        pm.registerEvents(new MobListener(this), this);

        this.getServer().getCommandMap().register("wg", new WgCommand(this));
        this.getServer().getCommandMap().register("rg", new RegionCommand(this));

        this.getServer().getLogger().info("[WG]WorldGuard-port by Aldrigo R. ENABLED");
    }

    @Override
    public void onDisable(){
        this.getServer().getLogger().info("[WG]disabled");
    }
}
