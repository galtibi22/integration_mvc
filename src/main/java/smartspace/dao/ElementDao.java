package smartspace.dao;

import java.util.List;
import java.util.Optional;

import smartspace.data.ElementEntity;

public interface ElementDao<ElementKey> {
	ElementEntity create(ElementEntity entity);
	Optional<ElementEntity> readById(ElementKey elementKey);
	List<ElementEntity> readAll();
	void update(ElementEntity elementToUpdate);
	void deleteByKey(ElementKey elementKey);
	void delete(ElementEntity elementEntity);
	void deleteAll();
}
