package gyurix.protectioncore.commands;

import PluginReference.MC_Command;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import com.google.common.collect.Lists;
import gyurix.chatapi.ChatAPI;
import gyurix.konfigfajl.KFA;
import gyurix.konfigfajl.LangFile;
import gyurix.protectioncore.ProtectionCore.CommandType;
import gyurix.protectioncore.ProtectionCore.FlagType;
import gyurix.protectioncore.Region;
import gyurix.protectioncore.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Default
  implements MC_Command
{
  public java.util.List<String> getAliases()
  {
    return Lists.newArrayList(KFA.lf.get("protectioncore.command.default").split("\\ "));
  }

  public String getCommandName()
  {
    return "rg";
  }

  public String getHelpLine(MC_Player plr)
  {
    if (Utils.hasPerm(plr, "use"))
      return KFA.l(plr, "protectioncore.help");
    return null;
  }

  public java.util.List<String> getTabCompletionList(MC_Player plr, String[] args)
  {
    if (!Utils.hasPerm(plr, "use")) {
      return null;
    }
    java.util.List out = new ArrayList();
    if (args.length == 1)
    {
      String[] arrayOfString;
      int j = (arrayOfString = new String[] { "define", "remove", "area", "info", "list", "flag", "removeflag", 
        "save", "reload", "priority", "gettool", "player", "group" }).length; for (int i = 0; i < j; i++) {
        String cmd = arrayOfString[i];
        if (Utils.hasPerm(plr, "command." + cmd))
          out.addAll(Arrays.asList(KFA.l(plr, "protectioncore.command." + cmd).split("\\ ")));
      }
      if (Utils.hasPerm(plr, "select")) {
        out.addAll(Arrays.asList(KFA.l(plr, "protectioncore.command.sel1").split("\\ ")));
        out.addAll(Arrays.asList(KFA.l(plr, "protectioncore.command.sel2").split("\\ ")));
      }
      for (int i = 0; i < out.size(); i++) {
        if (!((String)out.get(i)).toLowerCase().startsWith(args[0].toLowerCase())) {
          out.remove(i);
          i--;
        }
      }
      Collections.sort(out);
      return out;
    }
    ProtectionCore.CommandType type = Utils.getCommand(plr, args[0]);
    if ((type == null) || (!Utils.hasPerm(plr, "command." + type.name())))
      return null;
    if (args.length == 2) {
      if (type == ProtectionCore.CommandType.gettool) {
        out.addAll(Arrays.asList(KFA.l(plr, "protectioncore.tooltype.wand").split("\\ ")));
        out.addAll(Arrays.asList(KFA.l(plr, "protectioncore.tooltype.info").split("\\ ")));
        for (int i = 0; i < out.size(); i++) {
          if (!((String)out.get(i)).toLowerCase().startsWith(args[1].toLowerCase())) {
            out.remove(i);
            i--;
          }
        }
      }
      else if ((type != ProtectionCore.CommandType.define) && (type != ProtectionCore.CommandType.list) && (type != ProtectionCore.CommandType.reload) && 
        (type != ProtectionCore.CommandType.save) && (type != ProtectionCore.CommandType.sel1) && (type != ProtectionCore.CommandType.sel2)) {
        for (Region r : (java.util.List)Region.regions.get(Integer.valueOf(plr.getLocation().dimension))) {
          if ((r.name.toLowerCase().startsWith(args[1].toLowerCase())) && (Utils.hasPermCommand(plr, r, type.name()))) {
            out.add(r.name);
          }
        }
      }
    }
    else if (args.length == 3) {
      if (type == ProtectionCore.CommandType.area) {
        for (String cmd : new String[] { "expand", "groundair", "decrease", "redefine", "move" }) {
          if (Utils.hasPerm(plr, "command.area." + cmd))
            out.addAll(Arrays.asList(KFA.l(plr, "protectioncore.command.area." + cmd).split("\\ ")));
        }
        for (int i = 0; i < out.size(); i++) {
          if (!((String)out.get(i)).toLowerCase().startsWith(args[2].toLowerCase())) {
            out.remove(i);
            i--;
          }
        }
      }
      else if ((type == ProtectionCore.CommandType.flag) || (type == ProtectionCore.CommandType.removeflag) || (type == ProtectionCore.CommandType.group) || (type == ProtectionCore.CommandType.player)) {
        Region r = Region.get(plr.getLocation().dimension, args[1]);
        if ((r == null) || (!Utils.hasPermCommand(plr, r, type.name())))
          return null;
        for (String gr : r.groups) {
          if (gr.toLowerCase().startsWith(args[2].toLowerCase()))
            out.add(gr);
        }
      }
    }
    else if ((args.length == 4) && (
      (type == ProtectionCore.CommandType.flag) || (type == ProtectionCore.CommandType.removeflag) || (type == ProtectionCore.CommandType.player))) {
      Region r = Region.get(plr.getLocation().dimension, args[1]);
      if ((r == null) || (!Utils.hasPermCommand(plr, r, type.name())))
        return null;
      String pln;
      if (type == ProtectionCore.CommandType.player) {
        int id = r.groups.indexOf(args[2]);
        if (id < 1)
          return null;
        for (??? = ((HashSet)r.players.get(id - 1)).iterator(); ((Iterator)???).hasNext(); ) { pln = (String)((Iterator)???).next();
          if (pln.toLowerCase().startsWith(args[3].toLowerCase()))
            out.add(pln); }
      }
      else
      {
        for (ProtectionCore.FlagType ft : ProtectionCore.FlagType.values()) {
          if (ft.name().toLowerCase().startsWith(args[3].toLowerCase())) if (Utils.hasPermFlag(plr, r, new String[] { ft.name() })) {
              out.add(ft.name());
            }
        }
      }
    }

    Collections.sort(out);
    return out;
  }

  public void handleCommand(MC_Player plr, String[] args)
  {
    if (!Utils.hasPerm(plr, "use")) {
      ChatAPI.msg(plr, "protectioncore.noperm", new String[0]);
      return;
    }
    if (args.length == 0) {
      ChatAPI.msg(plr, "protectioncore.command.unknown", new String[0]);
      return;
    }
    if (Utils.isCommand(plr, args[0], "define")) {
      new Define(plr, args);
    }
    else if (Utils.isCommand(plr, args[0], "sel1")) {
      new Select(plr, plr.getLocation(), false, args);
    }
    else if (Utils.isCommand(plr, args[0], "sel2")) {
      new Select(plr, plr.getLocation(), true, args);
    }
    else if (Utils.isCommand(plr, args[0], "gettool")) {
      new Gettool(plr, args);
    }
    else if (Utils.isCommand(plr, args[0], "area")) {
      new EditArea(plr, args);
    }
    else if (Utils.isCommand(plr, args[0], "list")) {
      new List(plr, args);
    }
    else if (Utils.isCommand(plr, args[0], "info")) {
      new Info(plr, args);
    }
    else if (Utils.isCommand(plr, args[0], "remove")) {
      new Remove(plr, args);
    }
    else if (Utils.isCommand(plr, args[0], "flag")) {
      new Flag(plr, args);
    }
    else if (Utils.isCommand(plr, args[0], "removeflag")) {
      new Removeflag(plr, args);
    }
    else if (Utils.isCommand(plr, args[0], "priority")) {
      new Priority(plr, args);
    }
    else if (Utils.isCommand(plr, args[0], "save")) {
      if (Utils.hasPerm(plr, "command.save"))
        ChatAPI.msg(plr, 
          Utils.saveRegions() ? "protectioncore.save.saved" : "protectioncore.save.error", new String[0]);
      else
        ChatAPI.msg(plr, "protectioncore.noperm.save", new String[0]);
    }
    else if (Utils.isCommand(plr, args[0], "reload")) {
      if (Utils.hasPerm(plr, "command.reload")) {
        new Thread(new Reload(plr));
      }
      else
        ChatAPI.msg(plr, "protectioncore.noperm.reload", new String[0]);
    }
    else if (Utils.isCommand(plr, args[0], "player")) {
      new Player(plr, args);
    }
    else if (Utils.isCommand(plr, args[0], "group")) {
      new Group(plr, args);
    }
    else
      ChatAPI.msg(plr, "protectioncore.command.unknown", new String[0]);
  }

  public boolean hasPermissionToUse(MC_Player plr)
  {
    return Utils.hasPerm(plr, "use");
  }
  private class Reload implements Runnable {
    MC_Player plr;

    public Reload(MC_Player p) { this.plr = p; }

    public void run()
    {
      ChatAPI.msg(this.plr, 
        Utils.reload() ? "protectioncore.reload.reloaded" : "protectioncore.reload.error", new String[0]);
    }
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.commands.Default
 * JD-Core Version:    0.6.2
 */