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
		this.flag_shutdown = flag_shut;
		InetAddress addr = InetAddress.getByName("127.0.0.1");
		ssocket = new ServerSocket(port, 2, addr);
	}

	public Shutdown(int port, String flag_shut) throws Exception {
		this(port, 1000, flag_shut);
	}

	public Shutdown(int port) throws Exception {
		this(port, null);
	}

	@Override
	protected void exce_run() throws Exception {
		while (!this.isDoShut && this.ssocket != null) {
			SDHandler hder = new SDHandler(this, ssocket.accept());
			list.add(hder);
			ThreadEx.execute(hder);
			ThreadEx.sleep(200);
		}
	}

	final public void onCallShut(boolean isCanShut, String info, SDHandler hder) {
		this.isDoShut = isCanShut;
		try {
			if (this.ssocket == null)
				return;
			logShutDown(info, hder.socket);
			if (this.isDoShut) {
				clear();
				exce_exit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void clear() {
		try {
			int lens = list.size();
			for (int i = 0; i < lens; i++) {
				list.get(i).clear();
			}
			list.clear();

			this.flag_shutdown = null;
			if (this.ssocket != null) {
				this.ssocket.close();
			}
			this.ssocket = null;
		} catch (Exception e) {
		}
	}
}