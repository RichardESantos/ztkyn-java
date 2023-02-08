package org.gitee.ztkyn.core.string;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.experimental.Accessors;
import org.gitee.ztkyn.core.regex.RegexRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description 地址解析工具类
 * @date 2023/2/8 16:45
 */
public class AddressParserUtil {

	private static final Logger logger = LoggerFactory.getLogger(AddressParserUtil.class);

	/**
	 * 中文地址解析
	 * @param addressStr
	 * @return
	 */
	public static List<Address> addressResolution(String addressStr) {
		Matcher m = Pattern.compile(RegexRule.CHINESE_ADDRESS.getRegex()).matcher(addressStr);
		String province = null, city = null, county = null, town = null, village = null;
		List<Address> addressList = new ArrayList<Address>();
		while (m.find()) {
			Address address = new Address();
			province = m.group("province");
			address.setProvince(province == null ? "" : province.trim());
			city = m.group("city");
			address.setCity(city == null ? "" : city.trim());
			county = m.group("county");
			address.setCounty(county == null ? "" : county.trim());
			town = m.group("town");
			address.setTown(town == null ? "" : town.trim());
			village = m.group("village");
			address.setVillage(village == null ? "" : village.trim());
			addressList.add(address);
		}
		return addressList;
	}

	@Data
	@Accessors(chain = true)
	public static class Address {

		/**
		 * 省
		 */
		private String province;

		/**
		 * 市
		 */
		private String city;

		/**
		 * 县
		 */
		private String county;

		/**
		 * 镇
		 */
		private String town;

		/**
		 * 乡
		 */
		private String village;

		@Override
		public String toString() {
			return "Address{" + "province='" + province + '\'' + ", city='" + city + '\'' + ", county='" + county + '\''
					+ ", town='" + town + '\'' + ", village='" + village + '\'' + '}';
		}

	}

}
