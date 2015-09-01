package gyurix.protectioncore.commands;

import PluginReference.MC_Player;
import gyurix.konfigfajl.KFA;
import gyurix.konfigfajl.MojangAPI;
import gyurix.protectioncore.Region;
import gyurix.protectioncore.Utils;

public class Player
  implements Runnable
{
  MC_Player plr;
  String[] args;
  Region r;

  public Player(MC_Player p, String[] argl)
  {
    this.plr = p;
    this.args = argl;
    this.r = Utils.handleCommand(this.plr, this.args, "group");
    if (this.r == null)
      return;
    new Thread(this).run();
  }

  public void run() {
    if (this.args.length > 3) {
      String uuid = MojangAPI.getUUID(this.args[3]);
      if (uuid == null)
        uuid = this.args[3];
      byte out = this.r.addPlayer(this.args[2], uuid);
      if (out == 0) {
        this.plr.sendMessage(KFA.l(this.plr, "protectioncore.player.add")
          .replace("<region>", this.args[1])
          .replace("<group>", this.args[2])
          .replace("<player>", this.args[3]));
      }
      else if (out == 1) {
        this.plr.sendMessage(KFA.l(this.plr, "protectioncore.group.notfound")
          .replace("<region>", this.args[1])
          .replace("<group>", this.args[2]));
      }
      else if (out == 2) {
        this.r.removePlayer(this.args[2], uuid);
        this.plr.sendMessage(KFA.l(this.plr, "protectioncore.player.remove")
          .replace("<region>", this.args[1])
          .replace("<group>", this.args[2])
          .replace("<player>", this.args[3]));
      }
      else if (out == 3) {
        this.plr.sendMessage(KFA.l(this.plr, "protectioncore.player.strange")
          .replace("<region>", this.args[1])
          .replace("<group>", this.args[2])
          .replace("<player>", this.args[3]));
      }
    }
    else {
      String format = KFA.l(this.plr, "protectioncore.info.region");
      String grformat = KFA.l(this.plr, "protectioncore.info.group");
      String sep = KFA.l(this.plr, "protectioncore.info.group.separator");
      this.plr.sendMessage(this.r.getInfo(format, grformat, sep).replace("\\n", "\n"));
    }
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.commands.Player
 * JD-Core Version:    0.6.2
 */