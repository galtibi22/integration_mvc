package smartspace.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import org.junit.Test;
import smartspace.CommonTest;

public class ActionTests extends CommonTest {
	// The test has 5 DB operations
	@Test(timeout=5000)
	public void testCreateNewMessage() {
		// GIVEN element and user
		UserEntity user = this.entityFactory.createNewUser("test@tester.com", "2019b.oriw", "Testo", "test.png", UserRole.PLAYER, 400);
		ElementEntity element = this.entityFactory.createNewElement("Test", "Course", new Location(40, 30),new Date(), user.getUserEmail(), user.getUserSmartSpace(), false, new HashMap<>());
				
		// WHEN I insert new action to the database
		ActionEntity actionTest = entityFactory.createNewAction(
				element.getKey(),
				element.getElementSmartspace(),
				"Create",
				new Date(),
				user.getUserEmail(),
				user.getUserSmartSpace(),
				new HashMap<>());
		this.actionDao.create(actionTest);
		
		// THEN the database contains action with the text test
		assertThat(this.actionDao.readAll())
				.usingElementComparatorOnFields("actionId")
				.contains(actionTest);
	}
}
