package smartspace.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import smartspace.AppProperties;
import smartspace.dao.ExtendedElementDao;
import smartspace.dao.ExtendedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityGenerator;
import smartspace.layout.BoundaryKey;
import smartspace.layout.Creator;
import smartspace.layout.ElementBoundary;
import smartspace.layout.LatLng;
import smartspace.logic.ElementService;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties={"spring.profiles.active=default"})
public class ElementRestControllerTests extends ControllerCommonTest{
	int page = 1;
	int size = 5;
	int totalSize = 10;

	private String baseUrl;
	private int port;
	private ExtendedUserDao<String> userDao;
	private ExtendedElementDao<String> elementDao;
	private ElementService elementService;
	private RestTemplate restTemplate;
	private AppProperties appProperties;
	private UserEntity admin;
	private UserEntity manager;
	private UserEntity player;


	@Autowired
	public void init(ExtendedUserDao<String> userDao,ExtendedElementDao<String> elementDao,ElementService elementService,AppProperties appProperties){
		this.userDao = userDao;
		this.appProperties = appProperties;
		this.elementDao = elementDao;
		this.elementService = elementService;
		this.restTemplate = new RestTemplate();

	}
	@PostConstruct
	public void postConstruct(){
		this.baseUrl = "http://localhost:" + port + "/smartspace";

	}

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}


	@Before
	public void before() {
		admin = userDao.create(entityGenerator.userEntity(UserRole.ADMIN,appProperties.getName()));
		manager = userDao.create(entityGenerator.userEntity(UserRole.MANAGER,appProperties.getName()));
		player = userDao.create(entityGenerator.userEntity(UserRole.PLAYER,appProperties.getName()));

	}

	@After
	public void tearDown() {
		this.elementDao
			.deleteAll();
	}

	private String getBaseUrlWithAdminUser() {
		return baseUrl + "/admin/elements/" + admin.getUserSmartSpace() + "/" + admin.getUserEmail();
	}
	private String getBaseUrlMangerPlayerApi(UserEntity userEntity) {
		return baseUrl + "/elements/" + userEntity.getUserSmartSpace() + "/" + userEntity.getUserEmail();
	}



	@Test
	public void managerCreateElementAndPlayerGetIt(){
		ElementBoundary newElementBoundary = new ElementBoundary(entityGenerator.elementEntity());
		newElementBoundary.setKey(null);
		ElementBoundary postElement = this.restTemplate
				.postForObject(
						getBaseUrlMangerPlayerApi(manager),
						newElementBoundary,
						ElementBoundary.class);
		ElementBoundary getElement =this.restTemplate
				.getForObject(
						getBaseUrlMangerPlayerApi(player)+"/"
								+postElement.getKey().getSmartspace()+"/"
								+postElement.getKey().getId(),
						ElementBoundary.class);
		String key = postElement.getKey().getId();
		assertThat(getElement.getElementType().equals(postElement.getElementType()));
		assertThat(getElement.getName().equals(postElement.getName()));
		assertThat(getElement.getKey().equals(postElement.getKey()));
	}
	@Test
	public void managerCreateElementAndManagerGetIt(){
		ElementBoundary newElementBoundary = new ElementBoundary(entityGenerator.elementEntity());
		newElementBoundary.setKey(null);
		ElementBoundary postElement = this.restTemplate
				.postForObject(
						getBaseUrlMangerPlayerApi(manager),
						newElementBoundary,
						ElementBoundary.class);
		ElementBoundary getElement =this.restTemplate
				.getForObject(
						getBaseUrlMangerPlayerApi(manager)+"/"
								+postElement.getKey().getSmartspace()+"/"
								+postElement.getKey().getId(),
						ElementBoundary.class);
		String key = postElement.getKey().getId();
		assertThat(getElement.getElementType().equals(postElement.getElementType()));
		assertThat(getElement.getName().equals(postElement.getName()));
		assertThat(getElement.getKey().equals(postElement.getKey()));
	}

	@Test(expected=Exception.class)
	public void adminCannotGetElementWithManagerPlayerApi(){
		ElementBoundary newElementBoundary = new ElementBoundary(entityGenerator.elementEntity());
		newElementBoundary.setKey(null);
		ElementBoundary postElement=this.restTemplate
				.postForObject(
						getBaseUrlMangerPlayerApi(admin),
						newElementBoundary,
						ElementBoundary.class);
		ElementBoundary getElement =this.restTemplate
				.getForObject(
						getBaseUrlMangerPlayerApi(manager)+"/"
								+postElement.getKey().getSmartspace()+"/"
								+postElement.getKey().getId(),
						ElementBoundary.class);

	}

	@Test(expected=Exception.class)
	public void adminCannotCreateElementWithManagerPlayerApi(){
		ElementBoundary newElementBoundary = new ElementBoundary(entityGenerator.elementEntity());
		newElementBoundary.setKey(null);
		this.restTemplate
				.postForObject(
						getBaseUrlMangerPlayerApi(admin),
						newElementBoundary,
						ElementBoundary.class);
		

	}

	@Test(expected=Exception.class)
	public void playerCannotCreateElementWithManagerPlayerApi(){
		ElementBoundary newElementBoundary = new ElementBoundary(entityGenerator.elementEntity());
		newElementBoundary.setKey(null);
		this.restTemplate
				.postForObject(
						getBaseUrlMangerPlayerApi(player),
						newElementBoundary,
						ElementBoundary.class);
	}

	@Test
	public void managerUpdateElement(){
		ElementBoundary newElementBoundary = new ElementBoundary(entityGenerator.elementEntity());
		newElementBoundary.setKey(null);
		ElementBoundary postElement = this.restTemplate
				.postForObject(
						getBaseUrlMangerPlayerApi(manager),
						newElementBoundary,
						ElementBoundary.class);
		newElementBoundary.setKey(null);
		newElementBoundary.setName("newName");
		this.restTemplate
				.put(getBaseUrlMangerPlayerApi(manager)+
						"/"+postElement.getKey().getSmartspace()+
						"/"+postElement.getKey().getId(),
						newElementBoundary,
						ElementBoundary.class);
		ElementBoundary getElement =this.restTemplate
				.getForObject(
						getBaseUrlMangerPlayerApi(manager)+"/"
								+postElement.getKey().getSmartspace()+"/"
								+postElement.getKey().getId(),
						ElementBoundary.class);
		assertEquals(newElementBoundary.getName(),getElement.getName());

	}

	@Test(expected = Exception.class)
	public void playerCannotUpdateElement() {
		ElementBoundary newElementBoundary = new ElementBoundary(entityGenerator.elementEntity());
		newElementBoundary.setKey(null);
		ElementBoundary postElement = this.restTemplate.postForObject(getBaseUrlMangerPlayerApi(manager), newElementBoundary, ElementBoundary.class);
		newElementBoundary.setKey(null);
		newElementBoundary.setName("newName");
		this.restTemplate.put(getBaseUrlMangerPlayerApi(player) + "/" + postElement.getKey().getSmartspace() + "/" + postElement.getKey().getId(), newElementBoundary, ElementBoundary.class);
	}


	@Test(expected = Exception.class)
	public void adminCannotUpdateElement() {
		ElementBoundary newElementBoundary = new ElementBoundary(entityGenerator.elementEntity());
		newElementBoundary.setKey(null);
		ElementBoundary postElement = this.restTemplate.postForObject(getBaseUrlMangerPlayerApi(manager), newElementBoundary, ElementBoundary.class);
		newElementBoundary.setKey(null);
		newElementBoundary.setName("newName");
		this.restTemplate.put(getBaseUrlMangerPlayerApi(admin) + "/" + postElement.getKey().getSmartspace() + "/" + postElement.getKey().getId(), newElementBoundary, ElementBoundary.class);
	}
	@Test
	public void adminImportElement() throws Exception{
		ElementBoundary newElementBoundary = new ElementBoundary(entityGenerator.elementEntity("smartspace"));

		ElementBoundary[] actualResult = this.restTemplate
			.postForObject(
					getBaseUrlWithAdminUser(),
					new ElementBoundary[]{newElementBoundary},
					ElementBoundary[].class);

		String key = actualResult[0].getKey().getId() + UserEntity.KEY_DELIM + actualResult[0].getKey().getSmartspace();
		ElementEntity getElement=this.elementDao.readById(key).get();
		assertThat(getElement.getType().equals(actualResult[0].getElementType()));
		assertThat(getElement.getName().equals(actualResult[0].getName()));
		assertThat(getElement.getKey().equals(actualResult[0].getKey()));
	}

	@Test
	public void adminExportAllElementUsingPagination() throws Exception{
		Collection<ElementEntity> entities = elementService.store(admin.getUserSmartSpace(),
				admin.getUserEmail(),entityGenerator.elementList(totalSize,"smart"));
		List<ElementBoundary> allElements =
			entities.stream()
			.map(ElementBoundary::new)
			.collect(Collectors.toList());
		List<ElementBoundary> expected =
				allElements
				.stream()
				.skip(5)
				.limit(5).collect(Collectors.toList());

		ElementBoundary[] result =
		  this.restTemplate
			.getForObject(
					getBaseUrlWithAdminUser() + "?size={size}&page={page}",
					ElementBoundary[].class,
					size, page);
		assertThat(result)
			.usingElementComparatorOnFields("key").containsSequence(expected);
	}

	@Test()
	public void managerGetAllElementUsingPagination() throws Exception{
		Collection<ElementEntity> entities = elementService.store(admin.getUserSmartSpace(),
				admin.getUserEmail(),entityGenerator.elementList(totalSize,"smart"));
		List<ElementBoundary> allElements =
				entities.stream()
						.map(ElementBoundary::new)
						.collect(Collectors.toList());
		List<ElementBoundary> expected =
				allElements
						.stream().skip(5)
						.limit(5).collect(Collectors.toList());
		ElementBoundary[] result =
				this.restTemplate
						.getForObject(
								getBaseUrlMangerPlayerApi(manager) + "?size={size}&page={page}",
								ElementBoundary[].class,
								size, page);
		assertThat(result)
				.usingElementComparatorOnFields("key").containsSequence(expected);
	}

	@Test
	public void managerGetAllElementUsingSearchName() throws Exception{
		String search="name";
		String value="newName";
		Collection<ElementEntity> entities=entityGenerator.elementList(totalSize,"smart");
		((List<ElementEntity>) entities).get(0).setName(value);
		entities = elementService.store(admin.getUserSmartSpace(),
				admin.getUserEmail(),entities);
		List<ElementBoundary> allElements =
				entities.stream()
						.map(ElementBoundary::new)
						.collect(Collectors.toList());
		List<ElementBoundary> expected =
				allElements.stream().filter(elementBoundary -> elementBoundary.getName().equals(value)).collect(Collectors.toList());
		ElementBoundary[] result =
				this.restTemplate
						.getForObject(
								getBaseUrlMangerPlayerApi(manager) + "?search={search}&value={value}&size={size}&page={page}",
								ElementBoundary[].class,
								search,value,size, 0);
		assertThat(result)
				.usingElementComparatorOnFields("key").containsSequence(expected);
	}

	@Test
	public void managerGetAllElementUsingSearchType() throws Exception{
		String search="type";
		String value="newType";
		Collection<ElementEntity> entities=entityGenerator.elementList(totalSize,"smart");
		((List<ElementEntity>) entities).get(0).setType(value);
		entities = elementService.store(admin.getUserSmartSpace(),
				admin.getUserEmail(),entities);
		List<ElementBoundary> allElements =
				entities.stream()
						.map(ElementBoundary::new)
						.collect(Collectors.toList());
		List<ElementBoundary> expected =
				allElements.stream().filter(elementBoundary -> elementBoundary.getElementType().equals(value)).collect(Collectors.toList());
		ElementBoundary[] result =
				this.restTemplate
						.getForObject(
								getBaseUrlMangerPlayerApi(manager) + "?search={search}&value={value}&size={size}&page={page}",
								ElementBoundary[].class,
								search,value,size, 0);
		assertThat(result)
				.usingElementComparatorOnFields("key").containsSequence(expected);
	}


	@Test
	public void managerGetAllElementUsingSearchLocation() throws Exception{
		String search="location";
		String x="99";
		String y="99";
		String distance="10";
		Collection<ElementEntity> entities=entityGenerator.elementList(totalSize,"smart");
		entities.stream().forEach(elementEntity -> elementEntity.setLocation(new Location(1,1)));
		((List<ElementEntity>) entities).get(0).setLocation(new Location(100,100));
		entities = elementService.store(admin.getUserSmartSpace(),
				admin.getUserEmail(),entities);
		List<ElementBoundary> allElements =
				entities.stream()
						.map(ElementBoundary::new)
						.collect(Collectors.toList());
		List<ElementBoundary> expected =
				allElements.stream().filter(elementBoundary -> elementBoundary.getLatlng().getLat().equals(100.0)).collect(Collectors.toList());
		ElementBoundary[] result =
				this.restTemplate
						.getForObject(
								getBaseUrlMangerPlayerApi(manager) + "?search={search}&x={x}&y={y}&distance={distance}&size={size}&page={page}",
								ElementBoundary[].class,
								search,x,y,distance,size, 0);
		assertThat(result)
				.usingElementComparatorOnFields("key").containsSequence(expected);
	}

	@Test(expected=Exception.class)
	public void invalidAdminCannotImportElement() throws Exception{
		ElementBoundary newElementBoundary = new ElementBoundary(entityGenerator.elementEntity("smartspace"));

		this.restTemplate
			.postForObject(
					this.baseUrl + "/admin/elements/user/smartspace/",
					new ElementBoundary[]{newElementBoundary},
					ElementBoundary[].class);
	}

	@Test(expected=Exception.class)
	public void adminCannotImportElementWithLocalSmartspace() throws Exception{
		ElementBoundary newElementBoundary = new ElementBoundary(entityGenerator.elementEntity());
		this.restTemplate
			.postForObject(
					getBaseUrlWithAdminUser(),
					new ElementBoundary[]{newElementBoundary},
					ElementBoundary[].class);
	}

	@Test(expected=Exception.class)
	public void managerCannotExportElements() throws Exception{
		ElementBoundary newElementBoundary = new ElementBoundary(entityGenerator.elementEntity());
		this.restTemplate
			.postForObject(
					getBaseUrlWithAdminUser(),
					new ElementBoundary[]{newElementBoundary},
					ElementBoundary[].class);

		this.restTemplate
		.postForObject(
				baseUrl + "/admin/elements/"  + newElementBoundary.getKey().getId() + "/" + newElementBoundary.getKey().getSmartspace(),
				new ElementBoundary[]{newElementBoundary},
				ElementBoundary[].class);
	}
}
