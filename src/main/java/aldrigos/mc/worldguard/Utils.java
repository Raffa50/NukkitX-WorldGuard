package aldrigos.mc.worldguard;

import cn.nukkit.math.Vector3;

import java.util.ArrayList;
import java.util.Collection;

public final class Utils {
    public static <T> Collection<T> flatten(Collection<? extends Collection<T>> c){
        var result = new ArrayList<T>();
        c.stream().forEach(result::addAll);
        return result;
    }

    public static String toString(Vector3 p){
        return "X:" + p.x + " Y:" + p.y + " Z:" + p.z;
    }
}
