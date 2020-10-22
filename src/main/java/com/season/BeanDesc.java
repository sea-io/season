package cn.hutool.core.bean;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ModifierUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class BeanDesc {
    private Class<?> beanClass;
    private Map<String, BeanDesc.PropDesc> propMap = new LinkedHashMap();

    public BeanDesc(Class<?> beanClass) {
        Assert.notNull(beanClass);
        this.beanClass = beanClass;
        this.init();
    }

    public String getName() {
        return this.beanClass.getName();
    }

    public String getSimpleName() {
        return this.beanClass.getSimpleName();
    }

    public Map<String, BeanDesc.PropDesc> getPropMap(boolean ignoreCase) {
        return (Map)(ignoreCase ? new CaseInsensitiveMap(1.0F, this.propMap) : this.propMap);
    }

    public Collection<BeanDesc.PropDesc> getProps() {
        return this.propMap.values();
    }

    public BeanDesc.PropDesc getProp(String fieldName) {
        return (BeanDesc.PropDesc)this.propMap.get(fieldName);
    }

    public Field getField(String fieldName) {
        BeanDesc.PropDesc desc = (BeanDesc.PropDesc)this.propMap.get(fieldName);
        return null == desc ? null : desc.getField();
    }

    public Method getGetter(String fieldName) {
        BeanDesc.PropDesc desc = (BeanDesc.PropDesc)this.propMap.get(fieldName);
        return null == desc ? null : desc.getGetter();
    }

    public Method getSetter(String fieldName) {
        BeanDesc.PropDesc desc = (BeanDesc.PropDesc)this.propMap.get(fieldName);
        return null == desc ? null : desc.getSetter();
    }

    private BeanDesc init() {
        Field[] arr$ = ReflectUtil.getFields(this.beanClass);
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Field field = arr$[i$];
            if (!ModifierUtil.isStatic(field)) {
                this.propMap.put(field.getName(), this.createProp(field));
            }
        }

        return this;
    }

    private BeanDesc.PropDesc createProp(Field field) {
        String fieldName = field.getName();
        Class<?> fieldType = field.getType();
        boolean isBooeanField = fieldType == Boolean.class || fieldType == Boolean.TYPE;
        Method getter = null;
        Method setter = null;
        Method[] arr$ = ReflectUtil.getMethods(this.beanClass);
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Method method = arr$[i$];
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length <= 1) {
                String methodName = method.getName();
                if (parameterTypes.length == 0) {
                    if (this.isMatchGetter(methodName, fieldName, isBooeanField)) {
                        getter = method;
                    }
                } else if (this.isMatchSetter(methodName, fieldName, isBooeanField)) {
                    setter = method;
                }

                if (null != getter && null != setter) {
                    break;
                }
            }
        }

        return new BeanDesc.PropDesc(field, getter, setter);
    }

    private boolean isMatchGetter(String methodName, String fieldName, boolean isBooeanField) {
        methodName = methodName.toLowerCase();
        fieldName = fieldName.toLowerCase();
        if (!methodName.startsWith("get") && !methodName.startsWith("is")) {
            return false;
        } else if ("getclass".equals(methodName)) {
            return false;
        } else {
            if (isBooeanField) {
                if (fieldName.startsWith("is")) {
                    if (methodName.equals(fieldName) || methodName.equals("get" + fieldName) || methodName.equals("is" + fieldName)) {
                        return true;
                    }
                } else if (methodName.equals("is" + fieldName)) {
                    return true;
                }
            }

            return methodName.equals("get" + fieldName);
        }
    }

    private boolean isMatchSetter(String methodName, String fieldName, boolean isBooeanField) {
        methodName = methodName.toLowerCase();
        fieldName = fieldName.toLowerCase();
        if (!methodName.startsWith("set")) {
            return false;
        } else {
            return !isBooeanField || !fieldName.startsWith("is") || !methodName.equals("set" + StrUtil.removePrefix(fieldName, "is")) && !methodName.equals("set" + fieldName) ? methodName.equals("set" + fieldName) : true;
        }
    }

    public static class PropDesc {
        private Field field;
        private Method getter;
        private Method setter;

        public PropDesc(Field field, Method getter, Method setter) {
            this.field = field;
            this.getter = ClassUtil.setAccessible(getter);
            this.setter = ClassUtil.setAccessible(setter);
        }

        public String getFieldName() {
            return null == this.field ? null : this.field.getName();
        }

        public Field getField() {
            return this.field;
        }

        public Type getFieldType() {
            return null != this.field ? TypeUtil.getType(this.field) : this.findPropType(this.getter, this.setter);
        }

        public Class<?> getFieldClass() {
            return null != this.field ? TypeUtil.getClass(this.field) : this.findPropClass(this.getter, this.setter);
        }

        public Method getGetter() {
            return this.getter;
        }

        public Method getSetter() {
            return this.setter;
        }

        public Object getValue(Object bean) {
            if (null != this.getter) {
                return ReflectUtil.invoke(bean, this.getter, new Object[0]);
            } else {
                return ModifierUtil.isPublic(this.field) ? ReflectUtil.getFieldValue(bean, this.field) : null;
            }
        }

        public BeanDesc.PropDesc setValue(Object bean, Object value) {
            if (null != this.setter) {
                ReflectUtil.invoke(bean, this.setter, new Object[]{value});
            } else if (ModifierUtil.isPublic(this.field)) {
                ReflectUtil.setFieldValue(bean, this.field, value);
            }

            return this;
        }

        private Type findPropType(Method getter, Method setter) {
            Type type = null;
            if (null != getter) {
                type = TypeUtil.getReturnType(getter);
            }

            if (null == type && null != setter) {
                type = TypeUtil.getParamType(setter, 0);
            }

            return type;
        }

        private Class<?> findPropClass(Method getter, Method setter) {
            Class<?> type = null;
            if (null != getter) {
                type = TypeUtil.getReturnClass(getter);
            }

            if (null == type && null != setter) {
                type = TypeUtil.getFirstParamClass(setter);
            }

            return type;
        }
    }
}
