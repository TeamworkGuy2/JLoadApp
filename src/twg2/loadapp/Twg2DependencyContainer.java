package twg2.loadapp;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Twg2DependencyContainer implements DependencyContainer {
/** Associates types (abstract/interfaces) with concrete implementations (classes) */
final Map<Class<?>, Class<?>> types = new HashMap<>();
/** Associates types with dynamic custom resolvers */
final Map<Class<?>, Function<DependencyContainer, ? extends Object>> typeCreators = new HashMap<>();
/** Associates types with singleton/instance/static values instead of types to create instances of */
final Map<Class<?>, Object> typeSingletons = new HashMap<>();
/** Cache that maps a type (interface, not a concrete implementation) to the concrete implementation's first constructor and params */
final Map<Class<?>, Map.Entry<Constructor<?>, Parameter[]>> typeConstructorAndParamsCache = new HashMap<>();

	@Override
	public <TYPE, IMPL> void register(Class<TYPE> abstractType, Class<IMPL> concreteType) {
		types.put(abstractType, concreteType);
	}


	@Override
	public <TYPE, IMPL> void registerResolver(Class<TYPE> abstractType, Function<DependencyContainer, IMPL> resolver) {
		typeCreators.put(abstractType, resolver);
	}


	@Override
	public <TYPE, IMPL> void registerValue(Class<TYPE> abstractType, IMPL value) {
		typeSingletons.put(abstractType, value);
	}


	@Override
	public <T> T resolve(Class<T> type) {
		return resolve(type);
	}

}