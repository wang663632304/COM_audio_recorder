package com.baidu.com.base.audiorecorder;

import java.io.File;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Handler;
import com.baidu.utils.LogUtil;

/**
 * 音频录制类
 * @author huangweigan
 * @version 1.0
 * @date 2012-11-14
 */
public class ExtAudioRecorder
{	
	public final static int MSG_AUDIO_RECORDER_ERROR = 0;
	public final static int MSG_AUDIO_RECORDER_INFO = 1;
	public final static int MSG_AUDIO_RECORDER_MIC_STATUS = 2;
	
	public final static int AUDIO_RECORDER_TYPE_AMR = 1;
	public final static int AUDIO_RECORDER_TYPE_ACC = 2;
	public final static int AUDIO_RECORDER_TYPE_WAV = 3;
	public final static int AUDIO_RECORDER_TYPE_MP3 = 4;
	private int audioRecorderTypeDefault = AUDIO_RECORDER_TYPE_AMR;
	
	public final static int AUDIO_RECORDER_SAMPLE_RATE_8000 = 8000;
	public final static int AUDIO_RECORDER_SAMPLE_RATE_11025 = 11025;
	public final static int AUDIO_RECORDER_SAMPLE_RATE_22050 = 22050;
	public final static int AUDIO_RECORDER_SAMPLE_RATE_44100 = 44100;
	private int audioRecorderSampleRateDefault = AUDIO_RECORDER_SAMPLE_RATE_8000;
	
	public final static int AUDIO_RECORDER_FORMAT_8BIT = AudioFormat.ENCODING_PCM_8BIT;	
	public final static int AUDIO_RECORDER_FORMAT_16BIT = AudioFormat.ENCODING_PCM_16BIT;
	private int audioRecorderFormatDefault = AUDIO_RECORDER_FORMAT_16BIT;
	
	private MediaRecorder mRecorder = null;
	
	private String mFileName = null;
	
	private String mPrefixName = "zhidao_";
	
	private String mSuffName = ".amr";
	
	private String mAudioStoreDir = "/sdcard/data/baidu_zhidao/";
	
	private File mRecorderFile;
	
	private IRecorderListener mIRecorderListener = null;
	
	private Handler mHandler = null;
	
	private int mMicStatusUpdateTimer = 300;
	
	public ExtAudioRecorder() throws Exception
	{
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioSamplingRate(8000);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        createDir(mAudioStoreDir);
	}
	
	public void createDir(String path) throws IOException
	{
		File dir = new File(path);  
        if(dir.exists() == false)
        {
        	if(dir.mkdirs() == false)
        	{
        		throw new IOException("Fail to create directory!");
        	}
        }
	}
	
	public void setHandler(Handler handler)
	{
		mHandler = handler;
	}
	
	public void setOnRecorderListener(IRecorderListener recorderListener)
	{
		mIRecorderListener = recorderListener;
	}
	
	public void setMicStatusUpdateTimer(int timer)
	{
		mMicStatusUpdateTimer = timer;
	}
	
	public void startRecorder() throws IOException 
	{
		mRecorderFile = File.createTempFile(mPrefixName, mSuffName, new File(mAudioStoreDir));
		mRecorder.setOutputFile(mRecorderFile.getAbsolutePath());
		mRecorder.setOnInfoListener(mInfoListener);
		mRecorder.setOnErrorListener(mErrorListener);
		mRecorder.prepare();
		mRecorder.start();
		if(mHandler != null)
		{
			mHandler.post(mUpdateMicStatusTimer);
		}		
	}
	
	public void stopRecorder()
	{
		if(mHandler != null)
		{
			mHandler.removeCallbacks(mUpdateMicStatusTimer);
		}
		mRecorder.stop();
		
	}
	
	private OnInfoListener mInfoListener = new OnInfoListener()
	{
		
		@Override
		public void onInfo(MediaRecorder mr, int what, int extra)
		{
			// TODO Auto-generated method stub
			LogUtil.d("onInfo:what="+what+",extra="+"extra");
			if(mIRecorderListener != null) 
			{
				
			}
		}
	};
	
	private OnErrorListener mErrorListener = new OnErrorListener()
	{
		
		@Override
		public void onError(MediaRecorder mr, int what, int extra)
		{
			// TODO Auto-generated method stub
			LogUtil.d("onError:what="+what+",extra="+"extra");
			if(mIRecorderListener != null) 
			{
				
			}
		}
	};
	
	private Runnable mUpdateMicStatusTimer = new Runnable() {
		public void run() {
	   		int vuSize = 10 * mRecorder.getMaxAmplitude() / 32768;
	   		if(mIRecorderListener != null)
	   		{
	   			mIRecorderListener.OnCallBack(MSG_AUDIO_RECORDER_MIC_STATUS, vuSize + "");
	   		}
	   		mHandler.postDelayed(mUpdateMicStatusTimer, mMicStatusUpdateTimer);
	   }
	};

	
	public interface IRecorderListener
	{
		public void OnCallBack(int errNo, Object object);
	}
	
	
}
