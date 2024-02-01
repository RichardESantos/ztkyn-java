package org.gitee.ztkyn.core.https.gateway;

import org.gitee.ztkyn.core.https.GateWayTest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GateWayApi {

	@GET("/crypto")
	public Call<String> crypto();

	@GET("/index?name=supercopy-v3&uid=8e7f04ec34c24d4f88f94693e43005fa")
	public Call<String> index();

	@GET("/cryptoForm")
	public Call<String> cryptoForm(@Query("user") String user, @Query("passwd") String passwd);

	@POST("/cryptoJson")
	public Call<String> cryptoJson(@Body GateWayTest.User user);

}
