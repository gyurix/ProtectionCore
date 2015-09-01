package gyurix.protectioncore.commands;

import PluginReference.MC_DirectionNESWUD;
import PluginReference.MC_Player;
import gyurix.chatapi.ChatAPI;
import gyurix.konfigfajl.KFA;
import gyurix.protectioncore.Area;
import gyurix.protectioncore.Region;
import gyurix.protectioncore.Utils;
import java.util.Arrays;

public class EditArea
{
  public static void sendAreaInfo(MC_Player plr, Region r)
  {
    ChatAPI.msg(plr, "protectioncore.area.info", new String[] { 
      "<region>", r.name, 
      "<minx>", r.area.minx, 
      "<miny>", r.area.miny, 
      "<minz>", r.area.minz, 
      "<maxx>", r.area.maxx, 
      "<maxy>", r.area.maxy, 
      "<maxz>", r.area.maxz });
  }
  public EditArea(MC_Player plr, String[] args) {
    Region r = Utils.handleCommand(plr, args, "area");
    if (r == null)
      return;
    if ((args.length > 2) && (plr != null)) {
      if (r.blockPlace != null) {
        ChatAPI.msg(plr, "protectioncore.area.cantedit", new String[] { "<region>", args[1] });
      }
      MC_DirectionNESWUD[] dirs = MC_DirectionNESWUD.values();
      Arrays.sort(dirs);
      if (Utils.isCommand(plr, args[2], "area.redefine")) {
        Area a = Utils.getSelectedArea(plr);
        if (!Utils.handleArea(a, plr))
          return;
        r.area = a;
        plr.sendMessage(KFA.l(plr, "protectioncore.area.redefined")
          .replace("<minx>", a.minx)
          .replace("<maxx>", a.maxx)
          .replace("<miny>", a.miny)
          .replace("<maxy>", a.maxy)
          .replace("<minz>", a.minz)
          .replace("<maxz>", a.maxz)
          .replace("<region>", args[1]));
      }
      else if (Utils.isCommand(plr, args[2], "area.groundair")) {
        Area a = r.area.duplicate();
        a.maxy = 256L;
        a.miny = 0L;
        if (!Utils.handleArea(a, plr))
          return;
        r.area = a;
        plr.sendMessage(KFA.l(plr, "protectioncore.area.grair")
          .replace("<region>", args[1]));
      }
      else if (Utils.isCommand(plr, args[2], "area.expand")) {
        Area a = r.area.duplicate();
        int amount = 1;
        try {
          amount = Integer.valueOf(args[3]).intValue();
        } catch (Throwable localThrowable) {
        }
        MC_DirectionNESWUD dir = KFA.getLocationDirection(plr.getLocation());
        a.expand(dir, amount);
        if (!Utils.handleArea(a, plr))
          return;
        r.area = a;
        plr.sendMessage(KFA.l(plr, "protectioncore.area.expanded")
          .replace("<region>", args[1])
          .replace("<direction>", KFA.l(plr, "protectioncore.area.directions").split("\\ ")[
          org.apache.commons.lang3.ArrayUtils.indexOf(dirs, dir)])
          .replace("<block>", amount));
      }
      else if (Utils.isCommand(plr, args[2], "area.decrease")) {
        Area a = r.area.duplicate();
        int amount = 1;
        try {
          amount = Integer.valueOf(args[3]).intValue();
        } catch (Throwable localThrowable1) {
        }
        MC_DirectionNESWUD dir = KFA.getLocationDirection(plr.getLocation());
        a.decrease(dir, amount);
        if (!a.isSorted()) {
          plr.sendMessage(KFA.l(plr, "protectioncore.area.invalid")
            .replace("<region>", args[1])
            .replace("<direction>", KFA.l(plr, "protectioncore.area.directions").split("\\ ")[
            org.apache.commons.lang3.ArrayUtils.indexOf(dirs, dir)]));
          return;
        }
        if (!Utils.handleArea(a, plr))
          return;
        r.area = a;
        plr.sendMessage(KFA.l(plr, "protectioncore.area.expanded")
          .replace("<region>", args[1])
          .replace("<direction>", KFA.l(plr, "protectioncore.area.directions").split("\\ ")[
          org.apache.commons.lang3.ArrayUtils.indexOf(dirs, dir)])
          .replace("<block>", amount));
      }
      else if (Utils.isCommand(plr, args[2], "area.move")) {
        int amount = 1;
        try {
          amount = Integer.valueOf(args[3]).intValue();
        } catch (Throwable localThrowable2) {
        }
        MC_DirectionNESWUD dir = KFA.getLocationDirection(plr.getLocation());
        r.area.move(dir, amount);
        plr.sendMessage(KFA.l(plr, "protectioncore.area.moved")
          .replace("<region>", args[1])
          .replace("<direction>", KFA.l(plr, "protectioncore.area.directions").split("\\ ")[
          org.apache.commons.lang3.ArrayUtils.indexOf(dirs, dir)])
          .replace("<block>", amount));
      }
      else {
        sendAreaInfo(plr, r);
      }
    } else {
      sendAreaInfo(plr, r);
    }
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.commands.EditArea
 * JD-Core Version:    0.6.2
 */