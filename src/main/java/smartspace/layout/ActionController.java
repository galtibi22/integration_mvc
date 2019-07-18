package smartspace.layout;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import smartspace.data.ActionEntity;
import smartspace.logic.ActionService;
import smartspace.logic.ActionUserManagerService;

@RestController
public class ActionController {
	private ActionService actionService;
	private ActionUserManagerService actionUserManagerService;
	
	@Autowired
	public ActionController(ActionService actionService, ActionUserManagerService actionUserManagerService) {
		this.actionService = actionService;
		this.actionUserManagerService = actionUserManagerService;
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/smartspace/admin/actions/{adminsmartspace}/{adminEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] exportActions (
			@PathVariable("adminsmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail,
			@RequestParam(name="size", required=false, defaultValue="10") int size, 
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return this.actionService
			.getAll(adminSmartspace, adminEmail, size, page)
			.stream()
			.map(action->new ActionBoundary(action))
			.collect(Collectors.toList())
			.toArray(new ActionBoundary[0]);
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/smartspace/admin/actions/{adminsmartspace}/{adminEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] importActions (
			@PathVariable("adminsmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail,
			@RequestBody ActionBoundary[] actionsToImport) {
			Collection<ActionEntity> actionsEntitiesToImport = ((Arrays.asList(actionsToImport)))
					.stream()
					.map(action -> action.convertToEntity())
					.collect(Collectors.toList());

			return this.actionService
					.store(adminSmartspace, 
							adminEmail, 
							actionsEntitiesToImport)
					.stream()
					.map(ActionBoundary::new)
					.collect(Collectors.toList()).
					toArray(new ActionBoundary[0]);

	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/smartspace/actions",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary invoke (@RequestBody ActionBoundary newAction) {
		return new ActionBoundary
				(this.actionUserManagerService.invoke
						(newAction.convertToEntity()));
	
	}

}
