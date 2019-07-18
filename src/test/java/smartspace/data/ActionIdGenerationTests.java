package smartspace.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import smartspace.CommonTest;

public class ActionIdGenerationTests extends CommonTest {

	@Test
	public void testActionIdAreUnique() throws Exception {
		// GIVEN element and user
		UserEntity user = entityFactory.createNewUser("test@tester.com", "2019b.oriw", "Testo", "test.png", UserRole.PLAYER, 400);
		ElementEntity element = entityFactory.createNewElement("Test", "Course", new Location(40, 30),new Date(),  user.getUserEmail(), user.getUserSmartSpace(), false, new HashMap<>());
		
		// WHEN we create 10 actions
		Set<String> keys = 
		IntStream.range(1, 11)
			.mapToObj(obj->entityFactory.createNewAction(
					element.getKey(), 
					element.getElementSmartspace(), 
					"Register", new Date(), 
					user.getUserEmail(), 
					user.getUserSmartSpace(), 
					new HashMap<>()))
			.map(actionDao::create)
			.map(ActionEntity::getKey)
			.collect(Collectors.toSet());
		
		// THEN action's id's are unique
		assertThat(keys)
			.hasSize(10);
	}
}
