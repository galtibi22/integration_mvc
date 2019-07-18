package smartspace.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import smartspace.AppProperties;
import smartspace.dao.ExtendedActionDao;
import smartspace.dao.ExtendedElementDao;
import smartspace.dao.ExtendedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.layout.ActionBoundary;
import smartspace.layout.BoundaryKey;
import smartspace.layout.UserKey;
import smartspace.logic.ActionService;
import smartspace.logic.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties={"spring.profiles.active=default"})
public class ActionRestControllerTests {
	private static final String SMARTSPACE_ADMIN_ACTIONS = "/smartspace/admin/actions";
	private static final String SMARTSPACE_ACTIONS = "/smartspace/actions";
	private static final String HTTP_LOCALHOST = "http://localhost:";
	private String baseUrlAdmin;
	private String baseUrl;
	private int port;
	private ExtendedActionDao actionDao;
	private ExtendedUserDao<String> userDao;
	private ExtendedElementDao<String> elementDao;
	private ActionService actionService;
	private RestTemplate restTemplate;
	private AppProperties appProperties;
	private UserEntity admin;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@Autowired
	public void setActionDao(ExtendedActionDao actionDao) {
		this.actionDao = actionDao;
	}
	
	@Autowired
	public void setUserDao(ExtendedUserDao userDao) {
		this.userDao = userDao;
	}
	
	@Autowired
	public void setElementDao(ExtendedElementDao<String> elementDao) {
		this.elementDao = elementDao;
	}
	
	@Autowired
	public void setAppProperties(AppProperties appProperties) {
		this.appProperties = appProperties;
	}
	
	@Autowired
	public void setActionService(ActionService actionService) {
		this.actionService = actionService;
	}
	
	@PostConstruct
	public void init() {
		this.baseUrlAdmin = HTTP_LOCALHOST + port + SMARTSPACE_ADMIN_ACTIONS;
		this.baseUrl = HTTP_LOCALHOST + port + SMARTSPACE_ACTIONS;
		this.restTemplate = new RestTemplate();
	}
	
	@Before
	public void beforeAddAdmin() {
		this.admin = UsersCreator.AddAdmin(this.userDao, appProperties);
	}
	
	@After
	public void tearDown() {
		this.actionDao
			.deleteAll();
	}
	
	private String getBaseUrlWithAdminUser() {
		return baseUrlAdmin + "/" + admin.getUserSmartSpace() + "/" + admin.getUserEmail();
	}
	
	@Test
	public void testCreateAction() throws Exception{
		ActionBoundary newAction = new ActionBoundary();
		newAction.setActionKey(new BoundaryKey("1", "2019B.cvb"));
		newAction.setCreated(new Date());
		newAction.setPlayer(new UserKey("2019B.cvb", "test@test.com"));
		newAction.setElement(new BoundaryKey("34", "2019B.cvb"));
		newAction.setProperties(new HashMap<String, Object>());
		newAction.setType("CREATE_TEST");
		
		ActionBoundary[] actualResult = this.restTemplate
				.postForObject(getBaseUrlWithAdminUser(), 
						new ActionBoundary[]{newAction},
						ActionBoundary[].class);
		
		String key = actualResult[0].getActionKey().getId();
		
		assertThat(this.actionDao.readAll())
			.extracting("key")
			.containsExactly(key);
	}
	
	@Test
	public void testGetAllActionsUsingPagination() throws Exception {
		// GIVEN the database contains 10 actions
		int totalActions = 10;
		
		Collection<ActionEntity> entities =
			IntStream.range(0, totalActions)
			.mapToObj(i -> new ActionEntity(
					"a", 
					"a", 
					new Date(), 
					String.valueOf(i), 
					"a", 
					"email" + i + "@gmail.com", 
					"TYPE_" + i, 
					new HashMap<>()))
			.collect(Collectors.toList());
		
		entities = this.actionService.store(admin.getUserSmartSpace(), admin.getUserEmail(), entities);
		
		List<ActionBoundary> allActions = 
				entities
				.stream()
				.map(ActionBoundary::new)
				.collect(Collectors.toList());
		
		List<ActionBoundary> expected = 
				allActions
				.stream()
				.skip(4)
				.limit(5)
				.collect(Collectors.toList());
		
		// WHEN I get all actions using page 1 and size 5
		int page = 1;
		int size = 5;
		ActionBoundary[] result = this.restTemplate
				.getForObject(this.getBaseUrlWithAdminUser() + 
						"?size={size}&page={page}", 
						ActionBoundary[].class, 
						size, 
						page);
		
		// THEN the result contains 5 actions of the actions inserted to the database
		assertThat(result)
			.usingElementComparatorOnFields("actionKey")
			.containsAnyElementsOf(expected);
	}
	
	@Test(expected=Exception.class)
	public void testGetActionsWithInvalidAdmin() throws Exception {
		ActionBoundary newAction = new ActionBoundary();
		newAction.setActionKey(new BoundaryKey("1", "2019B.cvb"));
		newAction.setCreated(new Date());
		newAction.setPlayer(new UserKey("2019B.cvb", "test@test.com"));
		newAction.setElement(new BoundaryKey("34", "2019B.cvb"));
		newAction.setProperties(new HashMap<String, Object>());
		newAction.setType("CREATE_TEST");
		
		ActionBoundary[] actualResult = this.restTemplate
				.postForObject(this.baseUrlAdmin + "/user/smartspace", 
						new ActionBoundary[]{newAction},
						ActionBoundary[].class);
	}
	
	@Test(expected=Exception.class)
	public void testGetActionsWithInvalidSmartSpace() throws Exception {
		ActionBoundary newAction = new ActionBoundary();
		newAction.setActionKey(new BoundaryKey("1", "2019B.cvb"));
		newAction.setCreated(new Date());
		newAction.setPlayer(new UserKey("2019B.cvb", "test@test.com"));
		newAction.setElement(new BoundaryKey("34", "2019B.cvb"));
		newAction.setProperties(new HashMap<String, Object>());
		newAction.setType("CREATE_TEST");
		
		ActionBoundary[] actualResult = this.restTemplate
				.postForObject(this.baseUrlAdmin + "/" + this.admin.getUserEmail() + "/smartspace", 
						new ActionBoundary[]{newAction},
						ActionBoundary[].class);
	}
	
	@Test
	public void testInvokeEchoAction() throws Exception {
		// GIVEN user and element exist in the database
		UserEntity user = UserElementCreator.AddUserRolePlayer(this.userDao, this.appProperties);
		ElementEntity element = UserElementCreator.AddElement(this.elementDao, this.appProperties, "Test", "Test");
		
		// WHEN the user player want to use Echo action
		ActionBoundary newAction = new ActionBoundary();
		newAction.setPlayer(new UserKey(user.getUserSmartSpace(), user.getUserEmail()));
		newAction.setElement(new BoundaryKey(element.getKey().substring(0, element.getKey().indexOf(ElementEntity.KEY_DELIM)), element.getElementSmartspace()));
		newAction.setProperties(new HashMap<String, Object>());
		newAction.setType("echo");
		
		ActionBoundary actualResult = this.restTemplate
				.postForObject(this.baseUrl, 
						newAction,
						ActionBoundary.class);
		
		// THEN the action get created in the db
		ActionBoundary[] expected = this.actionDao
				.readAll()
				.stream()
				.map(ActionBoundary::new)
				.collect(Collectors.toList()).
				toArray(new ActionBoundary[0]);
		
		assertThat(expected)
			.usingElementComparatorOnFields("actionKey")
			.contains(actualResult);
	}
	
	@Test(expected=Exception.class)
	public void testInvokeEchoActionWithUserThatNotExist() throws Exception {
		// GIVEN element exist in the database and user not existed
		ElementEntity element = UserElementCreator.AddElement(this.elementDao, this.appProperties, "Test", "Test");
		
		// WHEN the user player want to use Echo action
		ActionBoundary newAction = new ActionBoundary();
		newAction.setPlayer(new UserKey(this.appProperties.getName(), "notexist@gmail.com"));
		newAction.setElement(new BoundaryKey(element.getKey().substring(0, element.getKey().indexOf(ElementEntity.KEY_DELIM)), element.getElementSmartspace()));
		newAction.setProperties(new HashMap<String, Object>());
		newAction.setType("echo");
		
		ActionBoundary actualResult = this.restTemplate
				.postForObject(this.baseUrl, 
						newAction,
						ActionBoundary.class);
	}
	
	@Test(expected=Exception.class)
	public void testInvokeEchoActionWithElementThatNotExist() throws Exception {
		// GIVEN user exist in the database and element not existed
		UserEntity user = UserElementCreator.AddUserRolePlayer(this.userDao, this.appProperties);
		
		// WHEN the user player want to use Echo action
		ActionBoundary newAction = new ActionBoundary();
		newAction.setPlayer(new UserKey(this.appProperties.getName(), "notexist@gmail.com"));
		newAction.setElement(new BoundaryKey("345", this.appProperties.getName()));
		newAction.setProperties(new HashMap<String, Object>());
		newAction.setType("echo");
		
		ActionBoundary actualResult = this.restTemplate
				.postForObject(this.baseUrl, 
						newAction,
						ActionBoundary.class);
	}
}
