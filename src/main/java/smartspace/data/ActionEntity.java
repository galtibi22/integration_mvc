package smartspace.data;

import javax.persistence.*;

import smartspace.dao.rdb.MapToJsonConverter;

import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "ACTIONS")
public class ActionEntity implements SmartSpaceEntity<String> {

	public static final String KEY_DELIMETER = "_";
	private String actionSmartspace;
	private String actionId;
	private String elementSmartspace;
	private String elementId;
	private String playerSmartspace;
	private String playerEmail;
	private String actionType;
	private java.util.Date creationTimeStamp;
	private Map<String, Object> moreAttributes;

	public ActionEntity(String actionSmartspace,String elementSmartspace,Date creationTimeStamp,String elementId,String playerSmartspace, String playerEmail,String actionType, Map<String, Object> moreAttributes) {
		this.actionSmartspace = actionSmartspace;
		this.elementSmartspace = elementSmartspace;
		this.elementId = elementId;
		this.playerSmartspace = playerSmartspace;
		this.playerEmail = playerEmail;
		this.actionType = actionType;
		this.moreAttributes = moreAttributes;
		this.creationTimeStamp=creationTimeStamp;
	}
	public ActionEntity() {}

	@Column(name="ID")
	@Id
	@Override
	public String getKey() {
		return this.actionId + KEY_DELIMETER + this.actionSmartspace;
	}

	@Override
	public void setKey(String key) {
		String[] temp = key.split(KEY_DELIMETER);
		this.actionId = temp[0];
		this.actionSmartspace = temp[1];
	}

	public String getActionSmartspace() {
		return this.actionSmartspace;
	}

	public void setActionSmartspace(String actionSmartspace) {
		this.actionSmartspace = actionSmartspace;
	}

	public String getElementSmartspace() {
		return elementSmartspace;
	}

	public void setElementSmartspace(String elementSmartspace) {
		this.elementSmartspace = elementSmartspace;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getPlayerSmartspace() {
		return playerSmartspace;
	}

	public void setPlayerSmartspace(String playerSmartspace) {
		this.playerSmartspace = playerSmartspace;
	}

	public String getPlayerEmail() {
		return playerEmail;
	}

	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(Date creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}
	
	@Lob
	@Convert(converter=MapToJsonConverter.class)
	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}
	@Override
	public String toString() {
		return "ActionEntity [actionSmartspace=" + actionSmartspace + ", actionId=" + getKey() + ", elementSmartspace="
				+ elementSmartspace + ", elementId=" + elementId + ", playerSmartspace=" + playerSmartspace
				+ ", playerEmail=" + playerEmail + ", actionType=" + actionType + ", creationTimeStamp="
				+ creationTimeStamp + ", moreAttributes=" + moreAttributes + "]";
	}

	
}
