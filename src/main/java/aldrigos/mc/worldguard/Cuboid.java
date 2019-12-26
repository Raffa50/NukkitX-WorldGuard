package aldrigos.mc.worldguard;

import cn.nukkit.math.Vector3;

public class Cuboid {
    public Vector3 P1, P2;

    public boolean contains(Vector3 block){
        double x1, x2, y1, y2, z1, z2;
        x1 = Math.min(P1.x, P2.x);
        x2 = Math.max(P1.x, P2.x);

        if(block.x < x1 || block.x > x2)
            return false;

        y1 = Math.min(P1.y, P2.y);
        y2 = Math.max(P1.y, P2.y);

        if(block.y < y1 || block.y > y2)
            return false;

        z1 = Math.min(P1.z, P2.z);
        z2 = Math.max(P1.z, P2.z);

        if(block.z < z1 || block.z > z2)
            return false;

        return true;
    }
}
