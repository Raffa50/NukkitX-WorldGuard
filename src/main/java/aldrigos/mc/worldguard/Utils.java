package aldrigos.mc.worldguard;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

public final class Utils {
    public static <T> Collection<T> flatten(Collection<? extends Collection<T>> c){
        var result = new ArrayList<T>();
        c.stream().forEach(result::addAll);
        return result;
    }

    public static String getTrace(Throwable e){
        var sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
