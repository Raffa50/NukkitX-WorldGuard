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

    public boolean getFlags(CommandSender sender, LinkedList<String> args) {
        if(!sender.hasPermission("wg.rg.flags")){
            Messages.NO_PERM.send(sender, "wg.rg.flags");
            return false;
        }

        if(args.isEmpty()) {
            Messages.PARAM_MISS.send(sender, "/rg flags <rgName>");
            return false;
        }

        String rgn = args.peekFirst();
        var reg = rgm.getByName(rgn);
        if(reg == null){
            Messages.REGION_NOT_EXIST.send(sender, rgn);
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
            Messages.NO_PERM.send(sender, "wg.rg.flag");
            return false;
        }

        if(args.isEmpty()){ //list all flags
            var sb = new StringBuilder();
            for(var f: FlagType.values())
                sb.append(f).append(" ");

            Messages.FLAG_LIST.send(sender, sb.toString());
            return true;
        }
        if(args.size() < 3){
            Messages.PARAM_MISS.send(sender, "/rg flag <region> <flag> <deny|allow>");
            return false;
        }

        String rgn = args.get(0);
        var reg = rgm.getByName(rgn);
        if(reg == null){
            Messages.REGION_NOT_EXIST.send(sender, rgn);
            return false;
        }

        String flagName = args.get(1).toLowerCase();
        var json = new Gson();
        FlagType flag = json.fromJson(flagName, FlagType.class);

        if(flag == null){
            Messages.FLAG_INVALID.send(sender, flagName);
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
                Messages.FLAG_MOD_INVALID.send(sender, args.get(2));
                return false;
        }
        Messages.FLAG_ADDED.send(sender, rgn, flagName, args.get(2));

        return true;
    }
}
