package whizvox.forte.client.core;

import whizvox.forte.common.Logger;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class ClientLogger extends Logger {

    public ClientLogger() throws FileNotFoundException {
        super(System.out, "Forte CLIENT", new PrintStream(Files.getLogFile()));
    }

}
