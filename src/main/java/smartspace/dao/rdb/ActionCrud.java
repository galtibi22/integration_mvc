package smartspace.dao.rdb;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import smartspace.data.ActionEntity;

public interface ActionCrud extends PagingAndSortingRepository<ActionEntity, String> {
	
	public List<ActionEntity> 
	findAllByActionTypeLike(
			@Param("actionType") String actionType, 
			Pageable pageable);

	public List<ActionEntity> 
		findAllByCreationTimeStampBetween(
				@Param("fromDate") Date fromDate, 
				@Param("toDate") Date toDate, 
				Pageable pageable);

}
