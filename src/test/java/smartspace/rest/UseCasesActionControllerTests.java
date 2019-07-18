package smartspace.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.stream.Collectors;

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
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.util.EntityFactory;
import smartspace.data.util.EntityFactoryImpl;
import smartspace.layout.ActionBoundary;
import smartspace.layout.BoundaryKey;
import smartspace.layout.UserKey;
import smartspace.logic.ActionService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties={"spring.profiles.active=default"})
public class UseCasesActionControllerTests {
	private static final String SMARTSPACE_ADMIN_ACTIONS = "/smartspace/admin/actions";
	private static final String SMARTSPACE_ACTIONS = "/smartspace/actions";
	private static final String HTTP_LOCALHOST = "http://localhost:";
	private String baseUrlAdmin;
	private String baseUrl;
	private int port;
	private ExtendedActionDao actionDao;
	private ExtendedUserDao<String> userDao;
	private ExtendedElementDao<String> elementDao;
	private EntityFactory entityFactory;
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
	
	@Autowired
	public void setEntityFactory(EntityFactory entityFactory) {
		this.entityFactory = entityFactory;
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
	public void testInvokeActionCheckin() throws Exception {
		// GIVEN player and a calculus course
		UserEntity user = UserElementCreator.AddUserRolePlayer(this.userDao, this.appProperties);
		ElementEntity element = UserElementCreator.AddElement(this.elementDao, this.appProperties, "Calculus", "Course");
		
		// WHEN the user player want to check in to Course
		ActionBoundary newAction = new ActionBoundary();
		newAction.setPlayer(new UserKey(user.getUserSmartSpace(), user.getUserEmail()));
		newAction.setElement(new BoundaryKey(element.getKey().substring(0, element.getKey().indexOf(ElementEntity.KEY_DELIM)), element.getElementSmartspace()));
		newAction.setProperties(new HashMap<String, Object>());
		newAction.setType("checkin");
		
		ActionBoundary actualResult = this.restTemplate
				.postForObject(this.baseUrl, 
						newAction,
						ActionBoundary.class);
		
		System.out.println(actualResult.toString());
		
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
	
	@Test
	public void testInvokeActionCheckout() throws Exception {
		// GIVEN player and a calculus course
		UserEntity user = UserElementCreator.AddUserRolePlayer(this.userDao, this.appProperties);
		ElementEntity element = UserElementCreator.AddElement(this.elementDao, this.appProperties, "Calculus", "Course");
		
		// WHEN the player want to check out from the course
		ActionBoundary newAction = new ActionBoundary();
		newAction.setPlayer(new UserKey(user.getUserSmartSpace(), user.getUserEmail()));
		newAction.setElement(new BoundaryKey(element.getKey().substring(0, element.getKey().indexOf(ElementEntity.KEY_DELIM)), element.getElementSmartspace()));
		newAction.setProperties(new HashMap<String, Object>());
		newAction.setType("checkout");
		
		ActionBoundary actualResult = this.restTemplate
				.postForObject(this.baseUrl, 
						newAction,
						ActionBoundary.class);
		
		System.out.println(actualResult.toString());
		
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
	
	@Test
	public void testInvokeActionGradeUpdate() throws Exception {
		// GIVEN player, teacher and a calculus course
		UserEntity student = UserElementCreator.AddUserRolePlayer(this.userDao, this.appProperties);
		UserEntity teacher = UserElementCreator.AddUserRolePlayer(this.userDao, this.appProperties);
		ElementEntity element = UserElementCreator.AddElement(this.elementDao, this.appProperties, "Calculus", "Course");
		
		ActionBoundary newAction = new ActionBoundary();
		newAction.setPlayer(new UserKey(student.getUserSmartSpace(),student.getUserEmail()));
		newAction.setElement(new BoundaryKey(element.getKey().substring(0, element.getKey().indexOf(ElementEntity.KEY_DELIM)), element.getElementSmartspace()));
		newAction.setProperties(new HashMap<String, Object>());
		newAction.setType("checkin");
		
		ActionBoundary actualResult = this.restTemplate
				.postForObject(this.baseUrl, 
						newAction,
						ActionBoundary.class);
						
		// WHEN the teacher wants to update the student grade
		ActionBoundary updateAction = new ActionBoundary();
		updateAction.setPlayer(new UserKey(teacher.getUserSmartSpace(),teacher.getUserEmail()));
		updateAction.setElement(new BoundaryKey(element.getKey().substring(0, element.getKey().indexOf(ElementEntity.KEY_DELIM)), element.getElementSmartspace()));
		updateAction.setProperties(new HashMap<String, Object>());
		updateAction.getProperties().put("studentId", student.getKey());
		updateAction.getProperties().put("newGrade", 90);
		updateAction.setType("updategrade");
		
		ActionBoundary updateResult = this.restTemplate
				.postForObject(this.baseUrl, 
						updateAction,
						ActionBoundary.class);
		
		System.out.println(updateResult.toString());
		
		// THEN the action get created in the db
		ActionBoundary[] expected = this.actionDao
				.readAll()
				.stream()
				.map(ActionBoundary::new)
				.collect(Collectors.toList()).
				toArray(new ActionBoundary[0]);
		
		assertThat(expected)
			.usingElementComparatorOnFields("actionKey")
			.contains(updateResult);
	}
}
