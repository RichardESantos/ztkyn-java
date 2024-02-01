package org.gitee.ztkyn.core.https.gateway;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GateWayServiceGenerator {

	private static final String BASE_URL = "http://localhost:8888/";

	private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL)
		.addConverterFactory(ScalarsConverterFactory.create())
		.addConverterFactory(JacksonConverterFactory.create());

	private static Retrofit retrofit = builder.build();

	private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

	private static final HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
		.setLevel(HttpLoggingInterceptor.Level.BASIC);

	public static <S> S createService(Class<S> serviceClass) {
		if (!httpClient.interceptors().contains(logging)) {
			httpClient.addInterceptor(logging);
			builder.client(httpClient.build());
			retrofit = builder.build();
		}
		return retrofit.create(serviceClass);
	}

	public static <S> S createService(Class<S> serviceClass, Interceptor... interceptors) {
		if (interceptors.length > 0) {
			httpClient.interceptors().clear();
			httpClient.addInterceptor(logging);
			for (Interceptor interceptor : interceptors) {
				httpClient.addInterceptor(interceptor);
			}
			builder.client(httpClient.build());
			retrofit = builder.build();
		}
		return retrofit.create(serviceClass);
	}

	public static <S> S createService(Class<S> serviceClass, final String token) {
		if (token != null) {
			httpClient.interceptors().clear();
			httpClient.addInterceptor(chain -> {
				Request original = chain.request();
				Request.Builder builder1 = original.newBuilder().header("Authorization", token);
				Request request = builder1.build();
				return chain.proceed(request);
			});
			builder.client(httpClient.build());
			retrofit = builder.build();
		}
		return retrofit.create(serviceClass);
	}

}
