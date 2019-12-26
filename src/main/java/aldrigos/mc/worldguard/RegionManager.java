package aldrigos.mc.worldguard;

import aldrigos.mc.worldguard.exceptions.AlreadyExistException;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Logger;

import java.util.*;

public class RegionManager {
    private Map<Integer, Collection<Region>> Regions = new HashMap<>();
    private final Logger log;

    public RegionManager(WorldGuardPlugin p){
        log = p.getLogger();
    }

    public Collection<Region> getAllRegions(){
        return Utils.flatten(Regions.values());
    }

    public Region getByName(String name){
        for( var r: getAllRegions() ) {
            if(r.getName().equals(name))
                return r;
        }
        return null;
    }

    public Region getBlockRegion(int worldId, Vector3 pos){
        //log.info("[WG-Debug] Block pos: "+Utils.toString(pos));
        var rgs = Regions.get(worldId);
        for (var r: rgs) {
            if(r.contains(pos))
                return r;
        }
        return null;
    }

    public void add(int worldId, Region r)
            throws AlreadyExistException
    {
        if(getByName(r.getName()) != null)
            throw new AlreadyExistException("Region "+r.getName()+" already exist");

        if(!Regions.containsKey(worldId))
            Regions.put(worldId, new ArrayList<>());

        Regions.get(worldId).add(r);
    }
}
