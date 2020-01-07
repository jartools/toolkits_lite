package com.bowlong.third;

import java.io.InputStream;
import java.net.Socket;

import com.bowlong.lang.InputStreamEx;
import com.bowlong.net.Tcp;

/**
 * 多线程处理消息
 * 
 * @author canyon / 龚阳辉
 *
 */
public class SDHandler extends InputStreamEx implements Runnable {

	static final String fmt = "= isCanDo=[%s],Shut down msg=[%s],flag=[%s],[%s]";

	private String flag_shutdown = null;
	private Shutdown stdWrap = null;
	protected Socket socket = null;

	public SDHandler(Shutdown std, Socket socket) {
		super();
		init(std, socket);
	}

	@Override
	public void run() {
		try {
			_exce();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init(Shutdown std, Socket socket) {
		clear();
		this.socket = socket;
		this.stdWrap = std;
		this.flag_shutdown = std.flag_shutdown;
	}

	private void _exce() throws Exception {
		if (socket == null || socket.isClosed()) {
			return;
		}

		String msg = null;
		try (InputStream inStream = socket.getInputStream()) {
			msg = inps2Str(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean _is = isCanShut(msg);
		String info = String.format(fmt, _is, msg, this.flag_shutdown, socket.toString());
		this.stdWrap.onCallShut(_is, info, this);
		close();
	}

	// 判断是否可以执行停止
	private boolean isCanShut(String msg) throws Exception {
		boolean _isRet = isEmpty(this.flag_shutdown);
		if (!_isRet) {
			_isRet = this.flag_shutdown.equalsIgnoreCase(msg);
		}
		return _isRet;
	}

	public void close() {
		Tcp.close(socket);
	}

	public void clear() {
		close();
		this.flag_shutdown = null;
		this.socket = null;
		this.socket = null;
	}
}