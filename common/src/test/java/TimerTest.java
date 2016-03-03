import whizvox.forte.common.Timer;

public class TimerTest {

    private static Timer loggingTimer1 = new Timer(1) {
        @Override
        public void loop() {
            System.out.println("Logging once a second");
        }
    };

    private static Timer loggingTimer2 = new Timer(2) {
        @Override
        public void loop() {
            System.out.println("Logging twice a second");
        }
    };

    public static void main(String[] args) {
        Timer.start(loggingTimer1);
        Timer.start(loggingTimer2);
    }

}
