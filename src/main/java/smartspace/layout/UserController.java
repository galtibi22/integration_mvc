package smartspace.layout;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import smartspace.data.UserEntity;
import smartspace.logic.ManagerAndPlayerUsersService;
import smartspace.logic.UserService;

@RestController
public class UserController {
	private UserService userService;
	private ManagerAndPlayerUsersService managerAndPlayerService;
	 	 
	@Autowired
	public UserController(UserService userService, ManagerAndPlayerUsersService managerAndPlayerService) {
		this.userService = userService;
		this.managerAndPlayerService = managerAndPlayerService;
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/smartspace/admin/users/{adminsmartspace}/{adminEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] exportUsers (
			@PathVariable("adminsmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail,
			@RequestParam(name="size", required=false, defaultValue="10") int size, 
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return this.userService
			.getAll(adminSmartspace, adminEmail, size, page)
			.stream()
			.map(user->new UserBoundary(user))
			.collect(Collectors.toList())
			.toArray(new UserBoundary[0]);
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/smartspace/admin/users/{adminsmartspace}/{adminEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] importUsers (
			@PathVariable("adminsmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail,
			@RequestBody UserBoundary[] usersToImport) {
			Collection<UserEntity> usersEntitiesToImport = ((Arrays.asList(usersToImport))).stream().map(user -> user.convertToEntity()).collect(Collectors.toList());

			return this.userService.store(adminSmartspace, adminEmail, usersEntitiesToImport).stream().map(UserBoundary::new).collect(Collectors.toList()).toArray(new UserBoundary[0]);

	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/smartspace/users/login/{userSmartspace}/{userEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary login (
			@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail) {
		if (this.managerAndPlayerService.getUser(userSmartspace, userEmail).isPresent()) {
			return new UserBoundary(this.managerAndPlayerService.getUser(userSmartspace, userEmail).get());
		} else {
			throw new RuntimeException("Wrong credentials");
		}
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/smartspace/users",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createUser (
			@RequestBody NewUserFormBoundary newUserForm) {
			UserEntity newUser =newUserForm.convertToEntity();
			return new UserBoundary(managerAndPlayerService.store(newUser));
	}
	
	@RequestMapping(
			method=RequestMethod.PUT,
			path="/smartspace/users/login/{userSmartspace}/{userEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public void updateUser (
			@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail,
			@RequestBody UserBoundary userBoundary) {
		UserEntity userToUpdate = userBoundary.convertToEntity();
		this.managerAndPlayerService.update(userSmartspace, userEmail, userToUpdate);
	}
}
