package smartspace.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.ExtendedActionDao;
import smartspace.dao.ExtendedElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;

/*
{
	"type":"checkout",
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
public class CheckoutPlugin implements PluginCommand {
	private ExtendedElementDao elementsDao;
	private ExtendedActionDao actionsDao;
	
	@Autowired
	public CheckoutPlugin(ExtendedElementDao elementsDao, ExtendedActionDao actionsDao) {
		this.elementsDao = elementsDao;
		this.actionsDao = actionsDao;
	}
	
	@Override
	public ActionEntity execute(ActionEntity action) {
		ElementEntity element = this.elementsDao.find(action.getElementId(), action.getElementSmartspace());
		
		if (element.getMoreAttributes().containsKey(action.getPlayerSmartspace()+ActionEntity.KEY_DELIMETER+action.getPlayerEmail())) {
			element.getMoreAttributes().remove(action.getPlayerSmartspace()+ActionEntity.KEY_DELIMETER+action.getPlayerEmail());
			
			this.elementsDao.update(element);
			action.getMoreAttributes().put("Checkout", action.getPlayerSmartspace()+ActionEntity.KEY_DELIMETER+action.getPlayerEmail());
		}
			
		return action;
	}

}
