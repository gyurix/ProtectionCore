package gyurix.protectioncore.commands;

import PluginReference.MC_Player;
import gyurix.konfigfajl.KFA;
import gyurix.protectioncore.Utils;

public class Gettool
{
  public Gettool(MC_Player plr, String[] args)
  {
    if (Utils.hasPerm(plr, "command.gettool")) {
      boolean info = (args.length == 2) && (args[1].equalsIgnoreCase("info"));
      plr.setItemInHand(info ? 
        Utils.infotool : Utils.wandtool);
      plr.sendMessage(KFA.l(plr, "protectioncore.gettool.success")
        .replace("<type>", KFA.l(plr, "protectioncore.tooltype." + (info ? "info" : "wand"))));
    }
    else {
      plr.sendMessage(KFA.l(plr, "protectioncore.noperm.gettool"));
    }
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.commands.Gettool
 * JD-Core Version:    0.6.2
 */