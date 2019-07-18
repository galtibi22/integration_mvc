package smartspace.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.dao.ExtendedActionDao;
import smartspace.dao.ExtendedElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;

/*
{
	"type":"checkin",
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
public class CheckinPlugin implements PluginCommand {
	private ExtendedElementDao elementsDao;
	private ExtendedActionDao actionsDao;
	
	@Autowired
	public CheckinPlugin(ExtendedElementDao elementsDao, ExtendedActionDao actionsDao) {
		this.elementsDao = elementsDao;
		this.actionsDao = actionsDao;
	}
	
	@Override
	public ActionEntity execute(ActionEntity action) {
		ElementEntity element = this.elementsDao.find(action.getElementId(), action.getElementSmartspace());
		
		if (!element.getMoreAttributes().containsKey(action.getPlayerSmartspace()+ActionEntity.KEY_DELIMETER+action.getPlayerEmail())) {
			element.getMoreAttributes().put(action.getPlayerSmartspace()+ActionEntity.KEY_DELIMETER+action.getPlayerEmail(), "-1");
					
			this.elementsDao.update(element);
			action.getMoreAttributes().put("CheckIn", action.getPlayerSmartspace()+ActionEntity.KEY_DELIMETER+action.getPlayerEmail());
		}
		
		return action;
	}

}
