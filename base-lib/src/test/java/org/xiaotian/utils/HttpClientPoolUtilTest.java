package org.xiaotian.utils;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpException;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;

public class HttpClientPoolUtilTest {

	@Test
	public void test() {
		ExecutorService service=Executors.newFixedThreadPool(5000);
		for(int i=0;i<=10*10000;i++){
			service.execute(new Runnable() {
				public void run() {
					try {
						String str=HttpClientPoolUtil.get("http://10.136.37.23:55443/qn?phone=7747300152");
					} catch (Exception e) {
						PoolingHttpClientConnectionManager connManager=HttpClientPoolUtil.getConnectionManager();
						System.out.println(connManager.getTotalStats().getLeased());
						e.printStackTrace();
					}
				}
			});
		}
		new Thread(new Runnable() {
			
			public void run() {
				while(true){
					PoolingHttpClientConnectionManager connManager=HttpClientPoolUtil.getConnectionManager();
					System.out.println(connManager.getTotalStats().getLeased());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).run();
	}

}
