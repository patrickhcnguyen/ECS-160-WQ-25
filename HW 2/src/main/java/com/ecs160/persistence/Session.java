package com.ecs160.persistence;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import redis.clients.jedis.Jedis;

// Assumption - only support int/long/and string values

/*
this class essentially manages the redis
persistence operations for objects with the @Persistable annotation
*/

public class Session {
    private final Jedis jedis;
    private final List<Object> pendingObjects;

    public Session(String host, int port) {
        this.jedis = new Jedis(host, port);
        this.pendingObjects = new ArrayList<>();
    }

    public void add(Object obj) {
        if (obj == null || !obj.getClass().isAnnotationPresent(Persistable.class)) {
            throw new IllegalArgumentException("Object must be annotated with @Persistable");
        }
        pendingObjects.add(obj);
    }

    public void persistAll() {
        for (Object obj : pendingObjects) {
            persistObject(obj);
        }
        pendingObjects.clear();
    }

    // load object from redis using its sequential ID
    public Object load(Object object) throws Exception {
        if (object == null || !object.getClass().isAnnotationPresent(Persistable.class)) {
            throw new IllegalArgumentException("Object must be annotated with @Persistable");
        }

        String id = getIdValue(object);

        Map<String, String> storedData = jedis.hgetAll(getRedisKey(object.getClass(), id));
        if (storedData.isEmpty()) {
            return null;
        }

        return populateObject(object.getClass(), storedData);
    }

    private void persistObject(Object obj) {
        try {
            String id = getIdValue(obj);

            /* this hashmap stores field names as keys and their values as strings ie:
            {
                "postContent": "This is the main post text...",
            } */

            Map<String, String> fieldMap = new HashMap<>();

            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                if (field.isAnnotationPresent(PersistableField.class)) {
                    Object value = field.get(obj);
                    if (value != null) {
                        fieldMap.put(field.getName(), value.toString());
                    }
                }
                else if (field.isAnnotationPresent(PersistableListField.class)) {
                    List<?> list = (List<?>) field.get(obj);
                    if (list != null) {
                        String listIds = list.stream()
                                .map(this::getIdValue)
                                .filter(Objects::nonNull)
                                .collect(Collectors.joining(","));
                        fieldMap.put(field.getName(), listIds);
                    }
                }
            }

            String redisKey = getRedisKey(obj.getClass(), id);
            jedis.hmset(redisKey, fieldMap);

        } catch (Exception e) {
            throw new RuntimeException("Error persisting object", e);
        }
    }

    private String getIdValue(Object obj) {
        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(PersistableId.class)) {
                    Object value = field.get(obj);
                    return value != null ? value.toString() : null;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error getting ID value", e);
        }
        return null;
    }

    private Object populateObject(Class<?> clazz, Map<String, String> data) throws Exception {
        Object instance = clazz.getDeclaredConstructor().newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String value = data.get(field.getName());

            if (value != null) {
                if (field.isAnnotationPresent(PersistableField.class) ||
                        field.isAnnotationPresent(PersistableId.class)) {
                    setFieldValue(field, instance, value);
                }
                else if (field.isAnnotationPresent(PersistableListField.class)) {
                    PersistableListField annotation = field.getAnnotation(PersistableListField.class);
                    List<Object> list = new ArrayList<>();

                    if (!value.isEmpty()) {
                        String[] ids = value.split(",");
                        Class<?> elementClass = Class.forName(annotation.className());

                        for (String id : ids) {
                            Object elementInstance = elementClass.getDeclaredConstructor().newInstance();
                            setIdField(elementInstance, id.trim());
                            Object loadedObject = load(elementInstance);
                            if (loadedObject != null) {
                                list.add(loadedObject);
                            }
                        }
                    }
                    field.set(instance, list);
                }
            }
        }

        return instance;
    }

    private void setFieldValue(Field field, Object instance, String value) throws Exception {
        Class<?> type = field.getType();
        if (type == String.class) {
            field.set(instance, value);
        } else if (type == Integer.class || type == int.class) {
            field.set(instance, Integer.parseInt(value));
        } else if (type == Long.class || type == long.class) {
            field.set(instance, Long.parseLong(value));
        } else if (type == Boolean.class || type == boolean.class) {
            field.set(instance, Boolean.parseBoolean(value));
        }
    }

    private void setIdField(Object obj, String id) throws Exception {
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(PersistableId.class)) {
                setFieldValue(field, obj, id);
                return;
            }
        }
    }

    // generate redis key
    private String getRedisKey(Class<?> clazz, String id) {
        return clazz.getName() + ":" + id;
    }

    public void close() {
            jedis.close();
    }
}