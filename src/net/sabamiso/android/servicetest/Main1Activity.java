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

public class Main1Activity extends Activity implements ServiceConnection {
	private final static String TAG = "ServiceTest";

	private TestService test_service;
	private TextView text_count;
	private Button button_intent;
	private Button button_clear;

	Handler handler = new Handler();
	Runnable timer_task = new Runnable() {
		@Override
		public void run() {
			if (test_service == null) return;			
			int count = test_service.getCount();
			text_count.setText(Integer.toString(count));
			
			handler.postDelayed(timer_task, 100);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);

		text_count = (TextView) findViewById(R.id.textCount);
		button_intent = (Button) findViewById(R.id.buttonIntent);
		button_intent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						Main2Activity.class);
				startActivity(intent);
			}
		});
		button_clear = (Button) findViewById(R.id.buttonClear);
		button_clear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (test_service == null) return;
				test_service.clearCount();
			}
		});
	}
	
	@Override
	public void onStart() {
		Log.d(TAG, "Main1Activity.onStart()");
		super.onStart();
		
		// �T�[�r�X�N��
		Intent intent = new Intent(getApplicationContext(), TestService.class);
		bindService(intent, this, BIND_AUTO_CREATE);
	}

	@Override
	public void onResume() {
		Log.d(TAG, "Main1Activity.onResume()");
		super.onResume();

		handler.postDelayed(timer_task, 100);
	}

	@Override
	public void onPause() {
		handler.removeCallbacks(timer_task);

		Log.d(TAG, "Main1Activity.onPause()");
		super.onPause();
	}

	@Override
	public void onStop() {
		Log.d(TAG, "Main1Activity.onStop()");
		super.onStop();

		// bind���Ă���Activity���Ȃ��Ȃ�����service��onDestory()����
		// onStop()�͑���Activity���L���ɂȂ��Č��݂�Activity�������Ȃ��Ȃ�Ƃ��ɌĂяo�����B
		unbindService(this);  
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "Main1Activity.onDestroy()");
		super.onDestroy();
	};
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// �T�[�r�X�Ƃ̐ڑ��������������ɌĂяo�����
		Log.d(TAG, "Main1Activity.onServiceConnected()");
		test_service = ((TestService.TestServiceBinder) service).getService();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// �T�[�r�X�Ƃ̐ڑ����ؒf���ꂽ�Ƃ��ɌĂяo�����
		Log.d(TAG, "Main1Activity.onServiceDisconnected()");
		test_service = null;
	}
}
