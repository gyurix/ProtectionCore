package gyurix.protectioncore.commands;

import PluginReference.MC_Player;
import gyurix.konfigfajl.KFA;
import gyurix.protectioncore.ProtectionCore.FlagType;
import gyurix.protectioncore.Region;
import gyurix.protectioncore.Utils;
import java.util.ArrayList;
import java.util.Map;

public class Removeflag
{
  public Removeflag(MC_Player plr, String[] args)
  {
    Region r = Utils.handleCommand(plr, args, "removeflag");
    if (r == null)
      return;
    if (args.length == 3) {
      plr.sendMessage(r.getInfo(KFA.l(plr, "protectioncore.flag.list.global"), 
        KFA.l(plr, "protectioncore.flag.list.global.group"), 
        KFA.l(plr, "protectioncore.flag.list.global.separator"))
        .replace("\\n", "\n"));
      return;
    }
    int grid = r.groups.indexOf(args[2]);
    if (grid == -1) {
      plr.sendMessage(KFA.l(plr, "protectioncore.group.notfound")
        .replace("<region>", r.name)
        .replace("<group>", args[2]));
      return;
    }
    if (args.length == 4) {
      plr.sendMessage(r.getInfo(KFA.l(plr, "protectioncore.flag.list.groups"), 
        KFA.l(plr, "protectioncore.flag.list.groups.format"), 
        KFA.l(plr, "protectioncore.flag.list.groups.separator"), 
        grid)
        .replace("\\n", "\n"));
      return;
    }
    try {
      ProtectionCore.FlagType ft = ProtectionCore.FlagType.valueOf(args[3]);
      if (!Utils.flagCheck(r.getFlag(plr, ProtectionCore.FlagType.EditFlags), new String[] { args[3] })) {
        ((Map)r.flags.get(grid)).remove(ft);
        plr.sendMessage(KFA.l(plr, "protectioncore.flagremove.removed")
          .replace("<region>", r.name)
          .replace("<group>", args[2])
          .replace("<flag>", args[3]));
      }
      else {
        plr.sendMessage(KFA.l(plr, "protectioncore.noperm.flagremove"));
      }
    } catch (Throwable e) {
      plr.sendMessage(KFA.l(plr, "protectioncore.flag.flagnotfound")
        .replace("<flag>", args[3]));
      return;
    }
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.commands.Removeflag
 * JD-Core Version:    0.6.2
 */