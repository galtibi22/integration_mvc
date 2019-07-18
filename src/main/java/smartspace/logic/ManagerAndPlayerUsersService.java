package smartspace.logic;

import java.util.Optional;

import smartspace.data.UserEntity;

public interface ManagerAndPlayerUsersService {
	
	public Optional<UserEntity> getUser(String userSmartspace, String userEmail);
	
	public UserEntity store(UserEntity newUser);

	public void update(String userSmartspace, String userEmail, UserEntity userEntity);
}
