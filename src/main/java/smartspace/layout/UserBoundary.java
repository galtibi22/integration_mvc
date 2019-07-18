package smartspace.layout;

import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public class UserBoundary{
	private UserKey key;
	private String role;
	private String username;
	private String avatar;
	private long points;
	
	public UserBoundary() {}
	
	public UserBoundary(UserEntity entity) {
		if(entity instanceof UserEntity) {
		this.key = new UserKey(entity.getUserSmartSpace(), entity.getUserEmail());
	} else {
		throw new RuntimeException("Trying to downcast an object that isn't an instance of UserEntity to a UserBoundary");
		}
		this.role = entity.getRole().name();
		this.username = entity.getUserName();
		this.avatar = entity.getAvatar();
		this.points = entity.getPoints();
	}
	
	public UserKey getKey() {
		return key;
	}
	public void setKey(UserKey key) {
		this.key = key;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public long getPoints() {
		return points;
	}
	public void setPoints(long points) {
		this.points = points;
	}
	

// Check if boundary attributes are null before converting user Boundary to user Entity, points are long, therefore cannot be null
	public UserEntity convertToEntity() {
		UserBoundary userBoundary=this;
		UserEntity userEntity = new UserEntity();
		userEntity.setPoints(userBoundary.getPoints());
		if(userBoundary.getAvatar() != null) {
			userEntity.setAvatar(userBoundary.getAvatar());
		} else {
			userEntity.setAvatar(null);
		}
		if(userBoundary.getUsername() != null) {
			userEntity.setUserName(userBoundary.getUsername());
		} else {
			userEntity.setUserName(null);
		}
		if (userBoundary.getRole() != null) {
			userEntity.setRole(UserRole.valueOf(userBoundary.getRole()));
		} else {
			userEntity.setRole(null);
		}
// No change should be applied to Entity key
		if (userBoundary.getKey() != null) {
			userEntity.setUserSmartSpace(userBoundary.getKey().getSmartspace());
			userEntity.setUserEmail(userBoundary.getKey().getEmail());
			userEntity.setKey(userBoundary.getKey().getSmartspace() + UserEntity.KEY_DELIM + userBoundary.getKey().getEmail());
		} else {
			userEntity.setUserSmartSpace(null);
			userEntity.setUserEmail(null);
			//userEntity.setKey(null);
		}
		return userEntity;	
	}
	
	@Override
	public String toString() {
		return "UserCoundary: key: " + this.key + 
				" points: " + this.points + 
				" avatar: " + this.avatar + 
				" role: " + this.role +
				" userName: " + this.username;
	}
}
