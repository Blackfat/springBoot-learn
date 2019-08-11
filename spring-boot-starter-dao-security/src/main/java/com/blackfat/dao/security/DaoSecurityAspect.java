package com.blackfat.dao.security;

import com.blackfat.dao.security.annotation.DaoSecurity;
import com.blackfat.dao.security.annotation.DaoSecurityParam;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author wangfeiyang
 * @Description
 * @create 2019-07-29 06:59
 * @since 1.0-SNAPSHOT
 */
@Aspect
@Component
public class DaoSecurityAspect {

    private static final String STRING_ENCODE = "utf-8";

    public DaoSecurityAspect() {
    }

    @Pointcut("@annotation(com.blackfat.dao.security.annotation.DaoSecurityEncrypt)")
    public void matchDaoMethodEncrypt() {
    }

    @Pointcut("@annotation(com.blackfat.dao.security.annotation.DaoSecurityDecrypt)")
    public void matchDaoMethodDecrypt() {
    }

    @Around("matchDaoMethodEncrypt()")
    public Object aroundEncrypt(ProceedingJoinPoint joinPoint) throws Throwable {
        return this.getObject(joinPoint, true);
    }

    @Around("matchDaoMethodDecrypt()")
    public Object aroundDecrypt(ProceedingJoinPoint joinPoint) throws Throwable {
        return this.getObject(joinPoint, false);
    }

    private Object getObject(ProceedingJoinPoint joinPoint, Boolean isEncrypt) throws Throwable{
        Class clazz = joinPoint.getTarget().getClass();
        if(!clazz.isAnnotationPresent(DaoSecurity.class)){
            return joinPoint.proceed(joinPoint.getArgs());
        }else {
            DaoSecurity daoSecurity = (DaoSecurity)joinPoint.getTarget().getClass().getAnnotation(DaoSecurity.class);
            Class modelCls = daoSecurity.value();
            String keyName = daoSecurity.keyName();
            DaoSecurityParam[] params = daoSecurity.params();
            if (isEncrypt) {
                Object[] args = joinPoint.getArgs();
                Object[] argsCopy = args;

                for(int i = 0; i < argsCopy.length; ++i) {
                    Object arg = argsCopy[i];
                    this.encrypt(arg, modelCls, keyName, params, true);
                }
            }

            Object result = joinPoint.proceed(joinPoint.getArgs());
            if (!isEncrypt) {
                this.encrypt(result, modelCls, keyName, params, false);
            }
            return result;

        }

    }

    private Object encrypt(Object object, Class cls, String securityKeyParam, DaoSecurityParam[] daoSecurityParams, Boolean isEncrypt) throws Throwable {
        Iterator iterator;
        Object obj;
        if (Collection.class.isInstance(object)) {
            Collection collection = (Collection)object;
            iterator = collection.iterator();

            while(iterator.hasNext()) {
                obj = iterator.next();
                this.encrypt(obj, cls, securityKeyParam, daoSecurityParams, isEncrypt);
            }
        } else if (Map.class.isInstance(object)) {
            Map map = (Map)object;
            iterator = map.values().iterator();

            while(iterator.hasNext()) {
                obj = iterator.next();
                this.encrypt(obj, cls, securityKeyParam, daoSecurityParams, isEncrypt);
            }
        } else if (cls.isInstance(object)) {
            Field keyField = object.getClass().getDeclaredField(securityKeyParam);
            keyField.setAccessible(true);
            Object objKey = keyField.get(object);
            if (objKey == null || objKey.toString().length() == 0) {
                throw new DaoSecurityException("Invalid data key");
            }

            String key = objKey.toString();
            Field[] declaredFields = object.getClass().getDeclaredFields();
            Field[] var10 = declaredFields;
            int var11 = declaredFields.length;

            for(int var12 = 0; var12 < var11; ++var12) {
                Field declaredField = var10[var12];
                declaredField.setAccessible(true);
                Object fieldObj = declaredField.get(object);
                Iterator var16;
                Object mapObj;
                if (Collection.class.isInstance(fieldObj)) {
                    Collection collection = (Collection)fieldObj;
                    var16 = collection.iterator();

                    while(var16.hasNext()) {
                        mapObj = var16.next();
                        if (cls.isInstance(mapObj)) {
                            this.encrypt(mapObj, cls, securityKeyParam, daoSecurityParams, isEncrypt);
                        }
                    }
                } else if (Map.class.isInstance(fieldObj)) {
                    Map map = (Map)fieldObj;
                    var16 = map.values().iterator();

                    while(var16.hasNext()) {
                        mapObj = var16.next();
                        if (cls.isInstance(mapObj)) {
                            this.encrypt(mapObj, cls, securityKeyParam, daoSecurityParams, isEncrypt);
                        }
                    }
                } else if (cls.isInstance(fieldObj)) {
                    this.encrypt(fieldObj, cls, securityKeyParam, daoSecurityParams, isEncrypt);
                } else {
                    DaoSecurityParam param = this.getParam(daoSecurityParams, declaredField.getName());
                    if (param != null) {
                        this.encryptParam(object, declaredField, fieldObj, key, param, isEncrypt);
                    }
                }
            }
        }

        return object;
    }


    private void encryptParam(Object object, Field field, Object obj, String key, DaoSecurityParam param, Boolean isEncrypt) throws Throwable {
        Object newObj = null;
        if (String.class.isInstance(obj)) {
            String objStr = (String)obj;
            if (objStr.length() > 0) {
                if (isEncrypt) {
                    if (param.maxLength() > 0 && !DaoSecurityUtil.isSafe(objStr) && this.getBytesLength(objStr) > param.maxLength()) {
                        throw new DaoSecurityException("Invalid data length");
                    }

                    if (param.searchName().length() > 0) {
                        Field searchField = object.getClass().getDeclaredField(param.searchName());
                        searchField.setAccessible(true);
                        searchField.set(object, DaoSecurityUtil.generateKey(DaoSecurityUtil.decrypt(key, objStr)));
                    }

                    newObj = DaoSecurityUtil.encrypt(key, objStr);
                } else {
                    newObj = DaoSecurityUtil.decrypt(key, objStr);
                }
            }
        } else if (Long.class.isInstance(obj)) {
            Long objLong = (Long)obj;
            if (isEncrypt) {
                newObj = DaoSecurityUtil.encrypt(key, objLong);
            } else {
                newObj = DaoSecurityUtil.decrypt(key, objLong);
            }
        }

        if (newObj != null) {
            field.set(object, newObj);
        }

    }

    private DaoSecurityParam getParam(DaoSecurityParam[] daoSecurityParams, String param) {
        DaoSecurityParam[] var3 = daoSecurityParams;
        int var4 = daoSecurityParams.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            DaoSecurityParam daoSecurityParam = var3[var5];
            if (daoSecurityParam.value().equals(param)) {
                return daoSecurityParam;
            }
        }

        return null;
    }

    private int getBytesLength(String content) {
        try {
            int len = content.getBytes(STRING_ENCODE).length;
            return len;
        } catch (Throwable var3) {
            var3.printStackTrace();
            return 0;
        }
    }

}
