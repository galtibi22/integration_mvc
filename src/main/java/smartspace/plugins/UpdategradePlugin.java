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
	"type":"updategrade",
	"element":{
		"id":"",
		"smartspace":""
	},
	"player":{
		"smartspace":"",
		"email":""
	},
	"properties":{
		"studentId":""
		"newGrade":
	}
}
 */

@Component
public class UpdategradePlugin implements PluginCommand {
	private ObjectMapper jackson;
	private ExtendedElementDao elementsDao;
	private ExtendedActionDao actionsDao;
	
	@Autowired
	public UpdategradePlugin(ExtendedElementDao elementsDao, ExtendedActionDao actionsDao) {
		this.jackson = new ObjectMapper();
		this.elementsDao = elementsDao;
		this.actionsDao = actionsDao;
	}
	
	@Override
	public ActionEntity execute(ActionEntity action) {
		try {
			UpdategradeInput input = this.jackson
					.readValue(
							this.jackson
							.writeValueAsString(action.getMoreAttributes()),
							UpdategradeInput.class);
			
			ElementEntity element = this.elementsDao.find(action.getElementId(), action.getElementSmartspace());
			
			if (element.getMoreAttributes().containsKey(input.getStudentId())) {
				element.getMoreAttributes().replace(input.getStudentId(), input.getNewGrade());
				
				this.elementsDao.update(element);
			}
				
			return action;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
