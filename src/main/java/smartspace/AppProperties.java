package smartspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {
	
	private String name;
	public final String AVATAR="avatar.png";
	public final String TYPE="type";

	public String getName() {
		return name;
	}
	
	@Autowired
	public void setName(@Value("${smartspace.name}")String name) {
		this.name = name;
	}
}
