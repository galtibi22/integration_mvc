package smartspace.logic;

import java.util.Collection;
import java.util.List;

import smartspace.data.UserEntity;

public interface UserService {
	public List<UserEntity> getAll(String adminSmartspace, String adminEmail, int size, int page);

	public Collection<UserEntity> store(String adminSmartspace, String adminEmail, Collection<UserEntity> usersEntitiesToImport);
}
