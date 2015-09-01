package gyurix.protectioncore.commands;

import PluginReference.MC_Player;
import gyurix.konfigfajl.KFA;
import gyurix.protectioncore.Flags;
import gyurix.protectioncore.ProtectionCore.FlagType;
import gyurix.protectioncore.Region;
import gyurix.protectioncore.Utils;
import java.util.ArrayList;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class Flag
{
  public Flag(MC_Player plr, String[] args)
  {
    Region r = Utils.handleCommand(plr, args, "flag");
    if (r == null)
      return;
    if (args.length == 2) {
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
    if (args.length == 3) {
      plr.sendMessage(r.getInfo(KFA.l(plr, "protectioncore.flag.list.groups"), 
        KFA.l(plr, "protectioncore.flag.list.groups.format"), 
        KFA.l(plr, "protectioncore.flag.list.groups.separator"), 
        grid)
        .replace("\\n", "\n"));
      return;
    }
    try {
      ProtectionCore.FlagType ft = ProtectionCore.FlagType.valueOf(args[3]);
      boolean allow = Utils.hasPerm(plr, "protectioncore.disableflagcheck.EditFlags");
      if (!allow)
        for (String f : r.getFlag(plr, ProtectionCore.FlagType.EditFlags)) {
          boolean poz = f.startsWith("+");
          if (poz == (KFA.search(Flags.getParameters(f), args[3]) == -1)) {
            allow = true;
            break;
          }
        }
      boolean cf = ((Map)r.flags.get(grid)).containsKey(ft);
      if (allow) {
        String value = args.length >= 5 ? StringUtils.join(args, " ", 4, args.length).replaceAll("(?i)&([a-f0-9k-or])", "§$1") : "";
        ((Map)r.flags.get(grid)).put(ft, value);
        plr.sendMessage((cf ? KFA.l(plr, "protectioncore.flag.modified") : 
          KFA.l(plr, "protectioncore.flag.created"))
          .replace("<region>", r.name)
          .replace("<group>", args[2])
          .replace("<flag>", args[3])
          .replace("<value>", value));
      }
      else {
        plr.sendMessage(cf ? KFA.l(plr, "protectioncore.flag.nopermchange") : 
          KFA.l(plr, "protectioncore.flag.nopermcreate"));
      }
    }
    catch (Throwable e)
    {
      plr.sendMessage(KFA.l(plr, "protectioncore.flag.flagnotfound")
        .replace("<flag>", args[3]));
      return;
    }
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.commands.Flag
 * JD-Core Version:    0.6.2
 */