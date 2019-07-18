package smartspace.layout;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public class NewUserFormBoundary {
	
	private String email;
	private String role;
	private String username;
	private String avatar;
	
	public NewUserFormBoundary() {}

	public NewUserFormBoundary(String email, String role, String username, String avatar) {
		super();
		this.email = email;
		this.role = role;
		this.username = username;
		this.avatar = avatar;
	}
	
	public String getSmartspace() {
		return "2019B.oriw";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public UserEntity convertToEntity() {
		NewUserFormBoundary formBoundary=this;
		UserEntity entity = new UserEntity();
		entity.setPoints(0);
		entity.setAvatar(formBoundary.getAvatar());
		entity.setUserName(formBoundary.getUsername());
		if (formBoundary.getRole() != null) {
			entity.setRole(UserRole.valueOf(formBoundary.getRole()));
		} else {
			entity.setRole(null);
		}
		entity.setUserSmartSpace(formBoundary.getSmartspace());
		entity.setUserEmail(formBoundary.getEmail());

		entity.setKey(entity.getUserSmartSpace() + UserEntity.KEY_DELIM + entity.getUserEmail());

		return entity;
	}

}
