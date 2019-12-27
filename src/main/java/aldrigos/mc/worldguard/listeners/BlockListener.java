package aldrigos.mc.worldguard.listeners;

import aldrigos.mc.worldguard.*;
import cn.nukkit.Player;
import cn.nukkit.event.*;
import cn.nukkit.event.block.*;
import cn.nukkit.utils.TextFormat;

import java.util.Map;

public class BlockListener implements Listener {
    public final RegionManager rgm;
    private final Map<Long, Cuboid> selection;

    public BlockListener(WorldGuardPlugin p){
        rgm = p.RegionManager;
        selection = p.Selection;
    }

    private Region check(Player p, BlockEvent e){
        if(p.isCreative())
            return null;

        return rgm.getBlockRegion(e.getBlock().getLocation());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        var region = check(e.getPlayer(), e);
        if(region == null)
            return;

        if(region.Deny.contains(FlagType.Block_place)) {
            e.getPlayer().sendMessage(TextFormat.RED+"[WG]Region is protected"+TextFormat.RESET);
            e.setCancelled();
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        var player = e.getPlayer();
        //check if player has wg rg stick
        if(player.isCreative() && e.getItem().getId() == Constants.Stick){
            var clickPosition = e.getBlock().getLocation();
            if(!selection.containsKey(player.getId()))
                selection.put(player.getId(), new Cuboid());

            selection.get(player.getId()).P1 = clickPosition;
            player.sendMessage(TextFormat.DARK_PURPLE+"[WG]First position set "+Utils.toString(clickPosition)+TextFormat.WHITE);

            e.setCancelled();
            return;
        }

        var region = check(player, e);
        if(region == null)
            return;

        if(region.Deny.contains(FlagType.Block_break)) {
            e.getPlayer().sendMessage(TextFormat.RED+"[WG]Region is protected"+TextFormat.RESET);
            e.setCancelled();
        }
    }
}
