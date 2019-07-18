package smartspace.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import smartspace.CommonTest;

public class ExtendedActionTests extends CommonTest {

	@Test
	public void testReadAllUsingPaginationAndSortingByKey () throws Exception{
		// GIVEN the database contains 6 actions
		List<ActionEntity> newActions = createActions(6, new Date());
		
		// WHEN we read up to 5 actions from the beginning sorted by key 
		List<ActionEntity> actual = this.actionDao.readAll(5, 0, "key");
		
		// THEN we receive the first 5 actions
		assertThat(actual)
			.usingElementComparatorOnFields("key")
			.containsExactlyElementsOf(
					newActions
					.stream()
					.sorted((m1,m2)->m1.getKey().compareTo(m2.getKey()))
					.limit(5)
					.collect(Collectors.toList()));
	}
	
	@Test
	public void testReadAllUsingPagination () throws Exception{
		// GIVEN the database contains 6 actions
		createActions(6, new Date());
		
		// WHEN we read up to 5 actions from the beginning 
		List<ActionEntity> actual = this.actionDao.readAll(5, 0);		
		
		// THEN we receive 5 actions exactly
		assertThat(actual)
			.hasSize(5);
	}

	@Test
	public void testReadAllUsingPaginationFromAPage () throws Exception{
		// GIVEN the database contains 6 actions
		createActions(6, new Date());
		
		// WHEN we read up to 5 actions from the from page 1 
		List<ActionEntity> actual = 
				this.actionDao.readAll(5, 1);
		
		// THEN we receive 1 actions exactly
		assertThat(actual)
			.hasSize(1);
	}

	@Test
	public void testReadAllUsingPaginationFromNonExistingPage () throws Exception{
		// GIVEN the database contains 6 actions
		createActions(6, new Date());
		
		// WHEN we read up to 5 actions from the from page 2 
		List<ActionEntity> actual = 
				this.actionDao.readAll(5, 2);
		
		// THEN we receive no actions
		assertThat(actual)
			.isEmpty();
	}
	
	@Test
	public void testReadElementsFromYesterday() {
		// GIVEN the database contains 6 actions from yesterday
		// AND the database contains 60 actions from now
		Date yesterday = new Date(System.currentTimeMillis() - 1000*3600*24);
		createActions(6, yesterday);
		createActions(60, new Date());
		
		// WHEN we read up to 10 actions 
		//    created between two days ago and one hour ago
		Date twoDaysAgo = new Date(System.currentTimeMillis() - 1000*3600*48);
		Date oneHourAgo = new Date(System.currentTimeMillis() - 1000*3600);
		
		List<ActionEntity> actual 
			= this.actionDao.getActionsWithTimestampRange(
					twoDaysAgo, oneHourAgo, 10, 0);
		
		// THEN we receive exactly 6 actions
		assertThat(actual)
			.hasSize(6);
	}
	
	private List<ActionEntity> createActions(int size, Date date) {
		UserEntity user = getUserEntity();
		ElementEntity element = getElementEntity(user);
		
		return 
		IntStream.range(1, size + 1)
			.mapToObj(i->this.entityFactory.createNewAction(
					element.getKey(),
					element.getElementSmartspace(),
					"Create",
					date,
					user.getUserEmail(),
					user.getUserSmartSpace(),
					new HashMap<>()))
			.map(this.actionDao::create)
			.collect(Collectors.toList());	
	}
	
	private UserEntity getUserEntity() {
		return this.userDao.create(this.entityFactory.createNewUser("test@tester.com", "2019b.oriw",
				"Testo", "test.png", UserRole.PLAYER, 400));
	}
	
	private ElementEntity getElementEntity(UserEntity user) {
		return this.elementDao.create(this.entityFactory.createNewElement("Test", "Course",
				new Location(40, 30),new Date(), user.getUserEmail(), user.getUserSmartSpace(),
				false, new HashMap<>()));
	}
	
}
