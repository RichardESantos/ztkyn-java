package org.gitee.ztkyn.core.string;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author richard
 * @version 1.0
 * @description
 * @date 2023/2/8 17:07
 */
class AddressParserUtilTest {

	@Test
	void addressResolution() {
		System.out.println(AddressParserUtil.addressResolution("湖北省武汉市洪山区"));
		System.out.println(AddressParserUtil.addressResolution("湖北省恩施土家族苗族自治州恩施市"));
		System.out.println(AddressParserUtil.addressResolution("北京市市辖区朝阳区"));
		System.out.println(AddressParserUtil.addressResolution("内蒙古自治区兴安盟科尔沁右翼前旗"));
		System.out.println(AddressParserUtil.addressResolution("西藏自治区日喀则地区日喀则市"));
		System.out.println(AddressParserUtil.addressResolution("海南省省直辖县级行政单位中沙群岛的岛礁及其海域"));

	}

}