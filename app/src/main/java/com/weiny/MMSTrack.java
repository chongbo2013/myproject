package com.weiny;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年3月13日
 * 
 * @简介
 */
public class MMSTrack {
	AudioTrack mAudioTrack;
	int mChannel;
	int mFrequency;
	int mSampBit;

	public MMSTrack(int paramInt1, int paramInt2, int paramInt3) {
		this.mFrequency = paramInt1;
		this.mChannel = paramInt2;
		this.mSampBit = paramInt3;
	}

	public int getPrimePlaySize() {
		return 2 * AudioTrack.getMinBufferSize(this.mFrequency, this.mChannel,
				this.mSampBit);
	}

	public void init() {
		if (this.mAudioTrack != null)
			release();
		int i = AudioTrack.getMinBufferSize(this.mFrequency,
				AudioFormat.CHANNEL_CONFIGURATION_STEREO, // CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		this.mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				this.mFrequency, AudioFormat.CHANNEL_CONFIGURATION_STEREO, // CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, i, 1);
		this.mAudioTrack.play();
	}

	public void playAudioTrack(byte[] paramArrayOfByte, int paramInt1,
			int paramInt2) {
		if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0))
			return;

		try {
			this.mAudioTrack.write(paramArrayOfByte, paramInt1, paramInt2);
		} catch (Exception localException) {
			Log.i("MyAudioTrack", "catch exception...");
		}

	}

	public void release() {
		if (this.mAudioTrack == null)
			return;
		this.mAudioTrack.stop();
		this.mAudioTrack.release();
	}
}