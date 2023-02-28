package org.gitee.ztkyn.common.base;

import java.util.Map;

import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Damerau;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.Levenshtein;
import info.debatty.java.stringsimilarity.LongestCommonSubsequence;
import info.debatty.java.stringsimilarity.MetricLCS;
import info.debatty.java.stringsimilarity.NGram;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.OptimalStringAlignment;
import info.debatty.java.stringsimilarity.QGram;
import info.debatty.java.stringsimilarity.RatcliffObershelp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.Similarity;
import org.xm.similarity.text.CosineSimilarity;

/**
 * @author whty
 * @version 1.0
 * @description 字符串相似度计算工具
 * @date 2023/2/27 10:20
 */
public class ZtkynStringSimilarityUtil {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynStringSimilarityUtil.class);

	/**
	 * 词语相似度计算 文本长度：词语粒度
	 *
	 * 推荐使用词林相似度：org.xm.Similarity.cilinSimilarity，是基于同义词词林的相似度计算方法
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double cilinSimilarity(String wordA, String wordB) {
		return Similarity.cilinSimilarity(wordA, wordB);
	}

	/**
	 * 拼音相似度
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double pinyinSimilarity(String wordA, String wordB) {
		return Similarity.pinyinSimilarity(wordA, wordB);
	}

	/**
	 * 概念相似度
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double conceptSimilarity(String wordA, String wordB) {
		return Similarity.conceptSimilarity(wordA, wordB);
	}

	/**
	 * 字面相似度
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double charBasedSimilarity(String wordA, String wordB) {
		return Similarity.charBasedSimilarity(wordA, wordB);
	}

	/**
	 * 短语相似度计算 文本长度：短语粒度
	 *
	 * 推荐使用短语相似度：org.xm.Similarity.phraseSimilarity，本质是通过两个短语具有的相同字符，和相同字符的位置计算其相似度的方法
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double phraseSimilarity(String wordA, String wordB) {
		return Similarity.phraseSimilarity(wordA, wordB);
	}

	/**
	 * 句子相似度计算 文本长度：句子粒度
	 *
	 * 推荐使用词形词序句子相似度：org.xm.similarity.morphoSimilarity，一种既考虑两个句子相同文本字面，也考虑相同文本出现的前后顺序的相似度方法
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double morphoSimilarity(String wordA, String wordB) {
		return Similarity.morphoSimilarity(wordA, wordB);
	}

	/**
	 * 段落文本相似度计算 文本长度：段落粒度（一段话，25字符 < length(text) < 500字符）
	 *
	 * 推荐使用词形词序句子相似度：org.xm.similarity.text.CosineSimilarity，一种考虑两个段落中相同的文本，经过切词，词频和词性权重加权，并用余弦计算相似度的方法
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double cosineSimilarity(String wordA, String wordB) {
		return new CosineSimilarity().getSimilarity(wordA, wordB);
	}

	/**
	 * 两个单词之间的 Levenshtein 距离是将一个单词更改为另一个单词所需的最小单字符编辑（插入、删除或替换）次数。
	 *
	 * 它是一个公制字符串距离。此实现使用动态规划（瓦格纳-费歇尔算法），只有 2 行数据。因此，空间要求为 O（m），算法以 O（m.n 为单位）运行。
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double levenshtein(String wordA, String wordB) {
		return new Levenshtein().distance(wordA, wordB);
	}

	/**
	 * Levenshtein 的实现，允许为不同的字符替换定义不同的权重。
	 *
	 * 此算法通常用于光学字符识别 （OCR） 应用。例如，对于 OCR，替换 P 和 R 的成本低于替换 P 和 M 的成本，因为从 OCR 的角度来看，P 与 R 相似。
	 *
	 * 它还可用于键盘打字自动更正。例如，在这里替换E和R的成本较低，因为它们在AZERTY或QWERTY键盘上彼此相邻。因此，用户错误键入字符的可能性更高。
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double normalizedLevenshtein(String wordA, String wordB) {
		return new NormalizedLevenshtein().distance(wordA, wordB);
	}

	/**
	 * 与 Levenshtein 类似，带换位的 Damerau-Levenshtein 距离（有时也称为不受限制的 Damerau-Levenshtein
	 * 距离）是将一个字符串转换为另一个字符串所需的最小操作数，其中操作定义为插入、删除或替换单个字符，或两个相邻字符的转置。
	 *
	 * 它确实尊重三角形不等式，因此是一个公制距离。
	 *
	 * 不要将此与最佳字符串对齐距离混淆，后者是不能多次编辑子字符串的扩展。
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double damerau(String wordA, String wordB) {
		return new Damerau().distance(wordA, wordB);
	}

	/**
	 * Damerau-Levenshtein 的最优字符串对齐变体（有时称为限制编辑距离）计算在没有子字符串多次编辑的情况下使字符串相等所需的编辑操作次数，而真正的
	 * Damerau-Levenshtein 则没有这样的限制。 与列文施泰因距离算法的区别在于为转置操作增加了一个重复。
	 *
	 * 请注意，对于最佳字符串对齐距离，三角形不等式不成立，因此它不是真正的度量。
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double optimalStringAlignment(String wordA, String wordB) {
		return new OptimalStringAlignment().distance(wordA, wordB);
	}

	/**
	 * Jaro-Winkler是在记录链接（重复检测）领域开发的字符串编辑距离（Winkler，1990）。Jaro-Winkler
	 * 距离度量旨在最适合短字符串（如人名）和检测换位拼写错误。
	 *
	 * Jaro-Winkler 计算 2 个字符串之间的相似性，返回值位于区间 [0.0， 1.0] 内。
	 * 它（大致）是Damerau-Levenshtein的变体，其中2个接近字符的换位被认为不如彼此远离的2个字符的换位重要。Jaro-Winkler惩罚不能表示为换位的添加或替换。
	 *
	 * 距离计算为 1 - Jaro-Winkler 相似性。
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double jaroWinkler(String wordA, String wordB) {
		return new JaroWinkler().distance(wordA, wordB);
	}

	/**
	 * 最长公共子序列 （LCS） 问题包括查找两个（或多个）序列共有的最长子序列。它不同于查找公共子字符串的问题：与子字符串不同，子序列不需要在原始序列中占据连续位置。
	 *
	 * 它由 diff 实用程序使用，由 Git 用于协调多个更改等。
	 *
	 * 字符串 X（长度为 n）和 Y（长度为 m）之间的 LCS 距离为 n + m - 2 |LCS（X， Y）| 最小值 = 0 最大值 = n + m
	 *
	 * 当只允许插入和删除（不替换）时，或者当替换成本是插入或删除成本的两倍时，LCS 距离相当于 Levenshtein 距离。
	 *
	 * 此类实现动态规划方法，该方法具有空间要求 O（m.n） 和计算开销 O（m.n）。
	 *
	 * 在“最大公共子序列的长度”中，K.S.
	 * Larsen提出了一种算法，该算法计算LCS在时间O（log（m）.log（n））中的长度。但是该算法具有内存要求O（m.n²），因此此处未实现。
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double longestCommonSubsequence(String wordA, String wordB) {
		return new LongestCommonSubsequence().distance(wordA, wordB);
	}

	/**
	 * 基于最长公共子序列的距离度量，来自 Daniel Bakkelund 的注释“基于 LCS
	 * 的字符串度量”。<a href="http://heim.ifi.uio.no/~danielry/StringMetric.pdf">MetricLCS</a>
	 *
	 * 距离计算为 1 - |LCS（s1， s2）|/ max（|s1|， |s2|）
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double metricLCS(String wordA, String wordB) {
		return new MetricLCS().distance(wordA, wordB);
	}

	/**
	 * Kondrak定义的规范化N-gram距离，“N-gram相似性和距离”，字符串处理和信息检索，计算机科学讲义第3772卷，2005年，第115-126页。
	 *
	 * <a href="http://webdocs.cs.ualberta.ca/~kondrak/papers/spire05.pdf">spire05.pdf</a>
	 *
	 * 该算法使用附加特殊字符 '\n' 来增加第一个字符的权重。归一化是通过将总相似度得分除以最长单词的原始长度来实现的。
	 *
	 * 在论文中，Kondrak还定义了一个相似性度量，但尚未实现。
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double nGram(String wordA, String wordB) {
		return new NGram().distance(wordA, wordB);
	}

	/**
	 * 一些算法的工作原理是将字符串转换为 n 元语法集（n 个字符的序列，有时也称为 k 带状疱疹）。字符串之间的相似性或距离就是集合之间的相似性或距离。
	 *
	 * 计算这些相似性和距离的成本主要由k-shingling（将字符串转换为k个字符的序列）主导。因此，这些算法通常有两种用例：
	 *
	 * 直接计算字符串之间的距离：
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double qGram(String wordA, String wordB) {
		return new QGram().distance(wordA, wordB);
	}

	/**
	 * 一些算法的工作原理是将字符串转换为 n 元语法集（n 个字符的序列，有时也称为 k 带状疱疹）。字符串之间的相似性或距离就是集合之间的相似性或距离。
	 *
	 * 计算这些相似性和距离的成本主要由k-shingling（将字符串转换为k个字符的序列）主导。因此，这些算法通常有两种用例：
	 *
	 * 或者，对于大型数据集，预先计算所有字符串的配置文件。然后可以计算配置文件之间的相似性：
	 *
	 * 请注意，这仅在使用相同的 KShingling 对象解析所有输入字符串时才有效！
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double cosineSimilarity2(String wordA, String wordB) {
		Cosine cosine = new Cosine(2);
		// Pre-compute the profile of strings
		Map<String, Integer> profile1 = cosine.getProfile(wordA);
		Map<String, Integer> profile2 = cosine.getProfile(wordB);
		return cosine.similarity(cosine.getProfile(wordA), cosine.getProfile(wordB));
	}

	/**
	 * Ratcliff/Obershelp 模式识别，也称为格式塔模式匹配，是一种用于确定两个字符串相似性的字符串匹配算法。它由John W. Ratcliff和John
	 * A. Obershelp于1983年开发，并于1988年<>月发表在Dr. Dobb's Journal上。
	 *
	 * Ratcliff/Obershelp 计算 2 个字符串之间的相似性，返回值位于区间 [0.0， 1.0] 内。
	 *
	 * 距离计算为 1 - 拉特克利夫/奥伯斯帮助相似性。
	 * @param wordA
	 * @param wordB
	 * @return
	 */
	public static double ratcliffObershelp(String wordA, String wordB) {
		return new RatcliffObershelp().distance(wordA, wordB);
	}

}
