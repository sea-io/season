package cn.hutool.core.builder;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilder implements Builder<Integer> {
    private static final int DEFAULT_INITIAL_VALUE = 17;
    private static final int DEFAULT_MULTIPLIER_VALUE = 37;
    private static final ThreadLocal<Set<IDKey>> REGISTRY = new ThreadLocal();
    private final int iConstant;
    private int iTotal = 0;

    static Set<IDKey> getRegistry() {
        return (Set)REGISTRY.get();
    }

    static boolean isRegistered(Object value) {
        Set<IDKey> registry = getRegistry();
        return registry != null && registry.contains(new IDKey(value));
    }

    private static void reflectionAppend(Object object, Class<?> clazz, HashCodeBuilder builder, boolean useTransients, String[] excludeFields) {
        if (!isRegistered(object)) {
            try {
                register(object);
                Field[] fields = clazz.getDeclaredFields();
                AccessibleObject.setAccessible(fields, true);
                Field[] arr$ = fields;
                int len$ = fields.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    Field field = arr$[i$];
                    if (!ArrayUtil.contains(excludeFields, field.getName()) && field.getName().indexOf(36) == -1 && (useTransients || !Modifier.isTransient(field.getModifiers())) && !Modifier.isStatic(field.getModifiers())) {
                        try {
                            Object fieldValue = field.get(object);
                            builder.append(fieldValue);
                        } catch (IllegalAccessException var14) {
                            throw new InternalError("Unexpected IllegalAccessException");
                        }
                    }
                }
            } finally {
                unregister(object);
            }

        }
    }

    public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object) {
        return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, false, (Class)null);
    }

    public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object, boolean testTransients) {
        return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, (Class)null);
    }

    public static <T> int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, T object, boolean testTransients, Class<? super T> reflectUpToClass, String... excludeFields) {
        if (object == null) {
            throw new IllegalArgumentException("The object to build a hash code for must not be null");
        } else {
            HashCodeBuilder builder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
            Class<?> clazz = object.getClass();
            reflectionAppend(object, clazz, builder, testTransients, excludeFields);

            while(clazz.getSuperclass() != null && clazz != reflectUpToClass) {
                clazz = clazz.getSuperclass();
                reflectionAppend(object, clazz, builder, testTransients, excludeFields);
            }

            return builder.toHashCode();
        }
    }

    public static int reflectionHashCode(Object object, boolean testTransients) {
        return reflectionHashCode(17, 37, object, testTransients, (Class)null);
    }

    public static int reflectionHashCode(Object object, Collection<String> excludeFields) {
        return reflectionHashCode(object, (String[])ArrayUtil.toArray(excludeFields, String.class));
    }

    public static int reflectionHashCode(Object object, String... excludeFields) {
        return reflectionHashCode(17, 37, object, false, (Class)null, excludeFields);
    }

    static void register(Object value) {
        Class var1 = HashCodeBuilder.class;
        synchronized(HashCodeBuilder.class) {
            if (getRegistry() == null) {
                REGISTRY.set(new HashSet());
            }
        }

        getRegistry().add(new IDKey(value));
    }

    static void unregister(Object value) {
        Set<IDKey> registry = getRegistry();
        if (registry != null) {
            registry.remove(new IDKey(value));
            Class var2 = HashCodeBuilder.class;
            synchronized(HashCodeBuilder.class) {
                registry = getRegistry();
                if (registry != null && registry.isEmpty()) {
                    REGISTRY.remove();
                }
            }
        }

    }

    public HashCodeBuilder() {
        this.iConstant = 37;
        this.iTotal = 17;
    }

    public HashCodeBuilder(int initialOddNumber, int multiplierOddNumber) {
        Assert.isTrue(initialOddNumber % 2 != 0, "HashCodeBuilder requires an odd initial value", new Object[0]);
        Assert.isTrue(multiplierOddNumber % 2 != 0, "HashCodeBuilder requires an odd multiplier", new Object[0]);
        this.iConstant = multiplierOddNumber;
        this.iTotal = initialOddNumber;
    }

    public HashCodeBuilder append(boolean value) {
        this.iTotal = this.iTotal * this.iConstant + (value ? 0 : 1);
        return this;
    }

    public HashCodeBuilder append(boolean[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            boolean[] arr$ = array;
            int len$ = array.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                boolean element = arr$[i$];
                this.append(element);
            }
        }

        return this;
    }

    public HashCodeBuilder append(byte value) {
        this.iTotal = this.iTotal * this.iConstant + value;
        return this;
    }

    public HashCodeBuilder append(byte[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            byte[] arr$ = array;
            int len$ = array.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                byte element = arr$[i$];
                this.append(element);
            }
        }

        return this;
    }

    public HashCodeBuilder append(char value) {
        this.iTotal = this.iTotal * this.iConstant + value;
        return this;
    }

    public HashCodeBuilder append(char[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            char[] arr$ = array;
            int len$ = array.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                char element = arr$[i$];
                this.append(element);
            }
        }

        return this;
    }

    public HashCodeBuilder append(double value) {
        return this.append(Double.doubleToLongBits(value));
    }

    public HashCodeBuilder append(double[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            double[] arr$ = array;
            int len$ = array.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                double element = arr$[i$];
                this.append(element);
            }
        }

        return this;
    }

    public HashCodeBuilder append(float value) {
        this.iTotal = this.iTotal * this.iConstant + Float.floatToIntBits(value);
        return this;
    }

    public HashCodeBuilder append(float[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            float[] arr$ = array;
            int len$ = array.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                float element = arr$[i$];
                this.append(element);
            }
        }

        return this;
    }

    public HashCodeBuilder append(int value) {
        this.iTotal = this.iTotal * this.iConstant + value;
        return this;
    }

    public HashCodeBuilder append(int[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            int[] arr$ = array;
            int len$ = array.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                int element = arr$[i$];
                this.append(element);
            }
        }

        return this;
    }

    public HashCodeBuilder append(long value) {
        this.iTotal = this.iTotal * this.iConstant + (int)(value ^ value >> 32);
        return this;
    }

    public HashCodeBuilder append(long[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            long[] arr$ = array;
            int len$ = array.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                long element = arr$[i$];
                this.append(element);
            }
        }

        return this;
    }

    public HashCodeBuilder append(Object object) {
        if (object == null) {
            this.iTotal *= this.iConstant;
        } else if (object.getClass().isArray()) {
            if (object instanceof long[]) {
                this.append((long[])((long[])object));
            } else if (object instanceof int[]) {
                this.append((int[])((int[])object));
            } else if (object instanceof short[]) {
                this.append((short[])((short[])object));
            } else if (object instanceof char[]) {
                this.append((char[])((char[])object));
            } else if (object instanceof byte[]) {
                this.append((byte[])((byte[])object));
            } else if (object instanceof double[]) {
                this.append((double[])((double[])object));
            } else if (object instanceof float[]) {
                this.append((float[])((float[])object));
            } else if (object instanceof boolean[]) {
                this.append((boolean[])((boolean[])object));
            } else {
                this.append((Object[])((Object[])object));
            }
        } else {
            this.iTotal = this.iTotal * this.iConstant + object.hashCode();
        }

        return this;
    }

    public HashCodeBuilder append(Object[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            Object[] arr$ = array;
            int len$ = array.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Object element = arr$[i$];
                this.append(element);
            }
        }

        return this;
    }

    public HashCodeBuilder append(short value) {
        this.iTotal = this.iTotal * this.iConstant + value;
        return this;
    }

    public HashCodeBuilder append(short[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            short[] arr$ = array;
            int len$ = array.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                short element = arr$[i$];
                this.append(element);
            }
        }

        return this;
    }

    public HashCodeBuilder appendSuper(int superHashCode) {
        this.iTotal = this.iTotal * this.iConstant + superHashCode;
        return this;
    }

    public int toHashCode() {
        return this.iTotal;
    }

    public Integer build() {
        return this.toHashCode();
    }

    public int hashCode() {
        return this.toHashCode();
    }
}
