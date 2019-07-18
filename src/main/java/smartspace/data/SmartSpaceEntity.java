package smartspace.data;

public interface SmartSpaceEntity<K> {
	K getKey();
	void setKey(K key);
}
