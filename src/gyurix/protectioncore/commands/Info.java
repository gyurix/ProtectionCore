package gyurix.protectioncore.commands;

import PluginReference.MC_Player;
import gyurix.chatapi.ChatAPI;
import gyurix.konfigfajl.KFA;
import gyurix.protectioncore.Region;
import gyurix.protectioncore.Utils;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class Info
{
  String[] args;
  MC_Player plr;

  public Info(MC_Player p, String[] argl)
  {
    this.args = argl;
    this.plr = p;
    new Thread(new Runnable()
    {
      public void run() {
        if (Info.this.args.length == 1) {
          if (!Utils.hasPerm(Info.this.plr, "command.info")) {
            ChatAPI.msg(Info.this.plr, "protectioncore.noperm.info", new String[0]);
            return;
          }
          List regs = new ArrayList();
          String format = KFA.l(Info.this.plr, "protectioncore.info.list.format");
          for (Region r : Region.get(Info.this.plr.getLocation())) {
            regs.add(r.getInfo(format, null, null));
          }
          Info.this.plr.sendMessage(KFA.l(Info.this.plr, "protectioncore.info.list")
            .replace("<regions>", StringUtils.join(regs, 
            KFA.l(Info.this.plr, "protectioncore.info.list.separator")))
            .replace("\\n", "\n"));
        }
        else {
          Region r = Utils.handleCommand(Info.this.plr, Info.this.args, "info");
          if (r == null)
            return;
          String format = KFA.l(Info.this.plr, "protectioncore.info.region");
          String grformat = KFA.l(Info.this.plr, "protectioncore.info.group");
          String sep = KFA.l(Info.this.plr, "protectioncore.info.group.separator");
          if (Info.this.plr == null)
            System.out.println(r.getInfo(format, grformat, sep).replace("\\n", "\n"));
          else
            Info.this.plr.sendMessage(r.getInfo(format, grformat, sep).replace("\\n", "\n"));
        }
      }
    }).run();
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.commands.Info
 * JD-Core Version:    0.6.2
 */