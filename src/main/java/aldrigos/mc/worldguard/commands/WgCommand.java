package aldrigos.mc.worldguard.commands;

import aldrigos.mc.worldguard.*;
import cn.nukkit.command.*;
import cn.nukkit.utils.Logger;

public class WgCommand extends Command {
    private static final String help = "[WG]Commands list:\n" +
            "/wg info : about this plugin\n"+
            "/rg create <region> : create a new region\n" +
            "/rg delete <region> : delete a region\n"+
            "/rg flag : list all available flags\n"+
            "/rg flag <region> <flag> <allow|deny> : add a flag to region\n"+
            "/rg flags <region> : show set flags for the region\n"+
            "/rg update <region> : change region cuboid with current selection";

    public final RegionManager rgm;
    public final Logger log;

    public WgCommand(WorldGuardPlugin p){
        super("wg");
        rgm = p.RegionManager;
        log = p.getLogger();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if(args.length < 1){
            sender.sendMessage(help);
            return true;
        }

        switch (args[0]){
            case "info":
                sender.sendMessage("[WG]WorldGuard port for Nukkit by Aldrigo Raffaele");
                return true;
            case "?":
            case "help":
                sender.sendMessage(help);
                return true;
            default:
                sender.sendMessage("[WG]Unknown wg sub-command: "+ cmd);
                return false;
        }
    }
}
