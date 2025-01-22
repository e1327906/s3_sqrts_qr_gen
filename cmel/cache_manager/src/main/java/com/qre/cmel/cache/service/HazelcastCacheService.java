package com.qre.cmel.cache.service;


import com.hazelcast.map.IMap;

import java.util.concurrent.ConcurrentMap;

public interface HazelcastCacheService<K,V> {

    ConcurrentMap<K, V> getHazelcastMap(String mapName);

    IMap<K, V> getHazelcastIMap(String mapName);

    long getNextValue(String atomicLongName);
}
