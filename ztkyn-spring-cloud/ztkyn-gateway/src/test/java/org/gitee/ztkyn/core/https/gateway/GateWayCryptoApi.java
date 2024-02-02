package org.gitee.ztkyn.core.https.gateway;

import org.gitee.ztkyn.web.beans.R;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GateWayCryptoApi {

	@GET("/crypto")
	Call<R<String>> crypto();

}
