/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package flower;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test
    void testFlowerInitializes() {
        Flower sut = new Flower("Rose");
        assertEquals("Rose", sut.name);
    }

    @Test
    void testFlowerInitializesWithInput() {
        Flower sut = new Flower("Daisy", true, 7);
        assertEquals("Daisy", sut.name);
        assertTrue(sut.hasPetals);
        // test the getter for private property of lifespan
        assertEquals(7, sut.getLifespan());
    }

    @Test
    void testFlowerToString() {
        Flower sut = new Flower("Sweet Pea");

        String res = sut.toString();

        assertEquals("I'm a flower and my name is: Sweet Pea", res);
    }
}
