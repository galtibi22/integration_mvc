package smartspace.dao.rdb;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import smartspace.data.ElementEntity;

public interface ElementCrud extends PagingAndSortingRepository<ElementEntity, String> {
	
	public List<ElementEntity> 
	findAllByNameLike(
			@Param("name") String name, 
			Pageable pageable);
	public List<ElementEntity>
	findByNameAllIgnoreCase(
			String name,
			Pageable pageable);
	public List<ElementEntity>
	findAllByTypeLike(
			@Param("type") String type,
			Pageable pageable);
	public List<ElementEntity> 
		findAllByCreationTimeStampBetween(
				@Param("fromDate") Date fromDate, 
				@Param("toDate") Date toDate, 
				Pageable pageable);
	
	@Query(value = "select * from elements where SQRT((x - :x) * (x - :x) + (y - :y) * (y - :y)) <= :distance", nativeQuery = true)
	public List<ElementEntity>
		getElementsNearLocation(
				@Param("distance") double distance, 
				@Param("x") double x,
				@Param("y") double y,
				Pageable pageable);
}
