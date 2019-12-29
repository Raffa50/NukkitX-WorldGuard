package aldrigos.mc.worldguard.commands;

import aldrigos.mc.worldguard.*;
import aldrigos.mc.worldguard.exceptions.AlreadyExistException;
import cn.nukkit.Player;
import cn.nukkit.command.*;
import cn.nukkit.utils.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

public class RegionCommand extends Command {
    public final RegionManager rgm;
    public final Map<Long, Cuboid> selection;
    public final Logger log;
    private final FlagsCommands flagsCmds;

    public RegionCommand(WorldGuardPlugin p){
        super("rg");
        rgm = p.RegionManager;
        selection = p.Selection;
        log = p.getLogger();
        flagsCmds = new FlagsCommands(p);
    }

    private boolean list(CommandSender sender) {
        if(!sender.hasPermission("wg.rg.list")){
            sender.sendMessage(TextFormat.RED+"[WG]No permission for wg.rg.list"+TextFormat.RESET);
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
        if(!sender.hasPermission("wg.rg.create")){
            sender.sendMessage(TextFormat.RED+"[WG]No permission for wg.rg.create"+TextFormat.RESET);
            return false;
        }

        if(args.isEmpty()){
            sender.sendMessage("[WG]Missing parameter <rgName>. Usage: /rg create <rgName>");
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

        sender.sendMessage(TextFormat.YELLOW+"[WG]Region "+rgn+" created"+TextFormat.RESET);
        return true;
    }

    private boolean delete(CommandSender sender, LinkedList<String> args){
        if(!sender.hasPermission("wg.rg.delete")){
            sender.sendMessage(TextFormat.RED+"[WG]No permission for wg.rg.delete"+TextFormat.RESET);
            return false;
        }

        if(args.isEmpty()){
            sender.sendMessage(TextFormat.RED+"[WG]Missing parameter. Usage: /rg delete <rgName>"+TextFormat.RESET);
            return false;
        }

        String rgn = args.getFirst();
        var reg = rgm.getByName(rgn);
        if(reg == null){
            sender.sendMessage(TextFormat.RED+"[WG]No region "+rgn+" doesn't exist"+TextFormat.RESET);
            return false;
        }

        rgm.delete(reg);

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
            case "delete":
                return delete(sender, largs);
            case "update":
                return update(sender, largs);
            case "list":
                return list(sender);
            case "flag":
                return flagsCmds.flag(sender, largs);
            case "flags":
                return flagsCmds.getFlags(sender, largs);
            default:
                sender.sendMessage(TextFormat.RED+"[WG]Unknown /rg sub-command: "+ sub+TextFormat.RESET);
                return false;
        }
    }

    private boolean update(CommandSender sender, LinkedList<String> args) {
        if(!sender.hasPermission("wg.rg.update")){
            sender.sendMessage(TextFormat.RED+"[WG]No permission for wg.rg.update"+TextFormat.RESET);
            return false;
        }

        if(args.isEmpty()){
            sender.sendMessage(TextFormat.RED+"[WG]Missing parameter. Usage: /rg update <rgName>"+TextFormat.RESET);
            return false;
        }

        if(!sender.isPlayer()){
            sender.sendMessage("[WG]This is a player command!");
            return false;
        }
        var player = (Player)sender;

        String rgn = args.pop();
        var reg = rgm.getByName(rgn);
        if(reg == null){
            sender.sendMessage(TextFormat.RED+"[WG]No region "+rgn+" doesn't exist"+TextFormat.RESET);
            return false;
        }

        var cuboid = selection.get(player.getId());
        if(cuboid == null || cuboid.P1 == null || cuboid.P2 == null){
            player.sendMessage("[WG]No selection was made");
            return false;
        }

        reg.set(cuboid);
        player.sendMessage("[WG]Region updated");

        return true;
    }
}
