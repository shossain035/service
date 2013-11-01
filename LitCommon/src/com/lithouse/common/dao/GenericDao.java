package com.lithouse.common.dao;

import java.util.List;


public interface GenericDao {
	public < T > T save ( T t );
	public < T, H > T find ( Class < T > clazz, H hashKey );
	public < T, H, R > T find ( Class < T > clazz, H hashKey, R rangeKey );
	public < T > void remove ( T t );
	public < T > List < T > queryItems ( Class <T> clazz, T hashKeyObject, DBFilter < String > filter );
	public < T > List < T > queryItems ( Class <T> clazz, T hashKeyObject );	
}
