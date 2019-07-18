package smartspace.rest;

import smartspace.AppProperties;
import smartspace.dao.UserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public class UsersCreator {
	/**
	 * Used to create an adminUser
	 * @param userDao
	 * @param appProperties
	 * @return
	 */
	public static UserEntity AddAdmin(UserDao<String> userDao, AppProperties appProperties) {
		UserEntity admin = new UserEntity();
		admin.setRole(UserRole.ADMIN);
		admin.setUserEmail("zadmin@gmail.com");
		admin.setUserName("Admin");
		admin.setPoints(0);
		admin.setAvatar("avatar.png");
		admin.setUserSmartSpace(appProperties.getName());
		return userDao.create(admin);
	}
	
	/**
	 * Used to create a player user
	 * @param userDao
	 * @param appProperties
	 * @return
	 */
	public static UserEntity AddPlayer(UserDao<String> userDao, AppProperties appProperties) {
		UserEntity admin = new UserEntity();
		admin.setRole(UserRole.PLAYER);
		admin.setUserEmail("zplayer@gmail.com");
		admin.setUserName("Player");
		admin.setPoints(0);
		admin.setAvatar("avatar.png");
		admin.setUserSmartSpace(appProperties.getName());
		return userDao.create(admin);
	}
	
	/**
	 * Used to create a manager user
	 * @param userDao
	 * @param appProperties
	 * @return
	 */
	public static UserEntity AddManager(UserDao<String> userDao, AppProperties appProperties) {
		UserEntity admin = new UserEntity();
		admin.setRole(UserRole.MANAGER);
		admin.setUserEmail("zmanager@gmail.com");
		admin.setUserName("Manager");
		admin.setPoints(0);
		admin.setAvatar("avatar.png");
		admin.setUserSmartSpace(appProperties.getName());
		return userDao.create(admin);
	}
}
