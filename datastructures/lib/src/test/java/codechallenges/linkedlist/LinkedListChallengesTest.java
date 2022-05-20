package codechallenges.linkedlist;

import codechallenges.linkedlist.LinkedListChallenges;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LinkedListChallengesTest {
    @Test
    void linkedListChallenge1Test() {
        LinkedListChallenges sut = new LinkedListChallenges();
        String testString = sut.challenge1();
        assertEquals("Challenge 1", testString);
    }
}
