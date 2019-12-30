package aldrigos.mc.worldguard;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

public enum Messages {
    NO_PERM(TextFormat.RED+"[WG]No permission for %s"),
    PARAM_MISS(TextFormat.RED+"[WG]Missing parameter. Usage: %s"),
    PLAYERCMD("[WG]This is a player command!"),
    NO_SELECTION(TextFormat.RED+"[WG]No selection was made"),
    REGION_EXIST(TextFormat.RED+"[WG]Region %s already exist"),
    REGION_NOT_EXIST(TextFormat.RED+"[WG]Region %s doesn't exist"),
    REGION_CREATED(TextFormat.GREEN+"[WG]Region %s created"),
    REGION_UPDATED(TextFormat.YELLOW+"[WG]Region %s updated"),
    REGION_PROTECTED(TextFormat.RED+"[WG]Region is protected"),
    REGION_NAME_INVALID(TextFormat.RED+"[WG]Invalid region name %s"),
    FLAG_INVALID(TextFormat.RED+"[WG]Flag: %s is invalid"),
    FLAG_MOD_INVALID(TextFormat.RED+"[WG]Invalid flag modifier: %s"),
    FLAG_ADDED(TextFormat.GREEN+"[WG]Region %s flag %s %s added"),
    FLAG_LIST(TextFormat.YELLOW+"[WG]Available flags: %s"),
    FLAG_DENIED(TextFormat.RED+"[WG]%s is denied in this region")
    ;

    private final String text;

    Messages(String msg){
        text = msg +TextFormat.RESET;
    }

    @Override
    public String toString() {
        return text;
    }

    public void send(CommandSender to, Object... args){
        to.sendMessage(String.format(this.toString(), args));
    }
}
