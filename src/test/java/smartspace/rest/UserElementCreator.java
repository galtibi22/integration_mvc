package smartspace.rest;

import java.util.Date;
import java.util.HashMap;

import smartspace.AppProperties;
import smartspace.dao.ElementDao;
import smartspace.dao.UserDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public class UserElementCreator {
	public static UserEntity AddUserRolePlayer(UserDao<String> userDao, AppProperties appProperties) {
		UserEntity user = new UserEntity();
		user.setRole(UserRole.PLAYER);
		user.setUserEmail("player@gmail.com");
		user.setUserName("Player0909");
		user.setPoints(0);
		user.setAvatar("avatar.png");
		user.setUserSmartSpace(appProperties.getName());
		return userDao.create(user);
	}
	
	public static UserEntity AddUserRoleManager(UserDao<String> userDao, AppProperties appProperties) {
		UserEntity user = new UserEntity();
		user.setRole(UserRole.MANAGER);
		user.setUserEmail("manager@gmail.com");
		user.setUserName("Manager0909");
		user.setPoints(0);
		user.setAvatar("avatar.png");
		user.setUserSmartSpace(appProperties.getName());
		return userDao.create(user);
	}
	
	public static ElementEntity AddElement(ElementDao<String> elementDao, AppProperties appProperties, String name, String type) {
		ElementEntity element = new ElementEntity();
		element.setCreationTimeStamp(new Date());
		element.setCreatorEmail("manager@gmail.com");
		element.setCreatorSmartSpace(appProperties.getName());
		element.setElementSmartspace(appProperties.getName());
		element.setExpired(false);
		element.setLocation(new Location(56, 85));
		element.setMoreAttributes(new HashMap<String, Object>());
		element.setName(name);
		element.setType(type);
		return elementDao.create(element);
	}
}
