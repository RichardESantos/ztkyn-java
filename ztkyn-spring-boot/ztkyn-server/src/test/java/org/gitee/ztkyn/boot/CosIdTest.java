package org.gitee.ztkyn.boot;

import org.springframework.boot.test.context.SpringBootTest;

import jakarta.annotation.Resource;
import me.ahoo.cosid.provider.IdGeneratorProvider;
import org.gitee.ztkyn.boot.framework.util.CosIdHelper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-06-25 09:43
 * @description CosIdTest
 * @version 1.0.0
 */
@SpringBootTest
public class CosIdTest {

	private static final Logger logger = LoggerFactory.getLogger(CosIdTest.class);

	@Resource
	private IdGeneratorProvider generatorProvider;

	@Resource
	private CosIdHelper cosIdHelper;

	@Test
	public void testCosId() {
		for (int i = 0; i < 100; i++) {
			System.out.println(generatorProvider.getShare().generate());
			System.out.println(cosIdHelper.nextId());
		}
	}

}
