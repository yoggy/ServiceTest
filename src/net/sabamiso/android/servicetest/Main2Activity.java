package net.sabamiso.android.servicetest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main2Activity extends Activity implements ServiceConnection {
	private final static String TAG = "ServiceTest";

	private TestService test_service;
	private TextView text_count;
	private Button button_intent;
	private Button button_clear;

	Handler handler = new Handler();
	Runnable timer_task = new Runnable() {
		@Override
		public void run() {
			if (test_service == null)
				return;
			int count = test_service.getCount();
			text_count.setText(Integer.toString(count));

			handler.postDelayed(timer_task, 100);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		text_count = (TextView) findViewById(R.id.textCount);
		button_intent = (Button) findViewById(R.id.buttonIntent);
		button_intent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						Main1Activity.class);
				startActivity(intent);
			}
		});
		button_clear = (Button) findViewById(R.id.buttonClear);
		button_clear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (test_service == null)
					return;
				test_service.clearCount();
			}
		});
	}

	@Override
	public void onStart() {
		Log.d(TAG, "Main2Activity.onStart()");
		super.onStart();
		Intent intent = new Intent(getApplicationContext(), TestService.class);
		bindService(intent, this, BIND_AUTO_CREATE);
	}

	@Override
	public void onResume() {
		Log.d(TAG, "Main2Activity.onResume()");
		super.onResume();

		handler.postDelayed(timer_task, 100);
	}

	@Override
	public void onPause() {
		handler.removeCallbacks(timer_task);

		Log.d(TAG, "Main2Activity.onPause()");
		super.onPause();
	}

	@Override
	public void onStop() {
		unbindService(this);

		Log.d(TAG, "Main2Activity.onStop()");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "Main2Activity.onDestroy()");
		super.onDestroy();
	};

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.d(TAG, "Main2Activity.onServiceConnected()");
		test_service = ((TestService.TestServiceBinder) service).getService();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		Log.d(TAG, "Main2Activity.onServiceDisconnected()");
		test_service = null;
	}
}
