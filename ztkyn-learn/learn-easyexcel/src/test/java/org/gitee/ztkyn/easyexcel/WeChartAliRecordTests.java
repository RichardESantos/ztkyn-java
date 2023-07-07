package org.gitee.ztkyn.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.gitee.ztkyn.core.colleciton.ECollectionUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author richard
 * @version 1.0.0
 * @date 2023-07-06 18:35
 * @description WeChartRecordTests
 */
public class WeChartAliRecordTests {

	private static final Logger logger = LoggerFactory.getLogger(WeChartAliRecordTests.class);

	@Test
	public void readRecord() {
		String path1 = "";
		String path2 = "";
		List<WechatRecord> recordList = ECollectionUtil.MutableList.newList();
		readCSV(path1, recordList);
		readCSV(path2, recordList);
		Map<String, List<WechatRecord>> providerMap = recordList.stream()
			.collect(Collectors.groupingBy(WechatRecord::getCounterparty));
		SortedSet<AliPayAliRecordTests.RecordSum> recordSumList = ECollectionUtil.MutableSet
			.newSortedSet(Comparator.comparingDouble(AliPayAliRecordTests.RecordSum::getAmount));
		for (Map.Entry<String, List<WechatRecord>> entry : providerMap.entrySet()) {
			List<WechatRecord> records = entry.getValue();
			BigDecimal amount = new BigDecimal("0.00");
			StringJoiner productStr = new StringJoiner("|");
			for (WechatRecord record : records) {
				String amountStr = record.getAmount().replace("￥", "").replace(",", "");
				switch (record.getReceipt()) {
					case "支出" -> amount = amount.add(new BigDecimal(amountStr));
					case "收入" -> amount = amount.subtract(new BigDecimal(amountStr));
					default -> {
					}
				}
				productStr.add(record.getCommodity());
			}
			if (amount.doubleValue() > 0) {
				recordSumList.add(new AliPayAliRecordTests.RecordSum(amount.doubleValue(), entry.getKey(),
						productStr.toString()));
			}
		}
		for (AliPayAliRecordTests.RecordSum recordSum : recordSumList) {
			System.out.println(MessageFormat.format("【{0}】,{1},【{2}】", recordSum.getProviderName(),
					recordSum.getAmount(), recordSum.getProductList()));
		}
	}

	private static void readCSV(String path1, List<WechatRecord> recordList) {
		EasyExcel.read(path1, WechatRecord.class, new ReadListener<WechatRecord>() {
			@Override
			public void invoke(WechatRecord data, AnalysisContext context) {
				if (Objects.equals(data.getReceipt(), "收入") && data.getCurrentState().contains("已退款"))
					return;
				recordList.add(data);
			}

			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {
				System.out.println("数据读取完成");
			}
		})
			// 设置 CSV 格式
			.excelType(ExcelTypeEnum.CSV)
			// 设置 读取编码格式
			.charset(Charset.forName("GB18030"))
			.sheet(0)
			.doRead();
	}

	public static class WechatRecord {

		@ExcelProperty("交易时间")
		private String transactionTime;

		@ExcelProperty("交易类型")
		private String transactionType;

		@ExcelProperty("交易对方")
		private String counterparty;

		@ExcelProperty("商品")
		private String commodity;

		@ExcelProperty("收/支")
		private String receipt;

		@ExcelProperty("金额(元)")
		private String amount;

		@ExcelProperty("支付方式")
		private String paymentMethod;

		@ExcelProperty("当前状态")
		private String currentState;

		@ExcelProperty("交易单号")
		private String transactionNumber;

		@ExcelProperty("商户单号")
		private String merchantTrackingNumber;

		@ExcelProperty("备注")
		private String remarks;

		public String getTransactionTime() {
			return transactionTime;
		}

		public void setTransactionTime(String transactionTime) {
			this.transactionTime = transactionTime;
		}

		public String getTransactionType() {
			return transactionType;
		}

		public void setTransactionType(String transactionType) {
			this.transactionType = transactionType;
		}

		public String getCounterparty() {
			return counterparty;
		}

		public void setCounterparty(String counterparty) {
			this.counterparty = counterparty;
		}

		public String getCommodity() {
			return commodity;
		}

		public void setCommodity(String commodity) {
			this.commodity = commodity;
		}

		public String getReceipt() {
			return receipt;
		}

		public void setReceipt(String receipt) {
			this.receipt = receipt;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getPaymentMethod() {
			return paymentMethod;
		}

		public void setPaymentMethod(String paymentMethod) {
			this.paymentMethod = paymentMethod;
		}

		public String getCurrentState() {
			return currentState;
		}

		public void setCurrentState(String currentState) {
			this.currentState = currentState;
		}

		public String getTransactionNumber() {
			return transactionNumber;
		}

		public void setTransactionNumber(String transactionNumber) {
			this.transactionNumber = transactionNumber;
		}

		public String getMerchantTrackingNumber() {
			return merchantTrackingNumber;
		}

		public void setMerchantTrackingNumber(String merchantTrackingNumber) {
			this.merchantTrackingNumber = merchantTrackingNumber;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

	}

}
