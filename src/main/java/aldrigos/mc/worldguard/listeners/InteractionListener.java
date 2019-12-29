package aldrigos.mc.worldguard.listeners;

import aldrigos.mc.worldguard.*;
import aldrigos.mc.worldguard.Utils;
import cn.nukkit.Player;
import cn.nukkit.event.*;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.utils.*;

import java.util.Map;

public class InteractionListener implements Listener {
    private final RegionManager rgm;
    private final Map<Long, Cuboid> selection;
    private final Logger log;

    public InteractionListener(WorldGuardPlugin p){
        rgm = p.RegionManager;
        log = p.getLogger();
        selection = p.Selection;
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
                player.sendMessage(TextFormat.RED+"[WG]Pvp is denied in this region"+TextFormat.RESET);
            }
        }else{ //creature
            if(reg.isDenied(FlagType.Damage_animals)){
                e.setCancelled();
                player.sendMessage(TextFormat.RED+"[WG]Mob damage is denied in this region"+TextFormat.RESET);
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
        if(item == null || item.getId() != Constants.Stick)
            return;

        //if(item.getCustomBlockData().)

        if(e.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK){
            if(!selection.containsKey(player.getId()))
                selection.put(player.getId(), new Cuboid());

            selection.get(player.getId()).P2 = new Vector3Adapter(clickPosition);
            player.sendMessage(TextFormat.DARK_PURPLE+"Second position set "+ Utils.toString(clickPosition)+TextFormat.WHITE);
        }
    }
}
