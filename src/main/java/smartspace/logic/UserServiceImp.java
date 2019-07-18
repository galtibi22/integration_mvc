package smartspace.logic;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import smartspace.dao.ExtendedUserDao;
import smartspace.AppProperties;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Service
public class UserServiceImp implements UserService{

	private ExtendedUserDao<String> userDao;
	private AppProperties appProperties;

	@Autowired
	public UserServiceImp(ExtendedUserDao<String> userDao, AppProperties appProperties) {
		this.userDao = userDao;
		this.appProperties = appProperties;
	}
	
	@Override
	public List<UserEntity> getAll(String adminSmartspace, String adminEmail, int size, int page) {
		if (this.userDao.getUserRole(adminSmartspace, adminEmail) != UserRole.ADMIN) {
			throw new RuntimeException("Only admin users can perform this action");
		}
		return this.userDao
				.readAll(size, page, "key");
	}

	@Override
	@Transactional
	public Collection<UserEntity> store(String adminSmartspace, String adminEmail, Collection<UserEntity> usersEntitiesToImport) {
		if (userDao.getUserRole(adminSmartspace, adminEmail) != UserRole.ADMIN) {
			throw new RuntimeException("Only admin users can perform this action");
		}else {
			if (usersEntitiesToImport.stream().anyMatch(entity -> entity.getUserSmartSpace().equals(appProperties.getName()))) {
				throw new RuntimeException("Not allowed to import data from the local smartspace");
			}
			
			return usersEntitiesToImport.stream()
			.map(entity -> userDao.updateOrInsert(entity))
			.collect(Collectors.toList());
		}
	}
	
}
