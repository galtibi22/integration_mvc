package smartspace.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import smartspace.AppProperties;
import smartspace.dao.ExtendedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.layout.UserBoundary;
import smartspace.layout.UserKey;
import smartspace.logic.UserService;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties={"spring.profiles.active=default"})
public class UserRestControllerTests extends ControllerCommonTest{
	private String baseUrl;
	
	private int port;
	
	private ExtendedUserDao<String> userDao;
	
	private UserService userService;
	
	private RestTemplate restTemplate;
	
	private AppProperties appProperties;
	
	private UserEntity admin;
	
	@Autowired
	public void setUserDao(ExtendedUserDao<String> userDao) {
		this.userDao = userDao;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setAppProperties(AppProperties appProperties) {
		this.appProperties = appProperties;
	}
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/users";
		this.restTemplate = new RestTemplate();
	}
	
	@Before
	public void beforeAddAdmin() {
		this.admin = UsersCreator.AddAdmin(this.userDao, appProperties);
	}
	
	@After
	public void tearDown() {
		this.userDao
			.deleteAll();
	}
	
	private String getBaseUrlWithAdminUser() {
		return baseUrl + "/" + admin.getUserSmartSpace() + "/" + admin.getUserEmail();
	}
	
	@Test
	public void testCreateUser() throws Exception{
		UserBoundary newUserBoundary = new UserBoundary();
		newUserBoundary.setAvatar("avatar.png");
		newUserBoundary.setRole(UserRole.PLAYER.name());
		newUserBoundary.setUsername("userName");
		newUserBoundary.setKey(new UserKey("a", "email@gmail.com"));
		
		UserBoundary[] actualResult = this.restTemplate
			.postForObject(
					getBaseUrlWithAdminUser(),
					new UserBoundary[]{newUserBoundary},
					UserBoundary[].class);
		
		String key = actualResult[0].getKey().getSmartspace() + UserEntity.KEY_DELIM + actualResult[0].getKey().getEmail();
		
		assertThat(this.userDao.readById(key))
			.isPresent()
			.get()
			.extracting("key", "userName", "role")
			.containsExactly(key, actualResult[0].getUsername(), UserRole.valueOf(actualResult[0].getRole()));
	}
	
	@Test
	public void testGetAllUsingPagination() throws Exception{
		int totalSize = 10;
		
		Collection<UserEntity> entities = IntStream
				.range(0, totalSize)
				.mapToObj(i->new UserEntity("a", 
						"email" + i + "@gmail.com", 
						"user" + i, 
						"avatar" + i + ".png", 
						UserRole.MANAGER, 0))
				.collect(Collectors.toList());
		
		entities = this.userService.store(admin.getUserSmartSpace(), admin.getUserEmail(), entities);
		
		List<UserBoundary> allUsers =
			entities.stream()
			.map(UserBoundary::new)
			.collect(Collectors.toList());
		
		List<UserBoundary> expected =
				allUsers
				.stream()
				.skip(4)
				.limit(5)
				.collect(Collectors.toList());
		
		int page = 1;
		int size = 5;
		UserBoundary[] result = 
		  this.restTemplate
			.getForObject(
					this.getBaseUrlWithAdminUser() + "?size={size}&page={page}", 
					UserBoundary[].class, 
					size, page);
		
		
		assertThat(result)
			.usingElementComparatorOnFields("key")
			.containsExactlyElementsOf(expected);
	}
	
	@Test(expected=Exception.class)
	public void testGetUsersWithInvalidAdmin() throws Exception{
		UserBoundary newUserBoundary = new UserBoundary();
		newUserBoundary.setAvatar("avatar.png");
		newUserBoundary.setRole(UserRole.PLAYER.name());
		newUserBoundary.setUsername("userName");
		newUserBoundary.setKey(new UserKey("a", "email@gmail.com"));
		
		this.restTemplate
			.postForObject(
					this.baseUrl + "/user/smartspace/",
					new UserBoundary[]{newUserBoundary},
					UserBoundary[].class);
	}
	
	@Test(expected=Exception.class)
	public void testGetUsersWithInvalidSmartSpace() throws Exception{
		UserBoundary newUserBoundary = new UserBoundary();
		newUserBoundary.setAvatar("avatar.png");
		newUserBoundary.setRole(UserRole.PLAYER.name());
		newUserBoundary.setUsername("userName");
		newUserBoundary.setKey(new UserKey(appProperties.getName(), "email@gmail.com"));
		
		this.restTemplate
			.postForObject(
					getBaseUrlWithAdminUser(),
					new UserBoundary[]{newUserBoundary},
					UserBoundary[].class);
	}
	
	@Test(expected=Exception.class)
	public void testGetUsersWithManagerUser() throws Exception{
		UserBoundary newUserBoundary = new UserBoundary();
		newUserBoundary.setAvatar("avatar.png");
		newUserBoundary.setRole(UserRole.PLAYER.name());
		newUserBoundary.setUsername("userName");
		newUserBoundary.setKey(new UserKey("a", "email@gmail.com"));
		
		this.restTemplate
			.postForObject(
					getBaseUrlWithAdminUser(),
					new UserBoundary[]{newUserBoundary},
					UserBoundary[].class);
		
		this.restTemplate
		.postForObject(
				this.baseUrl + "/"  + newUserBoundary.getKey().getEmail() + "/" + newUserBoundary.getKey().getSmartspace(),
				new UserBoundary[]{newUserBoundary},
				UserBoundary[].class);
	}
}
