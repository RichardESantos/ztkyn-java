package org.gitee.ztkyn.core.https.gateway;

import org.gitee.ztkyn.core.https.GateWayTest;
import org.gitee.ztkyn.web.beans.R;
import retrofit2.Call;
import retrofit2.http.*;

public interface GateWayTestApi {

	@GET("/test/index?name=supercopy-v3&uid=8e7f04ec34c24d4f88f94693e43005fa&name=supercopy-v4")
	Call<R<String>> index();

	@GET("/test/cryptoForm")
	Call<R<String>> cryptoForm(@Query("user") String user, @Query("passwd") String passwd);

	@FormUrlEncoded
	@POST("/test/cryptoFormData")
	Call<R<String>> cryptoFormData(@Field("user") String user, @Field("passwd") String passwd);

	@POST("/test/cryptoJson")
	Call<R<String>> cryptoJson(@Body GateWayTest.User user);

}
