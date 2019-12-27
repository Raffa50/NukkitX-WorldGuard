package aldrigos.mc.worldguard;

import java.util.Collection;
import java.util.HashSet;

public class Region extends Cuboid {
    private String Name;
    public String Greeting;
    private Collection<FlagType> Deny = new HashSet<>();

    public Region(){}

    public Region(Cuboid c, String name){
        P1 = c.P1;
        P2 = c.P2;
        this.Name = name;
    }

    public boolean isDenied(FlagType flag){
        return Deny.contains(flag);
    }

    public void setDenied(FlagType flag){
        Deny.add(flag);
    }

    public void setAllowed(FlagType flag){
        Deny.remove(flag);
    }

    public Iterable<FlagType> getDenied(){
        return Deny;
    }

    public String getName(){
        return Name;
    }

    public Iterable<FlagType> getForbids(){
        return Deny;
    }
}
