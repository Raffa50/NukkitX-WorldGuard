package aldrigos.mc.worldguard;

import aldrigos.mc.worldedit.*;
import aldrigos.mc.worldguard.commands.*;
import aldrigos.mc.worldguard.listeners.*;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Logger;
import com.google.gson.Gson;

import java.io.*;

public class WorldGuardPlugin extends PluginBase {
    private static final String regionFile = "plugins/worldguard/regions.json";
    public WorldEdit worldEdit;
    public RegionManager RegionManager;
    private Logger log;

    private boolean load(){
        var file = new File(regionFile);
        if(file.exists()) {
            var json = new Gson();
            try(var reader = new BufferedReader(new FileReader(file))){
                RegionManager = json.fromJson(reader, RegionManager.class);
            } catch (Exception e) {
                log.error("[WG]Exception: "+Utils.getTrace(e));
                return false;
            }
        }else
            RegionManager = new RegionManager(this);

        return true;
    }

    private void save(){
        var dir = new File("plugins/worldguard");
        dir.mkdir();

        var json = new Gson();
        try(var writer = new PrintWriter(regionFile, "UTF-8")){
            writer.print(json.toJson(RegionManager));
        } catch (Exception e) {
            log.error("[WG]Exception: "+Utils.getTrace(e));
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

        worldEdit = ((WorldEditPlugin) getServer().getPluginManager().getPlugin("SWorldEdit")).getApi();

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
