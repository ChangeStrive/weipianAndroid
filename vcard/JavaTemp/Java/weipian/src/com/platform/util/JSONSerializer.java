package com.platform.util;



import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer;

/*JSON 系列化*/
public class JSONSerializer {
	private static final Log log = LogFactory.getLog(JSONWriter.class);	
	private  static  final char[] hex = "0123456789ABCDEF".toCharArray();
	
    public static String serialize(Object object) throws JSONException {
        JSONWriter writer = new JSONWriter();
        return writer.write(object);
    }
    
    public static String serialize(Object object,boolean ignoreBeanDept) throws JSONException {
        JSONWriter writer = new JSONWriter();
        writer.setIgnoreBeanDept(ignoreBeanDept);
        return writer.write(object);
    }
    
	public static String serialize(Object object, boolean ignoreHierarchy,
			boolean excludeNullProperties) throws JSONException {
		JSONWriter writer = new JSONWriter();
		writer.setIgnoreHierarchy(ignoreHierarchy);
		return writer.write(object, null, null,
				excludeNullProperties);
	}
	
	public static String serialize(Object object, Collection<Pattern> excludeProperties,
			Collection<Pattern> includeProperties) throws JSONException {
		JSONWriter writer = new JSONWriter();
		return writer.write(object,  excludeProperties, includeProperties,
				false);
	}
    
	public static String serialize(Object object,
			Collection<Pattern> excludeProperties,
			Collection<Pattern> includeProperties, boolean ignoreHierarchy,
			boolean excludeNullProperties) throws JSONException {
		JSONWriter writer = new JSONWriter();
		writer.setIgnoreHierarchy(ignoreHierarchy);
		return writer.write(object, excludeProperties, includeProperties,
				excludeNullProperties);
	}
	
    
	public static String serialize(Object object,
			Collection<Pattern> excludeProperties,
			Collection<Pattern> includeProperties, boolean ignoreHierarchy,
			boolean enumAsBean, boolean excludeNullProperties)
			throws JSONException {
		JSONWriter writer = new JSONWriter();
		writer.setIgnoreHierarchy(ignoreHierarchy);
		writer.setEnumAsBean(enumAsBean);
		return writer.write(object, excludeProperties, includeProperties,
				excludeNullProperties);
	}
    
	public static void serialize(Writer writer, Object object)
			throws IOException, JSONException {
		writer.write(serialize(object));
	}

	public static void serialize(Writer writer, Object object,
			Collection<Pattern> excludeProperties,
			Collection<Pattern> includeProperties, boolean excludeNullProperties)
			throws IOException, JSONException {
		writer.write(serialize(object, excludeProperties, includeProperties,
				true, excludeNullProperties));
	}
	
	public static void appendEscape(Appendable sb,String str) throws IOException {
		if (str == null){
			sb.append("\"\"");
			return;
		}
		CharacterIterator it = new StringCharacterIterator(str);
		sb.append('"');
		for (char c = it.first(); c != CharacterIterator.DONE; c = it.next()) {
			if (c == '"') {
				sb.append("\\\"");
			} else if (c == '\\') {
				sb.append("\\\\");
			} else if (c == '/') {
				sb.append("\\/");
			} else if (c == '\b') {
				sb.append("\\b");
			} else if (c == '\f') {
				sb.append("\\f");
			} else if (c == '\n') {
				sb.append("\\n");
			} else if (c == '\r') {
				sb.append("\\r");
			} else if (c == '\t') {
				sb.append("\\t");
			} else if (Character.isISOControl(c)) {
				sb.append("\\u");
				int n = c;
				for (int i = 0; i < 4; ++i) {
					int digit = (n & 0xf000) >> 12;
					sb.append(hex[digit]);
					n <<= 4;
				}
			} else {
				sb.append(c);
			}
		}
		sb.append('"');
	}
	
	/**
	 * 转义字符串
	 * @param str
	 * @return
	 */
	public static String escape(String str){
		int len=5;
		if(str!=null){
			len+=str.length()*1.2;
		}
		StringBuilder sb=new StringBuilder(len);
		try {
			appendEscape(sb, str);
		} catch (IOException e) {
		}
		return sb.toString();
	}
	
	public static void deleteLastComma(StringBuilder sb){
		if(sb.length()==0) 
			return;
		else if(sb.charAt(sb.length()-1)==',') 
			sb.deleteCharAt(sb.length()-1);
	}

	/**
	 * 改类为Apache jsonplugin 的JSONWriter类的 修改版
	 * 对以未初始化的属性置null,不会抛出异常
	 * <p>Serializes an object into JavaScript Object Notation (JSON). If cyclic references are detected
	 * they will be nulled out. </p>
	 */
	@SuppressWarnings("unchecked")
	 static class JSONWriter {  
	    /**
	     * By default, enums are serialzied as name=value pairs
	     */
	    public static final boolean ENUM_AS_BEAN_DEFAULT = false;
	    final static String RFC3339_FORMAT = "yyyy/MM/dd HH:mm:ss";
	    private StringBuilder buf = new StringBuilder();
	    private Stack stack = new Stack();
	    private boolean ignoreHierarchy = true;
	    private Object root;
	    private boolean buildExpr = true;
	    private String exprStack = "";
	    private Collection<Pattern> excludeProperties;
	    private Collection<Pattern> includeProperties;
	    private DateFormat formatter;
	    private boolean enumAsBean = ENUM_AS_BEAN_DEFAULT;
	    private boolean excludeNullProperties;
	    private boolean validateHibernateLazy=true;
	    private boolean ignoreBeanDept=false;
	    private int beanDept=0;
	    
	    void setIgnoreBeanDept(boolean igb){
	    	this.ignoreBeanDept=igb;
	    }

	    /**
	     * @param object Object to be serialized into JSON
	     * @return JSON string for object
	     * @throws JSONException
	     */
	    public String write(Object object) throws JSONException {
	        return this.write(object, null, null, false);
	    }

	    /**
	     * @param object Object to be serialized into JSON
	     * @return JSON string for object
	     * @throws JSONException
	     */
	    public String write(Object object, Collection<Pattern> excludeProperties, Collection<Pattern> includeProperties, boolean excludeNullProperties)
	            throws JSONException {
	        this.excludeNullProperties = excludeNullProperties;
	        this.buf.setLength(0);
	        this.root = object;
	        this.exprStack = "";
	        this.buildExpr = ((excludeProperties != null) && !excludeProperties.isEmpty()) || ((includeProperties != null) && !includeProperties.isEmpty());
	        this.excludeProperties = excludeProperties;
	        this.includeProperties = includeProperties;
	        this.value(object, null);

	        return this.buf.toString();
	    }

	    /**
	     * Detect cyclic references
	     */
	    private void value(Object object, Method method) throws JSONException {
	        if (object == null||this.validateHibernateLazy&&!Hibernate.isInitialized(object)) {
	            this.add("null");
	            
	            return;
	        }
	        if (this.stack.contains(object)) {
	            Class clazz = object.getClass();

	            //cyclic reference
	            if (clazz.isPrimitive() || clazz.equals(String.class)) {
	                this.process(object, method);
	            } else {
	                if (log.isDebugEnabled()) {
	                    log.debug("Cyclic reference detected on " + object);
	                }

	                this.add("null");
	            }

	            return;
	        }

	        this.process(object, method);
	    }

	    /**
	     * Serialize object into json
	     */
	    private void process(Object object, Method method) throws JSONException {
	        this.stack.push(object);

	        if (object instanceof Class) {
	            this.string(object);
	        } else if (object instanceof Boolean) {
	            this.bool(((Boolean) object).booleanValue());
	        } else if (object instanceof Number) {
	            this.add(object);
	        } else if (object instanceof String) {
	            this.string(object);
	        } else if (object instanceof Character) {
	            this.string(object);
	        } else if (object instanceof Map) {
	            this.map((Map) object, method);
	        } else if (object.getClass().isArray()) {
	            this.array(object, method);
	        } else if (object instanceof Iterable) {
	            this.array(((Iterable) object).iterator(), method);
	        } else if (object instanceof Date) {
	            this.date((Date) object, method);
	        } else if (object instanceof Calendar) {
	            this.date(((Calendar) object).getTime(), method);
	        } else if (object instanceof Locale) {
	            this.string(object);
	        } else if (object instanceof Enum) {
	            this.enumeration((Enum) object);
	        } else {
	        	if(!ignoreBeanDept||beanDept<1){
	        		beanDept++;
	        		this.bean(object);
	        		beanDept--;
	        	}
	        	else{
	        		this.value(null, null);
	        	}
	        }

	        this.stack.pop();
	    }

	    /**
	     * Instrospect bean and serialize its properties
	     */
	    private void bean(Object object) throws JSONException {
	        this.add("{");

	        BeanInfo info;

	        try {
	            Class clazz = object.getClass();
	            info = ((object == this.root) && this.ignoreHierarchy) ? Introspector
	                    .getBeanInfo(clazz, clazz.getSuperclass()) : Introspector
	                    .getBeanInfo(clazz);

	            PropertyDescriptor[] props = info.getPropertyDescriptors();
	            boolean isCGLIB=clazz.getName().indexOf("$$EnhancerByCGLIB$$") > -1;
	           // boolean isJavassist=this.validateHibernateLazy&&clazz.getName().indexOf("$$_javassist") > -1;
	            
	            boolean hasData = false;
	            for (int i = 0; i < props.length; ++i) {
	                PropertyDescriptor prop = props[i];
	                String name = prop.getName();
	                Method accessor = prop.getReadMethod();
	                Method baseAccessor = null;
	                if (isCGLIB) {
	                    try {
	                        baseAccessor = Class.forName(
	                                clazz.getName().substring(0, clazz.getName().indexOf("$$")))
	                                .getMethod(accessor.getName(), accessor.getParameterTypes());
	                    } catch (Exception ex) {
	                        log.debug(ex.getMessage(), ex);
	                    }
	                }else 
	                    baseAccessor = accessor;

	                if (baseAccessor != null) {

//	                    JSON json = baseAccessor.getAnnotation(JSON.class);
//	                    if (json != null) {
//	                        if (!json.serialize())
//	                            continue;
//	                        else if (json.name().length() > 0)
//	                            name = json.name();
//	                    }

	                    //ignore "class" and others
	                    if (this.shouldExcludeProperty(clazz, prop)) {
	                        continue;
	                    }
	                    String expr = null;
	                    if (this.buildExpr) {
	                        expr = this.expandExpr(name);
	                        if (this.shouldExcludeProperty(expr)) {
	                            continue;
	                        }
	                        expr = this.setExprStack(expr);
	                    }
	                    Object value = accessor.invoke(object, new Object[0]);
	                    if(value!=null&&value instanceof JavassistLazyInitializer){
	                    	continue;
	                    }
	                    boolean propertyPrinted = this.add(name, value, accessor, hasData);
	                    hasData = hasData || propertyPrinted;
	                    if (this.buildExpr) {
	                        this.setExprStack(expr);
	                    }
	                }
	            }

	            // special-case handling for an Enumeration - include the name() as a property */
	            if (object instanceof Enum) {
	                Object value = ((Enum) object).name();
	                this.add("_name", value, object.getClass().getMethod("name"), hasData);
	            }
	        } catch (Exception e) {
	            throw new JSONException(e);
	        }

	        this.add("}");
	    }

	    /**
	     * Instrospect an Enum and serialize it as a name/value pair or as a bean including all its own properties
	     */
	    private void enumeration(Enum enumeration) throws JSONException {
	        if (enumAsBean) {
	            this.bean(enumeration);
	        } else {
	            this.string(enumeration.name());
	        }
	    }

	    /**
	     * Ignore "class" field
	     */
	    private boolean shouldExcludeProperty(Class clazz, PropertyDescriptor prop)
	            throws SecurityException, NoSuchFieldException {
	        String name = prop.getName();

	        if (name.equals("class") || name.equals("declaringClass")
	                || name.equals("cachedSuperClass") || name.equals("metaClass")) {
	            return true;
	        }

	        return false;
	    }

	    private String expandExpr(int i) {
	        return this.exprStack + "[" + i + "]";
	    }

	    private String expandExpr(String property) {
	        if (this.exprStack.length() == 0)
	            return property;
	        return this.exprStack + "." + property;
	    }

	    private String setExprStack(String expr) {
	        String s = this.exprStack;
	        this.exprStack = expr;
	        return s;
	    }

	    private boolean shouldExcludeProperty(String expr) {
	        if (this.excludeProperties != null) {
	            for (Pattern pattern : this.excludeProperties) {
	                if (pattern.matcher(expr).matches()) {
	                    if (log.isDebugEnabled())
	                        log.debug("Ignoring property because of exclude rule: " + expr);
	                    return true;
	                }
	            }
	        }

	        if (this.includeProperties != null) {
	            for (Pattern pattern : this.includeProperties) {
	                if (pattern.matcher(expr).matches()) {
	                    return false;
	                }
	            }

	            if (log.isDebugEnabled())
	                log.debug("Ignoring property because of include rule:  " + expr);
	            return true;
	        }

	        return false;
	    }

	    /**
	     * Add name/value pair to buffer
	     */
	    private boolean add(String name, Object value, Method method, boolean hasData) throws JSONException {
	        if (!excludeNullProperties || value != null) {
	            if (hasData) {
	                this.add(',');
	            }
	            this.add('"');
	            this.add(name);
	            this.add("\":");
	            this.value(value, method);
	            return true;
	        }

	        return false;
	    }

	    /**
	     * Add map to buffer
	     */
	    private void map(Map map, Method method) throws JSONException {
	        this.add("{");

	        Iterator it = map.entrySet().iterator();

	        boolean warnedNonString = false; // one report per map
			boolean hasData = false;
	        while (it.hasNext()) {
	            Map.Entry entry = (Map.Entry) it.next();
	            Object key = entry.getKey();
	            String expr = null;
	            if (this.buildExpr) {
	                if (key == null) {
	                    log
	                            .error("Cannot build expression for null key in " +
	                                    this.exprStack);
	                    continue;
	                } else {
	                    expr = this.expandExpr(key.toString());
	                    if (this.shouldExcludeProperty(expr)) {
	                        continue;
	                    }
	                    expr = this.setExprStack(expr);
	                }
	            }
	            if (hasData) {
	                this.add(',');
	            }
	            hasData = true;
				if(!warnedNonString && ! (key instanceof String)) {
					log.warn("JavaScript doesn't support non-String keys, using toString() on "
							+ key.getClass().getName());
					warnedNonString = true;
				}
	            this.value(key.toString(), method);
	            this.add(":");
	            this.value(entry.getValue(), method);
	            if (this.buildExpr) {
	                this.setExprStack(expr);
	            }
	        }

	        this.add("}");
	    }

	    /**
	     * Add date to buffer
	     */
	    private void date(Date date, Method method) {
//	        JSON json = null;
//	        if (method != null)
//	            json = method.getAnnotation(JSON.class);
	        if (this.formatter == null)
	            this.formatter = new SimpleDateFormat(RFC3339_FORMAT);

//	        DateFormat formatter = (json != null) && (json.format().length() > 0) ? new SimpleDateFormat(
//	                json.format())
//	                : this.formatter;
	        this.string(this.formatter.format(date));
	    }

	    /**
	     * Add array to buffer
	     */
	    private void array(Iterator it, Method method) throws JSONException {
	        this.add("[");

	        boolean hasData = false;
	        for (int i = 0; it.hasNext(); i++) {
	            String expr = null;
	            if (this.buildExpr) {
	                expr = this.expandExpr(i);
	                if (this.shouldExcludeProperty(expr)) {
	                    it.next();
	                    continue;
	                }
	                expr = this.setExprStack(expr);
	            }
	            if (hasData) {
	                this.add(',');
	            }
	            hasData = true;
	            this.value(it.next(), method);
	            if (this.buildExpr) {
	                this.setExprStack(expr);
	            }
	        }

	        this.add("]");
	    }

	    /**
	     * Add array to buffer
	     */
	    private void array(Object object, Method method) throws JSONException {
	        this.add("[");

	        int length = Array.getLength(object);

	        boolean hasData = false;
	        for (int i = 0; i < length; ++i) {
	            String expr = null;
	            if (this.buildExpr) {
	                expr = this.expandExpr(i);
	                if (this.shouldExcludeProperty(expr)) {
	                    continue;
	                }
	                expr = this.setExprStack(expr);
	            }
	            if (hasData) {
	                this.add(',');
	            }
	            hasData = true;
	            this.value(Array.get(object, i), method);
	            if (this.buildExpr) {
	                this.setExprStack(expr);
	            }
	        }

	        this.add("]");
	    }

	    /**
	     * Add boolean to buffer
	     */
	    private void bool(boolean b) {
	        this.add(b ? "true" : "false");
	    }

	    /**
	     * escape characters
	     */
	    private void string(Object obj) {
	    	try {
				appendEscape(this.buf, obj.toString());
			} catch (IOException e) {
			}
	    }

	    /**
	     * Add object to buffer
	     */
	    private void add(Object obj) {
	        this.buf.append(obj);
	    }

	    /**
	     * Add char to buffer
	     */
	    private void add(char c) {
	        this.buf.append(c);
	    }

	    public void setIgnoreHierarchy(boolean ignoreHierarchy) {
	        this.ignoreHierarchy = ignoreHierarchy;
	    }

	    /**
	     * If true, an Enum is serialized as a bean with a special property _name=name() as all as all other properties defined within the enum.<br/>
	     * If false, an Enum is serialized as a name=value pair (name=name())
	     *
	     * @param enumAsBean true to serialize an enum as a bean instead of as a name=value pair (default=false)
	     */
	    public void setEnumAsBean(boolean enumAsBean) {
	        this.enumAsBean = enumAsBean;
	    }
	}
	
	public static class JSONException extends Exception {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public JSONException(String message) {
	        super(message);
	    }

	    public JSONException(Throwable cause) {
	        super(cause);
	    }

	    public JSONException(String message, Throwable cause) {
	        super(message, cause);
	    }
	}

}
