package smartspace.dao;

import java.util.List;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public interface ExtendedUserDao<UserKey> extends UserDao<UserKey> {

	public List<UserEntity> readAll (int size, int page);
	public List<UserEntity> readAll (int size, int page, String sortBy);
	public UserRole			getUserRole (String smartspace, String email);
	public UserEntity		updateOrInsert (UserEntity user);	
	public UserEntity		insert (UserEntity user);	
}
