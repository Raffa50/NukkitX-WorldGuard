package aldrigos.mc.worldguard.commands;

import aldrigos.mc.worldguard.*;
import aldrigos.mc.worldguard.exceptions.AlreadyExistException;
import cn.nukkit.Player;
import cn.nukkit.command.*;
import cn.nukkit.utils.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RegionCommand extends Command {
    public final RegionManager rgm;
    public final Map<Long, Cuboid> selection;
    public final Logger log;

    public RegionCommand(WorldGuardPlugin p){
        super("rg");
        rgm = p.RegionManager;
        selection = p.Selection;
        log = p.getLogger();
    }

    private boolean list(CommandSender sender) {
        if(sender.isPlayer() && !sender.hasPermission("wg.rg.list")){
            log.info("No permission for wg.rg.list");
            return false;
        }

        var regions = new StringBuilder(TextFormat.YELLOW+"[WG]Regions: ");
        for(var r: rgm.getAllRegions())
            regions.append(r.getName()).append(", ");

        regions.deleteCharAt(regions.length() -2);
        regions.append(TextFormat.WHITE);

        sender.sendMessage(regions.toString());

        return true;
    }

    private boolean create(CommandSender sender, LinkedList<String> args) {
        if(args.isEmpty()){
            sender.sendMessage("[WG]Missing parameter <rgName>. Usage: /wg rg create <rgName>");
            return false;
        }

        if(!sender.isPlayer()){
            sender.sendMessage("[WG]This is a player command!");
            return false;
        }
        var player = (Player)sender;
        int worldId = player.level.getId();

        if(!player.hasPermission("wg.rg.create")){
            player.sendMessage("No permission for wg.rg.create");
            return false;
        }

        var cuboid = selection.get(player.getId());
        if(cuboid == null || cuboid.P1 == null || cuboid.P2 == null){
            player.sendMessage("[WG]No selection was made");
            return false;
        }

        String rgn = args.peekFirst();
        try {
            rgm.add(worldId, new Region(cuboid, rgn));
        } catch (AlreadyExistException e) {
            player.sendMessage("[WG]Region "+rgn+" already exist");
        }

        sender.sendMessage(TextFormat.YELLOW+"[WG]Region "+rgn+" created"+TextFormat.WHITE);
        return true;
    }

    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if(args.length == 0)
            return false;

        var largs = new LinkedList<>(Arrays.asList(args));
        String sub = largs.removeFirst();
        switch(sub){
            case "create":
                return create(sender, largs);
            case "list":
                return list(sender);
            case "flag":
                return flag(sender, largs);
            case "flags":
                return getFlags(sender, largs);
            default:
                sender.sendMessage("[WG]Unknown wg rg sub-command: "+ sub);
                return false;
        }
    }

    private boolean getFlags(CommandSender sender, LinkedList<String> largs) {
        if(largs.isEmpty())
            return false;

        String rgn = largs.peekFirst();
        var reg = rgm.getByName(rgn);
        if(reg == null){
            sender.sendMessage(TextFormat.RED+"[WG]No region "+rgn+" doesn't exist"+TextFormat.WHITE);
            return false;
        }

        var msg = new StringBuilder(TextFormat.YELLOW+"[WG]Region flags: ");

        for(var f: reg.Deny)
            msg.append(f).append(" deny, ");

        msg.deleteCharAt(msg.length() -2);
        msg.append(TextFormat.WHITE);
        sender.sendMessage(msg.toString());

        return true;
    }

    private boolean flag(CommandSender sender, List<String> args) {
        if(args.size() < 3){
            sender.sendMessage(TextFormat.RED+"[WG]Insufficient arguments. Usage: /rg flag <region> <flag> <deny|allow>"+TextFormat.WHITE);
            return false;
        }

        var reg = rgm.getByName(args.get(0));
        if(reg == null){
            sender.sendMessage(TextFormat.RED+"[WG]No region "+args.get(0)+" doesn't exist"+TextFormat.WHITE);
            return false;
        }

        FlagType flag;
        String flagName = args.get(1).toLowerCase();
        switch(flagName){
            case "block-break":
                flag = FlagType.Block_break;
                break;
            case "block-place":
                flag = FlagType.Block_place;
                break;
            case "mob-spawning":
                flag = FlagType.Mob_spawning;
                break;
            default:
                sender.sendMessage(TextFormat.RED+"[WG]Flag: "+flagName+" doesn't exist"+TextFormat.WHITE);
                return false;
        }

        switch (args.get(2)){
            case "deny":
                reg.Deny.add(flag);
                break;
            case "allow":
                reg.Deny.remove(flag);
                break;
            default:
                sender.sendMessage(TextFormat.RED+"[WG]Invalid flag modifier: "+args.get(2)+TextFormat.WHITE);
                return false;
        }

        sender.sendMessage(TextFormat.DARK_GREEN+"[WG]Region flag added"+TextFormat.WHITE);
        return true;
    }
}
