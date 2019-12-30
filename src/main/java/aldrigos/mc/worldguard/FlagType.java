package aldrigos.mc.worldguard;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public enum FlagType {
    @SerializedName("block-break")
    Block_break,
    @SerializedName("block-place")
    Block_place,
    @SerializedName("mob-spawning")
    Mob_spawning,
    @SerializedName("pvp")
    Pvp,
    @SerializedName("damage-animals")
    Damage_animals,
    @SerializedName("explosions")
    Explosions,
    @SerializedName("lighter")
    Lighter,
    @SerializedName("fire-spread")
    Fire_spread,
    @SerializedName("lava-fire")
    Lava_fire
    ;

    private static Gson json = new Gson();

    @Override
    public String toString(){
        return json.toJson(this);
    }
}
