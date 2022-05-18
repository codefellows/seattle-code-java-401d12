package alicesearcher;

import java.nio.file.Path;
import java.nio.file.Paths;

public class aliceSearcher {

        // whats a path?
        public static void main(String[] args) {
            // safety check
            System.out.println("My current directory is: " + System.getProperty("user.dir"));

            // PD: How many times does "Alice" show up in alice_in_wonderland.txt
            // first, we have to read(access) the file
            // read through it,
            // we can keep track of Alice instances

            Path alicePath = Paths.get("./alice_in_wonderland");
            System.out.println("Alice Path: " + alicePath.toAbsolutePath());
        }
}
