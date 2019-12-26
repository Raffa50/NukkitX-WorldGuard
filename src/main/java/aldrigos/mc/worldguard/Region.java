package aldrigos.mc.worldguard;

import java.util.Collection;
import java.util.HashSet;

public class Region extends Cuboid {
    private String Name;
    public String Greeting;
    public Collection<FlagType> Deny = new HashSet<>();

    public Region(){}

    public Region(Cuboid c, String name){
        P1 = c.P1;
        P2 = c.P2;
        this.Name = name;
    }

    public String getName(){
        return Name;
    }

    public Iterable<FlagType> getForbids(){
        return Deny;
    }
}
