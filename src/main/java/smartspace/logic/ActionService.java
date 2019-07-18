package smartspace.logic;

import java.util.Collection;
import java.util.List;

import smartspace.data.ActionEntity;

public interface ActionService {
	public List<ActionEntity> getAll(String adminSmartspace, String adminEmail, int size, int page);

	public Collection<ActionEntity> store(String adminSmartspace, String adminEmail, Collection<ActionEntity> actionEntitiesToImport);
}
