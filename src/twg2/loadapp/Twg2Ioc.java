package twg2.loadapp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;

/** A very simple dependency injector (inversion of control container) to get dependency injection quickly setup without fuss or configuration.
 * NOTE: this DI/IOC does not manage instance lifecycle or caching, just instance initialization
 * @author TeamworkGuy2
 * @since 2017-05-28
 */
public class Twg2Ioc {
	static final Twg2DependencyContainer ioc = new Twg2DependencyContainer();

	public static <TYPE> void register(Class<TYPE> abstractType, Class<? extends TYPE> concreteType) {
		ioc.types.put(abstractType, concreteType);
	}


	public static <TYPE> void registerResolver(Class<TYPE> abstractType, Function<DependencyContainer, ? extends TYPE> resolver) {
		ioc.typeCreators.put(abstractType, resolver);
	}


	public static <TYPE, IMPL extends TYPE> void registerValue(Class<TYPE> abstractType, IMPL value) {
		ioc.typeSingletons.put(abstractType, value);
	}


	public static <T> T resolve(Class<T> type) {
		// check for a singleton/static value for the type
		Object singletonValue = ioc.typeSingletons.get(type);
		// handles 'null' being a valid value for a type
		if(singletonValue != null || ioc.typeSingletons.containsKey(type)) {
			@SuppressWarnings("unchecked")
			T inst = (T) singletonValue;
			return inst;
		}

		// check for a custom resolver
		Function<DependencyContainer, ? extends Object> resolver = ioc.typeCreators.get(type);
		if(resolver != null) {
			@SuppressWarnings("unchecked")
			T inst = (T) resolver.apply(ioc);
			return inst;
		}

		Constructor<?> constructor = null;
		Parameter[] params = null;
		Map.Entry<Constructor<?>, Parameter[]> cnpCached = ioc.typeConstructorAndParamsCache.get(type);

		// if the type is not cached, look up the constructor and parameters and cache them
		if(cnpCached == null) {
			Class<?> implementation = ioc.types.getOrDefault(type, type);
			constructor = implementation != null ? firstOrLeastParamsConstructorIfJavaType(implementation) : null;
			if(constructor == null) {
				throw new RuntimeException("No registered type/value for " + type + ", impl: " + implementation);
			}
			params = constructor.getParameters();
			ioc.typeConstructorAndParamsCache.put(type, new AbstractMap.SimpleImmutableEntry<>(constructor, params));
		}
		// use the cached value
		else {
			constructor = cnpCached.getKey();
			params = cnpCached.getValue();
		}

		// create arguments for the constructor
		Object[] args = new Object[params.length];
		for(int i = 0, size = params.length; i < size; i++) {
			try {
				args[i] = resolve(params[i].getType());
			} catch (Exception err) {
				throw new RuntimeException("No registered type/value for param '" + params[i] + "' of type " + constructor.getDeclaringClass(), err);
			}
		}

		// create the instance
		try {
			@SuppressWarnings("unchecked")
			T inst = (T) constructor.newInstance(args);
			return inst;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException err) {
			throw new RuntimeException("Error constructing type '" + type + "', args: " + toString(args), err);
		}
	}


	/** Pick the first constructor of a type or, for Java types, the constructor with the least parameters
	 * @param type lookup a constructor from this type
	 * @return a constructor for 'type' or null if no constructor found
	 */
	private static Constructor<?> firstOrLeastParamsConstructorIfJavaType(Class<?> type) {
		Constructor<?>[] ts = type.getConstructors();
		// If its a java.* or javax.* class with a first/default constructor with 1 or more parameters, then search for a constructor with 0 parameters
		if(isJavaType(type) && ts.length > 0 && ts[0].getParameterCount() > 0) {
			int leastParams = Integer.MAX_VALUE;
			int leastParamsIdx = -1;
			for(int i = 0, size = ts.length; i < size; i++) {
				int paramCount = ts[i].getParameterCount();
				if(paramCount < leastParams) {
					leastParams = paramCount;
					leastParamsIdx = i;
				}
			}
			return ts[leastParamsIdx];
		}
		// just use the first constructor
		else {
			return ts != null && ts.length > 0 ? ts[0] : null;
		}
	}


	private static boolean isJavaType(Class<?> type) {
		String name = type.getName();
		return name.startsWith("java.") || name.startsWith("javax.");
	}


	@SafeVarargs
	private static <T> String toString(T... ts) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(T t : ts) {
			if(!first) {
				sb.append(", ");
			}
			sb.append(t);
			first = false;
		}
		return sb.toString();
	}

}
