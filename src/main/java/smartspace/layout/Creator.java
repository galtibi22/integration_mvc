package smartspace.layout;

public class Creator {
    String email;
    String smartspace;

    public Creator() {
	}
    
    public Creator(String email, String smartspace) {
		this.email = email;
		this.smartspace = smartspace;
	}
    
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

    @Override
    public String toString() {
        return "Creator{" + "email='" + email + '\'' + ", smartspace='" + smartspace + '\'' + '}';
    }
}
