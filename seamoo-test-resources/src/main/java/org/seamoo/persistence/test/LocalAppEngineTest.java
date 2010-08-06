package org.seamoo.persistence.test;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * Performs datastore setup, as described <a
 * href="http://code.google.com/appengine/docs/java/howto/unittesting.html"
 * >here</a>.
 *
 * @author androns
 */
public class LocalAppEngineTest {

    private final LocalServiceTestHelper helper;

    public LocalAppEngineTest() {
        LocalDatastoreServiceTestConfig datastoreConfig = new LocalDatastoreServiceTestConfig();
        LocalMemcacheServiceTestConfig memcacheConfig = new LocalMemcacheServiceTestConfig();
        helper = new LocalServiceTestHelper(datastoreConfig, memcacheConfig);
    }

    /**
     *
     */
    @BeforeMethod
    public void setUp() {
        helper.setUp();
    }

    @AfterMethod
    public void tearDown() {
        helper.tearDown();
    }

    private static LocalAppEngineTest instance;

    public static void staticSetUp() {
        instance = new LocalAppEngineTest();
        instance.setUp();
    }

    public static void staticTearDown() {
        instance.tearDown();
        instance = null;
    }

}
