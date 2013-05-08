package app.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T> {
	public T save(T t); // returns null if object already exists or if t == null
	public T find(Serializable id); // returns null if not found or if id == null
	public T update(T t); // returns null if not found or if t == null
	public void delete(Serializable id); // always succeeds
	public List<T> getAll();
	public void deleteAll();
}