package smartspace.dao;

import java.util.Date;
import java.util.List;

import smartspace.data.ActionEntity;

public interface ExtendedActionDao extends ActionDao {
	
	public List<ActionEntity> readAll (int size, int page);
	public List<ActionEntity> readAll (int size, int page, String sortBy);
	public List<ActionEntity> getActionsWithTimestampRange(
			Date fromDate, 
			Date toDate,
			int size, int page);

}
