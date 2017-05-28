package twg2.loadapp;

import org.junit.Assert;
import org.junit.Test;

public class LocationsTest {

	@Test
	public void checkLocation() {
		Locations.setProgramMain(LocationsTest.class);
		Assert.assertEquals(LocationsTest.class.getName(), Locations.getProgramMainClassName());
	}


	private static void testLocations() {
		Locations.setProgramMain(LocationsTest.class);
		System.out.println("program file location: " + Locations.getProgramFileLocation());
		System.out.println("program main class name: " + Locations.getProgramMainClassName());
		System.out.println("program main class location: " + Locations.getRelativeClassPath(LocationsTest.class));
		System.out.println("program relative file location: " + Locations.getRelativeResourceFile());
	}

}
