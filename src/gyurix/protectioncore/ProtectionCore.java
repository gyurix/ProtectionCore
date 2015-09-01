package gyurix.protectioncore;

import PluginReference.MC_ArmorStandActionType;
import PluginReference.MC_Block;
import PluginReference.MC_DamageType;
import PluginReference.MC_DirectionNESWUD;
import PluginReference.MC_Entity;
import PluginReference.MC_EntityType;
import PluginReference.MC_EventInfo;
import PluginReference.MC_GameMode;
import PluginReference.MC_HangingEntityType;
import PluginReference.MC_ItemFrameActionType;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_MiscGriefType;
import PluginReference.MC_Player;
import PluginReference.MC_PotionEffectType;
import PluginReference.MC_Server;
import PluginReference.MC_Sign;
import PluginReference.MC_World;
import PluginReference.PluginBase;
import gyurix.chatapi.ChatAPI;
import gyurix.konfigfajl.KFA;
import gyurix.permissions.Perm;
import gyurix.protectioncore.commands.Default;
import gyurix.protectioncore.commands.Define;
import gyurix.protectioncore.commands.Info;
import gyurix.protectioncore.commands.Select;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.ArrayUtils;

public class ProtectionCore extends PluginBase
{
  public static boolean loaded = false;
  public static int movecheckrate = 10;

  public ProtectionCore() { Utils.load();
    KFA.srv.registerCommand(new Default());
    loaded = true; }

  public void onShutdown()
  {
    Utils.saveRegions();
  }

  public void onTick(int tickNumber) {
    if ((loaded) && (tickNumber % movecheckrate == 0))
      for (MC_Player plr : KFA.srv.getPlayers()) {
        MC_Location locFrom = (MC_Location)Utils.plc.get(plr.getName());
        MC_Location loc = plr.getLocation();
        boolean cancel = Utils.eventCancelTest(plr, locFrom, FlagType.Move, true, new String[] { "" });
        if (cancel)
          plr.sendMessage(KFA.l(plr, "protectioncore.warning.move"));
        else
          cancel = Utils.regionChangeTest(plr, locFrom, loc, false);
        if (cancel) {
          plr.teleport(locFrom);
        }
        else
          Utils.plc.put(plr.getName(), loc);
      }
  }

  public void onPlayerJoin(MC_Player plr)
  {
    Utils.plc.put(plr.getName(), plr.getLocation());
    Utils.flagtests(plr, Region.get(plr.getLocation()));
    if ((plr.getGameMode() == MC_GameMode.CREATIVE) || (plr.getGameMode() == MC_GameMode.SPECTATOR)) {
      plr.setAllowFlight(true);
      plr.setFlying(true);
    }
  }

  public void onPlayerLogout(String playerName, UUID uuid) {
    Utils.plc.remove(playerName);
    Utils.sel1.remove(playerName);
    Utils.sel2.remove(playerName);
  }

  public Boolean onRequestPermission(String playerKey, String permission) {
    if ((!playerKey.equals("*")) && (playerKey.length() <= 16)) {
      MC_Player plr = KFA.srv.getOnlinePlayerByName(playerKey);
      if (plr != null) {
        List flags = Region.getFlag(Region.get(plr.getLocation()), FlagType.Permissions, plr);
        int j;
        int i;
        for (Iterator localIterator = flags.iterator(); localIterator.hasNext(); 
          i < j)
        {
          String flag = (String)localIterator.next();
          String[] arrayOfString;
          j = (arrayOfString = flag.split("\\ ")).length; i = 0; continue; String par = arrayOfString[i];
          Boolean b = new Perm(par).matches(permission);
          if (b != null)
            return b;
          i++;
        }

      }

    }

    return null;
  }

  public void onAttemptAttackEntity(MC_Player plr, MC_Entity ent, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    if (ent.getType() == MC_EntityType.PLAYER) {
      ei.isCancelled = Utils.eventCancelTest(plr, ent.getLocation(), FlagType.PVP, false, new String[] { 
        ent.getType().name().toLowerCase(), Utils.getEntityType(ent.getType()) });
      if (ei.isCancelled)
        plr.sendMessage(KFA.l(plr, "protectioncore.warning.nopvp"));
    }
    else
    {
      ei.isCancelled = Utils.eventCancelTest(plr, ent.getLocation(), FlagType.AttackEntity, true, new String[] { 
        ent.getType().name().toLowerCase(), Utils.getEntityType(ent.getType()) });
      if (ei.isCancelled)
        plr.sendMessage(KFA.l(plr, "protectioncore.warning.attackentity"));
    }
  }

  public void onAttemptEntityInteract(MC_Player plr, MC_Entity ent, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    ei.isCancelled = Utils.eventCancelTest(plr, ent.getLocation(), FlagType.InterractEntity, true, new String[] { 
      ent.getType().name().toLowerCase(), Utils.getEntityType(ent.getType()) });
    if (ei.isCancelled)
      plr.sendMessage(KFA.l(plr, "protectioncore.warning.interractentity"));
  }

  public void onAttemptBookChange(MC_Player plr, List<String> bookContent, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    ei.isCancelled = Utils.eventCancelTest(plr, plr.getLocation(), FlagType.BookChange, true, new String[] { 
      "" });
    if (ei.isCancelled)
      plr.sendMessage(KFA.l(plr, "protectioncore.warning.bookchange"));
  }

  public void onAttemptBlockBreak(MC_Player plr, MC_Location loc, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    MC_ItemStack it = plr.getItemInHand();
    MC_Block block = KFA.srv.getWorld(loc.dimension).getBlockAt((int)Math.floor(loc.x), (int)Math.floor(loc.y), (int)Math.floor(loc.z));
    String[] a = Utils.addtoAll(Utils.itemTests(it), "tool");
    String c = block.getId() + ":" + block.getSubtype();
    String d = String.valueOf(block.getId());
    ei.isCancelled = Utils.eventCancelTest(plr, loc, FlagType.BlockBreak, true, 
      (String[])ArrayUtils.addAll(Utils.addtoAll(a, c), (String[])ArrayUtils.addAll(Utils.addtoAll(a, d), new String[] { c, d })));
    if (ei.isCancelled) {
      plr.sendMessage(KFA.l(plr, "protectioncore.warning.blockbreak"));
    }
    else if ((it.getId() == Utils.wandtool.getId()) && (it.getDamage() == Utils.wandtool.getDamage()) && 
      (Utils.hasPerm(plr, "select"))) {
      new Select(plr, loc, false, new String[0]);
      ei.isCancelled = true;
    }
    else if ((it.getId() == Utils.infotool.getId()) && (it.getDamage() == Utils.wandtool.getDamage())) {
      if (!Utils.hasPerm(plr, "command.info")) {
        plr.sendMessage(KFA.l(plr, "protectioncore.noperm.info"));
      }
      else {
        for (Region r : Region.get(plr.getLocation()))
          new Info(plr, new String[] { KFA.l(plr, "protectioncore.command.info").split("\\ ")[0], r.name });
      }
    }
    else
    {
      List rl = Region.get(loc);
      for (Region r : rl)
        if ((r.blockPlace != null) && 
          (r.blockPlace.getBlockX() == loc.getBlockX()) && (r.blockPlace.getBlockY() == loc.getBlockY()) && (r.blockPlace.getBlockZ() == loc.getBlockZ()) && 
          (Utils.hasPermCommand(plr, r, "remove"))) {
          ChatAPI.msg(plr, "protectioncore.remove.removed", new String[] { "<region>", r.name });
          if (Utils.silktouch) {
            MC_World w = KFA.srv.getWorld(loc.dimension);
            MC_Block bl = w.getBlockAt(loc);
            w.dropItem(KFA.srv.createItemStack(bl.getId(), 1, bl.getSubtype()), loc, plr.getName());
            w.setBlockAt(loc, w.getBlockAt(loc.getBlockX(), 255, loc.getBlockZ()), 0);
            ei.isCancelled = true;
          }
          r.remove();
        }
    }
  }

  public void onAttemptItemUse(MC_Player plr, MC_ItemStack is, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    ei.isCancelled = Utils.eventCancelTest(plr, plr.getLocation(), FlagType.ItemUse, true, Utils.itemTests(is));
    if (ei.isCancelled)
      plr.sendMessage(KFA.l(plr, "protectioncore.warning.itemuse"));
  }

  public void onAttemptItemPickup(MC_Player plr, MC_ItemStack is, boolean isXpOrb, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    ei.isCancelled = (isXpOrb ? 
      Utils.eventCancelTest(plr, plr.getLocation(), FlagType.ExpPickup, true, new String[] { "" }) : 
      Utils.eventCancelTest(plr, plr.getLocation(), FlagType.ItemPickup, true, Utils.itemTests(is)));
    if (ei.isCancelled)
      plr.sendMessage(KFA.l(plr, isXpOrb ? 
        "protectioncore.warning.exppickup" : "protectioncore.warning.itempickup"));
  }

  public void onAttemptBlockFlow(MC_Location loc, MC_Block blk, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    ei.isCancelled = Utils.eventCancelTest(null, loc, FlagType.BlockFlow, true, new String[] { 
      blk.getId() + ":" + blk.getSubtype(), blk.getId() });
  }

  public void onAttemptEntitySpawn(MC_Entity ent, MC_EventInfo ei) {
    if (ei.isCancelled)
      return;
    ei.isCancelled = Utils.eventCancelTest(null, ent.getLocation(), FlagType.EntitySpawn, true, new String[] { 
      ent.getType().name().toLowerCase(), Utils.getEntityType(ent.getType()) });
  }

  public void onAttemptEntityDamage(MC_Entity ent, MC_DamageType dmgType, double amt, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    String dmg = dmgType.toString().toLowerCase();
    if (ent.getType() == MC_EntityType.PLAYER) {
      if ((ent.getAttacker() != null) && (ent.getAttacker().getType() == MC_EntityType.PLAYER))
        ei.isCancelled = Utils.eventCancelTest((MC_Player)ent, ent.getLocation(), FlagType.PVP, false, new String[] { 
          dmg });
      else
        ei.isCancelled = Utils.eventCancelTest((MC_Player)ent, ent.getLocation(), FlagType.PlayerDamage, false, new String[] { 
          dmg });
    }
    else {
      String a = ent.getType().name().toLowerCase() + "-" + dmg;
      String b = ent.getType().name().toLowerCase();
      String c = Utils.getEntityType(ent.getType()) + "-" + dmg;
      String d = Utils.getEntityType(ent.getType());
      ei.isCancelled = Utils.eventCancelTest(null, ent.getLocation(), FlagType.EntityDamage, true, new String[] { 
        dmg, a, b, c, d });
    }
  }

  public void onAttemptDamageHangingEntity(MC_Player plr, MC_Location loc, MC_HangingEntityType entType, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    ei.isCancelled = Utils.eventCancelTest(null, loc, FlagType.EntityDamage, true, new String[] { 
      entType.name(), "hanging" });
    if (ei.isCancelled)
      plr.sendMessage(KFA.l(plr, "protectioncore.warning.attackentity"));
  }

  public boolean onAttemptExplodeSpecific(MC_Entity ent, List<MC_Location> locs)
  {
    if (locs.size() == 0) {
      return false;
    }
    String a = ent.getType().name().toLowerCase();
    String b = Utils.getEntityType(ent.getType());
    MC_World world = KFA.srv.getWorld(((MC_Location)locs.get(0)).dimension);
    for (MC_Location loc : new ArrayList(locs)) {
      MC_Block bl = world.getBlockAt((int)Math.floor(loc.x), (int)Math.floor(loc.y), (int)Math.floor(loc.z));
      String c = bl.getId() + ":" + bl.getSubtype();
      String d = bl.getId();

      if (Utils.eventCancelTest(null, loc, FlagType.Explosion, true, new String[] { 
        a + "-" + c, a + "-" + d, b + "-" + c, b + "-" + d, a, b, c, d })) {
        locs.remove(loc);
      }
    }
    return true;
  }

  public void onAttemptExplosion(MC_Location loc, MC_EventInfo ei) {
    if (ei.isCancelled)
      return;
    ei.isCancelled = (ei.isCancelled = Utils.eventCancelTest(null, loc, FlagType.Explosion, true, new String[] { "" }));
  }

  public void onAttemptItemDrop(MC_Player plr, MC_ItemStack is, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    ei.isCancelled = Utils.eventCancelTest(plr, plr.getLocation(), FlagType.ItemDrop, true, 
      Utils.itemTests(is));
    if (ei.isCancelled)
      plr.sendMessage(KFA.l(plr, "protectioncore.warning.itemdrops"));
  }

  public void onAttemptItemFrameInteract(MC_Player plr, MC_Location loc, MC_ItemFrameActionType actionType, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    ei.isCancelled = Utils.eventCancelTest(plr, loc, FlagType.InterractEntity, true, new String[] { 
      "item_frame-" + actionType.name().toLowerCase().replace("_inner_item", ""), "item_frame", "hanging" });
    if (ei.isCancelled)
      plr.sendMessage(KFA.l(plr, "protectioncore.warning.interractentity"));
  }

  public void onAttemptArmorStandInteract(MC_Player plr, MC_Entity entStand, MC_ArmorStandActionType actionType, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    ei.isCancelled = Utils.eventCancelTest(plr, entStand.getLocation(), FlagType.InterractEntity, true, new String[] { 
      "armor_stand-" + actionType.name().toLowerCase(), "armor_stand", "hanging" });
    if (ei.isCancelled)
      plr.sendMessage(KFA.l(plr, "protectioncore.warning.interractentity"));
  }

  public void onAttemptPistonAction(MC_Location loc, MC_DirectionNESWUD dir, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    MC_World world = KFA.srv.getWorld(loc.dimension);
    MC_Block bl1 = world.getBlockAt(loc);
    MC_Block bl2 = world.getBlockAt(loc.getLocationAtDirection(dir));

    if (!Utils.eventCancelTest(null, loc, FlagType.PistonAction, true, new String[] { 
      dir.name().toLowerCase(), bl1.getId() + "-" + bl1.getSubtype(), bl1.getId() }));
    ei.isCancelled = 
      (Utils.eventCancelTest(null, loc.getLocationAtDirection(dir), FlagType.PistonAction, true, new String[] { 
      dir.name().toLowerCase(), bl2.getId() + "-" + bl2.getSubtype(), bl2.getId() }));
  }

  public void onAttemptPlaceOrInteract(MC_Player plr, MC_Location loc, MC_EventInfo ei, MC_DirectionNESWUD dir)
  {
    if (ei.isCancelled)
      return;
    MC_ItemStack it = plr.getItemInHand();
    MC_Block block = KFA.srv.getWorld(loc.dimension).getBlockAt((int)Math.floor(loc.x), (int)Math.floor(loc.y), (int)Math.floor(loc.z));
    String[] a = Utils.addtoAll(Utils.itemTests(it), "tool");
    String c = block.getId() + ":" + block.getSubtype();
    String d = String.valueOf(block.getId());
    ei.isCancelled = Utils.eventCancelTest(plr, loc, FlagType.BlockInterract, true, 
      (String[])ArrayUtils.addAll(Utils.addtoAll(a, c), (String[])ArrayUtils.addAll(Utils.addtoAll(a, d), new String[] { c, d })));
    if (ei.isCancelled) {
      plr.sendMessage(KFA.l(plr, "protectioncore.warning.blockinterract"));
    }
    else {
      if ((it.getId() == Utils.wandtool.getId()) && (it.getDamage() == Utils.wandtool.getDamage()) && (Utils.hasPerm(plr, "select"))) {
        new Select(plr, loc, true, new String[0]);
        ei.isCancelled = true;
        return;
      }
      if ((it.getId() == Utils.infotool.getId()) && (it.getDamage() == Utils.wandtool.getDamage()))
        new Info(plr, new String[] { KFA.l(plr, "protectioncore.command.info").split("\\ ")[0] });
    }
  }

  public void onAttemptBlockPlace(MC_Player plr, MC_Location loc, MC_Block blk, MC_ItemStack isHandItem, MC_Location locPlacedAgainst, MC_DirectionNESWUD dir, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    MC_ItemStack it = plr.getItemInHand();
    String[] a = Utils.addtoAll(Utils.itemTests(it), "tool");
    String c = blk.getId() + ":" + blk.getSubtype();
    String d = String.valueOf(blk.getId());
    ei.isCancelled = Utils.eventCancelTest(plr, loc, FlagType.BlockPlace, true, 
      (String[])ArrayUtils.addAll(Utils.addtoAll(a, c), (String[])ArrayUtils.addAll(Utils.addtoAll(a, d), new String[] { c, d })));
    if (ei.isCancelled) {
      plr.sendMessage(KFA.l(plr, "protectioncore.warning.blockplace"));
    }
    else {
      Region r = Region.getTemplate(plr);
      if (r != null) {
        String blidtype = blk.getId() + ":" + blk.getSubtype();
        String pln = plr.getName();
        List fv = r.getFlag(plr, FlagType.CreationBlock);
        if (Utils.flagCheck(fv, new String[] { blidtype, blk.getId() })) {
          MC_Location loc2 = Utils.getRelativeBlockPlaceLocation(fv, blidtype);
          if (loc2 == null)
            loc2 = Utils.getRelativeBlockPlaceLocation(fv, blk.getId());
          Utils.sel1.put(pln, new MC_Location(loc.x - loc2.x, loc.y - loc2.y, loc.z - loc2.z, loc.dimension));
          Utils.sel2.put(pln, new MC_Location(loc.x + loc2.x, loc.y + loc2.y, loc.z + loc2.z, loc.dimension));
          String n = Utils.nextRegionName(plr);
          new Define(plr, new String[] { "define", n });
          Region r1 = Region.get(plr.getLocation().dimension, n);
          if (r1 != null)
            r1.blockPlace = loc;
        }
      }
    }
  }

  public void onAttemptEntityMiscGrief(MC_Entity ent, MC_Location loc, MC_MiscGriefType griefType, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    ei.isCancelled = Utils.eventCancelTest(null, loc, FlagType.EntityGrief, true, new String[] { 
      griefType.name().toLowerCase() });
  }

  public void onAttemptSpectateEntity(MC_Player plr, MC_Entity ent, MC_EventInfo ei)
  {
    if ((ei.isCancelled) || (ent == null))
      return;
    ei.isCancelled = Utils.eventCancelTest(plr, ent.getLocation(), FlagType.SpectateEntity, true, new String[] { 
      Utils.getEntityType(ent.getType()), ent.getType().name() });
    if (ei.isCancelled)
      plr.sendMessage(KFA.l(plr, "protectioncore.warning.spectateentity"));
  }

  public void onPlayerInput(MC_Player plr, String msg, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    if (msg.startsWith("/")) {
      ei.isCancelled = Utils.eventCancelTest(plr, plr.getLocation(), FlagType.Commands, true, 
        Utils.split(msg.substring(1)));
    }
    else {
      ei.isCancelled = Utils.eventCancelTest(plr, plr.getLocation(), FlagType.ChatSend, true, new String[] { 
        "" });
      if (ei.isCancelled)
        plr.sendMessage(KFA.l(plr, "protectioncore.warning.chatsend"));
    }
  }

  public void onAttemptPlayerChangeDimension(MC_Player plr, int dim, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    ei.isCancelled = Utils.eventCancelTest(plr, plr.getLocation(), FlagType.DimensionChange, true, new String[] { 
      dim });
    if (!Region.regions.containsKey(Integer.valueOf(dim))) {
      System.out.println("Global region creating for new dimension, " + dim + "...");
      int min = -2147483648;
      int max = 2147483647;
      new Region(new Area(min, min, min, max, max, max, dim), "{global}", 0, Region.getTemplate("{global}"));
    }
    if (ei.isCancelled)
      plr.sendMessage(KFA.l(plr, "protectioncore.warning.dimensionchange"));
  }

  public void onAttemptPlayerTeleport(MC_Player plr, MC_Location loc, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    MC_Location locFrom = plr.getLocation();
    ei.isCancelled = Utils.eventCancelTest(plr, locFrom, FlagType.Teleport, true, new String[] { 
      "" });
    if (ei.isCancelled) {
      plr.sendMessage(KFA.l(plr, "protectioncore.warning.teleport"));
      return;
    }
    ei.isCancelled = Utils.regionChangeTest(plr, plr.getLocation(), loc, true);
    if (!ei.isCancelled)
      Utils.plc.put(plr.getName(), loc);
  }

  public void onAttemptPotionEffect(MC_Player plr, MC_PotionEffectType potionType, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    ei.isCancelled = Utils.eventCancelTest(plr, plr.getLocation(), FlagType.GetPotionEffects, true, new String[] { 
      potionType.name().toLowerCase() });
    if (ei.isCancelled)
      plr.sendMessage(KFA.l(plr, "protectioncore.warning.getpotioneffects"));
  }

  public void onSignChanging(MC_Player plr, MC_Sign sign, MC_Location loc, List<String> neWLines, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    ei.isCancelled = Utils.eventCancelTest(plr, plr.getLocation(), FlagType.SignChange, true, new String[] { 
      "" });
    if (ei.isCancelled)
      plr.sendMessage(KFA.l(plr, "protectioncore.warning.singchange"));
  }

  public void onAttemptCropTrample(MC_Entity ent, MC_Location loc, MC_EventInfo ei)
  {
    if (ei.isCancelled)
      return;
    MC_Block bl = KFA.srv.getWorld(loc.dimension).getBlockAt((int)Math.floor(loc.x), (int)Math.floor(loc.y), (int)Math.floor(loc.z));
    if (ent.getType() == MC_EntityType.PLAYER) {
      MC_Player plr = (MC_Player)ent;
      ei.isCancelled = Utils.eventCancelTest(plr, loc, FlagType.CropTrample, true, new String[] { 
        bl.getId() + ":" + bl.getSubtype(), bl.getId() });
      if (ei.isCancelled)
        plr.sendMessage(KFA.l(plr, "protectioncore.warning.croptrample"));
    }
    else
    {
      ei.isCancelled = Utils.eventCancelTest(null, loc, FlagType.CropTrample, true, new String[] { 
        "" });
    }
  }

  public static enum CommandType
  {
    define, remove, area, info, list, flag, removeflag, save, reload, priority, gettool, player, group, 
    sel1, sel2;
  }

  public static enum FlagType
  {
    BookChange, BlockBreak, BlockPlace, BlockInterract, BlockFlow, 
    CreationBlock, Commands, ChatSend, ChatReceive, CropTrample, 
    AttackEntity, EditFlags, EntityDamage, EntityGrief, EntitySpawn, InterractEntity, 
    ExpPickup, Explosion, Entry, Leave, Fly, 
    GameMode, Greeting, Farewell, ItemDrop, ItemPickup, ItemUse, ManageRegion, 
    Permissions, PotionEffects, PVP, PlayerDamage, 
    PistonAction, GetPotionEffects, SpectateEntity, 
    Move, Teleport, DimensionChange, SignChange;
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.ProtectionCore
 * JD-Core Version:    0.6.2
 */