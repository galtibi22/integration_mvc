package smartspace.logic;

import smartspace.data.ActionEntity;

public interface ActionUserManagerService {
	public ActionEntity invoke(ActionEntity newAction);
}
