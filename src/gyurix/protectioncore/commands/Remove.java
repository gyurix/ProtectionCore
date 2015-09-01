package gyurix.protectioncore.commands;

import PluginReference.MC_Player;
import gyurix.chatapi.ChatAPI;
import gyurix.protectioncore.Region;
import gyurix.protectioncore.Utils;

public class Remove
{
  public Remove(MC_Player plr, String[] args)
  {
    Region r = Utils.handleCommand(plr, args, "remove");
    if (r == null)
      return;
    r.remove();
    ChatAPI.msg(plr, "protectioncore.remove.removed", new String[] { "<region>", args[1] });
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.commands.Remove
 * JD-Core Version:    0.6.2
 */