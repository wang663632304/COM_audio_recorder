package com.example.com_audio_recorder;

import com.baidu.com.base.audiorecorder.ExtAudioRecorder;
import com.baidu.com.base.audiorecorder.ExtAudioRecorder.IRecorderListener;
import com.baidu.utils.LogUtil;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {
	
	private SeekBar mSeekBar = null;
	private Button mStart = null;
	private Button mStop = null;
	private MediaPlayer mMediaPlayer = null;
	private String mFilePath = null;
	
	private Handler mHandler = new Handler();
	
	private ExtAudioRecorder mAudioRecorder = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		setupViews();
	}
	
	public void setupViews()
	{
		mStart = (Button)findViewById(R.id.button_start);
		mStop = (Button)findViewById(R.id.button_stop);
		mStart.setOnClickListener(mOnClickListener);
		mStop.setOnClickListener(mOnClickListener);
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.button_start)
		{
			try
			{
				mAudioRecorder = new ExtAudioRecorder();
				mAudioRecorder.setHandler(mHandler);
				mAudioRecorder.setOnRecorderListener(mIRecorderListener);
				mAudioRecorder.startRecorder();
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogUtil.d("error");
				mAudioRecorder = null;
			}
		}
		else if(v.getId() == R.id.button_stop) 
		{
			if(mAudioRecorder != null)
			{
				mAudioRecorder.stopRecorder();
			}
		}
	}
	
	public IRecorderListener mIRecorderListener = new IRecorderListener()
	{
		
		@Override
		public void OnCallBack(int errNo, Object object)
		{
			// TODO Auto-generated method stub
			if(errNo == ExtAudioRecorder.MSG_AUDIO_RECORDER_MIC_STATUS)
			{
				LogUtil.d((String)object);
			}
		}
	};
};

    
}
