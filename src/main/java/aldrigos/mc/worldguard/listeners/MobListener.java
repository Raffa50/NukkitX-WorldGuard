package aldrigos.mc.worldguard.listeners;

import aldrigos.mc.worldguard.*;
import cn.nukkit.event.*;
import cn.nukkit.event.entity.EntitySpawnEvent;

public class MobListener implements Listener {
    private final RegionManager rgm;

    public MobListener(WorldGuardPlugin p){
        rgm = p.RegionManager;
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e){
        if(!e.isCreature() || e.getPosition() == null)
            return;

        var reg = rgm.getBlockRegion(e.getPosition());
        if(reg == null)
            return;

        if(reg.isDenied(FlagType.Mob_spawning))
            e.setCancelled();
    }
}
