package smartspace.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.dao.ExtendedActionDao;
import smartspace.dao.ExtendedElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;

/*
{
	"type":"viewschedule",
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
public class ViewschedulePlugin implements PluginCommand {
	private ObjectMapper jackson;
	private ExtendedElementDao elementsDao;
	private ExtendedActionDao actionsDao;
	
	@Autowired
	public ViewschedulePlugin(ExtendedElementDao elementsDao, ExtendedActionDao actionsDao) {
		this.jackson = new ObjectMapper();
		this.elementsDao = elementsDao;
		this.actionsDao = actionsDao;
	}
	
	@Override
	public ActionEntity execute(ActionEntity action) {
		try {
			String userKey = action.getPlayerSmartspace()+ActionEntity.KEY_DELIMETER+action.getPlayerEmail();
			
			List<String> results = new ArrayList<>();
			
			this.elementsDao
			.readAll()
			.stream()
			.filter(ent->((ElementEntity)ent).getMoreAttributes().containsKey(userKey))
			.forEach(entity->results.add(((ElementEntity)entity).getName()));
			
			action.getMoreAttributes().put("Schedule", results);

			return action;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
