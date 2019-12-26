package aldrigos.mc.worldguard.listeners;

import aldrigos.mc.worldguard.FlagType;
import aldrigos.mc.worldguard.RegionManager;
import aldrigos.mc.worldguard.WorldGuardPlugin;
import cn.nukkit.event.*;
import cn.nukkit.event.entity.EntitySpawnEvent;

public class MobListener implements Listener {
    private final RegionManager rgm;

    public MobListener(WorldGuardPlugin p){
        rgm = p.RegionManager;
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e){
        if(!e.isCreature())
            return;

        var reg = rgm.getBlockRegion(e.getPosition().level.getId(), e.getPosition());
        if(reg == null)
            return;

        if(reg.Deny.contains(FlagType.Mob_spawning))
            e.setCancelled();
    }
}
