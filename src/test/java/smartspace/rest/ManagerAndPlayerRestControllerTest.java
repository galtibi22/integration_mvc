package smartspace.rest;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import smartspace.AppProperties;
import smartspace.dao.ExtendedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.layout.NewUserFormBoundary;
import smartspace.layout.UserBoundary;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties={"spring.profiles.active=default"})
public class ManagerAndPlayerRestControllerTest extends ControllerCommonTest {

	private String baseUrl;
	private int port;
	private ExtendedUserDao<String> userDao;
	private RestTemplate restTemplate;
	private AppProperties appProperties;
	
	@Autowired
	public void setUserDao(ExtendedUserDao<String> userDao) {
		this.userDao = userDao;
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
		this.baseUrl = "http://localhost:" + port + "/smartspace/users";
		this.restTemplate = new RestTemplate();
	}
	
	@Before
	public void setup() {
		this.userDao
			.deleteAll();
	}
	
	@After
	public void tearDown() {
		this.userDao
			.deleteAll();
	}
	
	private String getBaseUrl() {
		return this.baseUrl;
	}
	
	@Test
	public void testLogin() {
		// GIVEN a user with PLAYER role in the database
		UserEntity exists = UsersCreator.AddPlayer(userDao, appProperties);
		
		// WHEN a login request is sent
		UserBoundary result = this.restTemplate.getForObject(this.getBaseUrl() + "/login/" + exists.getUserSmartSpace()
			+ "/" + exists.getUserEmail(), UserBoundary.class);
		
		// THEN we get the user boundary
		UserBoundary expected = new UserBoundary(exists);
		
		assertThat(result).hasFieldOrPropertyWithValue("key", expected.getKey());
	}
	
	@Test(expected=Exception.class)
	public void testAdminLogin() {
		// GIVEN a user with PLAYER role in the database
		UserEntity exists = UsersCreator.AddAdmin(userDao, appProperties);
		
		// WHEN a login request is sent
		UserBoundary result = this.restTemplate.getForObject(this.getBaseUrl() + "/login/" + exists.getUserSmartSpace()
			+ "/" + exists.getUserEmail(), UserBoundary.class);
		
		// THEN we get the user boundary
		UserBoundary expected = new UserBoundary(exists);
		
		assertThat(result).hasFieldOrPropertyWithValue("key", expected.getKey());
	}
	
	@Test
	public void testCreateUser() {
		// GIVEN an empty database
		
		// WHEN a create user request is sent
		NewUserFormBoundary form = new NewUserFormBoundary("tester@test.com", UserRole.PLAYER.toString(), "newsername11", ":>");
		UserBoundary newUser = new UserBoundary(form.convertToEntity());
		UserBoundary result = this.restTemplate.postForObject(this.getBaseUrl(),
				form, UserBoundary.class);
		
		// THEN the user is created
		assertThat(result).hasFieldOrPropertyWithValue("key", newUser.getKey());
		
	}
	
	@Test
	public void testCreateUserAdmin() {
		// GIVEN an empty database
		
		// WHEN a create user request is sent
		NewUserFormBoundary form = new NewUserFormBoundary("tester@test.com", UserRole.ADMIN.toString(), "newsername11", ":>");
		UserBoundary newUser = new UserBoundary(form.convertToEntity());
		UserBoundary result = this.restTemplate.postForObject(this.getBaseUrl(),
				form, UserBoundary.class);
		
		// THEN the user is created
		assertThat(result).hasFieldOrPropertyWithValue("key", newUser.getKey());
		
	}
	
	@Test
	public void testUpdateUser() {
		// GIVEN a user with PLAYER role in the database
		UserEntity exists = UsersCreator.AddPlayer(userDao, appProperties);
		
		// WHEN we update his user name
		exists.setUserName("newusername");
		this.restTemplate.put(this.getBaseUrl() + "/login/" + exists.getUserSmartSpace() + "/" + exists.getUserEmail(),
				new UserBoundary(exists));
		
		// THEN he is updated
		assertThat(this.userDao.readById(exists.getKey()).get().getUserName()).isEqualTo("newusername");
	}
	
}
