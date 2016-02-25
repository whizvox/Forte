package whizvox.forte.server;

import whizvox.forte.common.Application;
import whizvox.forte.common.Props;

public class ForteServer extends Application {

    @Override
    protected void init(Props parameters) {

    }

    @Override
    public void run() {

    }

    private static ForteServer instance;

    public static void main(String[] args) {
        (instance = new ForteServer()).begin(args);
    }

}
