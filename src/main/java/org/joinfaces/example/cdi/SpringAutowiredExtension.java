package org.joinfaces.example.cdi;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.literal.InjectLiteral;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.WithAnnotations;
import jakarta.enterprise.inject.spi.configurator.AnnotatedFieldConfigurator;
import jakarta.enterprise.inject.spi.configurator.AnnotatedMethodConfigurator;
import jakarta.enterprise.inject.spi.configurator.AnnotatedParameterConfigurator;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class SpringAutowiredExtension implements Extension {

	private final Map<Class<?>, String> autowiredFields = new ConcurrentHashMap<>();

	private static Object getBeanFromSpringContext(Class<?> type, String name) {
		try {
			try {//NOSONAR
				return SpringBeanPicker.getContext().getBean(type);
			} catch (NoUniqueBeanDefinitionException ignore) {
				return SpringBeanPicker.getContext().getBean(name);
			}
		} catch (Exception e) {
			throw new IllegalStateException("Cannot get bean from Spring context", e);
		}
	}

	private static void injectBeanViaSpringContext(AfterBeanDiscovery event, Class<?> type,
			String name) {
		event.addBean().addType(type)
				.createWith(ignoreCdiContext -> getBeanFromSpringContext(type, name));
	}

	public void afterBeanDiscovery(@Observes AfterBeanDiscovery event) {
		autowiredFields.forEach((key, value) -> injectBeanViaSpringContext(event, key, value));
	}

	@SuppressWarnings("java:S1172")
	public <T> void processAnnotatedType(
			@Observes @WithAnnotations(Named.class) ProcessAnnotatedType<T> processAnnotatedType,
			BeanManager beanManager) {
		Class<T> beanClass = processAnnotatedType.getAnnotatedType().getJavaClass();

		if (!beanClass.getPackage().getName().startsWith("org.joinfaces.example")) {
			LoggerFactory.getLogger(getClass()).info("ignored: bean:{} package:{}", beanClass.getSimpleName(), beanClass.getPackage().getName());
			return; // This should filter out any CDI managed beans provided by e.g. Faces and OmniFaces
			// themselves.
		}
		LoggerFactory.getLogger(getClass()).info("found: bean:{} package:{}", beanClass.getSimpleName(), beanClass.getPackage().getName());
		//Fields annotated with autowired
		processAnnotatedType.configureAnnotatedType()
				.filterFields(field -> field.isAnnotationPresent(Autowired.class))
				.forEach(this::registerAutowiredField);

		//Constructor-Parameters annotated with autowired
		processAnnotatedType.configureAnnotatedType()
				.filterConstructors(constructor -> constructor.isAnnotationPresent(Inject.class))
				.flatMap(constructor -> constructor.filterParams(
						param -> param.isAnnotationPresent(Autowired.class)))
				.forEach(this::registerAutowiredParam);

		//Setter annotated with autowired
		processAnnotatedType.configureAnnotatedType()
				.filterMethods(method -> method.isAnnotationPresent(Autowired.class))
				.map(AnnotatedMethodConfigurator::params).flatMap(List::stream)
				.forEach(this::registerAutowiredParam);

	}

	private void registerAutowiredField(AnnotatedFieldConfigurator<?> fieldConfigurator) {
		fieldConfigurator.add(InjectLiteral.INSTANCE);
		Field field = fieldConfigurator.getAnnotated().getJavaMember();
		autowiredFields.put(field.getType(), field.getName());
	}

	private <T> void registerAutowiredParam(
			AnnotatedParameterConfigurator<T> annotatedParameterConfigurator) {
		final Parameter parameter = annotatedParameterConfigurator.getAnnotated().getJavaParameter();
		autowiredFields.put(parameter.getType(), parameter.getName());
	}
}

