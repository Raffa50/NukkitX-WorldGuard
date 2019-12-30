package aldrigos.mc.worldguard.commands;

import aldrigos.mc.worldguard.Cuboid;
import aldrigos.mc.worldguard.*;
import cn.nukkit.command.*;
import cn.nukkit.utils.Logger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

public class WgCommand extends Command {
    public final RegionManager rgm;
    public final Map<Long, Cuboid> selection;
    public final Logger log;

    public WgCommand(WorldGuardPlugin p){
        super("wg");
        rgm = p.RegionManager;
        selection = p.Selection;
        log = p.getLogger();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if(args.length < 1){
            sender.sendMessage("[WG]Commands list:\n" +
                    "/wg info : about this plugin\n"+
                    "/rg create <region> : create a new region\n" +
                    "/rg delete <region> : delete a region\n"+
                    "/rg flag : list all available flags\n"+
                    "/rg flag <region> <flag> <allow|deny> : add a flag to region\n"+
                    "/rg flags <region> : show set flags for the region\n"+
                    "/rg update <region> : change region cuboid with current selection"
            );
            return true;
        }

        final String sub = args[0];

        switch (sub){
            case "info":
                sender.sendMessage("[WG]WorldGuard port for Nukkit by Aldrigo Raffaele");
                return true;
            default:
                sender.sendMessage("[WG]Unknown wg sub-command: "+ cmd);
                return false;
        }

        /*var subArgs = new LinkedList<String>(Arrays.asList(args));
        subArgs.removeFirst();

        return executor.execute(sender, cmd, subArgs);*/
    }
}
