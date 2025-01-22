package com.qre.cmel.cache.service.impl;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;
import com.hazelcast.map.IMap;
import com.qre.cmel.cache.service.HazelcastCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentMap;


@Service
public class HazelcastCacheServiceImpl<K,V> implements HazelcastCacheService{

    @Autowired
    private HazelcastInstance hazelcastInstance;

    public ConcurrentMap<K, V> getHazelcastMap(String mapName) {
        return hazelcastInstance.getMap(mapName);
    }

    public IMap<K, V> getHazelcastIMap(String mapName) {
        return hazelcastInstance.getMap(mapName);
    }

    @Override
    public long getNextValue(String atomicLongName) {
        IAtomicLong atomicLong = hazelcastInstance.getCPSubsystem().getAtomicLong(atomicLongName);
        return atomicLong.incrementAndGet();
    }

}
