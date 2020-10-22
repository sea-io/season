package cn.hutool.core.annotation;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationUtil {
    public AnnotationUtil() {
    }

    public static CombinationAnnotationElement toCombination(AnnotatedElement annotationEle) {
        return annotationEle instanceof CombinationAnnotationElement ? (CombinationAnnotationElement)annotationEle : new CombinationAnnotationElement(annotationEle);
    }

    public static Annotation[] getAnnotations(AnnotatedElement annotationEle, boolean isCombination) {
        return null == annotationEle ? null : ((AnnotatedElement)(isCombination ? toCombination(annotationEle) : annotationEle)).getAnnotations();
    }

    public static <A extends Annotation> A getAnnotation(AnnotatedElement annotationEle, Class<A> annotationType) {
        return null == annotationEle ? null : toCombination(annotationEle).getAnnotation(annotationType);
    }

    public static <T> T getAnnotationValue(AnnotatedElement annotationEle, Class<? extends Annotation> annotationType) throws UtilException {
        return getAnnotationValue(annotationEle, annotationType, "value");
    }

    public static <T> T getAnnotationValue(AnnotatedElement annotationEle, Class<? extends Annotation> annotationType, String propertyName) throws UtilException {
        Annotation annotation = getAnnotation(annotationEle, annotationType);
        if (null == annotation) {
            return null;
        } else {
            Method method = ReflectUtil.getMethodOfObj(annotationEle, propertyName, new Object[0]);
            return null == method ? null : ReflectUtil.invoke(annotationEle, method, new Object[0]);
        }
    }

    public static Map<String, Object> getAnnotationValueMap(AnnotatedElement annotationEle, Class<? extends Annotation> annotationType) throws UtilException {
        Annotation annotation = getAnnotation(annotationEle, annotationType);
        if (null == annotation) {
            return null;
        } else {
            Method[] methods = ReflectUtil.getMethods(annotationType, new Filter<Method>() {
                public boolean accept(Method t) {
                    if (ArrayUtil.isEmpty(t.getParameterTypes())) {
                        String name = t.getName();
                        return !"hashCode".equals(name) && !"toString".equals(name) && !"annotationType".equals(name);
                    } else {
                        return false;
                    }
                }
            });
            HashMap<String, Object> result = new HashMap(methods.length, 1.0F);
            Method[] arr$ = methods;
            int len$ = methods.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Method method = arr$[i$];
                result.put(method.getName(), ReflectUtil.invoke(annotation, method, new Object[0]));
            }

            return result;
        }
    }

    public static RetentionPolicy getRetentionPolicy(Class<? extends Annotation> annotationType) {
        Retention retention = (Retention)annotationType.getAnnotation(Retention.class);
        return null == retention ? RetentionPolicy.CLASS : retention.value();
    }

    public static ElementType[] getTargetType(Class<? extends Annotation> annotationType) {
        Target target = (Target)annotationType.getAnnotation(Target.class);
        return null == target ? new ElementType[]{ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE} : target.value();
    }

    public static boolean isDocumented(Class<? extends Annotation> annotationType) {
        return annotationType.isAnnotationPresent(Documented.class);
    }

    public static boolean isInherited(Class<? extends Annotation> annotationType) {
        return annotationType.isAnnotationPresent(Inherited.class);
    }
}
