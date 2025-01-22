package ru.practicum;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StatsServerAppTest extends TestCase {

    public StatsServerAppTest(String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( StatsServerAppTest.class );
    }

    public void testApp() {
        assertTrue( true );
    }
}
