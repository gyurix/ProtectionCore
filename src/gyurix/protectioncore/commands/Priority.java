package gyurix.protectioncore.commands;

import PluginReference.MC_Player;
import gyurix.chatapi.ChatAPI;
import gyurix.protectioncore.Region;
import gyurix.protectioncore.Utils;

public class Priority
{
  public Priority(MC_Player plr, String[] args)
  {
    Region r = Utils.handleCommand(plr, args, "priority");
    if (r == null)
      return;
    try {
      r.priority = Integer.valueOf(args[2]).intValue();
      ChatAPI.msg(plr, "protectioncore.priority.set", new String[] { "<region>", args[1], "<priority>", args[2] });
    }
    catch (Throwable e) {
      ChatAPI.msg(plr, "protectioncore.priority.notnumber", new String[] { "<priority>", args[2] });
    }
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.commands.Priority
 * JD-Core Version:    0.6.2
 */