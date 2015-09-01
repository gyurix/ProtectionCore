package gyurix.protectioncore;

import PluginReference.MC_DirectionNESWUD;
import PluginReference.MC_Location;
import gyurix.konfigfajl.KFA;

public class Area
{
  public long minx;
  public long miny;
  public long minz;
  public long maxx;
  public long maxy;
  public long maxz;
  public int worldid;

  public Area()
  {
  }

  public Area(String str, int wid)
  {
    if ((str == null) || (str.isEmpty())) {
      return;
    }
    String[] a = str.split("\\ ");
    this.minx = Long.valueOf(a[0]).longValue();
    this.miny = Long.valueOf(a[1]).longValue();
    this.minz = Long.valueOf(a[2]).longValue();
    this.maxx = Long.valueOf(a[3]).longValue();
    this.maxy = Long.valueOf(a[4]).longValue();
    this.maxz = Long.valueOf(a[5]).longValue();
    this.worldid = wid;
  }
  public Area(MC_Location loc1, MC_Location loc2) {
    this.minx = ((int)Math.floor(loc1.x > loc2.x ? loc2.x : loc1.x));
    this.miny = ((int)Math.floor(loc1.y > loc2.y ? loc2.y : loc1.y));
    this.minz = ((int)Math.floor(loc1.z > loc2.z ? loc2.z : loc1.z));
    this.maxx = ((int)Math.floor(loc1.x <= loc2.x ? loc2.x : loc1.x));
    this.maxy = ((int)Math.floor(loc1.y <= loc2.y ? loc2.y : loc1.y));
    this.maxz = ((int)Math.floor(loc1.z <= loc2.z ? loc2.z : loc1.z));
    this.worldid = loc1.dimension;
  }

  public Area(long minx, long miny, long minz, long maxx, long maxy, long maxz, int worldid) {
    this.minx = minx;
    this.maxx = maxx;
    this.miny = miny;
    this.maxy = maxy;
    this.minz = minz;
    this.maxz = maxz;
    this.worldid = worldid;
    sort();
  }
  public void expand(MC_DirectionNESWUD dir, int num) {
    if (dir == MC_DirectionNESWUD.WEST) this.minx -= num;
    if (dir == MC_DirectionNESWUD.EAST) this.maxx += num;
    if (dir == MC_DirectionNESWUD.DOWN) this.miny -= num;
    if (dir == MC_DirectionNESWUD.UP) this.maxy += num;
    if (dir == MC_DirectionNESWUD.NORTH) this.minz -= num;
    if (dir == MC_DirectionNESWUD.SOUTH) this.maxz += num; 
  }

  public void decrease(MC_DirectionNESWUD dir, int num) { if (dir == MC_DirectionNESWUD.WEST) this.minx += num;
    if (dir == MC_DirectionNESWUD.EAST) this.maxx -= num;
    if (dir == MC_DirectionNESWUD.DOWN) this.miny += num;
    if (dir == MC_DirectionNESWUD.UP) this.maxy -= num;
    if (dir == MC_DirectionNESWUD.NORTH) this.minz += num;
    if (dir == MC_DirectionNESWUD.SOUTH) this.maxz -= num;  } 
  public void move(MC_DirectionNESWUD dir, int num)
  {
    expand(dir, num);
    decrease(KFA.negateDirection(dir), num);
  }
  public boolean isSorted() {
    return (this.minx <= this.maxx) && (this.miny <= this.maxy) && (this.minz <= this.maxz);
  }

  public void sort() {
    if (this.minx > this.maxx) {
      long temp = this.minx;
      this.minx = this.maxx;
      this.maxx = temp;
    }
    if (this.miny > this.maxy) {
      long temp = this.miny;
      this.miny = this.maxy;
      this.maxy = temp;
    }
    if (this.minz > this.maxz) {
      long temp = this.minz;
      this.minz = this.maxz;
      this.maxz = temp;
    }
  }

  public boolean contains(MC_Location loc) { return (loc.getBlockX() >= this.minx) && (loc.getBlockY() >= this.miny) && (loc.getBlockZ() >= this.minz) && 
      (loc.getBlockX() <= this.maxx) && (loc.getBlockY() <= this.maxy) && (loc.getBlockZ() <= this.maxz) && 
      (loc.dimension == this.worldid); }

  public boolean contains(Area a) {
    if (this.worldid != a.worldid)
      return false;
    boolean x = (a.maxx >= this.minx) && (a.minx <= this.maxx);
    boolean y = (a.maxy >= this.miny) && (a.miny <= this.maxy);
    boolean z = (a.maxz >= this.minz) && (a.minz <= this.maxz);
    return (x) && (y) && (z);
  }

  public boolean equals(Object obj) {
    if (obj.getClass() == Area.class) {
      Area a = (Area)obj;
      return (a.minx == this.minx) && (a.maxx == this.maxx) && (a.miny == this.miny) && (a.maxy == this.maxy) && (a.minz == this.minz) && (a.maxz == this.maxz) && (a.worldid == this.worldid);
    }
    return false;
  }

  public String toString() {
    return this.minx + " " + this.miny + " " + this.minz + " " + this.maxx + " " + this.maxy + " " + this.maxz;
  }
  public Area duplicate() {
    return new Area(this.minx, this.miny, this.minz, this.maxx, this.maxy, this.maxz, this.worldid);
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.Area
 * JD-Core Version:    0.6.2
 */