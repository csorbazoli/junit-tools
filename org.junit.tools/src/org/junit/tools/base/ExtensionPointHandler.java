package org.junit.tools.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.junit.tools.generator.ITestClassGenerator;
import org.junit.tools.generator.ITestDataFactory;
import org.junit.tools.generator.TestClassGenerator;

/**
 * Handles all extension points of the junit-tools
 * 
 * @author JUnit-Tools-Team
 * 
 */
public class ExtensionPointHandler {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private static final String EP_GENERATOR = "org.junit.tools.generator";
    private static final String EP_GENERATOR_TD_FACTORY = "org.junit.tools.generator.factory.testdata";
    private static final String EP_PREFERENCES = "org.junit.tools.preferences";

    // generators
    private boolean customGeneratorsInitialized = false;
    private Vector<ITestClassGenerator> testClassGenerators = null;
    private Vector<ITestClassGenerator> customTestClassGenerators = null;

    // test-data factories
    private boolean testDataFactoriesInitialized = false;
    private List<ITestDataFactory> testDataFactories = null;

    // preference initializer
    private boolean preferenceInitializerInitialized = false;
    private Vector<AbstractPreferenceInitializer> preferenceInitializers;

    /**
     * Getter for the test-class-generators. Do not change the list of the
     * generators!
     * 
     * @return test-class-generator
     * @throws CoreException
     */
    public Vector<ITestClassGenerator> getTestClassGenerators()
	    throws CoreException {
	initCustomGenerators();

	if (customTestClassGenerators != null
		&& customTestClassGenerators.size() > 0) {
	    return customTestClassGenerators;
	}

	if (testClassGenerators == null) {
	    testClassGenerators = new Vector<ITestClassGenerator>();
	    testClassGenerators.add(new TestClassGenerator());
	}
	return testClassGenerators;
    }

    private void initCustomGenerators() {

	if (!customGeneratorsInitialized) {
	    customGeneratorsInitialized = true;
	    customTestClassGenerators = new Vector<ITestClassGenerator>();

	    IExtensionRegistry registry = Platform.getExtensionRegistry();
	    IExtensionPoint point = registry.getExtensionPoint(EP_GENERATOR);
	    if (point != null) {
		String elementName;

		for (IExtension extension : point.getExtensions()) {
		    for (IConfigurationElement configElement : extension
			    .getConfigurationElements()) {
			elementName = configElement.getName();
			Object generator;
			try {
			    generator = configElement
				    .createExecutableExtension("class");
			} catch (CoreException e) {
			    logger.warning("unable to initialize custom generator, element name: "
				    + elementName
				    + " message: "
				    + e.getMessage());
			    continue;
			}

			if (generator != null) {
			    try {
				if ("testclass_generator".equals(elementName)) {
				    customTestClassGenerators
					    .add((ITestClassGenerator) generator);
				}
			    } catch (ClassCastException e) {
				logger.warning("wrong custom generator class "
					+ elementName + " message: "
					+ e.getMessage());
				continue;
			    }
			}
		    }
		}
	    }
	}

    }

    public List<ITestDataFactory> getTestDataFactories() throws CoreException {
	if (!testDataFactoriesInitialized && testDataFactories == null) {
	    testDataFactories = new ArrayList<ITestDataFactory>();

	    IExtensionRegistry registry = Platform.getExtensionRegistry();
	    IExtensionPoint point = registry
		    .getExtensionPoint(EP_GENERATOR_TD_FACTORY);

	    if (point != null) {
		String elementName;

		for (IExtension extension : point.getExtensions()) {
		    for (IConfigurationElement configElement : extension
			    .getConfigurationElements()) {
			try {
			    elementName = configElement.getName();
			    Object testDataFactory = configElement
				    .createExecutableExtension("class");

			    if (testDataFactory != null) {
				if ("testdata_factory".equals(elementName)) {
				    testDataFactories
					    .add((ITestDataFactory) testDataFactory);
				}

			    }
			} catch (Exception ex) {
			    logger.warning("unable to load class of extension-point org.junit.tools.generator.factory.testdata"
				    + ex.getMessage());
			}
		    }
		}
	    }
	}

	return testDataFactories;
    }

    public Vector<AbstractPreferenceInitializer> getPreferenceInitializer() {
	if (!preferenceInitializerInitialized && preferenceInitializers == null) {
	    preferenceInitializers = new Vector<AbstractPreferenceInitializer>();

	    IExtensionRegistry registry = Platform.getExtensionRegistry();
	    IExtensionPoint point = registry.getExtensionPoint(EP_PREFERENCES);

	    if (point != null) {
		for (IExtension extension : point.getExtensions()) {
		    for (IConfigurationElement configElement : extension
			    .getConfigurationElements()) {
			AbstractPreferenceInitializer initializer;
			try {
			    initializer = (AbstractPreferenceInitializer) configElement
				    .createExecutableExtension("class");

			    if (initializer != null) {
				preferenceInitializers.add(initializer);
			    }
			} catch (CoreException e) {
			    Logger logger = Logger.getLogger(this.getClass()
				    .getName());
			    logger.warning("class for extension-point org.junit.tools.preferences could not be loaded! "
				    + e.getMessage());
			}
		    }
		}
	    }
	}

	return preferenceInitializers;
    }
}
