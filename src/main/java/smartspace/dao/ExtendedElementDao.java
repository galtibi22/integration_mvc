package smartspace.dao;

import java.util.Date;
import java.util.List;

import smartspace.data.ElementEntity;

public interface ExtendedElementDao<ElementKey> extends ElementDao<ElementKey> {
	
	public List<ElementEntity> readAll (int size, int page);
	public List<ElementEntity> readAll (int size, int page, String sortBy);
	//public List<ElementEntity> readAll (int size, int page,String userSmartspace,String userEmail);
	public List<ElementEntity> readAll(int size,int page,/*String userSmartspace,String userEmail,*/double x,double y,double distance);
	public List<ElementEntity> readAll(int size,String name,int page/*,String userSmartspace,String userEmail,*/);
	public List<ElementEntity> readAll(String type,int size,int page/*,String userSmartspace,String userEmail*/);

	public ElementEntity find (String elementId,String elementSmartspace);


	public List<ElementEntity> getElementsWithTimestampRange(
			Date fromDate, 
			Date toDate,
			int size, int page);
	public ElementEntity		updateOrInsert (ElementEntity element);
	public ElementEntity		insert (ElementEntity element);


}
