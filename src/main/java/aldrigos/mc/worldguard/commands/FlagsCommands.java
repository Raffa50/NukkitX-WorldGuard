package aldrigos.mc.worldguard.commands;

import aldrigos.mc.worldguard.*;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

class FlagsCommands {
    private final RegionManager rgm;

    public FlagsCommands(WorldGuardPlugin p){
        rgm = p.RegionManager;
    }

    public boolean getFlags(CommandSender sender, LinkedList<String> largs) {
        if(!sender.hasPermission("wg.rg.flags")){
            sender.sendMessage(TextFormat.RED+"[WG]No permission for wg.rg.flags"+TextFormat.RESET);
            return false;
        }

        if(largs.isEmpty()) {
            sender.sendMessage(TextFormat.RED+"[WG]Insufficient arguments. Usage: /rg flags <rgName>"+TextFormat.RESET);
            return false;
        }

        String rgn = largs.peekFirst();
        var reg = rgm.getByName(rgn);
        if(reg == null){
            sender.sendMessage(TextFormat.RED+"[WG]No region "+rgn+" doesn't exist"+TextFormat.RESET);
            return false;
        }

        var msg = new StringBuilder(TextFormat.YELLOW+"[WG]Region flags: ");

        for(var f: reg.getDenied())
            msg.append(f).append(" deny, ");

        msg.deleteCharAt(msg.length() -2);
        msg.append(TextFormat.RESET);
        sender.sendMessage(msg.toString());

        return true;
    }

    public boolean flag(CommandSender sender, List<String> args) {
        if(!sender.hasPermission("wg.rg.flag")){
            sender.sendMessage(TextFormat.RED+"[WG]No permission for wg.rg.flag"+TextFormat.RESET);
            return false;
        }

        if(args.size() < 3){
            sender.sendMessage(TextFormat.RED+"[WG]Insufficient arguments. Usage: /rg flag <region> <flag> <deny|allow>"+TextFormat.RESET);
            return false;
        }

        String rgn = args.get(0);
        var reg = rgm.getByName(rgn);
        if(reg == null){
            sender.sendMessage(TextFormat.RED+"[WG]No region "+rgn+" doesn't exist"+TextFormat.RESET);
            return false;
        }

        String flagName = args.get(1).toLowerCase();
        var json = new Gson();
        FlagType flag = json.fromJson(flagName, FlagType.class);

        if(flag == null){
            sender.sendMessage(TextFormat.RED+"[WG]Flag: "+flagName+" doesn't exist"+TextFormat.RESET);
            return false;
        }

        switch (args.get(2)){
            case "deny":
                reg.setDenied(flag);
                break;
            case "allow":
                reg.setAllowed(flag);
                break;
            default:
                sender.sendMessage(TextFormat.RED+"[WG]Invalid flag modifier: "+args.get(2)+TextFormat.RESET);
                return false;
        }

        sender.sendMessage(TextFormat.DARK_GREEN+"[WG]Region "+rgn+" flag "+flagName+" added"+TextFormat.RESET);
        return true;
    }
}
