package smartspace.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USERS")
public class UserEntity implements SmartSpaceEntity<String> {
	
	public static final String KEY_DELIM = "_";
	
	@NotNull
	private String userSmartSpace;
	@NotNull
	private String userEmail;
	@NotNull
	private String userName;
	@NotNull
	private String avatar;
	@NotNull
	private UserRole role;
	@Min(0)
	private long points;
	
	@Column(name="ID")
	@Id
	@Override
	public String getKey() {
		return this.userSmartSpace + KEY_DELIM + this.userEmail;
	}
	
	@Override
	public void setKey(String key) {
		String[] temp = key.split(KEY_DELIM);
		this.userSmartSpace = temp[0];
		this.userEmail = temp[1];
	}
	
	@Transient
	public String getUserSmartSpace() {
		return userSmartSpace;
	}

	public void setUserSmartSpace(String userSmartSpace) {
		this.userSmartSpace = userSmartSpace;
	}

	@Transient
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	public UserEntity() {}
	
	public UserEntity(String userSmartSpace,
			String userEmail,
			String userName,
			String avatar,
			UserRole role,
			long points) {
		this.userSmartSpace = userSmartSpace;
		this.userEmail = userEmail;
		this.userName = userName;
		this.avatar = avatar;
		this.role = role;
		this.points = points;
	}

	@Override
	public String toString() {
		return "UserEntity{" + "key='" + getKey() + '\'' + ", userSmartSpace='" + userSmartSpace + '\'' + ", userEmail='" + userEmail + '\'' + ", avatar='" + avatar + '\'' + ", role=" + role + ", points=" + points + ", userName=" + userName + '}';
	}
}
