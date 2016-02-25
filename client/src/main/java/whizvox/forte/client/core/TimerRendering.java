package whizvox.forte.client.core;

import whizvox.forte.common.Timer;

public class TimerRendering extends Timer {

    public TimerRendering() {
        super(ForteClient.getWindow().getMonitor().refreshRate);
    }

    @Override
    public void invoke() {

    }

}
