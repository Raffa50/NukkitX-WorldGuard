package aldrigos.mc.worldguard;

import aldrigos.mc.worldguard.commands.*;
import aldrigos.mc.worldguard.listeners.*;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Logger;
import com.google.gson.Gson;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WorldGuardPlugin extends PluginBase {
    private static final String regionFile = "regions.dat";
    public final Map<Long, Cuboid> Selection= new HashMap<>();
    public RegionManager RegionManager;
    private Logger log;

    private boolean load(){
        var file = new File(regionFile);
        if(file.exists()) {
            var json = new Gson();
            try(var reader = new BufferedReader(new FileReader(file))){
                RegionManager = json.fromJson(reader, RegionManager.class);
            } catch (Exception e) {
                log.error("[WG]Exception: "+e.getStackTrace());
                return false;
            }
        }else
            RegionManager = new RegionManager(this);

        return true;
    }

    private void save(){
        var file = new File(regionFile);
        try {
            file.createNewFile();
        } catch (IOException e) {
            log.error("[WG]Exception: "+e.getStackTrace());
        }

        var json = new Gson();
        try(var writer = new PrintWriter("the-file-name.txt", "UTF-8")){
            writer.println(json.toJson(RegionManager));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onEnable(){
        log = this.getServer().getLogger();
        //load regions
        if(!load()){
            this.setEnabled(false);
            return;
        }

        var pm = this.getServer().getPluginManager();
        pm.registerEvents(new InteractionListener(this), this);
        pm.registerEvents(new BlockListener(this), this);
        pm.registerEvents(new MobListener(this), this);

        this.getServer().getCommandMap().register("wg", new WgCommand(this));
        this.getServer().getCommandMap().register("rg", new RegionCommand(this));

        log.info("[WG]WorldGuard-port by Aldrigo R. ENABLED");
    }

    @Override
    public void onDisable(){
        save();
        log.info("[WG]disabled");
    }
}
