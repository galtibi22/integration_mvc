package smartspace.layout;

import smartspace.data.ActionEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

import java.util.Date;
import java.util.Map;

import jdk.nashorn.internal.runtime.Context.ThrowErrorManager;

public class ActionBoundary{
	private BoundaryKey actionKey;
    private String type;
    private Date created;
    private BoundaryKey element;
    private UserKey player;
    private Map<String,Object> properties;
    
    public ActionBoundary() {}
    
    public ActionBoundary(ActionEntity entity) {
    	if (entity instanceof ActionEntity) {
    		this.actionKey = new BoundaryKey(entity.getKey(), entity.getActionSmartspace());
    	} else {
    		throw new RuntimeException("Trying to downcast an object that isn't an instance of ActionEntity to an ActionBoundary");
    		}
		this.type = entity.getActionType();
		this.created = entity.getCreationTimeStamp();
		this.element = new BoundaryKey(entity.getElementId(), entity.getElementSmartspace());
		this.player = new UserKey(entity.getPlayerSmartspace(), entity.getPlayerEmail());
		this.properties = entity.getMoreAttributes();
	}
    
    public BoundaryKey getActionKey() {
        return actionKey;
    }

    public void setActionKey(BoundaryKey actionKey) {
        this.actionKey = actionKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public BoundaryKey getElement() {
        return element;
    }

    public void setElement(BoundaryKey element) {
        this.element = element;
    }

    public UserKey getPlayer() {
        return player;
    }

    public void setPlayer(UserKey player) {
        this.player = player;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
// Check if boundary attributes are null before converting action Boundary to action Entity 
    public ActionEntity convertToEntity() {
    	ActionBoundary actionBoundary = this;
		ActionEntity actionEntity = new ActionEntity();
		if(actionBoundary.getType() != null) {
			actionEntity.setActionType(actionBoundary.getType());
		} else {
			actionEntity.setActionType(null);
		}
		if(actionBoundary.getProperties() != null) {
			actionEntity.setMoreAttributes(actionBoundary.getProperties());
		} else {
			actionEntity.setMoreAttributes(null);
		}
		if(actionBoundary.getCreated() != null) {
			actionEntity.setCreationTimeStamp(actionBoundary.getCreated());
		} else {
			actionEntity.setCreationTimeStamp(null);
		}
		if(actionBoundary.getElement().getId() != null) {
			actionEntity.setElementId(actionBoundary.getElement().getId());
		} else {
			actionEntity.setElementId(null);
		}
		if(actionBoundary.getElement().getSmartspace() != null) {
			actionEntity.setElementSmartspace(actionBoundary.getElement().getSmartspace());
		} else {
			actionEntity.setElementSmartspace(null);
		}
		if(actionBoundary.getPlayer().getEmail() != null) {
			actionEntity.setPlayerEmail(actionBoundary.getPlayer().getEmail());
		} else {
			actionEntity.setPlayerEmail(null);
		}
		if(actionBoundary.getPlayer().getSmartspace() != null) {
			actionEntity.setPlayerSmartspace(actionBoundary.getPlayer().getSmartspace());
		} else {
			actionEntity.setPlayerSmartspace(null);
		}
// No change should be applied to Entity key
		if(actionBoundary.getActionKey() != null) {
			actionEntity.setKey(actionBoundary.getActionKey().getId() + ActionEntity.KEY_DELIMETER + actionBoundary.getActionKey().getSmartspace());
			actionEntity.setActionSmartspace(actionBoundary.getActionKey().getSmartspace());
		} else {
	    	//actionEntity.setKey(null);
			actionEntity.setActionSmartspace(null);
	    }

        return actionEntity;
    }

	@Override
	public String toString() {
		return "ActionBoundary [actionKey=" + actionKey + ", type=" + type + ", created=" + created + ", element="
				+ element + ", player=" + player + ", properties=" + properties + "]";
	}
}
