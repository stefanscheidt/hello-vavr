package hello.vavr;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Application {


    public static void main(final String[] args) throws Exception {

        System.out.println(Files.readAllLines(Paths.get(Application.class.getResource("/first.txt").toURI()), Charset.defaultCharset()));

    }

}
