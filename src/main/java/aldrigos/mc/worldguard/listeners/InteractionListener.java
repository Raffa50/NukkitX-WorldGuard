package aldrigos.mc.worldguard.listeners;

import aldrigos.mc.worldguard.*;
import cn.nukkit.Player;
import cn.nukkit.event.*;
import cn.nukkit.event.entity.*;
import cn.nukkit.event.player.*;
import cn.nukkit.utils.*;

import java.util.Map;

import static cn.nukkit.event.player.PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK;

public class InteractionListener implements Listener {
    private final RegionManager rgm;
    private final Logger log;
    private final WorldGuardPlugin wg;

    public InteractionListener(WorldGuardPlugin p){
        rgm = p.RegionManager;
        log = p.getLogger();
        wg = p;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player))
            return;
        var player = (Player) e.getDamager();

        var reg = rgm.getBlockRegion(e.getEntity().getLocation());
        if(reg == null)
            return;

        if(e.getEntity() instanceof Player){
            if(reg.isDenied(FlagType.Pvp)){
                e.setCancelled();
                Messages.FLAG_DENIED.send(player, "pvp");
            }
        }else{ //creature
            if(reg.isDenied(FlagType.Damage_animals)){
                e.setCancelled();
                Messages.FLAG_DENIED.send(player, "damage mob");
            }
        }
    }

    @EventHandler
    public void onExplode(ExplosionPrimeEvent e){
        var reg = rgm.getBlockRegion(e.getEntity().getPosition());
        if(reg == null)
            return;

        if(reg.isDenied(FlagType.Explosions))
            e.setCancelled();
    }

    @EventHandler
    public void onExplode(EntityExplosionPrimeEvent e){
        var reg = rgm.getBlockRegion(e.getEntity().getPosition());
        if(reg == null)
            return;

        if(reg.isDenied(FlagType.Explosions))
            e.setCancelled();
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent e){
        if(e.getPlayer().isCreative())
            return;

        var pos = e.getBlockClicked().getLocation();
        var reg = rgm.getBlockRegion(pos);
        if(reg == null)
            return;

        if(reg.isDenied(FlagType.Block_place)) {
            e.setCancelled();
            Messages.REGION_PROTECTED.send(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent e){
        if(e.getPlayer().isCreative())
            return;

        var pos = e.getBlockClicked().getLocation();
        var reg = rgm.getBlockRegion(pos);
        if(reg == null)
            return;

        if(reg.isDenied(FlagType.Block_break)) {
            e.setCancelled();
            Messages.REGION_PROTECTED.send(e.getPlayer());
        }
    }
}
