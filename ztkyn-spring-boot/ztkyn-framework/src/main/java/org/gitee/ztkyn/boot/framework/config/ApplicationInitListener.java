package org.gitee.ztkyn.boot.framework.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import me.ahoo.cosid.provider.IdGeneratorProvider;
import me.ahoo.cosid.spring.boot.starter.ConditionalOnCosIdEnabled;
import org.gitee.ztkyn.boot.framework.util.CosIdHelper;
import org.gitee.ztkyn.boot.framework.util.SpringUtil;
import org.gitee.ztkyn.core.function.DataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-06-27 14:18
 * @description 在应用启动完成后，注册 CosIdHelper
 * @version 1.0.0
 */
@Component
@ConditionalOnCosIdEnabled
public class ApplicationInitListener implements ApplicationListener<ApplicationReadyEvent> {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationInitListener.class);

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		// 在spring 完成启动之后，手动注册bean
		DataHandler.notNull(SpringUtil.getBean(IdGeneratorProvider.class)).ifTrue(idGeneratorProvider -> {
			// 需要手动注册，原因是，bean 容器自动获取到的 idGeneratorProvider 还没有设置 指定属性，依赖其他bean 进行属性设置
			// 因此等 bean 容器完成所有bean 的属性设置之后，在进行手动注册
			SpringUtil.registerBean(new CosIdHelper(idGeneratorProvider));
		});
	}

}
