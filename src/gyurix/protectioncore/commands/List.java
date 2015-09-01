package gyurix.protectioncore.commands;

import PluginReference.MC_Location;
import PluginReference.MC_Player;
import gyurix.chatapi.ChatAPI;
import gyurix.konfigfajl.KFA;
import gyurix.protectioncore.Region;
import gyurix.protectioncore.Utils;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

public class List
{
  public List(MC_Player plr, String[] args)
  {
    if (Utils.hasPerm(plr, "command.list")) {
      int pldim = plr == null ? 2147483647 : plr.getLocation().dimension;
      if ((plr == null) && (args.length == 1)) {
        ChatAPI.msg(null, "protectioncore.region.nodimension", new String[0]);
        return;
      }
      try {
        int dim = args.length > 1 ? Integer.valueOf(args[1]).intValue() : pldim;
        ChatAPI.msg(plr, "protectioncore.list" + (dim == pldim ? "" : ".dimension"), new String[] { "<regions>", 
          StringUtils.join((Iterable)Region.regnames.get(Integer.valueOf(dim)), 
          KFA.l(plr, "protectioncore.list.separator")), 
          "<dimension>", dim });
      }
      catch (Throwable e) {
        if (plr != null) return; 
      }ChatAPI.msg(null, "protectioncore.region.notnumber", new String[] { "<dimension>", args[0] });
    }
    else
    {
      ChatAPI.msg(plr, "protectioncore.noperm.list", new String[0]);
    }
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.commands.List
 * JD-Core Version:    0.6.2
 */