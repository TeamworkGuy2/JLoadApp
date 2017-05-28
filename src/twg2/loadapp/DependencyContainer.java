package twg2.loadapp;

import java.util.function.Function;

public interface DependencyContainer {

	public <TYPE, IMPL> void register(Class<TYPE> abstractType, Class<IMPL> concreteType);


	public <TYPE, IMPL> void registerResolver(Class<TYPE> abstractType, Function<DependencyContainer, IMPL> resolver);


	public <TYPE, IMPL> void registerValue(Class<TYPE> abstractType, IMPL value);


	public <T> T resolve(Class<T> type);

}
