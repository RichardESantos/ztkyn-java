package org.gitee.ztkyn.core.https;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.gitee.ztkyn.core.http.PortUtil;
import org.gitee.ztkyn.core.https.gateway.CryptoInterceptor;
import org.gitee.ztkyn.core.https.gateway.GateWayCryptoApi;
import org.gitee.ztkyn.core.https.gateway.GateWayServiceGenerator;
import org.gitee.ztkyn.core.https.gateway.GateWayTestApi;
import org.gitee.ztkyn.web.beans.R;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GateWayTest {

	@Test
	public void testGetUrl() throws IOException {
		String host = "localhost";
		int port = 8888;
		if (PortUtil.isPortOpen(host, port)) {
			GateWayCryptoApi cryptoApi = GateWayServiceGenerator.createService(GateWayCryptoApi.class);
			R<String> stringR = cryptoApi.crypto().execute().body();
			GateWayTestApi testApi = GateWayServiceGenerator.createService(GateWayTestApi.class,
					new CryptoInterceptor(stringR.getData(), true));

			// System.out.println(testApi.index().execute());
			;
			// .out.println(testApi.cryptoForm("user", "passwd").execute());
			;
			System.out.println(testApi.cryptoFormData("user", "passwd").execute());
			System.out.println(testApi.cryptoFormData2("user", "passwd").execute());
			System.out.println(testApi.cryptoFormData3("user", "passwd").execute());
			;
			// System.out.println(testApi.cryptoJson(new
			// User().setUser("user").setPasswd("passwd")).execute());
			;
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
