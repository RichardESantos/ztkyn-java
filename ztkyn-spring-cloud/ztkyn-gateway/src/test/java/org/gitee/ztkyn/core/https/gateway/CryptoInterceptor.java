package org.gitee.ztkyn.core.https.gateway;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.NoArgsConstructor;
import okhttp3.*;
import okio.Buffer;
import org.apache.http.util.TextUtils;
import org.gitee.ztkyn.core.crypto.ZtkynSMUtil;
import org.gitee.ztkyn.gateway.configuration.context.GateWayConstants;
import org.gitee.ztkyn.web.utils.RequestUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static org.gitee.ztkyn.core.json.JsonObjectUtil.jsonConfig;
import static org.gitee.ztkyn.core.json.JsonObjectUtil.sortFullJson;

/**
 * 加密，解密 拦截器
 */
@NoArgsConstructor
public class CryptoInterceptor implements Interceptor {

	private SM2 sm2;

	private boolean isCrypto = false;

	private static final Digester digester = DigestUtil.digester("sm3");

	private static final Logger logger = LoggerFactory.getLogger(CryptoInterceptor.class);

	public CryptoInterceptor(String publicKey) {
		this.sm2 = ZtkynSMUtil.genSm2FromPublicKey(publicKey);
	}

	public CryptoInterceptor(String publicKey, boolean isCrypto) {
		this.sm2 = ZtkynSMUtil.genSm2FromPublicKey(publicKey);
		this.isCrypto = isCrypto;
	}

	@NotNull
	@Override
	public Response intercept(@NotNull Chain chain) throws IOException {
		// 请求
		Request request = chain.request();
		RequestBody requestBody = request.body();
		String sm4Key = ZtkynSMUtil.genSM4Key();
		if (Objects.isNull(requestBody) && isCrypto) {
			// 优先从 url 中解析参数
			HttpUrl httpUrl = request.url();
			String encryptHex = ZtkynSMUtil.mixEncodeHex(sm2, sm4Key, signUrlParams(httpUrl.query()));
			URL url = httpUrl.url();
			String newUrl = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + url.getPath() + "?"
					+ encryptHex;
			request = request.newBuilder().url(newUrl).build();

		}
		else {
			MediaType mediaType = requestBody.contentType();
			long contentLength = requestBody.contentLength();
			if (contentLength > 0 && isCrypto) {
				// 获取未加密数据
				RequestBody oldRequestBody = request.body();
				Buffer requestBuffer = new Buffer();
				oldRequestBody.writeTo(requestBuffer);
				String oldBodyStr = requestBuffer.readUtf8();
				requestBuffer.close();
				if ("application/x-www-form-urlencoded".equals(mediaType.toString())) {
					RequestBody newBody = RequestBody.create(mediaType,
							ZtkynSMUtil.mixEncodeHex(sm2, sm4Key, signUrlParams(oldBodyStr)));
					// 构造新的request
					request = request.newBuilder().headers(request.headers()).method(request.method(), newBody).build();
				}
				else if ("application/json; charset=UTF-8".equals(mediaType.toString())) {
					RequestBody newBody = RequestBody.create(mediaType,
							ZtkynSMUtil.mixEncodeHex(sm2, sm4Key, signJson(oldBodyStr)));
					// 构造新的request
					request = request.newBuilder().headers(request.headers()).method(request.method(), newBody).build();
				}
			}
		}
		// 响应
		Response response = chain.proceed(request);
		if (response.code() == 200 && isCrypto) {
			try {
				// 获取加密的响应数据
				ResponseBody oldResponseBody = response.body();
				MediaType mediaType = oldResponseBody.contentType();
				String oldResponseBodyStr = oldResponseBody.string();
				// 加密的响应数据用AES秘钥解密
				String newResponseBodyStr = "";
				if (!TextUtils.isEmpty(oldResponseBodyStr)) {
					newResponseBodyStr = ZtkynSMUtil.decodeHex(ZtkynSMUtil.genSM4(sm4Key), oldResponseBodyStr);
				}
				System.out.println(newResponseBodyStr);
				oldResponseBody.close();
				// 构造新的response
				ResponseBody newResponseBody = ResponseBody.create(mediaType, newResponseBodyStr);
				response = response.newBuilder().body(newResponseBody).build();
			}
			catch (Exception e) {
				logger.info("RetrofitLog", e);
			}
			finally {
				response.close();
			}
		}
		// 返回
		return response;
	}

	private String signUrlParams(String orgQuery) {
		// 添加随机数
		orgQuery += "&" + GateWayConstants.SIGN_NONCE_KEY + "=" + UUID.fastUUID();
		orgQuery += "&" + GateWayConstants.SIGN_TIMESTAMP_KEY + "=" + System.currentTimeMillis();
		JSONObject jsonObject = JSONUtil.parseObj(RequestUtil.transfer(orgQuery), jsonConfig);
		String paramJson = sortFullJson(jsonObject).toString();
		logger.info("请求参数: {}", paramJson);
		return orgQuery + "&" + GateWayConstants.SIGN_SIGN_KEY + "=" + digester.digestHex(paramJson);
	}

	private String signJson(String jsonStr) {
		JSONObject jsonObject = JSONUtil.parseObj(jsonStr, jsonConfig);
		// 添加随机数
		jsonObject.set(GateWayConstants.SIGN_NONCE_KEY, UUID.fastUUID().toString());
		jsonObject.set(GateWayConstants.SIGN_TIMESTAMP_KEY, System.currentTimeMillis());
		String paramJson = sortFullJson(jsonObject).toString();
		logger.info("请求参数: {}", paramJson);
		jsonObject.set(GateWayConstants.SIGN_SIGN_KEY, digester.digestHex(paramJson));
		return jsonObject.toString();
	}

}
