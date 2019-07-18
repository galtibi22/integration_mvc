package smartspace.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;
import smartspace.CommonTest;

public class ElementTests extends CommonTest {

	
	// The test has 2 DB operations
	@Test(timeout=2000)
	public void newElementCreatedInDb() throws Exception {
		// GIVEN the database is clean

		// WHEN I insert new element to the database
		ElementEntity elementTest = entityGenerator.elementEntity();
		elementDao.create(elementTest);
		// THEN the database contains the element
		assertThat(this.elementDao.readAll()).usingElementComparatorOnFields("elementId").contains(elementTest);
	}
	
	@Test
	public void readById() throws Exception {
		// GIVEN the database is clean

		// WHEN I insert new element to the database
		ElementEntity elementTest = entityGenerator.elementEntity();
		ElementEntity created = this.elementDao.create(elementTest);

		// THEN I can readById it
		assertThat(this.elementDao.readById(created.getKey())).isPresent();
	}
	
	@Test(expected=AssertionError.class)
	public void faildReadById() throws Exception {
		// GIVEN the database is clean

		// WHEN I insert new element to the database
		ElementEntity elementTest =entityGenerator.elementEntity();
		this.elementDao.create(elementTest);

		// THEN no element gets read when I readById it with a wrong key
		assertThat(this.elementDao.readById("notTheKey")).isPresent();
	}
	
	// The test has 3 DB operations
	@Test(timeout=3000)
	public void updateElement() throws Exception {
		// GIVEN an element in the database
		ElementEntity created =entityGenerator.elementEntity();
		elementDao.create(created);
		// WHEN I change the name attribute and update in the database
		created.setName("newName");
		this.elementDao.update(created);

		// THEN when the element's name updated in the database
		assertThat(this.elementDao.readAll().get(0).getName().equals(created.getName()));
	}
	
	// The test has 3 DB operations
	@Test(timeout=3000)
	public void updateElementWithPatching() throws Exception {
		// GIVEN an element in the database
		ElementEntity created =entityGenerator.elementEntity();
		elementDao.create(created);
		// WHEN I change the name and type attributes and set all other attributes to null and update in the database
		// CommonValidator prevent entityFactory to create new instance with null values so Changed all manually.
		created.setName("newName");
		created.setCreationTimeStamp(null);
		created.setCreatorEmail(null);
		created.setCreatorSmartSpace(null);
		created.setExpired(null);
		created.setLocation(null);
		created.setType("newType");
		this.elementDao.update(created);

		ElementEntity firstElement = this.elementDao.readAll().get(0);
		// THEN when the element's name updated in the database
		assertThat(firstElement.getName().equals(created.getName()));
		assertThat(firstElement.getType().equals(created.getType()));
		assertThat(firstElement.getExpired().equals(created.getExpired()));
		assertThat(firstElement.getCreationTimeStamp().equals(created.getCreationTimeStamp()));
		assertThat(firstElement.getCreatorEmail().equals(created.getCreatorEmail()));
		assertThat(firstElement.getCreatorSmartSpace().equals(created.getCreatorSmartSpace()));
		assertThat(firstElement.getElementSmartspace().equals(created.getElementSmartspace()));
	}	
}
