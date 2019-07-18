package smartspace.logic;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import smartspace.AppProperties;
import smartspace.dao.ExtendedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Service
public class ManagerAndPlayerUsersServiceImp implements ManagerAndPlayerUsersService {

	private ExtendedUserDao<String> userDao;

	@Autowired
	public ManagerAndPlayerUsersServiceImp(ExtendedUserDao<String> userDao, AppProperties appProperties) {
		this.userDao = userDao;
	}

	@Override
	public Optional<UserEntity> getUser(String userSmartspace, String userEmail) {
		managerPlayerUserOrThrow(userSmartspace, userEmail);
		return this.userDao.readById(userSmartspace + UserEntity.KEY_DELIM + userEmail);
	}

	@Override
	public UserEntity store(UserEntity newUser) {
		if (newUser.getRole() != UserRole.MANAGER
				&& newUser.getRole() != UserRole.PLAYER) {
			throw new RuntimeException("Only managers and players users can perform this action");
		}
		return this.userDao.create(newUser);
	}

	@Override
	public void update(String userSmartspace, String userEmail, UserEntity userEntity) {
		managerPlayerUserOrThrow(userSmartspace, userEmail);
		userEntity.setKey(userSmartspace + UserEntity.KEY_DELIM + userEmail);
		long points = this.userDao.readById(userEntity.getKey()).get().getPoints();
		userEntity.setPoints(points);
		this.userDao.update(userEntity);
	}

	private void managerPlayerUserOrThrow(String userSmartspace, String userEmail) {
		if (this.userDao.getUserRole(userSmartspace, userEmail) != UserRole.MANAGER
				&& this.userDao.getUserRole(userSmartspace, userEmail) != UserRole.PLAYER) {
			throw new RuntimeException("Only managers and players users can perform this action");
		}
	}

}
