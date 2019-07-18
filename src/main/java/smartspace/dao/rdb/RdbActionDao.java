package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.data.ActionEntity;
import smartspace.AppProperties;
import smartspace.dao.ExtendedActionDao;

@Repository
public class RdbActionDao implements ExtendedActionDao {
	private ActionCrud actionCrud;
	private IdGeneratorCrud idGeneratorCrud;
	private AppProperties appProperties;

	@Autowired
	public RdbActionDao(ActionCrud actionCrud, IdGeneratorCrud idGeneratorCrud, AppProperties appProperties) {
		super();
		this.actionCrud = actionCrud;
		this.idGeneratorCrud = idGeneratorCrud;
		this.appProperties = appProperties;
	}
	
	@Override
	@Transactional
	public ActionEntity create(ActionEntity actionEntity) {
		// Getting new Id via IdGenerator mechanism
		IdGeneratorEntity nextId = this.idGeneratorCrud.save(new IdGeneratorEntity());
		
		// setting the new ID to the new action
		if (actionEntity.getActionSmartspace() != null)
			actionEntity.setKey(nextId.getNextId()+ ActionEntity.KEY_DELIMETER + actionEntity.getActionSmartspace());
		else
			actionEntity.setKey(nextId.getNextId()+ ActionEntity.KEY_DELIMETER + appProperties.getName());
		System.err.println(actionEntity.toString());
		this.idGeneratorCrud.delete(nextId);
		
		// saving to the DB
		return this.actionCrud.save(actionEntity);
	}

	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readAll() {
		List<ActionEntity> rv = new ArrayList<>();
		this.actionCrud
			.findAll()
			.forEach(action -> rv.add(action));
		return rv;
	}

	@Override
	@Transactional
	public void deleteAll() {
		this.actionCrud.deleteAll();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readAll(int size, int page) {
		return this.actionCrud
				.findAll(PageRequest.of(page, size))
				.getContent();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readAll(int size, int page, String sortBy) {
		return this.actionCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, sortBy))
				.getContent();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> getActionsWithTimestampRange(
			Date fromDate, 
			Date toDate, int size, int page) {
		return 
			this.actionCrud
				.findAllByCreationTimeStampBetween(
						fromDate, 
						toDate, 
						PageRequest.of(page, size));
	}

}
