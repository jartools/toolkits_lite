package com.bowlong.third;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.List;

import com.bowlong.basic.ExOrigin;
import com.bowlong.lang.task.ThreadEx;

//SHUTDOWN
public class Shutdown extends RShutdown {
	private ServerSocket ssocket = null;
	private boolean isDoShut = false;
	final List<SDHandler> list = ExOrigin.newList();
	protected String flag_shutdown = null;

	public Shutdown(int port, int sleep, String flag_shut) throws Exception {
		if (sleep > 0) {
			ThreadEx.sleep(sleep);
		}
		addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				doExitByHook();
			}
		}));

		this.flag_shutdown = flag_shut;
		InetAddress addr = InetAddress.getByName("127.0.0.1");
		ssocket = new ServerSocket(port, 2, addr);
	}

	public Shutdown(int port, String flag_shut) throws Exception {
		this(port, 100, flag_shut);
	}

	public Shutdown(int port) throws Exception {
		this(port, null);
	}

	@Override
	protected void exce_run() throws Exception {
		while (true) {
			if (this.isShuting || this.isDoShut)
				break;
			ServerSocket _ss = this.ssocket;
			if (_ss == null || _ss.isClosed())
				break;
			SDHandler hder = new SDHandler(this, _ss.accept());
			list.add(hder);
			ThreadEx.execute(hder);
			ThreadEx.sleep(500);
		}
	}

	final public void onCallShut(boolean isCanShut, String info, SDHandler hder) {
		this.isDoShut = isCanShut;
		try {
			if (this.ssocket == null)
				return;
			logShutDown(info, hder.socket);
			if (this.isDoShut) {
				doExit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void clear() {
		try {
			int lens = list.size();
			for (int i = 0; i < lens; i++) {
				list.get(i).clear();
			}
			list.clear();

			this.flag_shutdown = null;
			ServerSocket _ss = this.ssocket;
			this.ssocket = null;
			if (_ss != null) {
				_ss.close();
			}
		} catch (Exception e) {
		}
	}
}