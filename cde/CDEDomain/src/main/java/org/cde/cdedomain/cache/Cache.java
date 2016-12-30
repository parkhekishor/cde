package org.cde.cdedomain.cache;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public  class Cache {
	
	//private static Logger logger_ =   LoggerFactory.getLogger(Cache.class);
	
	private static Map<String, Object> cache = new ConcurrentHashMap<String, Object>();
	private static boolean objectInUse = false;

	public static  boolean setCacheData(String id, Object data) {
		//logger_.warn("<<  setCacheData() : [ id : {}, data : {}]",id,data);
		
		if (data == null) {
//			logger_.warn(">>  setCacheData() : [ return : {}]",false);
			return false;
		}
		Cache.cache.put(id, data);
//		logger_.warn(">>  setCacheData() : [ return : {}]",true);
		return true;
	}

	public static Object getCacheData(String id) {
//		logger_.warn("<<  getCacheData()  :[ id : {}]",id);

		while (isObjectInUse()) {
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e) {
//				logger_.warn(" Cache is being updated. Exception caught in the wait for the method");
			}
		}

		Object object = Cache.cache.get(id);
		
//		logger_.warn("<<  getCacheData()  :[ return : {}]",object);

		return object;
	}

	

	public static boolean removeFromCache(String id) {
		
//		logger_.warn("<< removeFromCache() : [ id : {}]",id);
		
		while (isObjectInUse()) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
//				logger_.error("** Cache is being updated. Exception caught in the wait for the method",e);
			}
		}

		if(Cache.cache.containsKey(id)){
			setObjectInUse(true);
			Cache.cache.remove(id);
			setObjectInUse(false);
		}
		
//		logger_.warn("<< removeFromCache() : [ return : {}]",true);
		return true;
	}
	
	public static boolean clearCache() {
		
//		logger_.warn("<<  clearCache() ");
		
		while (isObjectInUse()) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
		}
		setObjectInUse(true);
		Cache.cache.clear();
		setObjectInUse(false);
		
//		logger_.warn(">>  clearCache()  :[ return : {}]",true);
		return true;
	}
	
	
	
	public static Object getCacheData(String id, boolean ignoreToken) {
		
//		logger_.warn("<<  getCacheData() : [  id : {},  ignoreToken  : {}]",id,ignoreToken);
		
	
		if (!ignoreToken)
			while (isObjectInUse()) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
//					logger_.warn(" Cache is being updated. Exception caught in the wait for the method");
				}
		}
		
//		logger_.warn(">>  getCacheData() : [ return : {} ]",Cache.cache.get(id));
		
		return Cache.cache.get(id);

	}

	public static boolean isAvailable(String id) {
		if (Cache.cache.containsKey(id))
			return true;
		return false;
	}

	public Map<String, Object> getCache() {
		return Cache.cache;
	}

	public void setCache(Map<String, Object> cache) {
		stCache(cache);
	}

	private static void stCache(Map<String, Object> cache) {
		Cache.cache = cache;
	}


	public static boolean isObjectInUse() {
		return objectInUse;
	}

	public static void setObjectInUse(boolean objectInUse) {
		Cache.objectInUse = objectInUse;
	}

	public static long getCacheSize() {
		return Cache.cache.size();
	}
}
