package smartspace.plugins;

import org.springframework.stereotype.Component;

import smartspace.data.ActionEntity;

/*
{
	"type":"echo",
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
public class EchoPlugin implements PluginCommand {

	@Override
	public ActionEntity execute(ActionEntity action) {
		return action;
	}

}
