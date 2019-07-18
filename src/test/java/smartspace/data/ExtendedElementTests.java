package smartspace.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import smartspace.CommonTest;

public class ExtendedElementTests extends CommonTest {
	
	@Test
	public void testReadAllUsingPaginationAndSortingByKey () throws Exception{
		// GIVEN the database contains 6 elements
		List<ElementEntity> newElements = createElements(6, new Date());
		
		// WHEN we read up to 5 elements from the beginning sorted by key 
		List<ElementEntity> actual = this.elementDao.readAll(5, 0, "key");
		
		// THEN we receive the first 5 elements
		assertThat(actual)
			.usingElementComparatorOnFields("key")
			.containsExactlyElementsOf(
					newElements
					.stream()
					.sorted((m1,m2)->m1.getKey().compareTo(m2.getKey()))
					.limit(5)
					.collect(Collectors.toList()));
	}
	

	@Test
	public void testReadAllUsingPagination () throws Exception{
		// GIVEN the database contains 6 elements
		createElements(6, new Date());
		
		// WHEN we read up to 5 elements from the beginning 
		List<ElementEntity> actual = this.elementDao.readAll(5, 0);		
		
		// THEN we receive 5 elements exactly
		assertThat(actual)
			.hasSize(5);
	}

	// The test has 7 DB operations
	@Test(timeout=7000)
	public void testReadAllUsingPaginationFromAPage () throws Exception{
		// GIVEN the database contains 6 elements
		createElements(6, new Date());
		
		// WHEN we read up to 5 elements from the from page 1 
		List<ElementEntity> actual = 
				this.elementDao.readAll(5, 1);
		
		// THEN we receive 1 elements exactly
		assertThat(actual)
			.hasSize(1);
	}

	@Test
	public void testReadAllUsingPaginationFromNonExistingPage () throws Exception{
		// GIVEN the database contains 6 elements
		createElements(6, new Date());
		
		// WHEN we read up to 5 elements from the from page 2 
		List<ElementEntity> actual = 
				this.elementDao.readAll(5, 2);
		
		// THEN we receive no messages
		assertThat(actual)
			.isEmpty();
	}
	
	@Test
	public void testReadElementsFromYesterday() {
		// GIVEN the database contains 6 elements from yesterday
		// AND the database contains 60 elements from now
		Date yesterday = new Date(System.currentTimeMillis() - 1000*3600*24);
		createElements(6, yesterday);
		createElements(60, new Date());
		
		// WHEN we read up to 10 messages 
		//    created between two days ago and one hour ago
		Date twoDaysAgo = new Date(System.currentTimeMillis() - 1000*3600*48);
		Date oneHourAgo = new Date(System.currentTimeMillis() - 1000*3600);
		
		List<ElementEntity> actual 
			= this.elementDao.getElementsWithTimestampRange(
					twoDaysAgo, oneHourAgo, 10, 0);
		
		// THEN we receive exactly 6 elements
		assertThat(actual)
			.hasSize(6);
	}
	
	private List<ElementEntity> createElements(int size, Date date) {
		return
		IntStream.range(1, size + 1)
			.mapToObj(i->this.entityFactory.createNewElement(
					"name", "type", new Location(1,1), date,
					 "email@gmail.com", "2019b.oriw", false, new HashMap<>()))
			.map(this.elementDao::create)
			.collect(Collectors.toList());
	}

}
