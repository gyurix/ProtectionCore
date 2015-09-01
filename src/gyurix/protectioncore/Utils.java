package gyurix.protectioncore;

import PluginReference.MC_EntityType;
import PluginReference.MC_GameMode;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_Server;
import ProtectionCore.MyPlugin;
import gyurix.chatapi.ChatAPI;
import gyurix.konfigfajl.ConfigFile;
import gyurix.konfigfajl.KFA;
import gyurix.konfigfajl.LangFile;
import gyurix.konfigfajl.MojangAPI;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import joebkt.WorldServer;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class Utils
{
  public static MC_EntityType[] hostile = { MC_EntityType.BLAZE, MC_EntityType.CREEPER, MC_EntityType.ENDERMAN, MC_EntityType.ENDERMITE, 
    MC_EntityType.GHAST, MC_EntityType.GUARDIAN, MC_EntityType.LAVA_SLIME, MC_EntityType.PIG_ZOMBIE, 
    MC_EntityType.SILVERFISH, MC_EntityType.SKELETON, MC_EntityType.SLIME, MC_EntityType.SPIDER, MC_EntityType.WITCH, MC_EntityType.ZOMBIE };

  public static MC_EntityType[] passive = { MC_EntityType.BAT, MC_EntityType.CHICKEN, MC_EntityType.COW, MC_EntityType.HORSE, 
    MC_EntityType.MUSHROOM_COW, MC_EntityType.OCELOT, MC_EntityType.PIG, MC_EntityType.RABBIT, MC_EntityType.SHEEP, 
    MC_EntityType.SQUID, MC_EntityType.VILLAGER, MC_EntityType.WOLF };

  public static MC_EntityType[] boss = { MC_EntityType.WITHERBOSS, MC_EntityType.ENDERDRAGON, MC_EntityType.GIANT };
  public static MC_EntityType[] hanging = { MC_EntityType.ARMOR_STAND, MC_EntityType.ENDER_CRYSTAL, MC_EntityType.HANGING };
  public static MC_EntityType[] vehicle = { MC_EntityType.BOAT, MC_EntityType.MINECART };
  public static MC_EntityType[] utility = { MC_EntityType.SNOWMAN, MC_EntityType.VILLAGER_GOLEM };
  public static MC_EntityType[] throwable = { MC_EntityType.ARROW, MC_EntityType.EYE_OF_ENDER_SIGNAL, MC_EntityType.SNOWBALL, 
    MC_EntityType.FIREBALL, MC_EntityType.THROWN_EGG, MC_EntityType.THROWN_ENDERPEARL, 
    MC_EntityType.THROWN_EXP_BOTTLE, MC_EntityType.THROWN_POTION, MC_EntityType.SMALL_FIREBALL };

  public static MC_ItemStack wandtool = KFA.srv.createItemStack(271, 1, 0);
  public static MC_ItemStack infotool = KFA.srv.createItemStack(268, 1, 0);
  public static Map<String, MC_Location> sel1 = new HashMap();
  public static Map<String, MC_Location> sel2 = new HashMap();
  public static Map<String, MC_Location> plc = new HashMap();
  public static ConfigFile kf;
  public static ConfigFile rf;
  public static ConfigFile tf;
  public static int[] limits;
  public static String dir;
  public static boolean opperms;
  public static boolean dfcheck;
  public static boolean verchange;
  public static boolean silktouch;

  public static boolean reload()
  {
    ProtectionCore.loaded = false;
    sel1.clear();
    sel2.clear();
    Region.regions.clear();
    Region.regnames.clear();
    Region.templatenames.clear();
    Region.templates.clear();
    boolean l = load();
    ProtectionCore.loaded = true;
    return l;
  }
  public static void loadConfig() {
    dir = KFA.fileCopy(MyPlugin.pl, "config.yml", false);
    kf = new ConfigFile(dir + File.separator + "config.yml");
    long oldver = kf.getLong("version", 0L);
    if (oldver < 7L) {
      dir = KFA.fileCopy(MyPlugin.pl, "config.yml", true);
      kf = new ConfigFile(dir + File.separator + "config.yml");
      verchange = true;
    }
    else if (oldver == 7L) {
      kf.set("forcelangcopy", "+");
      kf.set("version", "8");
      verchange = false;
    }
    if (kf.getBoolean("forcelangcopy")) {
      KFA.lf.insert(new LangFile(KFA.getFileStream(MyPlugin.pl, "lang.lng")));
      kf.set("forcelangcopy", "-");
    }
    kf.repair_missing = true;
    opperms = kf.getBoolean("permissions.op", true);
    dfcheck = kf.getBoolean("permissions.disableflagcheck", true);
    silktouch = kf.getBoolean("creatorblock.silktouch", true);
    ProtectionCore.movecheckrate = (int)kf.getLong("movecheckrate", 10L);
    try {
      String[] tool = (kf.get("tools.wand", "271") + ":0").split("\\:");
      wandtool = KFA.srv.createItemStack(Integer.valueOf(tool[0]).intValue(), 1, Integer.valueOf(tool[1]).intValue());
      tool = (kf.get("tools.info", "268") + ":0").split("\\:");
      infotool = KFA.srv.createItemStack(Integer.valueOf(tool[0]).intValue(), 1, Integer.valueOf(tool[1]).intValue());
    }
    catch (Throwable e) {
      System.out.println("[ProtectionCore] Error in configuration, the tools aren't defined properly");
      e.printStackTrace();
    }
    limits = new int[0];
    for (String s : kf.get("regionlimits").split("\\ ")) {
      try {
        limits = ArrayUtils.add(limits, Integer.valueOf(s).intValue());
      }
      catch (Throwable e) {
        System.out.println("[ProtectionCore] Error the limit \"" + s + "\" is not number!");
      }
    }
    kf.save();
  }
  public static boolean loadRegion(ConfigFile rf, Region r, String adr) {
    try {
      r.priority = ((int)rf.getLong(adr + "priority", 0L));
      r.perm = rf.get(adr + "permission", "");
      r.blockPlace = rf.getLocation(adr + "blockPlace");
      r.creator = rf.get(adr + "creator", "");
      if ((!r.creator.isEmpty()) && (r.creator.length() < 17)) {
        String creatorid = MojangAPI.getUUID(r.creator);
        if (creatorid != null)
          r.creator = creatorid;
      }
      for (String group : rf.get(adr + "groups").split("\\ "))
        r.groups.add(group);
      for (int i = 1; i < r.groups.size(); i++) {
        String[] pll = rf.get(adr + "players." + (String)r.groups.get(i), "").split("\\ ");
        if ((pll.length == 1) && (pll[0].isEmpty())) {
          r.players.add(new HashSet());
        } else {
          HashSet pls = new HashSet();
          for (String p : pll) {
            if ((p.length() > 16) || (p.equals("<creator>"))) {
              pls.add(p);
            } else {
              String uuid = MojangAPI.getUUID(p);
              pls.add(uuid == null ? p : uuid);
            }
          }
          r.players.add(pls);
        }
      }
      for (int i = 0; i < r.groups.size(); i++) {
        Object flags = new HashMap();
        String group = (String)r.groups.get(i);
        for (String flag : rf.mainAdressList(adr + "flags." + group)) {
          try {
            ((Map)flags).put(ProtectionCore.FlagType.valueOf(flag), rf.get(adr + "flags." + group + "." + flag));
          }
          catch (Throwable e) {
            System.out.println("[ProtectionCore] Error on loading " + adr + " regions " + flag + " flag.");
          }
        }
        r.flags.add(flags);
      }
    }
    catch (Throwable e) {
      System.out.println("[ProtectionCore] Error on loading region " + adr);
      e.printStackTrace();
      return false;
    }
    return true;
  }
  public static boolean loadRegions() {
    boolean loaded = true;
    rf = new ConfigFile(dir + File.separator + "regions.yml");
    for (WorldServer s : MinecraftServer.getServer().worldServers) {
      rf.set(s.dimensionSetAtCreate, "\r");
    }
    rf.save();
    for (String world : rf.mainAdressList()) {
      System.out.println("Loading dimension " + world);
      Region.regions.put(Integer.valueOf(world), new ArrayList());
      Region.regnames.put(Integer.valueOf(world), new ArrayList());
      for (??? = rf.mainAdressList(world).iterator(); ((Iterator)???).hasNext(); ) { String regname = (String)((Iterator)???).next();
        String adr = world + "." + regname + ".";
        Region r = new Region(new Area(rf.get(adr + "area"), Integer.valueOf(world).intValue()), regname, 1, null);
        loaded = (loadRegion(rf, r, adr)) && (loaded);
      }
      if (!((List)Region.regnames.get(Integer.valueOf(world))).contains("{global}")) {
        System.out.println("Global region creating...");
        int min = -2147483648;
        int max = 2147483647;
        new Region(new Area(min, min, min, max, max, max, Integer.valueOf(world).intValue()), "{global}", 0, Region.getTemplate("{global}"));
      }
    }
    return true;
  }
  public static boolean loadTemplates() {
    boolean loaded = true;
    KFA.fileCopy(MyPlugin.pl, "templates.yml", verchange);
    tf = new ConfigFile(dir + File.separator + "templates.yml");
    for (String regname : tf.mainAdressList()) {
      loaded = (loadRegion(tf, new Region(new Area(tf.get(regname + ".area", ""), 0), regname, 2, null), regname + ".")) && (loaded);
    }
    return loaded;
  }
  public static boolean load() {
    boolean loaded = true;
    try {
      loadConfig();
      loaded = (loadTemplates()) && (loaded);
      loaded = (loadRegions()) && (loaded);
    }
    catch (Throwable e) {
      System.out.println("[ProtectionCore] Error on loading plugin!");
      e.printStackTrace();
      return false;
    }
    return loaded;
  }
  public static boolean saveRegions() {
    rf.clear();
    Iterator localIterator2;
    for (Iterator localIterator1 = Region.regions.entrySet().iterator(); localIterator1.hasNext(); 
      localIterator2.hasNext())
    {
      Map.Entry e = (Map.Entry)localIterator1.next();
      localIterator2 = ((List)e.getValue()).iterator(); continue; Region r = (Region)localIterator2.next();
      String adr = e.getKey() + "." + r.name + ".";
      rf.set(adr + "area", r.area.toString());
      rf.set(adr + "priority", Integer.valueOf(r.priority));
      if (r.blockPlace != null)
        rf.setlocation(adr + "blockPlace", r.blockPlace);
      rf.set(adr + "creator", r.creator);
      rf.set(adr + "groups", StringUtils.join(r.groups, " "));
      for (int id = 1; id < r.groups.size(); id++)
        rf.set(adr + "players." + (String)r.groups.get(id), StringUtils.join((Iterable)r.players.get(id - 1), " "));
      for (int id = 0; id < r.groups.size(); id++) {
        List keys = new ArrayList(((Map)r.flags.get(id)).keySet());
        List values = new ArrayList(((Map)r.flags.get(id)).values());
        for (int id2 = 0; id2 < keys.size(); id2++) {
          rf.set(adr + "flags." + (String)r.groups.get(id) + "." + ((ProtectionCore.FlagType)keys.get(id2)).name(), values.get(id2));
        }
      }
    }

    return rf.save();
  }
  public static boolean hasPerm(MC_Player plr, String perm) {
    return (plr == null) || ((opperms) && (plr.isOp())) || (plr.hasPermission("protectioncore." + perm));
  }
  public static boolean hasPermFlag(MC_Player plr, Region r, String[] par) {
    if (!flagCheck(r.getFlag(plr, ProtectionCore.FlagType.ManageRegion), par)) {
      return (dfcheck) && (hasPerm(plr, "disableflagcheck.manageregion"));
    }
    return true;
  }
  public static boolean hasPermCommand(MC_Player plr, Region r, String cmd) {
    if (!hasPerm(plr, "command." + cmd)) {
      return false;
    }
    return hasPermFlag(plr, r, new String[] { cmd });
  }
  public static Region handleCommand(MC_Player plr, String[] args, String cmd) {
    if (args.length == 1) {
      ChatAPI.msg(plr, "protectioncore.region.noargument", new String[0]);
      return null;
    }

    String[] rname = args[1].split("\\|", 2);
    Region r;
    if (rname.length == 1) {
      if (plr == null) {
        ChatAPI.msg(plr, "protectioncore.region.nodimension", new String[0]);
        return null;
      }
      r = Region.get(plr.getLocation().dimension, rname[0]);
    }
    else {
      try {
        r = Region.get(Integer.valueOf(rname[0]).intValue(), rname[1]);
      }
      catch (Throwable e)
      {
        Region r;
        ChatAPI.msg(plr, "protectioncore.region.notnumber", new String[] { "<dimension>", rname[0] });
        return null;
      }
    }
    Region r;
    if (r == null) {
      ChatAPI.msg(plr, "protectioncore.region.notexists", new String[] { "<region>", args[1] });
      return null;
    }
    boolean hasperm = hasPermCommand(plr, r, cmd);
    if (!hasperm) {
      plr.sendMessage(KFA.l(plr, "protectioncore.noperm." + cmd));
      return null;
    }
    return r;
  }
  public static String getEntityType(MC_EntityType base) {
    if (KFA.search(passive, base) != -1)
      return "passive";
    if (KFA.search(hostile, base) != -1)
      return "hostile";
    if (KFA.search(vehicle, base) != -1)
      return "vehicle";
    if (KFA.search(throwable, base) != -1)
      return "throwable";
    if (KFA.search(hanging, base) != -1)
      return "hanging";
    if (KFA.search(utility, base) != -1)
      return "utility";
    if (KFA.search(boss, base) != -1)
      return "boss";
    return "other";
  }
  public static boolean flagCheck(List<String> flags, String[] par) {
    for (String flag : flags) {
      boolean trf = Flags.isTrue(flag);
      String[] pm = Flags.getParameters(flag);
      int c = 0;
      for (int i = 0; i < par.length; i++) {
        int id = KFA.search(pm, par[i]);
        if (trf == (id == -1)) {
          c++;
        }
      }
      if (((trf) && (c == par.length)) || ((!trf) && (c == 1))) {
        return true;
      }
    }
    return false;
  }
  public static boolean eventCancelTest(MC_Player plr, MC_Location loc, ProtectionCore.FlagType type, boolean disableFlagChecks, String[] par) {
    if (!ProtectionCore.loaded)
      return false;
    if ((plr != null) && (dfcheck) && (disableFlagChecks) && (hasPerm(plr, "disableflagcheck." + type.name().toLowerCase()))) {
      return false;
    }
    return !flagCheck(Region.getFlag(Region.get(loc), type, plr), par);
  }
  public static String[] itemTests(MC_ItemStack is) {
    if (is == null) {
      return new String[] { "nothing" };
    }
    return new String[] { is.getCount() + "x" + is.getId() + ":" + is.getDamage(), is.getCount() + "x" + is.getId(), 
      is.getId() + ":" + is.getDamage(), is.getId(), is.getCount() + "x" };
  }

  public static String[] split(String s) {
    String[] out = s.split("\\ ");
    for (int i = 1; i < out.length; i++) {
      out[i] = (out[(i - 1)] + " " + out[i]);
    }
    return out;
  }
  public static String[] addtoAll(String[] t, String s) {
    int tl = t.length;
    String[] t2 = (String[])t.clone();
    for (int i = 0; i < tl; i++)
    {
      int tmp20_18 = i;
      String[] tmp20_17 = t2; tmp20_17[tmp20_18] = (tmp20_17[tmp20_18] + "-" + s);
    }
    return t2;
  }
  public static void flagtests(MC_Player plr, List<Region> r) {
    List flags = Region.getFlag(r, ProtectionCore.FlagType.Fly, plr);
    if (flags.contains("+"))
      plr.setAllowFlight(true);
    else if ((flags.contains("-")) && 
      (plr.getGameMode() != MC_GameMode.CREATIVE) && (plr.getGameMode() != MC_GameMode.SPECTATOR)) {
      plr.setAllowFlight(false);
    }
    flags = Region.getFlag(r, ProtectionCore.FlagType.GameMode, plr);
    if (flags.contains("1"))
      plr.setGameMode(MC_GameMode.CREATIVE);
    else if (flags.contains("3")) {
      plr.setGameMode(MC_GameMode.SPECTATOR);
    }
    else if (flags.contains("0")) {
      plr.setGameMode(MC_GameMode.SURVIVAL);
    }
    else if (flags.contains("2")) {
      plr.setGameMode(MC_GameMode.ADVENTURE);
    }
    flags = Region.getFlag(r, ProtectionCore.FlagType.PotionEffects, plr);
    String par;
    for (String flag : flags) {
      if (flag.startsWith("+")) {
        for (par : Flags.getParameters(flag)) {
          KFA.srv.executeCommand("effect " + plr.getName() + " " + par.replace("|", " "));
        }
      }
    }
    flags = Region.getFlag(r, ProtectionCore.FlagType.Permissions, plr);
    String pln = plr.getName();
    for (String flag : flags)
      if (flag.startsWith("+")) {
        for (String par : Flags.getParameters(flag)) {
          KFA.srv.givePermission(pln, par);
        }
      }
      else
        for (String par : Flags.getParameters(flag))
          KFA.srv.takePermission(pln, par);
  }

  public static boolean regionChangeTest(MC_Player plr, MC_Location loc1, MC_Location loc2, boolean noentryleavecheck)
  {
    List r1 = Region.get(loc1);
    List r2 = Region.get(loc2);
    List leaved = Region.leavedRegions(r1, r2);
    List entered = Region.enteredRegions(r1, r2);
    if (!noentryleavecheck) {
      if (!hasPerm(plr, "disableflagcheck.leave")) {
        for (Region r : leaved) {
          List flags = r.getFlag(plr, ProtectionCore.FlagType.Leave);
          if ((flags.contains("-")) && (!flags.contains("+"))) {
            return true;
          }
        }
      }
      if (!hasPerm(plr, "disableflagcheck.entry")) {
        for (Region r : entered) {
          List flags = r.getFlag(plr, ProtectionCore.FlagType.Entry);
          if ((flags.contains("-")) && (!flags.contains("+"))) {
            return true;
          }
        }
      }
    }
    if (entered.size() + leaved.size() > 1)
      flagtests(plr, r2);
    plr.sendMessage(StringUtils.join(Region.getFlag(leaved, ProtectionCore.FlagType.Farewell, plr), "\n"));
    plr.sendMessage(StringUtils.join(Region.getFlag(entered, ProtectionCore.FlagType.Greeting, plr), "\n"));
    return false;
  }
  public static boolean isCommand(MC_Player plr, String str, String command) {
    return KFA.search(KFA.l(plr, "protectioncore.command." + command).split("\\ "), str) != -1;
  }
  public static Area getSelectedArea(MC_Player plr) {
    MC_Location loc1 = (MC_Location)sel1.get(plr.getName());
    MC_Location loc2 = (MC_Location)sel2.get(plr.getName());
    if ((loc1 == null) && (loc2 == null)) {
      plr.sendMessage(KFA.l(plr, "protectioncore.selection.noboth"));
      return null;
    }
    if (loc1 == null) {
      plr.sendMessage(KFA.l(plr, "protectioncore.selection.nofirst"));
      return null;
    }
    if (loc2 == null) {
      plr.sendMessage(KFA.l(plr, "protectioncore.selection.nosecond"));
      return null;
    }
    return new Area((MC_Location)sel1.get(plr.getName()), (MC_Location)sel2.get(plr.getName()));
  }
  public static boolean handleArea(Area a, MC_Player plr) {
    if (a == null)
      return false;
    Region r = Region.getTemplate(plr);
    if (r == null)
      return false;
    long x = a.maxx - a.minx + 1L;
    long y = a.maxy - a.miny + 1L;
    long z = a.maxz - a.minz + 1L;
    if ((x < r.area.minx) || (y < r.area.miny) || (z < r.area.minz)) {
      plr.sendMessage(KFA.l(plr, "protectioncore.area.smaller")
        .replace("<x>", x)
        .replace("<y>", y)
        .replace("<z>", z)
        .replace("<minx>", r.area.minx)
        .replace("<miny>", r.area.miny)
        .replace("<minz>", r.area.minz));
      return false;
    }
    if ((r.area.maxx < x) || (r.area.maxy < y) || (r.area.maxz < z)) {
      plr.sendMessage(KFA.l(plr, "protectioncore.area.bigger")
        .replace("<x>", x)
        .replace("<y>", y)
        .replace("<z>", z)
        .replace("<maxx>", r.area.maxx)
        .replace("<maxy>", r.area.maxy)
        .replace("<maxz>", r.area.maxz));
      return false;
    }
    if (!hasPerm(plr, "allowsamepriority")) {
      for (Region r2 : (List)Region.regions.get(Integer.valueOf(a.worldid))) {
        if ((r2.priority == r.priority) && (r2.area.contains(a))) {
          plr.sendMessage(KFA.l(plr, "protectioncore.area.contains"));
          return false;
        }
      }
    }
    return true;
  }
  public static MC_Location getRelativeBlockPlaceLocation(List<String> fl, String bl) {
    bl = bl + "-";
    int j;
    int i;
    for (Iterator localIterator = fl.iterator(); localIterator.hasNext(); 
      i < j)
    {
      String flag = (String)localIterator.next();
      String[] arrayOfString1;
      j = (arrayOfString1 = Flags.getParameters(flag)).length; i = 0; continue; String par = arrayOfString1[i];
      if (par.startsWith(bl)) {
        String[] coords = par.substring(bl.length()).split("\\-");
        return new MC_Location(Double.valueOf(coords[0]).doubleValue(), Double.valueOf(coords[1]).doubleValue(), Double.valueOf(coords[2]).doubleValue(), 0);
      }
      i++;
    }

    return null;
  }
  public static int nextRegionID(MC_Player plr) {
    int c = 1;
    String pln = plr.getName();
    for (Region r : (List)Region.regions.get(Integer.valueOf(plr.getLocation().dimension)))
      if (r.creator.equals(pln))
        c++;
    return c;
  }
  public static String nextRegionName(MC_Player plr) {
    String pln = plr.getName();
    List rn = (List)Region.regnames.get(Integer.valueOf(plr.getLocation().dimension));

    for (int i = 1; rn.contains(pln + "(" + i + ")"); i++);
    return pln + "(" + i + ")";
  }
  public static int canCreateNewRegion(MC_Player plr) {
    if (hasPerm(plr, "limit.-1")) {
      return -1;
    }
    int c = nextRegionID(plr);
    for (int limit : limits) {
      if (limit < c) {
        return c - 1;
      }
      if (hasPerm(plr, "limit." + limit))
        return -1;
    }
    return c - 1;
  }
  public static ArrayList<String> getPlayerNames(HashSet<String> names) {
    ArrayList out = new ArrayList();
    for (String o : names) {
      List nl = MojangAPI.getPlayerNames(o);
      if (nl == null) {
        out.add(o);
      }
      else {
        out.addAll(nl);
      }
    }
    return out;
  }
  public static ProtectionCore.CommandType getCommand(MC_Player plr, String str) {
    for (ProtectionCore.CommandType cmd : ProtectionCore.CommandType.values()) {
      if (KFA.search(KFA.l(plr, "protectioncore.command." + cmd.name()).split("\\ "), str) != -1) {
        return cmd;
      }
    }
    return null;
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.Utils
 * JD-Core Version:    0.6.2
 */