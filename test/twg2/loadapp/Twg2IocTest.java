package twg2.loadapp;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class Twg2IocTest {

	@Test
	public void registerAndResolve1() {
		Twg2Ioc.register(List.class, ArrayList.class);
		Twg2Ioc.registerResolver(SecurityManager.class, (ioc) -> System.getSecurityManager());

		TestClass1 inst;

		try {
			inst = Twg2Ioc.resolve(TestClass1.class);
			Assert.fail("Expected error resolving Double");
		} catch (Exception err) {
			Assert.assertTrue(err != null);
		}

		Twg2Ioc.registerValue(Number.class, Double.valueOf(0));

		inst = Twg2Ioc.resolve(TestClass1.class);
		inst = Twg2Ioc.resolve(TestClass1.class);

		Assert.assertEquals(ArrayList.class, inst.strs.getClass());
		Assert.assertEquals(System.getSecurityManager(), inst.sub.securer);
		Assert.assertEquals(Double.class, inst.sub.id.getClass());
		Assert.assertEquals(0.0, inst.sub.id);
	}


	public static class TestClass1 {
		List<String> strs;
		SecurityClass sub;

		public TestClass1(List<String> strs, SecurityClass sub) {
			this.strs = strs;
			this.sub = sub;
		}
	}


	public static class SecurityClass {
		private SecurityManager securer;
		private Number id;
		
		public SecurityClass(Number id, SecurityManager securer) {
			this.securer = securer;
			this.id = id;
		}
	}

}
