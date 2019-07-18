package smartspace.logic;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import smartspace.AppProperties;
import smartspace.dao.ExtendedActionDao;
import smartspace.dao.ExtendedElementDao;
import smartspace.dao.ExtendedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.plugins.PluginCommand;

@Service
public class ActionUserManagerServiceImpl implements ActionUserManagerService {
	private ApplicationContext ctx;
	private ExtendedActionDao actionDao;
	private ExtendedUserDao<String> userDao;
	private ExtendedElementDao<String> elementDao;
	private AppProperties appProperties;
	
	@Autowired
	public ActionUserManagerServiceImpl(
			ApplicationContext ctx,
			ExtendedActionDao actionDao,
			ExtendedUserDao<String> userDao,
			ExtendedElementDao<String> elementDao) {
		this.ctx = ctx;
		this.actionDao = actionDao;
		this.userDao = userDao;
		this.elementDao = elementDao;
	}
	
	@Override
	@Transactional
	public ActionEntity invoke(ActionEntity newAction) {
		try {
			String actionType = newAction.getActionType();
			if (actionType != null && !actionType.trim().isEmpty()) {
				if (validate(newAction)) {
					newAction.setCreationTimeStamp(new Date());
					
					String className = 
							"smartspace.plugins."
							+ actionType.toUpperCase().charAt(0)
							+ actionType.substring(1)
							+ "Plugin";
					
					Class<?> theClass = Class.forName(className);
					Object plugin = ctx.getBean(theClass);
					plugin = ((PluginCommand)plugin).execute(newAction);
					return this.actionDao.create(newAction);
				} else {
					throw new RuntimeException("Action is invalid due to worng element of user.");
				}
			} else {
				throw new RuntimeException("Illegal action type");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private boolean validate(ActionEntity action) {
		if(action.getElementId() != null &&
				action.getElementSmartspace() != null &&
				action.getPlayerEmail() != null &&
				action.getPlayerSmartspace() != null) {
			if (this.userDao.readById(action.getPlayerSmartspace() + UserEntity.KEY_DELIM + action.getPlayerEmail()).isPresent() && 
				this.elementDao.readById(action.getElementId() + ElementEntity.KEY_DELIM + action.getElementSmartspace()).isPresent()) {
					return true;
			}
		}
		
		return false;
	}

}
