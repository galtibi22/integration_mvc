package smartspace.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import smartspace.CommonTest;

//@DataJpaTest
public class UserTests extends CommonTest {

	private UserEntity testUser;
	@Before
	public void addTestUser() {
		testUser = entityFactory.createNewUser("email@gmail.com",
				appProperties.getName(),
				"userName",
				"avatar.png",
				UserRole.PLAYER,
				0l);
		testUser = userDao.create(testUser);
	}

	@Test
	public void addUser() {
		assertThat(testUser.getKey()).isEqualTo(testUser.getUserSmartSpace() + UserEntity.KEY_DELIM + testUser.getUserEmail());
	}
	
	@Test
	public void readById() {
		Optional<UserEntity> userRead = userDao.readById(testUser.getUserSmartSpace() + UserEntity.KEY_DELIM + testUser.getUserEmail());
		assert(userRead.isPresent());
	}
	
	@Test(timeout=7000)
	public void readByWrongId() {
		Optional<UserEntity> userRead = userDao.readById(testUser.getUserSmartSpace() + UserEntity.KEY_DELIM + testUser.getUserEmail());
		assert(userRead.isPresent());
	}
	
	@Test(timeout=2000)
	public void readAll() {
		UserEntity userToCreate2 = entityFactory.createNewUser("email2@gmail.com",
				appProperties.getName(),
				"userName2",
				"avatar2.png",
				UserRole.PLAYER,
				0);

		userToCreate2 = userDao.create(userToCreate2);
		
		List<UserEntity> users = userDao.readAll();
		
		assertThat(users.size()).isEqualTo(2);
		
		UserEntity firstUser = users.get(0);
		UserEntity secondUser = users.get(1);
		
		assertThat(users)
		.usingElementComparatorOnFields("key")
		.contains(userToCreate2);

		assertThat(users)
		.usingElementComparatorOnFields("key")
		.contains(testUser);
	}
	
	@Test
	public void updateSome() {
		testUser.setAvatar(null);
		testUser.setRole(UserRole.MANAGER);

		userDao.update(testUser);
		
		List<UserEntity> users = userDao.readAll();
		
		assertThat(users.size()).isEqualTo(1);
		
		// Checking that the avatar was not changed
		assertThat(users.get(0).getAvatar()).isEqualTo("avatar.png");
		assertThat(users.get(0).getRole()).isEqualTo(testUser.getRole());
		assertThat(users.get(0).getPoints()).isEqualTo(testUser.getPoints());
		assertThat(users.get(0).getUserEmail()).isEqualTo(testUser.getUserEmail());
		assertThat(users.get(0).getUserName()).isEqualTo(testUser.getUserName());
	}
	
	@Test
	public void updatePointsOnly() {
		testUser.setPoints(4);
		testUser.setAvatar(null);
		testUser.setUserEmail("email@gmail.com");
		testUser.setUserSmartSpace(appProperties.getName());
		testUser.setRole(null);

		userDao.update(testUser);
		
		List<UserEntity> users = userDao.readAll();
		
		assertThat(users.size()).isEqualTo(1);
		
		// Checking that the avatar was not changed
		assertThat(users.get(0).getAvatar()).isEqualTo("avatar.png");
		assertThat(users.get(0).getRole()).isEqualTo(UserRole.PLAYER);
		assertThat(users.get(0).getPoints()).isEqualTo(testUser.getPoints());
		assertThat(users.get(0).getUserEmail()).isEqualTo("email@gmail.com");
		assertThat(users.get(0).getUserName()).isEqualTo("userName");
	}
	
	@Test(timeout=2000)
	public void update() {
		testUser.setAvatar("Avatar2.png");
		testUser.setPoints(1);
		testUser.setRole(UserRole.MANAGER);
		testUser.setUserName("user");

		userDao.update(testUser);
		
		List<UserEntity> users = userDao.readAll();
		
		assertThat(users.size()).isEqualTo(1);
		
		checkUsersEquals(users.get(0), testUser);
	}
	
	private void checkUsersEquals(UserEntity user1, UserEntity user2) {
		assertThat(user1.getKey()).isEqualTo(user2.getUserSmartSpace() + UserEntity.KEY_DELIM + user2.getUserEmail());
		assertThat(user1.getUserEmail()).isEqualTo(user2.getUserEmail());
		assertThat(user1.getUserName()).isEqualTo(user2.getUserName());
		assertThat(user1.getAvatar()).isEqualTo(user2.getAvatar());
		assertThat(user1.getRole()).isEqualTo(user2.getRole());
		assertThat(user1.getPoints()).isEqualTo(user2.getPoints());
	}
}
