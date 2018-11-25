package gmfood.saltyjeff.github.io.gm_food;

import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gm.android.vehicle.hardware.RotaryControlHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecordOrderActivity extends AppCompatActivity {
	String vendorId;
	MediaRecorder recorder = new MediaRecorder();
	File outputFile;
	TextView text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_order);
		vendorId = getIntent().getStringExtra("id");
		text = (TextView)findViewById(R.id.recordText);
		text.setText("Preparing to record");
		File outputDir = getCacheDir(); // context being the Activity pointer
		try {
			outputFile = File.createTempFile("recordOrder", ".mp3", outputDir);
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			FileOutputStream fos = new FileOutputStream(outputFile);
			recorder.setOutputFile(fos.getFD());
			recorder.prepare();
			recorder.start();
			text.setText("Recording to "+outputFile.toString()+" - press button to stop recording");
			((Button)findViewById(R.id.stopRecordButton)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					recorder.stop();
					recorder.release();
					uploadRecording();
				}
			});
		}
		catch(IOException e) {
			Log.e("IO ERR", e.toString());
			finish();
		}
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		boolean didHandleRotary = RotaryControlHelper.onKeyEvent(event, new RotaryControlHelper.RotaryListener() {
			@Override
			public boolean onRotaryClockwise(int velocity) {
				// Rotary was turned clockwise
				return true;
			}

			@Override
			public boolean onRotaryCounterClockwise(int velocity) {
				// Rotary was turned counter clockwise
				return true;
			}

			@Override
			public boolean onMenuButtonClick() {
				// Menu button was pressed
				recorder.stop();
				recorder.release();
				uploadRecording();
				return true;
			}
		});
		return didHandleRotary || super.dispatchKeyEvent(event); // didHandleRotary will be what you return in the interface methods above. If true, super will not be called.
	}
	void uploadRecording() {
		//upload file here
		text.setText("Uploading recording, please wait");
		//dooooodoooo
		text.setText("Done Recording");
		finish();
	}
}
