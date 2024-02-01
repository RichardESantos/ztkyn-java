package org.gitee.ztkyn.core.http;

import org.gitee.ztkyn.core.exception.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 网络端口工具类
 */
public class PortUtil {

	private static final Logger logger = LoggerFactory.getLogger(PortUtil.class);

	/**
	 * 判断端口是否连接正常
	 * @param host
	 * @param port
	 * @return
	 */
	public static boolean isPortOpen(String host, int port) {
		AssertUtil.notBlank(host, "host is null");
		try (Socket socket = new Socket()) {
			int timeout = 5000;
			socket.connect(new InetSocketAddress(host, port), timeout);
			return socket.isConnected();
		}
		catch (Exception e) {
			return false;
		}
	}

}
