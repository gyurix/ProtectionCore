package gyurix.protectioncore.commands;

import PluginReference.MC_Location;
import PluginReference.MC_Player;
import gyurix.konfigfajl.KFA;
import gyurix.protectioncore.Utils;
import java.util.Map;

public class Select
{
  public Select(MC_Player plr, MC_Location loc, boolean second, String[] args)
  {
    Map sel = second ? Utils.sel2 : Utils.sel1;
    if (args.length > 1) {
      if (!Utils.hasPerm(plr, "select")) {
        plr.sendMessage(KFA.l(plr, "protectioncore.noperm.select"));
        return;
      }
      try {
        loc = new MC_Location(Double.valueOf(args[1]).doubleValue(), Double.valueOf(args[2]).doubleValue(), Double.valueOf(args[3]).doubleValue(), plr.getLocation().dimension, 0.0F, 0.0F);
      }
      catch (Throwable h) {
        loc = plr.getLocation();
      }
    }
    sel.put(plr.getName(), loc);
    plr.sendMessage(KFA.l(plr, "protectioncore.selection." + (second ? "second" : "first"))
      .replace("<x>", loc.getBlockX())
      .replace("<y>", loc.getBlockY())
      .replace("<z>", loc.getBlockZ()));
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.commands.Select
 * JD-Core Version:    0.6.2
 */