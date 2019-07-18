package smartspace.dao.rdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.AppProperties;
import smartspace.dao.ExtendedElementDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class RdbElementDao implements ExtendedElementDao<String> {

	private ElementCrud elementCrud;
	private IdGeneratorCrud idGeneratorCrud;
	private AppProperties appProperties;

	@Autowired
	public RdbElementDao(ElementCrud elementCrud, IdGeneratorCrud idGeneratorCrud, AppProperties appProperties) {
		super();
		this.elementCrud = elementCrud;
		this.idGeneratorCrud = idGeneratorCrud;
		this.appProperties = appProperties;
	}

	@Override
	@Transactional
	public ElementEntity create(ElementEntity entity) {
		// Getting new Id via IdGenerator mechanism
		IdGeneratorEntity nextId = this.idGeneratorCrud.save(new IdGeneratorEntity());

		// setting the new ID to the new element
		entity.setKey(nextId.getNextId() + ElementEntity.KEY_DELIM + appProperties.getName());
		this.idGeneratorCrud.delete(nextId);

		// saving to the DB
		return this.elementCrud.save(entity);
	}
	
	@Override
	public ElementEntity insert(ElementEntity element) {
		// Getting new Id via IdGenerator mechanism
		IdGeneratorEntity nextId = this.idGeneratorCrud.save(new IdGeneratorEntity());

		// setting the new ID to the new element
		element.setKey(nextId.getNextId() + ElementEntity.KEY_DELIM + element.getElementSmartspace());
		this.idGeneratorCrud.delete(nextId);

		// saving to the DB
		return this.elementCrud.save(element);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Optional<ElementEntity> readById(String id) {
		return elementCrud.findById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAll() {
		List<ElementEntity> rv = new ArrayList<>();
		this.elementCrud.findAll().forEach(element -> rv.add(element));
		return rv;
	}

	@Override
	@Transactional
	public void update(ElementEntity elementToUpdate) {
		ElementEntity existing = this.readById(elementToUpdate.getKey())
				.orElseThrow(() -> new RuntimeException("no message with id: " + elementToUpdate.getKey()));

		// Patching
		if (elementToUpdate.getExpired() != null) {
			existing.setExpired(elementToUpdate.getExpired());
		}
		
		if (elementToUpdate.getLocation() != null) {
			existing.setLocation(elementToUpdate.getLocation());
		}
		
		if (elementToUpdate.getMoreAttributes() != null) {
			existing.setMoreAttributes(elementToUpdate.getMoreAttributes());
		}
		
		if (elementToUpdate.getName() != null) {
			existing.setName(elementToUpdate.getName());
		}
		
		if (elementToUpdate.getType() != null) {
			existing.setType(elementToUpdate.getType());
		}
		
		this.elementCrud.save(existing);
	}

	@Override
	@Transactional
	public void deleteByKey(String id) {
		this.elementCrud.deleteById(id);
	}

	@Override
	public void delete(ElementEntity elementEntity) {
		this.elementCrud.delete(elementEntity);
	}

	@Override
	public void deleteAll() {
		this.elementCrud.deleteAll();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAll(int size, int page) {
		return this.elementCrud
				.findAll(PageRequest.of(page, size))
				.getContent();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAll(int size, int page, String sortBy) {
		return this.elementCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, sortBy))
				.getContent();
	}

	//TODO fix to use PageRequest
	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAll(int size, int page, double x, double y, double distance) {
		return this.elementCrud.getElementsNearLocation(distance, x, y, PageRequest.of(page, size));
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAll(int size, String name, int page) {
		List<ElementEntity> entities= elementCrud.findAllByNameLike(name, PageRequest.of(page, size));

		return entities;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAll(String type, int size, int page) {
		List<ElementEntity> entities= elementCrud.findAllByTypeLike(type, PageRequest.of(page, size));
		return entities;
	}

	@Override
	public ElementEntity find(String elementId, String elementSmartspace) {
		ElementEntity elementEntity=readById(elementId + UserEntity.KEY_DELIM + elementSmartspace).orElseThrow(()->new RuntimeException("Cannot find Element with elementId "+elementId));
		if (!elementEntity.getElementSmartspace().equals(elementSmartspace))
			throw new RuntimeException("Cannot find Element entity with elementId "+elementId+" and elementSmartspace "+ elementSmartspace);
		return elementEntity;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> getElementsWithTimestampRange(
			Date fromDate, 
			Date toDate, int size, int page) {
		return 
			this.elementCrud
				.findAllByCreationTimeStampBetween(
						fromDate, 
						toDate, 
						PageRequest.of(page, size));
	}
	
	@Override
	public ElementEntity updateOrInsert(ElementEntity element) {
		ElementEntity existing = 
				this.readById(element.getKey()).orElse(null);
		
		if (existing == null) {
			return insert(element);
		} else {
			update(element);
			return this.readById(element.getKey()).get();
		}
	}
}
