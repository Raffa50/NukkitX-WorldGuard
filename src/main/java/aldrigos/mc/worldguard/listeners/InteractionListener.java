package aldrigos.mc.worldguard.listeners;

import aldrigos.mc.worldguard.*;
import aldrigos.mc.worldguard.Utils;
import cn.nukkit.event.*;
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

            selection.get(player.getId()).P2 = clickPosition;
            player.sendMessage(TextFormat.DARK_PURPLE+"Second position set "+ Utils.toString(clickPosition)+TextFormat.WHITE);
        }
    }
}
