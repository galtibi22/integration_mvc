package smartspace.data.util;

import java.util.Map;

import smartspace.data.*;

public interface EntityFactory {
	UserEntity createNewUser(String userEmail, 
			String userSmartSpace, 
			String username, 
			String avatar, 
			UserRole role, 
			long points);
	
	ElementEntity createNewElement(String name,
								   String type,
								   Location location,
								   java.util.Date creationTimeStamp,
								   String creatorEmail,
								   String creatorSmartSpace,
								   boolean expired,
								   Map<String, Object> moreAttributes);
	
	ActionEntity createNewAction(String elementId,
			String elementSmartSpace,
			String actionType,
			java.util.Date creationTimeStamp,
			String playerEmail,
			String playerSmartSpace,
			Map<String, Object> moreAttributes);

}
