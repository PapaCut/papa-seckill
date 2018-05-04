package org.papa.canal.server.redis;

import java.io.*;
import java.util.*;

/**
 * Created by (Even wu for yoju) on 2018/4/1.
 *
 */
public class SerializableRedisService extends AbstractRedisService<Serializable>{
    @Override
    public void setexObject(String key, int seconds, Serializable value)
            throws Exception {
        setexAsByteArray(key, seconds, serialize(value));
    }

    @Override
    public void setObject(String key, Serializable value) throws Exception {
        setAsByteArray(key, serialize(value));
    }

    @Override
    public Serializable getObject(String key) throws Exception {
        if(get(key) != null)
            return deserialize(getAsByteArray(key));
        else
            return null;
    }

    @Override
    public void hmsetObject(String key, Map<String, Serializable> hash)
            throws Exception {
        Map<String, byte[]> byteHash = new HashMap<String, byte[]>();
        for (String hkey : hash.keySet()) {
            Serializable obj = hash.get(hkey);
            byteHash.put(hkey, serialize(obj));
        }
        hmset(key, byteHash);
    }

    @Override
    public List<Serializable> hmgetObject(String key, String... fields)
            throws Exception {
        List<Serializable> list = new ArrayList<Serializable>();
        List<byte[]> byteHash = hmget(key, fields);
        for (byte[] bytes : byteHash) {
            list.add(deserialize(bytes));
        }
        return list;
    }

    @Override
    public boolean sismemberObject(String key, Serializable value)
            throws Exception {
        byte[] bytes = serialize(value);
        return sismember(key, bytes);
    }

    @Override
    public void saddObject(String key, Serializable value) throws Exception {
        byte[] bytes = serialize(value);
        saddObject(key, bytes);
    }

    @Override
    public Set<Serializable> smembersObject(String key) throws Exception {
        Set<byte[]> bytes = smembers(key);
        Set<Serializable> set = new HashSet<Serializable>();
        for (byte[] b : bytes) {
            set.add(deserialize(b));
        }
        return set;
    }

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        return bos.toByteArray();
    }

    private Serializable deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (Serializable)ois.readObject();
    }
}
