package smartspace.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import smartspace.CommonTest;

public class ExtendedUserTests extends CommonTest {
	
	@Test
	public void testReadAllUsingPaginationAndSortingByKey () throws Exception{
		// GIVEN the database contains 6 users
		List<UserEntity> newUsers = createUsers("tester1@test.com", "tester2@test.com", "tester3@test.com",
				"tester4@test.com", "tester5@test.com", "tester6@test.com");
		
		// WHEN we read up to 5 users from the beginning sorted by key 
		List<UserEntity> actual = this.userDao.readAll(5, 0, "key");
		
		// THEN we receive the first 5 users
		assertThat(actual)
			.usingElementComparatorOnFields("key")
			.containsExactlyElementsOf(
					newUsers
					.stream()
					.sorted((m1,m2)->m1.getKey().compareTo(m2.getKey()))
					.limit(5)
					.collect(Collectors.toList()));
	}
	

	@Test
	public void testReadAllUsingPagination () throws Exception{
		// GIVEN the database contains 6 users
		createUsers("tester1@test.com", "tester2@test.com", "tester3@test.com",
				"tester4@test.com", "tester5@test.com", "tester6@test.com");
		
		// WHEN we read up to 5 users from the beginning 
		List<UserEntity> actual = this.userDao.readAll(5, 0);		
		
		// THEN we receive 5 users exactly
		assertThat(actual)
			.hasSize(5);
	}

	// The test has 7 DB operations
	@Test(timeout=7000)
	public void testReadAllUsingPaginationFromAPage () throws Exception{
		// GIVEN the database contains 6 users
		createUsers("tester1@test.com", "tester2@test.com", "tester3@test.com",
				"tester4@test.com", "tester5@test.com", "tester6@test.com");
		
		// WHEN we read up to 5 users from the from page 1 
		List<UserEntity> actual = 
				this.userDao.readAll(5, 1);
		
		// THEN we receive 1 users exactly
		assertThat(actual)
			.hasSize(1);
	}

	@Test
	public void testReadAllUsingPaginationFromNonExistingPage () throws Exception{
		// GIVEN the database contains 6 users
		createUsers("tester1@test.com", "tester2@test.com", "tester3@test.com",
				"tester4@test.com", "tester5@test.com", "tester6@test.com");
		
		// WHEN we read up to 5 users from the from page 2 
		List<UserEntity> actual = 
				this.userDao.readAll(5, 2);
		
		// THEN we receive no users
		assertThat(actual)
			.isEmpty();
	}

	private List<UserEntity> createUsers(String... userEmail) {
		return 
		Stream.of(userEmail)
			.map(email->this.entityFactory.createNewUser(
					email,
					appProperties.getName(),
					"userName",
					"avatar.png",
					UserRole.PLAYER,
					0l))
			.map(this.userDao::create)
			.collect(Collectors.toList());	
	}

}
