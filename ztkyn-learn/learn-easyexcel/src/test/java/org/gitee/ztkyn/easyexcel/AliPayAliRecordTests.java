package org.gitee.ztkyn.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.gitee.ztkyn.core.colleciton.ECollectionUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author richard
 * @version 1.0.0
 * @date 2023-07-06 14:58
 * @description AliPayRecordTests
 */
public class AliPayAliRecordTests {

	private static final Logger logger = LoggerFactory.getLogger(AliPayAliRecordTests.class);

	@Test
	public void read() {
		String filePath = "";
		if (!Paths.get(filePath).toFile().exists())
			return;

		List<AliRecord> aliRecordList = ECollectionUtil.MutableList.newList();
		EasyExcel.read(filePath, AliRecord.class, new ReadListener<AliRecord>() {
			@Override
			public void invoke(AliRecord data, AnalysisContext context) {
				if (Objects.equals(data.getProductName(), "信用卡还款")
						|| Objects.equals(data.getTransactionStatus(), "交易关闭"))
					return;
				aliRecordList.add(data);
			}

			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {
				System.out.println("数据读取完成");
			}
		}).sheet(0).doRead();
		Map<String, List<AliRecord>> orderMap = aliRecordList.stream()
			.collect(Collectors.groupingBy(AliRecord::getMerchantOrderNumber));
		List<AliRecord> payList = ECollectionUtil.MutableList.newList();
		Iterator<Map.Entry<String, List<AliRecord>>> iterator = orderMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, List<AliRecord>> next = iterator.next();
			List<AliRecord> aliRecords = next.getValue();
			// 支出
			BigDecimal outPay = new BigDecimal("0.00");
			// 收入
			BigDecimal inPay = new BigDecimal("0.00");
			for (AliRecord aliRecord : aliRecords) {
				switch (aliRecord.getReceipt()) {
					case "支出" -> outPay = outPay.add(new BigDecimal(aliRecord.getAmount()));
					case "不计收支", "收入" -> inPay = inPay.add(new BigDecimal(aliRecord.getAmount()));
					default -> System.out.println("数据错误");
				}
			}
			if (inPay.doubleValue() > 0 && outPay.doubleValue() > 0) {
				System.out.println();
			}
			if (inPay.compareTo(outPay) == 0) {
				// 退款成功
				System.out.println();
			}
			else {
				payList.addAll(aliRecords);
			}
		}
		Map<String, List<AliRecord>> providerMap = payList.stream()
			.collect(Collectors.groupingBy(AliRecord::getCounterparty));
		SortedSet<RecordSum> recordSumList = ECollectionUtil.MutableSet
			.newSortedSet(Comparator.comparingDouble(o -> o.amount));
		for (Map.Entry<String, List<AliRecord>> entry : providerMap.entrySet()) {
			BigDecimal amount = new BigDecimal("0.00");
			StringJoiner productStr = new StringJoiner("|");
			for (AliRecord aliRecord : entry.getValue()) {
				switch (aliRecord.getReceipt()) {
					case "支出" -> amount = amount.add(new BigDecimal(aliRecord.getAmount()));
					case "不计收支", "收入" -> amount = amount.subtract(new BigDecimal(aliRecord.getAmount()));
					default -> System.out.println("数据错误");
				}
				productStr.add(aliRecord.getProductName());
			}
			if (amount.doubleValue() > 0) {
				recordSumList.add(new RecordSum(amount.doubleValue(), entry.getKey(), productStr.toString()));
			}
		}
		for (RecordSum recordSum : recordSumList) {
			System.out.println(MessageFormat.format("【{0}】,{1},【{2}】", recordSum.getProviderName(),
					recordSum.getAmount(), recordSum.getProductList()));
			// logger.info("【{}】,{},【{}】", recordSum.getProviderName(),
			// recordSum.getAmount(), recordSum.getProductList());
		}
	}

	@Getter
	@Setter
	public static class AliRecord {

		@ExcelProperty("交易号")
		private String transactionNumber;

		@ExcelProperty("商家订单号")
		private String merchantOrderNumber;

		@ExcelProperty("交易创建时间")
		private String transactionCreationTime;

		@ExcelProperty("付款时间")
		private String paymentTime;

		@ExcelProperty("最近修改时间")
		private String lastModifiedTime;

		@ExcelProperty("交易来源地")
		private String sourceOfTransaction;

		@ExcelProperty("类型")
		private String type;

		@ExcelProperty("交易对方")
		private String counterparty;

		@ExcelProperty("商品名称")
		private String productName;

		@ExcelProperty("金额（元）")
		private String amount;

		@ExcelProperty("收/支")
		private String receipt;

		@ExcelProperty("交易状态")
		private String transactionStatus;

		@ExcelProperty("服务费（元）")
		private String serviceFee;

		@ExcelProperty("成功退款（元）")
		private String successfullyRefunded;

		@ExcelProperty("备注")
		private String remarks;

		@ExcelProperty("资金状态")
		private String fundingStatus;

	}

	@Getter
	@Setter
	@AllArgsConstructor
	public static class RecordSum {

		private double amount;

		private String providerName;

		private String productList;

	}

}
