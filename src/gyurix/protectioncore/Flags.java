package gyurix.protectioncore;

public final class Flags
{
  public static boolean isTrue(String flag)
  {
    return flag.startsWith("+");
  }
  public static String[] getParameters(String flag) {
    String[] par = flag.substring(1).split("\\ ");
    if ((par.length == 1) && (par[0].isEmpty())) {
      return new String[0];
    }
    return par;
  }
  public static String getColor(String flag) {
    if (flag.equals("+")) {
      return "§a";
    }
    if (flag.startsWith("+")) {
      return "§2";
    }
    if (flag.equals("-")) {
      return "§4";
    }
    if (flag.startsWith("-")) {
      return "§c";
    }
    return "§b";
  }
}

/* Location:           D:\GitHub\ProtectionCore.jar
 * Qualified Name:     gyurix.protectioncore.Flags
 * JD-Core Version:    0.6.2
 */