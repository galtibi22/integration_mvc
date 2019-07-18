package smartspace.layout;

public class UserKey {
	private String smartspace;
	private String email;
	
	public String getSmartspace() {
		return smartspace;
	}
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public UserKey() {}
	
	public UserKey(String smartspace, String email) {
		this.smartspace = smartspace;
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "email: " + this.email + 
				" smartspace: " + this.smartspace;
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
}
