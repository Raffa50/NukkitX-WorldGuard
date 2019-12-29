package aldrigos.mc.worldguard;

import cn.nukkit.math.Vector3;

public class Vector3Adapter {
    public double x, y, z;

    public Vector3Adapter(){}

    public Vector3Adapter(Vector3 v){
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Vector3 toVector3(){
        return new Vector3(x,y,z);
    }
}
