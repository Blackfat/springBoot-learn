package com.blackfat.kernel.ability.engine;

import com.blackfat.kernel.ability.core.ExtendAbility;
import com.blackfat.kernel.ability.core.ExtendAbilityOperation;
import com.blackfat.kernel.ability.core.ExtendAbilityOperationImpl;
import com.blackfat.kernel.ability.entity.AbilityEntity;
import com.blackfat.kernel.ability.entity.OperationEntity;
import com.blackfat.kernel.ability.entity.OperationImplEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;


@Slf4j
@Component("abilityEngine")
public class AbilityEngine implements ApplicationContextAware {

    public static final int METHOD_OPERATION_MAX_COUNT = 1;
    private ApplicationContext applicationContext;
    /**
     * include businessId -- used operationCodes
     */
    private Map<String, List<String>> productConfig;

    /**
     * include ExtendAbilityOperation -- List operationCodes
     */
    private Map<Class<?>, List<String>> operationMap;

    /**
     * include ExtendAbilityOperation -- List operationCodes <br/>
     * 与 operationMap 映射内容基本一致，增加了配置信息
     */
    private Map<Class<?>, List<OperationImplEntity>> operationImplMap;
    /**
     * abilityInterface -- ability配置信息
     */
    private Map<Class<?>, AbilityEntity> abilityMap;

    @PostConstruct
    public void init() {
        initAbilityTree();

        initOperationConfig();

        initProductConfig();

    }

    private void initAbilityTree() {
        abilityMap = Collections.synchronizedMap(new HashMap<>(20));
    }

    private void initOperationConfig() {

        Map<String, Object> operations = applicationContext.getBeansWithAnnotation(ExtendAbilityOperationImpl.class);
        if (!operations.isEmpty()) {
            operationMap = Collections.synchronizedMap(new HashMap<>(20));
            operationImplMap = Collections.synchronizedMap(new HashMap<>(20));

            for (Map.Entry<String, Object> entry : operations.entrySet()) {
                parseOperationImpl(entry);
            }

        } else {
            log.warn(" operations is empty");
        }
    }

    private void parseOperationImpl(Map.Entry<String, Object> entry) {
        Class<?>[] interfaces = entry.getValue().getClass().getInterfaces();
        ExtendAbilityOperationImpl[] annotations =
                entry.getValue().getClass().getAnnotationsByType(ExtendAbilityOperationImpl.class);
        if (interfaces.length > 0) {
            for (Class<?> anInterface : interfaces) {
                ExtendAbilityOperation[] type = anInterface.getAnnotationsByType(ExtendAbilityOperation.class);
                if (type.length > 0) {
                    List<String> list = operationMap.computeIfAbsent(anInterface, k -> new ArrayList<>(20));
                    List<OperationImplEntity> list2 =
                            operationImplMap.computeIfAbsent(anInterface, k -> new ArrayList<>(20));
                    list.add(entry.getKey());
                    list2.add(new OperationImplEntity(annotations[0]));
                } else {
                    log.warn("this {} class is not a ExtendAbilityOperation ", anInterface);
                }
            }
        } else {
            log.warn("this {} class has no interface ", entry.getValue().getClass());
        }
    }

    private void initProductConfig() {
        productConfig = Collections.synchronizedMap(new HashMap<>(20));
//        for (BusinessOperationConfig config : EngineConfig.configs) {
//            productConfig.put(config.getBusinessId(), new ArrayList<>(config.getOperationCodesList()));
//        }
    }

    /**
     * 根据 业务id和 扩展能力方法，获取其对应的 执行实例
     *
     * @param businessId 业务id，当前为产品id
     * @param operationClass 声明了 ExtendAbilityOperation 的操作接口
     * @param <T> T
     * @return 这个业务id在这个扩展操作配置的操作实例
     */
    public <T> T getAbility(String businessId, Class<T> operationClass) {
        List<String> operationImplList = getOperationImplCodeList(operationClass);
        List<String> businessIdImplsList = getBusinessIdOperationCodeList(businessId);
        operationImplList.retainAll(businessIdImplsList);

        if (operationImplList.size() > METHOD_OPERATION_MAX_COUNT) {
            log.error("found for one operationClass config multiple implement {},{},{}", businessId, operationClass,
                    StringUtils.join(operationImplList));
            throw new RuntimeException(
                    "found for one operationClass config multiple implement businessId:" + businessId
                            + "  operationClass:" + operationClass + " implement:" + StringUtils.join(operationImplList));
        } else if (operationImplList.size() == METHOD_OPERATION_MAX_COUNT) {
            return (T)this.applicationContext.getBean(operationImplList.get(0));
        }
        return null;
    }

    /**
     * 根据 业务id和 扩展能力方法，获取其对应的 执行实例
     *
     * @param businessId 业务id，当前为产品id
     * @param abilityClass 声明了 ExtendAbility 的操作接口
     * @param methodName 方法名
     * @return 这个业务id在这个扩展操作配置的操作实例
     */
    public Object getAbility(String businessId, Class<?> abilityClass, String methodName) {
        AbilityEntity entity = this.abilityMap.get(abilityClass);
        if (entity != null) {
            OperationEntity op = entity.getMethodOperationMap().get(methodName);
            if (op != null) {
                return getAbility(businessId, op.getOperationInterface());
            } else {
                log.warn("this {} interface not a ExtendAbilityOperation", entity);
            }
        } else {
            log.warn("not found ExtendAbilityOperation on this class {}", abilityClass);
        }
        return null;
    }

    /**
     * 获取所有 能力扩展点配置
     *
     * @return 所有扩展点信息
     */
    public ArrayList<AbilityEntity> getAllAbility() {
        return new ArrayList<>(abilityMap.values());
    }

    /**
     * 获取某个操作的实例列表
     *
     * @param operationInterface 操作的接口类型
     * @return 实例信息列表
     */
    public ArrayList<OperationImplEntity> getOperationImpl(Class<?> operationInterface) {
        List<OperationImplEntity> entities = this.operationImplMap.get(operationInterface);
        return entities != null ? new ArrayList<>(entities) : new ArrayList<>();
    }

    /**
     * 获取所有配置了的业务id
     *
     * @return 业务id列表
     */
    public ArrayList<String> getAbilityBusinessIdList() {
        return new ArrayList<>(productConfig.keySet());
    }

    /**
     * 根据业务id，获取配置的操作编码
     *
     * @param businessId 业务id
     * @return 操作编码表
     */
    public ArrayList<String> getBusinessIdOperationCodeList(String businessId) {
        return new ArrayList<>(productConfig.getOrDefault(businessId, new ArrayList<>()));
    }

    /**
     * 更具操作接口，返回实现操作接口的 operationCode 列表
     *
     * @param operationInterface 操作接口
     * @return code 列表
     */
    public ArrayList<String> getOperationImplCodeList(Class<?> operationInterface) {
        return new ArrayList<>(operationMap.getOrDefault(operationInterface, new ArrayList<>()));
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected void addAbilityInterface(Class<?> abilityInterface) {

        ExtendAbility[] annotations = abilityInterface.getAnnotationsByType(ExtendAbility.class);
        if (annotations.length != 1) {
            log.warn("{} class ability is error", abilityInterface);
            return;
        }
        abilityMap.computeIfAbsent(abilityInterface, aClass -> getAbilityEntity(abilityInterface, annotations[0]));

    }

    private AbilityEntity getAbilityEntity(Class<?> abilityInterface, ExtendAbility annotation) {
        AbilityEntity entity = new AbilityEntity();
        entity.setAbilityInterface(abilityInterface).setByAnnotation(annotation);

        Class<?>[] interfaces = abilityInterface.getInterfaces();
        if (interfaces.length > 0) {
            for (Class<?> anInterface : interfaces) {
                ExtendAbilityOperation[] annotationsByType =
                        anInterface.getAnnotationsByType(ExtendAbilityOperation.class);
                if (annotationsByType.length == 1) {
                    Method[] declaredMethods = anInterface.getDeclaredMethods();
                    for (Method declaredMethod : declaredMethods) {
                        String name = declaredMethod.getName();
                        entity.addMethodOperation(name, new OperationEntity(anInterface, annotationsByType[0]));
                    }
                } else {
                    log.warn("this {} interface not a ExtendAbilityOperation", anInterface);
                }
            }
        } else {
            log.warn("not found ExtendAbilityOperation on this class {}", abilityInterface);
        }
        return entity;
    }
}

