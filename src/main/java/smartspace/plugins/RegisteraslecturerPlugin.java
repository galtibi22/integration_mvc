package smartspace.plugins;

import javax.persistence.EntityExistsException;

import org.hibernate.query.criteria.internal.predicate.ExistsPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.ExtendedActionDao;
import smartspace.dao.ExtendedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.UserEntity;

/*
{
	"type":"registeraslecturer",
	"element":{
		"id":"",
		"smartspace":""
	},
	"player":{
		"smartspace":"",
		"email":""
	},
	"properties":{
	}
}
 */

@Component
public class RegisteraslecturerPlugin implements PluginCommand {
	private ExtendedActionDao actionsDao;
	private ExtendedUserDao usersDao;
	
	@Autowired
	public RegisteraslecturerPlugin(ExtendedActionDao actionsDao, ExtendedUserDao usersDao) {
		this.actionsDao = actionsDao;
		this.usersDao = usersDao;
	}
	@Override
	public ActionEntity execute(ActionEntity action) {
		UserEntity user = (UserEntity) this.usersDao.readById(action.getPlayerSmartspace()+UserEntity.KEY_DELIM+action.getPlayerEmail()).get();
		
		if (user != null) {
			if (user.getPoints() < 999) {
				user.setPoints(999);
				this.usersDao.update(user);
			} else {
				throw new EntityExistsException("User already a teacher");
			}
		} else {
			throw new NullPointerException("User is not existed");
		}
		
		return action;
	}

}
