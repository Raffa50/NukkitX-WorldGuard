package aldrigos.mc.worldguard;

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
    Explosions
}
