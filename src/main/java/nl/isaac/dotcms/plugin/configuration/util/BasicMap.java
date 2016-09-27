package nl.isaac.dotcms.plugin.configuration.util;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * An abstract map where all methods, save {@link #get(Object)} are effectively abstract, they throw {@link AbstractMethodError}.
 * This clears up classes that wish to provide some simple Map like properties without actually being a map. Mostly used for Velocity interaction.
 *
 * @author maarten
 *
 * @param <K> The Key type for the map
 * @param <V> The Value type for the map
 */
public abstract class BasicMap<K, V> implements Map<K, V> {

	@Override
	public abstract V get(Object key);


	@Override public int size() {
		throw new AbstractMethodError("BasicMap#size is not implemented");
	}
	@Override public boolean isEmpty() {
		throw new AbstractMethodError("BasicMap#isEmpty is not implemented");
	}
	@Override public boolean containsKey(Object key) {
		throw new AbstractMethodError("BasicMap#containsKey(Object) is not implemented");
	}
	@Override public boolean containsValue(Object value) {
		throw new AbstractMethodError("BasicMap#containsValue(Object) is not implemented");
	}
	@Override public V put(K key, V value) {
		throw new UnsupportedOperationException("BasicMap#put(K, V) is not supported");
	}
	@Override public V remove(Object key) {
		throw new UnsupportedOperationException("BasicMap#remove(Object) is not supported");
	}
	@Override public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException("BasicMap#putAll(Map) is not supported");
	}
	@Override public void clear() {
		throw new UnsupportedOperationException("BasicMap#clear is not supported");
	}
	@Override public Set<K> keySet() {
		throw new AbstractMethodError("BasicMap#keySet is not implemented");
	}
	@Override public Collection<V> values() {
		throw new AbstractMethodError("BasicMap#values is not implemented");
	}
	@Override public Set<java.util.Map.Entry<K, V>> entrySet() {
		throw new AbstractMethodError("BasicMap#values is not implemented");
	}
}
