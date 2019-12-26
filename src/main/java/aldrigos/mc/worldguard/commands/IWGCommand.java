package aldrigos.mc.worldguard.commands;

import cn.nukkit.command.*;

import java.util.LinkedList;

interface IWGCommand {
    boolean execute(CommandSender sender, String cmd, LinkedList<String> args);
}
