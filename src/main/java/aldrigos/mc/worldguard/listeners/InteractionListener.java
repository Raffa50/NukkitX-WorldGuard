package aldrigos.mc.worldguard.listeners;

import aldrigos.mc.worldguard.*;
import aldrigos.mc.worldguard.Utils;
import cn.nukkit.Player;
import cn.nukkit.block.BlockID;
import cn.nukkit.event.*;
import cn.nukkit.event.entity.*;
import cn.nukkit.event.player.*;
import cn.nukkit.item.ItemID;
import cn.nukkit.utils.*;

import java.util.Map;

import static cn.nukkit.event.player.PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK;

public class InteractionListener implements Listener {
    private final RegionManager rgm;
    private final Map<Long, Cuboid> selection;
    private final Logger log;
    private final WorldGuardPlugin wg;

    public InteractionListener(WorldGuardPlugin p){
        rgm = p.RegionManager;
        log = p.getLogger();
        selection = p.Selection;
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
    public void onPlayerInteract(PlayerInteractEvent e){
        var player = e.getPlayer();
        var clickPosition = e.getBlock().getLocation();

        if(!player.isCreative())
            return;

        var item = e.getItem();
        //check if player has wg rg stick
        if(item == null || item.getId() != ItemID.STICK)
            return;

        //if(item.getCustomBlockData().)

        if(e.getAction() == RIGHT_CLICK_BLOCK){
            if(!selection.containsKey(player.getId()))
                selection.put(player.getId(), new Cuboid());

            selection.get(player.getId()).P2 = new Vector3Adapter(clickPosition);
            player.sendMessage(TextFormat.DARK_PURPLE+"[WG]Second position set "+ Utils.toString(clickPosition)+TextFormat.WHITE);
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
