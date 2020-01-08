package aldrigos.mc.worldguard.listeners;

import aldrigos.mc.worldguard.*;
import cn.nukkit.Player;
import cn.nukkit.event.*;
import cn.nukkit.event.block.*;
import cn.nukkit.item.ItemID;
import cn.nukkit.utils.TextFormat;

import java.util.Map;

public class BlockListener implements Listener {
    public final RegionManager rgm;

    public BlockListener(WorldGuardPlugin p){
        rgm = p.RegionManager;
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

        if(region.isDenied(FlagType.Block_place)) {
            e.setCancelled();
            Messages.REGION_PROTECTED.send(e.getPlayer());
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        var player = e.getPlayer();

        var region = check(player, e);
        if(region == null)
            return;

        if(region.isDenied(FlagType.Block_break)) {
            e.getPlayer().sendMessage(TextFormat.RED+"[WG]Region is protected"+TextFormat.RESET);
            e.setCancelled();
        }
    }

    @EventHandler
    public void onIgnite(BlockIgniteEvent e){
        var reg = rgm.getBlockRegion(e.getBlock().getLocation());
        if(reg == null)
            return;

        switch(e.getCause()){
            case FLINT_AND_STEEL:
                if(reg.isDenied(FlagType.Lighter)) {
                    e.setCancelled();
                    var player = (Player)e.getEntity();
                    Messages.REGION_PROTECTED.send(player);
                }
                break;
            case SPREAD:
                if(reg.isDenied(FlagType.Fire_spread))
                    e.setCancelled();
                break;
            case LAVA:
                if(reg.isDenied(FlagType.Lava_fire))
                    e.setCancelled();
        }
    }
}
