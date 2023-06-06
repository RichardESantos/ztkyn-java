package org.gitee.ztkyn.easyexcel;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.gitee.ztkyn.core.json.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @version 1.0.0
 * @date 2023-05-31 10:01
 * @description FreeMarkerTemplateUtils
 */
public class FreeMarkerTemplateUtils {

	private static final Logger logger = LoggerFactory.getLogger(FreeMarkerTemplateUtils.class);

	private static final Configuration configuration = new Configuration(Configuration.getVersion());

	private FreeMarkerTemplateUtils() {
	}

	static {
		// 这里比较重要，用来指定加载模板所在的路径，此处配置加载 resources/templates 目录下的模板
		configuration.setTemplateLoader(new ClassTemplateLoader(FreeMarkerTemplateUtils.class, "/templates/"));
		configuration.setDefaultEncoding("UTF-8");
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		configuration.setCacheStorage(NullCacheStorage.INSTANCE);
	}

	public static Template getTemplate(String templateName) throws IOException {
		return configuration.getTemplate(templateName);
	}

	/**
	 * 解析模版，赋值
	 * @param object
	 * @param template
	 * @return
	 */
	public static byte[] parseTemplate(Object object, Template template) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
				Writer writer = new OutputStreamWriter(new BufferedOutputStream(out))) {
			Map<String, Object> objectMap = JacksonUtil.json2Map(JacksonUtil.obj2String(object), String.class,
					Object.class);
			return parseProcess(objectMap, out, writer, template);
		}
		catch (TemplateException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解析模版，赋值
	 * @param object
	 * @param templateName
	 * @return
	 */
	public static byte[] parseTemplate(Object object, String templateName) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
				Writer writer = new OutputStreamWriter(new BufferedOutputStream(out))) {
			return parseProcess(object, out, writer, getTemplate(templateName));
		}
		catch (TemplateException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 详细解析过程
	 * @param object
	 * @param out
	 * @param writer
	 * @param template
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	private static byte[] parseProcess(Object object, ByteArrayOutputStream out, Writer writer, Template template)
			throws TemplateException, IOException {
		// Create the builder:
		BeansWrapperBuilder builder = new BeansWrapperBuilder(Configuration.getVersion());
		// Set desired BeansWrapper configuration properties:
		builder.setUseModelCache(true);
		builder.setExposeFields(true);
		template.process(object, writer, builder.build());
		writer.flush();
		return out.toByteArray();
	}

	/**
	 * 清理缓存
	 */
	public static void clearCache() {
		configuration.clearTemplateCache();
	}

}
