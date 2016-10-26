/**
 * Describes possible actions 
 */
public enum Action {
  Look         (new String[] {"look", "see", "inspect"}),
  SilentListen (new String[] {}),
  Listen       (new String[] {"listen", "wait"}),
  Say          (new String[] {"say", "speak", "yell", "talk"}),
  Open         (new String[] {"open"}),
  GoUp         (new String[] {"go up", "ascend"}),
  GoDown       (new String[] {"go down", "descend"}),
  Go           (new String[] {"go", "goto", "go to", "enter", "walk", "run"}),
  Dance        (new String[] {"dance"}),
  Write        (new String[] {"write"}),
  Sit          (new String[] {"sit", "sit down"}),
  Swim         (new String[] {"swim"}),
  Stop         (new String[] {"stop"}),
  Report       (new String[] {"report", "problem"}),
  Help         (new String[] {"help", "?"}),
  Quit         (new String[] {"quit", "exit", "leave"});
  
  String[] invocations;
  Action(String[] invocations) {
    this.invocations = invocations;
  }
  
  public static Action getAction(String possibleAction) {
    possibleAction = possibleAction.toLowerCase();
    for (Action a : Action.values()) {
      for (String invocation : a.invocations) {
        if (possibleAction.equals(invocation)) {
          return a;
        }
      }
    }
    return null;
  }
  
}