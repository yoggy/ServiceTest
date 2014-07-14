package net.sabamiso.android.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class TestService extends Service {
	private final static String TAG = "ServiceTest";
	
	private Handler handler = new Handler();
	private int count = 0;
	
	public class TestServiceBinder extends Binder {
		TestService getService() {
			return TestService.this;
		}
	};
	private final IBinder binder = new TestServiceBinder();
	
	private final Runnable timer_task = new Runnable() {
		@Override
		public void run() {
			synchronized(TestService.this) {
				count += 1;
			}
			handler.postDelayed(timer_task, 100);
		}
	};
	
	@Override
    public void onCreate() {
        Log.i(TAG, "TestService.onCreate()");        
        handler.post(timer_task);
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
    public void onDestroy() {
        Log.i(TAG, "TestService.onDestroy()");
        handler.removeCallbacks(timer_task);
    }
	
	public synchronized void clearCount() {
		count = 0;
	}
	
	public synchronized int getCount() {
		return count;
	}
}
