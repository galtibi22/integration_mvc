package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.AppProperties;
import smartspace.dao.ExtendedUserDao;

@Repository
public class RdbUserDao implements ExtendedUserDao<String> {
	private UserCrud userCrud;
	private AppProperties appProperties;

	@Autowired
	public RdbUserDao(UserCrud userCrud, AppProperties appProperties) {
		this.userCrud = userCrud;
		this.appProperties = appProperties;
	}

	@Override
	@Transactional
	public UserEntity create(UserEntity entity) {
		entity.setKey(appProperties.getName() + UserEntity.KEY_DELIM  + entity.getUserEmail());
		return this.userCrud.save(entity);
	}

	@Override
	public UserEntity insert(UserEntity entity) {
		entity.setKey( entity.getUserSmartSpace() + UserEntity.KEY_DELIM + entity.getUserEmail());
		return this.userCrud.save(entity);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Optional<UserEntity> readById(String userKey) {
		return this.userCrud.findById(userKey);
	}

	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> readAll() {
		List<UserEntity> rv = new ArrayList<>();
		this.userCrud
			.findAll()
			.forEach(user->rv.add(user));
		return rv;
	}

	@Override
	@Transactional
	public void update(UserEntity userToUpdate) {
		UserEntity existing = 
				this.readById(userToUpdate.getKey())
				  .orElseThrow(()-> new RuntimeException("No user with key: " + userToUpdate.getKey()));
		
		// Patching
		if (userToUpdate.getAvatar() != null) {
			existing.setAvatar(userToUpdate.getAvatar());
		}

		if (userToUpdate.getPoints() > -1) {
			existing.setPoints(userToUpdate.getPoints());
		}
		
		if (userToUpdate.getRole() != null) {
			existing.setRole(userToUpdate.getRole());
		}
		
		if (userToUpdate.getUserName() != null) {
			existing.setUserName(userToUpdate.getUserName());
		}
		
		this.userCrud.save(existing);
	}

	@Override
	@Transactional
	public void deleteAll() {
		this.userCrud.deleteAll();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> readAll(int size, int page) {
		return this.userCrud
				.findAll(PageRequest.of(page, size))
				.getContent();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> readAll(int size, int page, String sortBy) {
		return this.userCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, sortBy))
				.getContent();
	}

	@Override
	public UserRole getUserRole(String smartspace, String email) {
		String userKey = smartspace + UserEntity.KEY_DELIM + email;

		UserEntity existing =this.readById(userKey)
				  .orElseThrow(()-> new RuntimeException("No user with key: " + userKey));
		return existing.getRole();
	}

	@Override
	public UserEntity updateOrInsert(UserEntity user) {
		UserEntity existing =null;
		if (user.getKey()!=null)
		 existing = this.readById(user.getKey()).orElse(null);
		
		if (existing == null) {
			return insert(user);
		} else {
			update(user);
			return this.readById(user.getKey()).get();
		}
	}
}
