package whizvox.forte.server;

import whizvox.forte.common.Application;

public class ForteServer extends Application {

    @Override
    protected void init(String[] progArgs) {

    }

    @Override
    public void run() {

    }

    private static ForteServer instance;

    public static void main(String[] args) {
        (instance = new ForteServer()).begin(args);
    }

}
