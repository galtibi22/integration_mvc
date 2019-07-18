package smartspace.dao;

import java.util.List;
import java.util.Optional;

import smartspace.data.UserEntity;

public interface UserDao<UserKey> {
	UserEntity create(UserEntity entity);
	Optional<UserEntity> readById(UserKey userKey);
	List<UserEntity> readAll();
	void update(UserEntity userToUpdate);
	void deleteAll();
}
