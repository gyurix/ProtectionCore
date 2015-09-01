package gyurix.protectioncore;

import PluginReference.MC_Location;
import PluginReference.MC_Player;
import gyurix.konfigfajl.KFA;
import gyurix.konfigfajl.MojangAPI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

public class Region
{
  public static final HashMap<Integer, List<Region>> regions = new HashMap();
  public static final HashMap<Integer, List<String>> regnames = new HashMap();
  public static final List<Region> templates = new ArrayList();
  public static final List<String> templatenames = new ArrayList();
  public MC_Location blockPlace;
  public int priority;
  public String name;
  public String creator = "";
  public String perm;
  public Area area;
  public ArrayList<String> groups = new ArrayList();

  public ArrayList<HashSet<String>> players = new ArrayList();

  public ArrayList<Map<ProtectionCore.FlagType, String>> flags = new ArrayList();
  public Region template;

  public Region(Area a, String n, int mode, Region r)
  {
    this.area = a;
    this.name = n;
    if (mode == 0) {
      if (r != null) {
        this.priority = r.priority;
        this.groups = new ArrayList(r.groups);
        this.players = new ArrayList();
        for (HashSet plrs : r.players) {
          this.players.add(new HashSet(plrs));
        }
        this.flags = new ArrayList();
        for (Map f : r.flags) {
          if (f.isEmpty()) {
            this.flags.add(new EnumMap(ProtectionCore.FlagType.class));
          }
          else {
            this.flags.add(new EnumMap(f));
          }
        }
        this.template = r;
      }
      else {
        this.priority = 0;
        this.groups.add("strange");
        this.groups.add("owner");
        this.groups.add("member");
        this.players.add(new HashSet());
        this.players.add(new HashSet());
        this.flags.add(new EnumMap(ProtectionCore.FlagType.class));
        this.flags.add(new EnumMap(ProtectionCore.FlagType.class));
        this.flags.add(new EnumMap(ProtectionCore.FlagType.class));
      }
    }
    if ((mode == 0) || (mode == 1)) {
      List rl = (List)regions.get(Integer.valueOf(a.worldid));
      rl.add(this);
      regions.put(Integer.valueOf(a.worldid), rl);
      List rn = (List)regnames.get(Integer.valueOf(a.worldid));
      rn.add(n);
      regnames.put(Integer.valueOf(a.worldid), rn);
    }
    else {
      templates.add(this);
      templatenames.add(n);
    }
  }

  public void remove() { int dim = this.area.worldid;
    this.area = null;
    this.creator = null;
    this.flags = null;
    this.groups = null;
    this.players = null;
    this.blockPlace = null;
    ((List)regnames.get(Integer.valueOf(dim))).remove(this.name);
    this.name = null;
    this.perm = null;
    ((List)regions.get(Integer.valueOf(dim))).remove(this); }

  public static List<Region> sort(List<Region> regl) {
    int max = regl.size();
    for (int i = 0; i < max - 1; i++)
      for (int j = 1; j < max; j++) {
        Region r1 = (Region)regl.get(i);
        Region r2 = (Region)regl.get(j);
        if (r1.priority < r2.priority) {
          regl.set(i, r2);
          regl.set(j, r1);
        }
      }
    return regl;
  }
  public static Region get(int dim, String name) {
    try {
      return (Region)((List)regions.get(Integer.valueOf(dim))).get(((List)regnames.get(Integer.valueOf(dim))).indexOf(name));
    } catch (Throwable e) {
    }
    return null;
  }

  public static List<Region> get(MC_Location loc) {
    List regl = (List)regions.get(Integer.valueOf(loc.dimension));
    List found = new ArrayList();
    int maxid = regl.size();
    for (int i = 0; i < maxid; i++) {
      Region r = (Region)regl.get(i);
      if (r.area.contains(loc)) {
        found.add(r);
      }
    }
    found = sort(found);
    return found;
  }
  public static Region getTemplate(MC_Player plr) {
    for (Region r : templates) {
      if (plr.hasPermission(r.perm))
        return r;
    }
    return null;
  }
  public static Region getTemplate(String name) {
    int id = templatenames.indexOf(name);
    if (id == -1)
      return null;
    return (Region)templates.get(id);
  }
  public static List<Region> leavedRegions(List<Region> reg1, List<Region> reg2) {
    List leaved = new ArrayList();
    for (int i = 0; i < reg1.size(); i++) {
      Region r = (Region)reg1.get(i);
      if (!reg2.contains(r)) {
        leaved.add(r);
      }
    }
    return leaved;
  }
  public static List<Region> enteredRegions(List<Region> reg1, List<Region> reg2) {
    List entered = new ArrayList();
    for (int i = 0; i < reg2.size(); i++) {
      Region r = (Region)reg2.get(i);
      if (!reg1.contains(r)) {
        entered.add(r);
      }
    }
    return entered;
  }
  public static List<String> getFlag(List<Region> regl, ProtectionCore.FlagType type, MC_Player plr) {
    if (plr == null) {
      return getFlag(regl, type);
    }
    for (Region r : regl) {
      List flags = r.getFlag(plr, type);
      if (flags.size() > 0)
        return flags;
    }
    return new ArrayList();
  }
  public static List<String> getFlag(List<Region> regl, ProtectionCore.FlagType type) {
    for (Region r : regl) {
      List flags = r.getFlag(type);
      if (flags.size() > 0)
        return flags;
    }
    return null;
  }
  public byte addPlayer(String group, String plr) {
    int gid = this.groups.indexOf(group) - 1;
    if (gid == -2)
      return 1;
    if (gid == -1)
      return 3;
    HashSet pls = (HashSet)this.players.get(gid);
    if (pls.contains(plr)) {
      return 2;
    }
    pls.add(plr);
    return 0;
  }
  public byte removePlayer(String group, String plr) {
    int gid = this.groups.indexOf(group) - 1;
    if (gid == -2)
      return 1;
    if (gid == -1)
      return 3;
    if (((HashSet)this.players.get(gid)).remove(plr)) {
      return 0;
    }
    return 2;
  }
  public byte groupManage(String group, String prevGroup) {
    int gid = this.groups.indexOf(group);
    if (gid == -1) {
      this.groups.add(group);
      this.players.add(new HashSet());
      EnumMap map = new EnumMap(ProtectionCore.FlagType.class);
      int prevgid = prevGroup == null ? -1 : this.groups.indexOf(prevGroup);
      if (prevgid != -1)
        map.putAll((Map)this.flags.get(prevgid));
      this.flags.add(map);
      if (prevGroup == null)
        return 0;
      return (byte)(prevgid == -1 ? 3 : 4);
    }
    if (gid < 3)
      return 2;
    this.groups.remove(gid);
    this.players.remove(gid - 1);
    this.flags.remove(gid);
    return 1;
  }
  public List<String> getFlag(MC_Player plr, ProtectionCore.FlagType type) {
    int fs = this.flags.size();
    String pln = plr.getUUID().toString().replace("-", "");
    List found = new ArrayList();
    boolean foundplayer = false;
    for (int i = 1; i < fs; i++) {
      if (((HashSet)this.players.get(i - 1)).contains(pln)) {
        foundplayer = true;
        String flag = (String)((Map)this.flags.get(i)).get(type);
        if (flag != null) {
          found.add(flag);
        }
      }
    }
    if (!foundplayer) {
      String flag = (String)((Map)this.flags.get(0)).get(type);
      if (flag != null)
        found.add(flag);
    }
    return found;
  }
  public List<String> getFlag(ProtectionCore.FlagType type) {
    int fs = this.flags.size();
    List found = new ArrayList();
    for (int i = 0; i < fs; i++) {
      String flag = (String)((Map)this.flags.get(i)).get(type);
      if (flag != null) {
        found.add(flag);
      }
    }
    return found;
  }
  public String getFlagList(int grid, String format, String sep) {
    String out = "";
    Object[] t = ((Map)this.flags.get(grid)).keySet().toArray();
    Arrays.sort(t);
    for (Object fno : t) {
      String fn = ((ProtectionCore.FlagType)fno).name();
      String value = (String)((Map)this.flags.get(grid)).get(fno);
      out = out + sep + format.replace("<flagname>", fn)
        .replace("<value>", value)
        .replace("<coloredflag>", new StringBuilder(String.valueOf(Flags.getColor(value))).append(fn).toString());
    }
    if (out.length() == 0) {
      return "";
    }
    return out.substring(sep.length());
  }
  public String getColoredFlags(int grid) {
    String out = "";
    Object[] t = ((Map)this.flags.get(grid)).keySet().toArray();
    Arrays.sort(t);
    for (Object fno : t) {
      String fn = ((ProtectionCore.FlagType)fno).name();
      out = out + ", " + Flags.getColor((String)((Map)this.flags.get(grid)).get(fno)) + fn;
    }
    if (out.length() == 0) {
      return "";
    }
    return out.substring(2);
  }
  public String getGroups(String format, String sep) {
    if (format == null) {
      return "";
    }
    String out = format.replace("<name>", (CharSequence)this.groups.get(0))
      .replace("<players>", "")
      .replace("<coloredflag>", getColoredFlags(0));
    for (int i = 1; i < this.groups.size(); i++) {
      out = out + sep + format.replace("<name>", (CharSequence)this.groups.get(i))
        .replace("<players>", StringUtils.join(Utils.getPlayerNames((HashSet)this.players.get(i - 1)), ", "))
        .replace("<coloredflag>", getColoredFlags(i));
    }
    return out;
  }
  public String getInfo(String format, String grformat, String sep) {
    return getInfo(format, grformat, sep, 0);
  }
  public String getInfo(String format, String grformat, String sep, int grid) {
    String cr = StringUtils.join(MojangAPI.getPlayerNames(this.creator), ", ");
    format = format.replace("<name>", this.name)
      .replace("<priority>", this.priority)
      .replace("<minx>", this.area.minx)
      .replace("<miny>", this.area.miny)
      .replace("<minz>", this.area.minz)
      .replace("<maxx>", this.area.maxx)
      .replace("<maxy>", this.area.maxy)
      .replace("<maxz>", this.area.maxz)
      .replace("<creator>", cr == null ? this.creator : cr)
      .replace("<block>", this.blockPlace == null ? KFA.l(null, "protectioncore.info.nocreationblock") : 
      KFA.l(null, "protectioncore.info.creationblock")
      .replace("<x>", this.blockPlace.getBlockX())
      .replace("<y>", this.blockPlace.getBlockY())
      .replace("<z>", this.blockPlace.getBlockZ()))
      .replace("<group>", (String)this.groups.get(grid));
    if (format.contains("<groups>")) {
      return format.replace("<groups>", getGroups(grformat, sep));
    }
    if (format.contains("<flaglist>")) {
      return format.replace("<flaglist>", getFlagList(grid, grformat, sep));
    }
    return format;
  }

  public String toString() {
    return getInfo("§c{<priority>}§b<name>§e[§f<minx>§e,§f<miny>§e,§f<minz>§e] - [§f<maxx>§e,§f<maxy>§e,§f<maxz>§e] §fregion", "", "");
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.Region
 * JD-Core Version:    0.6.2
 */