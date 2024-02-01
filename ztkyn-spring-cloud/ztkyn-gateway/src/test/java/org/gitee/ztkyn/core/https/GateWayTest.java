package org.gitee.ztkyn.core.https;

import cn.hutool.crypto.asymmetric.SM2;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import okhttp3.*;
import okio.Buffer;
import org.gitee.ztkyn.core.crypto.ZtkynSMUtil;
import org.gitee.ztkyn.core.http.PortUtil;
import org.gitee.ztkyn.core.https.gateway.GateWayApi;
import org.gitee.ztkyn.core.https.gateway.GateWayServiceGenerator;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class GateWayTest {

	@Test
	public void testGetUrl() throws IOException {
		String host = "localhost";
		int port = 8888;
		if (PortUtil.isPortOpen(host, port)) {
			GateWayApi wayApi = GateWayServiceGenerator.createService(GateWayApi.class);
			Response<String> execute = wayApi.crypto().execute();
			System.out.println(execute.body());
			final SM2 sm2 = ZtkynSMUtil.genSm2FromPublicKey(execute.body());
			sm2.usePlainEncoding();

			GateWayApi cryptoApi = GateWayServiceGenerator.createService(GateWayApi.class, new Interceptor() {
				@NotNull
				@Override
				public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
					// 请求
					Request request = chain.request();
					// MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
					RequestBody requestBody = request.body();
					if (Objects.isNull(requestBody)) {
						// 优先从 url 中解析参数
						HttpUrl httpUrl = request.url();
						String orgQuery = httpUrl.query();
						String encryptHex = ZtkynSMUtil.mixEncodeHex(sm2, orgQuery);
						URL url = httpUrl.url();
						String newUrl = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + url.getPath()
								+ "?" + encryptHex;
						request = request.newBuilder().url(newUrl).build();
					}
					else {
						MediaType mediaType = requestBody.contentType();
						long contentLength = requestBody.contentLength();
						if (contentLength > 0) {
							// 获取未加密数据
							RequestBody oldRequestBody = request.body();
							Buffer requestBuffer = new Buffer();
							oldRequestBody.writeTo(requestBuffer);
							String oldBodyStr = requestBuffer.readUtf8();
							requestBuffer.close();
							RequestBody newBody = RequestBody.create(mediaType,
									ZtkynSMUtil.mixEncodeHex(sm2, oldBodyStr));
							// 构造新的request
							request = request.newBuilder()
								.headers(request.headers())
								.method(request.method(), newBody)
								.build();
						}
					}

					try {

						// 获取未加密数据
						RequestBody oldRequestBody = request.body();
						Buffer requestBuffer = new Buffer();
						oldRequestBody.writeTo(requestBuffer);
						String oldBodyStr = requestBuffer.readUtf8();
						requestBuffer.close();

						// //未加密数据用AES秘钥加密
						// String newBodyStr=
						// EncryptionManager.getInstance().publicEncryptClient(oldBodyStr);
						// //AES秘钥用服务端RSA公钥加密
						// String key=
						// EncryptionManager.getInstance().publicEncrypt(aesKey);
						// //构成新的request 并通过请求头发送加密后的AES秘钥
						// Headers headers = request.headers();
						// RequestBody newBody = RequestBody.create(mediaType,
						// newBodyStr);
						// //构造新的request
						// request = request.newBuilder()
						// .headers(headers)
						// .addHeader("Device-Key", key)
						// .method(request.method(), newBody)
						// .build();
					}
					catch (Exception e) {

					}
					return chain.proceed(request);
				}
			});

			cryptoApi.index().execute();
			cryptoApi.cryptoForm("user", "passwd").execute();
			cryptoApi.cryptoJson(new User().setUser("user").setPasswd("passwd")).execute();
		}
	}

	@Getter
	@Setter
	@Accessors(chain = true)
	public static class User {

		private String user;

		private String passwd;

	}

}
