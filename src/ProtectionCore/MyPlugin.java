package ProtectionCore;

import PluginReference.PluginBase;
import PluginReference.PluginInfo;
import gyurix.protectioncore.ProtectionCore;

public class MyPlugin extends PluginBase
{
  public static PluginInfo info;
  public static PluginBase pl;

  public PluginInfo getPluginInfo()
  {
    PluginInfo inf = new PluginInfo();
    inf.eventSortOrder = -100000000.0D;
    inf.description = "The core of all the protection management";
    pl = this; inf.ref = this;
    return MyPlugin.info = inf;
  }

  public void onServerFullyLoaded() {
    info.ref = new ProtectionCore();
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     ProtectionCore.MyPlugin
 * JD-Core Version:    0.6.2
 */