package org.zollty.util.msg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.zollty.log.LogFactory;
import org.zollty.log.Logger;
import org.zollty.util.LocaleUtils;
import org.zollty.util.NestedRuntimeException;
import org.zollty.util.ResourceUtils;
import org.zollty.util.resource.Resource;

/**
 * 基于Properties文件的I18nMsg的实现。
 * <br>
 * 在classpath*下面可以有多个相同名称的Properties文件。
 * 其中优先读取classpath下面的文件，再读取jar包中的文件。
 * 
 * @author zollty
 * @since 2016-3-20
 */
public class I18nMsgProperties implements I18nMsg {
	
	private static final Logger LOG = LogFactory.getLogger(I18nMsgProperties.class);
    
    /**
     * Map<Locale, List<MSG_Properties>>
     */
    private Map<Locale, List<Properties>> msgHolder = new HashMap<Locale, List<Properties>>();
    
    /**
     * 不包含Locale（语言_国家_地区）后缀的Properties文件名（去掉.properties后缀后的全路径）
     */
    private String moduleName;
    
    public I18nMsgProperties() {
    }
    
    /**
     * @param moduleName 不包含Locale（语言_国家_地区）后缀的文件名（全路径）
     */
    public I18nMsgProperties(String moduleName) {
        this.moduleName = moduleName;
    }
    
    /**
     * 用于从外部自行添加Properties文件
     */
    public void addProps(String localeStr, List<Properties> props) {
        addProps(LocaleUtils.toLocale(localeStr), props);
    }
    
    /**
     * 用于从外部自行添加Properties文件
     */
    public void addProps(Locale locale, List<Properties> props) {
        List<Properties> old = msgHolder.get(locale);
        if (old == null) {
            msgHolder.put(locale, props);
        } else {
            old.addAll(props);
        }
    }
    
    /**
     * 用于从外部自行添加Properties文件
     * @param fileName Properties文件名（全路径）
     */
    public void addFile(String localeStr, String fileName) {
    	List<Properties> props;
    	try {
            props = getProps(fileName);
        } catch (IOException e) {
            throw new NestedRuntimeException(e);
        }
        
        addProps(localeStr, props);
    }
    
    /**
     * 用于从外部自行添加Properties文件
     * @param fileName Properties文件名（全路径）
     */
    public void addFile(Locale locale, String fileName) {
    	List<Properties> props;
    	try {
            props = getProps(fileName);
        } catch (IOException e) {
            throw new NestedRuntimeException(e);
        }
        
        addProps(locale, props);
    }

    /**
     * 根据baseName，即不包含Locale（语言_国家_地区）后缀的文件名（全路径），获取包含Locale的文件名
     * @param baseName 不包含Locale（语言_国家_地区）后缀的文件名（全路径）
     * @param locale
     * @return
     */
	public static String toFileName(String baseName, Locale locale) {
		if (locale == Locale.ROOT) {
			return baseName;
		}
		String loc = locale.toString();
		if (loc.length() != 0) {
			return baseName + "_" + loc + ".properties";
		} else {
			return baseName + ".properties";
		}
	}
    
    /**
     * 获取 "classpath*:" + fileName 下面的所有Properties
     * @param fileName Properties文件名（全路径）
     * @return
     * @throws IOException
     */
    public static List<Properties> getProps(String fileName) throws IOException {
        Resource[] ress = ResourceUtils.getResourcesByPathMatchingResolver("classpath*:" + fileName);
        List<Properties> props = new ArrayList<Properties>();
        if (ress != null) {
        	LOG.debug("Get resource classpath*:{}, size={}", fileName, ress.length);
            for (int i = 0; i < ress.length; i++) {
                LOG.info("Get resource {}", ress[i].getURL());
                Properties prop = new Properties();
                prop.load(ress[i].getInputStream());
                props.add(prop);
            }
        }
        return props;
    }
    
    /**
     * @return 不包含Locale（语言_国家_地区）后缀的文件名（全路径）
     */
    public String getModuleName() {
        return moduleName;
    }
    
    /**
     * @param moduleName 不包含Locale（语言_国家_地区）后缀的文件名（全路径）
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    
    @Override
    public String getString(String key, Locale localLocale) {
        List<Properties> props = msgHolder.get(localLocale);
        if (props == null) {
            String fileName = toFileName(moduleName, localLocale);
            try {
                props = getProps(fileName);
            } catch (IOException e) {
                throw new NestedRuntimeException(e);
            }
            
            addProps(localLocale, props);
        }
        String ret = null;
        for (Properties prop : props) {
            ret = prop.getProperty(key);
            if (ret != null) {
                break;
            }
        }
        return ret;
    }

    @Override
    public Long getLong(String key, Locale locale) {
        return Long.valueOf(getString(key, locale));
    }

    @Override
    public Integer getInteger(String key, Locale locale) {
        return Integer.valueOf(getString(key, locale));
    }

    @Override
    public Double getDouble(String key, Locale locale) {
        return Double.valueOf(getString(key, locale));
    }

    @Override
    public Float getFloat(String key, Locale locale) {
        return Float.valueOf(getString(key, locale));
    }

    @Override
    public Byte getByte(String key, Locale locale) {
        return Byte.valueOf(getString(key, locale));
    }

    @Override
    public String getString(String key, String locale) {
        return getString(key, LocaleUtils.toLocale(locale));
    }

    @Override
    public Long getLong(String key, String locale) {
        return getLong(key, LocaleUtils.toLocale(locale));
    }

    @Override
    public Integer getInteger(String key, String locale) {
        return getInteger(key, LocaleUtils.toLocale(locale));
    }

    @Override
    public Double getDouble(String key, String locale) {
        return getDouble(key, LocaleUtils.toLocale(locale));
    }

    @Override
    public Float getFloat(String key, String locale) {
        return getFloat(key, LocaleUtils.toLocale(locale));
    }

    @Override
    public Byte getByte(String key, String locale) {
        return getByte(key, LocaleUtils.toLocale(locale));
    }
}
