package co.edu.unbosque.periodicazo.service;

import java.util.List;

public interface CRUDoperation <T>{
	
	public int create(T data);
	
	public List<T> getAll();
	
	public int updateByID(Long id, T data);
	
	public int deleteByID (Long id);
	
	
}
