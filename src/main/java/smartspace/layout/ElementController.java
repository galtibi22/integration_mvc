package smartspace.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import smartspace.data.ElementEntity;
import smartspace.logic.ElementService;
import smartspace.logic.ElementUserManagerService;

@RestController
public class ElementController {
	private ElementService elementService;
	private ElementUserManagerService elementUserManagerService;
	
	@Autowired
	public ElementController(ElementService elementService,ElementUserManagerService elementUserManagerService) {

		this.elementService = elementService;
		this.elementUserManagerService=elementUserManagerService;
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/smartspace/admin/elements/{adminsmartspace}/{adminEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] exportElements (
			@PathVariable("adminsmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail,
			@RequestParam(name="size", required=false, defaultValue="10") int size, 
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		 return elementService.getAll(adminSmartspace, adminEmail, size, page).stream().
				 map(elementEntity -> new ElementBoundary(elementEntity)).collect(Collectors.toList()).toArray(new ElementBoundary[0]);
	}

	@RequestMapping(
			method=RequestMethod.POST,
			path="/smartspace/admin/elements/{adminsmartspace}/{adminEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] importElements (
			@PathVariable("adminsmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail,
			@RequestBody ElementBoundary[] elementsToImport) {
			Collection<ElementEntity> elementsEntitiesToImport = ((Arrays.asList(elementsToImport))).stream().map(element -> element.convertToEntity()).collect(Collectors.toList());

			return this.elementService.store(adminSmartspace, adminEmail, elementsEntitiesToImport).stream().map(ElementBoundary::new).collect(Collectors.toList()).toArray(new ElementBoundary[0]);

	}

	@RequestMapping(
			method=RequestMethod.POST,
			path="/smartspace/elements/{managertSmartspace}/{managerEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary createNewElement (
			@PathVariable("managertSmartspace") String managertSmartspace,
			@PathVariable("managerEmail") String managerEmail,
			@RequestBody ElementBoundary elementBoundary){
			if (elementBoundary.getKey()!=null)
				throw new RuntimeException("Key must be null");
			return new ElementBoundary(
					elementUserManagerService.store(managertSmartspace,managerEmail,elementBoundary.convertToEntity()));
	}

	@RequestMapping(
			method=RequestMethod.PUT,
			path="/smartspace/elements/{managertSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateElement (
			@PathVariable("managertSmartspace") String managertSmartspace,
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementSmartspace") String elementSmartspace,
			@PathVariable("elementId") String elementId,
			@RequestBody ElementBoundary elementBoundary){
		if (elementBoundary.getKey()!=null)
			throw new RuntimeException("Key must be null");
		elementUserManagerService.update(managertSmartspace,managerEmail,elementSmartspace,elementId,elementBoundary.convertToEntity());

	}

	@RequestMapping(
			method=RequestMethod.GET,
			path="/smartspace/elements/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary getElement (
			@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("elementSmartspace") String elementSmartspace,
			@PathVariable("elementId") String elementId)
			{
				return new ElementBoundary(elementUserManagerService.get(userSmartspace,userEmail,elementSmartspace,elementId));

			}



	@RequestMapping(
			method=RequestMethod.GET,
			path="/smartspace/elements/{userSmartspace}/{userEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAll (
			@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="search",required = false) String search,
			@RequestParam(name="value",required = false) String value,
            @RequestParam(name="x",required = false) Double x,
            @RequestParam(name="y",required = false) Double y,
            @RequestParam(name="distance",required = false) Double distance,
           // @RequestParam(name="type",required = false) String type,
            @RequestParam(name="size", required=false, defaultValue="10") Integer size,
            @RequestParam(name="page", required=false, defaultValue="0") Integer page){
		List<ElementEntity> elementBoundaries=new ArrayList<>();
		if (search==null)
			elementBoundaries=elementUserManagerService.getAll(userSmartspace,userEmail,page,size);
		else if (search.equals("name"))
	        elementBoundaries= elementUserManagerService.getAllByName(userSmartspace,userEmail,value,page,size);
	    else if (search.equals("type"))
            elementBoundaries= elementUserManagerService.getAllByType(userSmartspace,userEmail,value,page,size);
        else if (search.equals("location"))
            elementBoundaries= elementUserManagerService.getAllByLocation(userSmartspace,userEmail,x,y,distance,page,size);
        return elementBoundaries.stream().map(elementEntity ->
				new ElementBoundary(elementEntity)).collect(Collectors.toList()).toArray(new ElementBoundary[0]);

    }


}
