package don.demo.generator.test.model;

import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import don.demo.generator.model.PropertyModelLoader;

public class TestPropertyModelLoader {

    private static final String SPRING_TEST_APP_CONTEXT = "/META-INF/main/spring/test-app-context.xml";

    private static boolean[] initialized = new boolean[] { false };

    private static ApplicationContext ctx = null;

    private PropertyModelLoader modelLoader = null;

    @Before
    public void setUp() throws Exception {
        synchronized (initialized) {
            if (!initialized[0]) {
                //
                // Initialize
                final ApplicationContext ctxT = new ClassPathXmlApplicationContext(
                        SPRING_TEST_APP_CONTEXT);
                Assert.assertNotNull(SPRING_TEST_APP_CONTEXT + " null", ctxT);

                final String ver = (String) ctxT.getBean("version.test");
                Assert.assertNotNull(SPRING_TEST_APP_CONTEXT + " null", ver);

                ctx = ctxT;

                initialized[0] = true;
            }
        }

        modelLoader = (PropertyModelLoader) ctx.getBean("modelloader");
    }

    private String version = null;
    private String testVersion = null;

    @After
    public void tearDown() throws Exception {
        modelLoader = null;
    }

    @Test
    public void testAllNeededBeansPresent() {
        String name = "version";
        version = (String) ctx.getBean(name);
        Assert.assertEquals("bean " + name + " differs", "1.0.0-SNAPSHOT",
                version);

        name = "version.test";
        testVersion = (String) ctx.getBean(name);
        Assert.assertEquals("bean " + name + " differs", "TEST-1.0.0-SNAPSHOT",
                testVersion);

        Assert.assertNotNull("modelLoader null", modelLoader);
    }

    @Test
    public void testLoadFlatPropertiesFile() {
        final String testSpec = "classpath:model_read_tests/MyTestAppModel.properties";
        final Properties model = modelLoader.loadProperties(testSpec);
        Assert.assertNotNull("model" + testSpec + " null", model);

        final Properties expected = new Properties();
        expected.setProperty("app_title", "My Glorious Application");
        expected.setProperty("an_object_id", "1250 Plymouth Ave.");
        expected.setProperty("an_object_value", "Balboa Park Station");
        expected.setProperty("override_me", "basic");
        Assert.assertEquals("properties lengths differ", expected.size(),
                model.size());

        int n = 0;
        int m = 0;
        for (final Object key : model.keySet()) {
            final Object ve = expected.get(key);
            final Object vm = model.get(key);
            boolean ne = ve == null;
            if (ne) {
                ne = vm == null;
            } else {
                ne = ve.equals(vm);
            }

            if (!ne) {
                System.err.println("key[" + m + "] " + key
                        + " differs; expected: " + ve + ";  model: " + vm);
                n++;
            }

            m++;
        }
        Assert.assertEquals("model property entries differ from expected", 0, n);
        Assert.assertEquals("model inconsistency", model.size(), m);
    }
}
