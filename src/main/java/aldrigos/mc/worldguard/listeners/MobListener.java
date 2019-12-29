package aldrigos.mc.worldguard.listeners;

import aldrigos.mc.worldguard.*;
import cn.nukkit.event.*;
import cn.nukkit.event.entity.EntitySpawnEvent;
import cn.nukkit.utils.Logger;

public class MobListener implements Listener {
    private final RegionManager rgm;
    private final Logger log;

    public MobListener(WorldGuardPlugin p){
        rgm = p.RegionManager;
        log = p.getLogger();
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e){
        try {
            if (!e.isCreature() || e.getPosition() == null)
                return;

            var reg = rgm.getBlockRegion(e.getPosition());
            if (reg == null)
                return;

            if (reg.isDenied(FlagType.Mob_spawning))
                e.setCancelled();
        } catch(Exception ex){
            log.error("[WG]Exception"+ex.getStackTrace());
        }
    }
}
