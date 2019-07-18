package smartspace.data;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class UserKey implements Comparable<UserKey>, Serializable{
	public static final String KEY_DELIM = "_";

	private String email;
	private String smartspace;

	public UserKey() {}

	public UserKey(String smartspace, String email) {
		this.email = email;
		this.smartspace = smartspace;
	}

	@NotNull
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@NotNull
	public String getSmartspace() {
		return smartspace;
	}

	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((smartspace == null) ? 0 : smartspace.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserKey other = (UserKey) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (smartspace == null) {
			if (other.smartspace != null)
				return false;
		} else if (!smartspace.equals(other.smartspace))
			return false;
		return true;
	}

	@Override
	public int compareTo(UserKey o) {
		return (o.getSmartspace() + KEY_DELIM + o.getEmail()).compareTo(this.getSmartspace() + KEY_DELIM + this.getEmail());
	}
	
	@Override
	public String toString() {
		return "UserKey{" + "smartspace='" + smartspace + '\'' + ", email='" + email + '}';
	}
}
