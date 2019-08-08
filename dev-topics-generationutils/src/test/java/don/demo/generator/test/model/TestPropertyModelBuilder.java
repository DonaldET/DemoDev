package don.demo.generator.test.model;

import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import don.demo.generator.model.PropertyModelBuilder;
import don.demo.generator.model.PropertyModelLoader;

/**
 * Tests a context (model) builder using Java properties files
 * 
 * @author Donald Trummell
 *
 *         Copyright (c) 2016. Donald Trummell. All Rights Reserved. Permission
 *         to use, copy, modify, and distribute this software and its
 *         documentation for educational, research, and not-for-profit purposes,
 *         without fee and without a signed licensing agreement, is hereby
 *         granted, provided that the above copyright notice, and this
 *         paragraph, appear in all copies, modifications, and distributions.
 *         Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
public class TestPropertyModelBuilder {
	private static final String SPRING_TEST_APP_CONTEXT = "/META-INF/main/spring/test-app-context.xml";

	private static boolean[] initialized = new boolean[] { false };

	private static ApplicationContext ctx = null;

	private PropertyModelBuilder modelBuilder = null;
	private PropertyModelLoader modelLoader = null;

	@Before
	public void setUp() throws Exception {
		synchronized (initialized) {
			if (!initialized[0]) {
				//
				// Initialize
				final ApplicationContext ctxT = new ClassPathXmlApplicationContext(SPRING_TEST_APP_CONTEXT);
				Assert.assertNotNull(SPRING_TEST_APP_CONTEXT + " null", ctxT);

				ctx = ctxT;

				initialized[0] = true;
			}
		}

		modelBuilder = (PropertyModelBuilder) ctx.getBean("modelbuilder");
		modelLoader = (PropertyModelLoader) ctx.getBean("modelloader");
	}

	private String version = null;
	private String testVersion = null;

	@After
	public void tearDown() throws Exception {
		modelBuilder = null;
		modelLoader = null;
	}

	@Test
	public void testAllNeededBeansPresent() {
		String name = "version";
		version = (String) ctx.getBean(name);
		Assert.assertEquals("bean " + name + " differs", "1.0.0-SNAPSHOT", version);

		name = "version.test";
		testVersion = (String) ctx.getBean(name);
		Assert.assertEquals("bean " + name + " differs", "TEST-1.0.0-SNAPSHOT", testVersion);

		Assert.assertNotNull("modelLoader null", modelLoader);
	}

	@Test
	public void testEmptyPropertiesBuilder() {
		modelBuilder.clear();
		Assert.assertEquals("new builder not empty", 0, modelBuilder.build().size());
	}

	@Test
	public void testModelAugment() {
		modelBuilder.clear();
		Assert.assertEquals("new builder not empty", 0, modelBuilder.build().size());
		final Properties aug = new Properties();
		aug.setProperty("new key", "new value");
		final PropertyModelBuilder augmented = modelBuilder.augment(aug);
		Assert.assertTrue("simple augment failed", aug.equals(augmented.build()));
	}

	@Test
	public void testModelAugmentOverride() {
		modelBuilder.clear();
		Assert.assertEquals("new builder not empty", 0, modelBuilder.build().size());
		final Properties aug = new Properties();
		aug.setProperty("new key", "new value");
		aug.setProperty("override key", "old value");
		Assert.assertEquals("simple augment failed", aug, modelBuilder.augment(aug).build());

		final Properties augOvr = new Properties();
		augOvr.setProperty("override key", "new value");
		augOvr.setProperty("added key", "added value");

		final Properties augExp = new Properties();
		augExp.setProperty("new key", "new value");
		augExp.setProperty("override key", "new value");
		augExp.setProperty("added key", "added value");
		Assert.assertEquals("override augment failed", augExp, modelBuilder.augment(augOvr).build());
	}

	/**
	 * An integration test combining reading and sequentially augmenting models
	 */
	@Test
	public void testLoadFlatPropertiesFile() {
		final String testSpec = "classpath:model_read_tests/MyTestAppModel.properties";
		final Properties model = modelLoader.loadProperties(testSpec);
		Assert.assertNotNull("model" + testSpec + " null", model);

		final String testSpecOver = "classpath:model_read_tests/MyTestAppOverride.properties";
		final Properties modelOver = modelLoader.loadProperties(testSpecOver);
		Assert.assertNotNull("model" + testSpecOver + " null", modelOver);

		modelBuilder.clear();
		final Properties finished = modelBuilder.augment(model).augment(modelOver).build();

		final Properties modelExp = new Properties();
		modelExp.setProperty("app_title", "My Glorious Application");
		modelExp.setProperty("an_object_id", "1250 Plymouth Ave.");
		modelExp.setProperty("an_object_value", "Balboa Park Station");
		modelExp.setProperty("override_me", "overridden");
		modelExp.setProperty("override_specific", "by_override");
		Assert.assertEquals("file based override augment failed", modelExp, finished);
	}
}
