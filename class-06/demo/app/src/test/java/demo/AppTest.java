/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test void appHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }

    // CAN"T INSTANTIATE ABSTRACT OBJECTS ABSTRACT != new Abstract()  NO NO NO!!!
//    @Test
//    void canCreateNewArtistObj() {
//        // arrange
//        Artist newArtist = new Artist();
//        //  act
//
//        // assert
//        assertEquals("Zork Rockers", newArtist.name);
//    }

    @Test
    void isMusicianInstanceOfArtist() {
        Musician newMusician = new Musician("Rex", "swing", 9);
        boolean isIt = (newMusician instanceof Artist);
        assertTrue(isIt, "This is true!!");
    }
}
