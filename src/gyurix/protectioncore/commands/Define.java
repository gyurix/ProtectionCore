package gyurix.protectioncore.commands;

import PluginReference.MC_Player;
import gyurix.chatapi.ChatAPI;
import gyurix.konfigfajl.KFA;
import gyurix.permissions.PermApi;
import gyurix.protectioncore.Area;
import gyurix.protectioncore.Region;
import gyurix.protectioncore.Utils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class Define
{
  public Define(MC_Player plr, String[] args)
  {
    if (!Utils.hasPerm(plr, "command.define")) {
      plr.sendMessage(KFA.l(plr, "protectioncore.noperm.define"));
      return;
    }
    Region templ;
    if (args.length == 2) {
      Region templ = Region.getTemplate(plr);
      if (templ == null) {
        ChatAPI.msg(plr, "protectioncore.noperm.template.any", new String[0]);
      }
    }
    else
    {
      templ = Region.getTemplate(args[2]);
      if (templ == null) {
        ChatAPI.msg(plr, "protectioncore.define.invalidtemplate", new String[] { "<template>", args[2] });
        return;
      }
      if (!PermApi.has(plr, templ.perm)) {
        ChatAPI.msg(plr, "protectioncore.noperm.template.given", new String[] { "<template>", args[2] });
        return;
      }
    }
    int regnum = Utils.canCreateNewRegion(plr);
    if (regnum != -1) {
      plr.sendMessage(KFA.l(plr, "protectioncore.define.reached")
        .replace("<num>", regnum));
      return;
    }
    Area a = Utils.getSelectedArea(plr);
    if (!Utils.handleArea(a, plr))
      return;
    if (((List)Region.regnames.get(Integer.valueOf(a.worldid))).contains(args[1])) {
      plr.sendMessage(KFA.l(plr, "protectioncore.region.exists").replace("<region>", args[1]));
      return;
    }
    Region r = new Region(a, args[1], 0, templ);
    r.creator = plr.getName();
    for (HashSet pll : r.players) {
      if (pll.remove("<creator>")) {
        UUID uuid = plr.getUUID();
        pll.add(uuid.version() == 3 ? plr.getName() : uuid.toString());
      }
    }
    plr.sendMessage(KFA.l(plr, "protectioncore.define.created").replace("<region>", args[1]));
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.commands.Define
 * JD-Core Version:    0.6.2
 */