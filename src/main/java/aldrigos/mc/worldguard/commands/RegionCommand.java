package aldrigos.mc.worldguard.commands;

import aldrigos.mc.worldedit.WorldEdit;
import aldrigos.mc.worldguard.*;
import aldrigos.mc.worldguard.exceptions.AlreadyExistException;
import cn.nukkit.Player;
import cn.nukkit.command.*;
import cn.nukkit.utils.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class RegionCommand extends Command {
    public final RegionManager rgm;
    public final WorldEdit we;
    public final Logger log;
    private final FlagsCommands flagsCmds;

    public RegionCommand(WorldGuardPlugin p){
        super("rg");
        rgm = p.RegionManager;
        we = p.worldEdit;
        log = p.getLogger();
        flagsCmds = new FlagsCommands(p);
    }

    private boolean list(CommandSender sender) {
        if(!sender.hasPermission("wg.rg.list")){
            Messages.NO_PERM.send(sender, "wg.rg.list");
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
            Messages.NO_PERM.send(sender, "wg.rg.create");
            return false;
        }

        if(args.isEmpty()){
            Messages.PARAM_MISS.send(sender, "/rg create <rgName>");
            return false;
        }

        if(!sender.isPlayer()){
            Messages.PLAYERCMD.send(sender);
            return false;
        }
        var player = (Player)sender;
        int worldId = player.level.getId();

        if(!player.hasPermission("wg.rg.create")){
            player.sendMessage("No permission for wg.rg.create");
            return false;
        }

        var cuboid = we.getSelection(player.getId());
        if(cuboid == null || cuboid.P1 == null || cuboid.P2 == null){
            Messages.NO_SELECTION.send(player);
            return false;
        }

        String rgn = args.peekFirst();
        //check valid name
        if(!rgn.matches("[a-zA-Z][a-zA-Z0-9\\-_]*")){
            Messages.REGION_NAME_INVALID.send(sender, rgn);
            return false;
        }

        try {
            rgm.add(worldId, new Region(cuboid, rgn, player.getId()));
        } catch (AlreadyExistException e) {
            Messages.REGION_EXIST.send(player, rgn);
        }

        Messages.REGION_CREATED.send(player, rgn);
        return true;
    }

    private boolean delete(CommandSender sender, LinkedList<String> args){
        if(!sender.hasPermission("wg.rg.delete")){
            Messages.NO_PERM.send(sender, "wg.rg.delete");
            return false;
        }

        if(args.isEmpty()){
            Messages.PARAM_MISS.send(sender, "/rg delete <rgName>");
            return false;
        }

        String rgn = args.getFirst();
        var reg = rgm.getByName(rgn);
        if(reg == null){
            Messages.REGION_NOT_EXIST.send(sender, rgn);
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
            Messages.NO_PERM.send(sender, "wg.rg.update");
            return false;
        }

        if(args.isEmpty()){
            Messages.PARAM_MISS.send(sender, "/rg update <rgName>");
            return false;
        }

        if(!sender.isPlayer()){
            Messages.PLAYERCMD.send(sender);
            return false;
        }
        var player = (Player)sender;

        String rgn = args.pop();
        var reg = rgm.getByName(rgn);
        if(reg == null){
            Messages.REGION_NOT_EXIST.send(sender, rgn);
            return false;
        }

        var cuboid = we.getSelection(player.getId());
        if(cuboid == null || cuboid.P1 == null || cuboid.P2 == null){
            Messages.NO_SELECTION.send(player);
            return false;
        }

        reg.set(cuboid);
        Messages.REGION_UPDATED.send(player, rgn);

        return true;
    }
}
