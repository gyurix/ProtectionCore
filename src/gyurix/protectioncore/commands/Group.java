package gyurix.protectioncore.commands;

import PluginReference.MC_Player;
import gyurix.konfigfajl.KFA;
import gyurix.protectioncore.Region;
import gyurix.protectioncore.Utils;

public class Group
{
  public Group(MC_Player plr, String[] args)
  {
    Region r = Utils.handleCommand(plr, args, "group");
    if (r == null)
      return;
    if (args.length == 2) {
      String format = KFA.l(plr, "protectioncore.info.region");
      String grformat = KFA.l(plr, "protectioncore.info.group");
      String sep = KFA.l(plr, "protectioncore.info.group.separator");
      plr.sendMessage(r.getInfo(format, grformat, sep).replace("\\n", "\n"));
    }
    else {
      byte out = r.groupManage(args[2], args.length == 3 ? null : args[3]);
      if (out == 0) {
        plr.sendMessage(KFA.l(plr, "protectioncore.group.create")
          .replace("<region>", args[1])
          .replace("<group>", args[2]));
      }
      else if (out == 1) {
        plr.sendMessage(KFA.l(plr, "protectioncore.group.remove")
          .replace("<region>", args[1])
          .replace("<group>", args[2]));
      }
      else if (out == 2) {
        plr.sendMessage(KFA.l(plr, "protectioncore.group.default")
          .replace("<region>", args[1])
          .replace("<group>", args[2]));
      }
      else if (out == 3) {
        plr.sendMessage(KFA.l(plr, "protectioncore.group.create.badcopygroup")
          .replace("<region>", args[1])
          .replace("<group>", args[2])
          .replace("<copygroup>", args[3]));
      }
      else if (out == 4)
        plr.sendMessage(KFA.l(plr, "protectioncore.group.create.copygroup")
          .replace("<region>", args[1])
          .replace("<group>", args[2])
          .replace("<copygroup>", args[3]));
    }
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.commands.Group
 * JD-Core Version:    0.6.2
 */