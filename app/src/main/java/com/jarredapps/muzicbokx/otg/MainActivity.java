package com.jarredapps.muzicbokx.otg;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.media.MediaPlayer;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;
import org.json.*;

import androidx.annotation.*;
import android.support.*;
import android.support.v7.app.*;
import android.support.v13.*;
import javax.xml.parsers.*;
import android.database.*;
import android.provider.*;
import android.widget.RadioGroup.*;
import android.opengl.*;

public class MainActivity extends Activity {
	public final int REQ_CD_AUDIOPICKER = 101;
	public final int REQ_CD_BGPICKER = 102;
	
	private MainBinding binding;

	private Intent audioPicker = new Intent(Intent.ACTION_GET_CONTENT);
	private Intent bgPicker = new Intent(Intent.ACTION_GET_CONTENT);
	private MediaPlayer soundPlayer;
	
	private Dialog aboutDlg;
	private Dialog emailDlg;
	
	private Timer timer;
	
	EditText bugsText;
	EditText expectedBehaviorText;
	EditText actualBehaviorText;
	EditText featureText;
	EditText featureReasonsText;
	EditText suggestedBehaviorText;
	EditText nameText;
	
	String bugsTextData;
	String expectedBehaviorTextData;
	String actualBehaviorTextData;
	String featureTextData;
	String featureReasonsTextData;
	String suggestedBehaviorTextData;
	String nameTextData;
	
	//int soundDuration;
	String audioFileName;
	String audioUnsafeName;
	
	Boolean loopMusicChecked;
	Boolean isSoundPlaying;
	int onLongPressCount;

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		binding = MainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		initialize(_savedInstanceState);
		
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
			} else {
				initializeLogic();
			}
		} else {
			initializeLogic();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}

	private void initialize(Bundle _savedInstanceState) {
		audioPicker.setType("audio/*");
		audioPicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		bgPicker.setType("video/*");
		bgPicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		
		// it will crash the app on start
		/*soundPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer sp) {
				//soundDuration = sp.getDuration();
			}
		});*/
		
		binding.openButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					startActivityForResult(audioPicker, REQ_CD_AUDIOPICKER);
				}
			});

		binding.loadBgButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					startActivityForResult(bgPicker, REQ_CD_BGPICKER);
				}
			});

		binding.aboutButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					aboutDlg = new Dialog(MainActivity.this);
					
					LayoutInflater inflater = getLayoutInflater();
					View aboutDlgView = inflater.inflate(R.layout.aboutdialog, null);
					aboutDlg.setContentView(aboutDlgView);
					
					Button emailButton = aboutDlgView.findViewById(R.id.button1);
					Button closeButton = aboutDlgView.findViewById(R.id.button2);
					
					emailButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View _view) {
							emailDialog();
						}
					});
					
					closeButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View _view) {
							aboutDlg.dismiss();
						}
					});
					
					aboutDlg.setCancelable(true);
					aboutDlg.setTitle("About MuzicBokx");
					aboutDlg.show();
				}
			});

		binding.play.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					if (soundPlayer.isPlaying()) {
						Toast toast = Toast.makeText(getApplicationContext(), "Your music is already playing!", Toast.LENGTH_LONG);
						View view = toast.getView();

						view.setBackgroundColor(Color.RED);

						TextView text = view.findViewById(android.R.id.message);
						text.setTextColor(Color.GREEN);
						text.setTextSize(16);

						toast.show();
					}
					else {
						isSoundPlaying = true;
						binding.openButton.setEnabled(false);
						soundPlayer.start();
						binding.bg.start();
						timeCodeStart();
					}
				}
			});

		binding.pause.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					if (soundPlayer.isPlaying()) {
						isSoundPlaying = true;
						binding.openButton.setEnabled(false);
						soundPlayer.pause();
						binding.bg.pause();
						timer.cancel();
					}
					else {
						Toast toast = Toast.makeText(getApplicationContext(), "Your music is already paused or stopped!", Toast.LENGTH_LONG);
						View view = toast.getView();

						view.setBackgroundColor(Color.RED);

						TextView text = view.findViewById(android.R.id.message);
						text.setTextColor(Color.GREEN);
						text.setTextSize(16);

						toast.show();
					}
				}
			});

		binding.rewind.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					if (soundPlayer.isPlaying()) {
						int forwardTime = 5000; // 5 seconds in milliseconds
						int currentPosition = soundPlayer.getCurrentPosition();

						if (currentPosition - forwardTime > 0 /*soundDuration*/) {
							soundPlayer.seekTo(currentPosition - forwardTime);
						} else {
							// If forward exceeds start of track, go to beginning
							soundPlayer.seekTo(0);
						}
					}
				}
			});

		binding.stop.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					soundPlayer.seekTo(0);
					soundPlayer.pause();
					binding.bg.seekTo(0);
					binding.bg.pause();
					timer.cancel();
					isSoundPlaying = false;
					binding.openButton.setEnabled(true);
				}
			});

		binding.forward.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					if (soundPlayer.isPlaying()) {
						int forwardTime = 5000; // 5 seconds in milliseconds
						int currentPos = soundPlayer.getCurrentPosition();
						if (currentPos + forwardTime <= soundPlayer.getDuration()) {
							soundPlayer.seekTo(currentPos + forwardTime);
						} else {
							soundPlayer.seekTo(soundPlayer.getDuration());
						}
					}
				}
			});
			
		binding.loopBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton cb, boolean checked) {
				if (checked == true) {
					loopMusicChecked = true;
					//if (soundPlayer.isPlaying()) {
						soundPlayer.setLooping(loopMusicChecked);
					//}
				}
				else {
					loopMusicChecked = false;
					//if (soundPlayer.isPlaying()) {
					soundPlayer.setLooping(loopMusicChecked);
					//}
				}
			}
		});
			
		binding.bg.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.setVolume(0f, 0f);
					mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
					//mp.start();
					//mp.setLooping(true);
					
					float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
					float screenRatio = binding.mainBgLayout.getWidth() / (float) binding.mainBgLayout.getHeight();
					float scaleX = 1f;
					float scaleY = 1f;

					if (videoRatio > screenRatio) {
						scaleY = videoRatio / screenRatio;
					} else {
						scaleX = screenRatio / videoRatio;
					}

					binding.bg.setScaleX(scaleX);
					binding.bg.setScaleY(scaleY);
				}
			});
			
		binding.bg.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.start();
			}
		});
		
		
		binding.bg.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View p1)
				{
					onLongPressCount++;
					if (onLongPressCount == 1) {
						binding.linear1.setVisibility(View.GONE);
						binding.linear7.setVisibility(View.GONE);
						
						View decorView = getWindow().getDecorView();
						int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION 
							| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
						decorView.setSystemUiVisibility(uiOptions);
						
						Toast toast = Toast.makeText(getApplicationContext(), "Long press again to exit Fullscreen Mode...", Toast.LENGTH_LONG);
						View view = toast.getView();

						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.setDuration(500);
						view.setBackgroundColor(Color.TRANSPARENT);

						TextView text = view.findViewById(android.R.id.message);
						text.setTextColor(Color.WHITE);
						text.setTextAppearance(Typeface.BOLD);
						text.setTextSize(24);

						toast.show();
					}
					if (onLongPressCount == 2) {
						binding.linear1.setVisibility(View.VISIBLE);
						binding.linear7.setVisibility(View.VISIBLE);
						
						View decorView = getWindow().getDecorView();
						// Setting the value to 0 clears all flags, making system bars reappear
						decorView.setSystemUiVisibility(0);
						
						onLongPressCount = 0;
					}
					return false;
				}
		});
	}

	private void initializeLogic() {
		//soundDuration = 0;
		
		String path = "android.resource://" + getPackageName() + "/" + R.raw.gradient1;
		binding.bg.setVideoURI(Uri.parse(path));
		
		audioFileName = "To start, click open, pick an audio, and hit play";
		binding.albumName.setText(audioFileName);
		binding.timeCode.setText("No audio file loaded");
		isSoundPlaying = false;
		//binding.bg.start();
	}

	private void timeCodeStart() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					if (soundPlayer != null && soundPlayer.isPlaying()) {
						final int currentPos = soundPlayer.getCurrentPosition();

						// You MUST use runOnUiThread to touch the UI
						runOnUiThread(new Runnable() {
								@Override
								public void run() {
									binding.timeCode.setText(formatTime(currentPos));
								}
							});
					}
				}
			}, 0, 1000); // 0 delay, repeat every 1000ms
	}
	
	// Add this method to your Activity or Helper class
	private String formatTime(int milliseconds) {
		long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - 
			TimeUnit.MINUTES.toSeconds(minutes);

		// Formats as "MM:SS" (e.g., 03:05)
		return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
	}
	
	
	private void emailDialog() {
		emailDlg = new Dialog(MainActivity.this);
		
		LayoutInflater inflater = getLayoutInflater();
		View emailDlgView = inflater.inflate(R.layout.emaildialog, null);
		emailDlg.setContentView(emailDlgView);
		
		bugsText = emailDlgView.findViewById(R.id.bugsText);
		featureText = emailDlgView.findViewById(R.id.featureText);
		nameText = emailDlgView.findViewById(R.id.nameText);
		expectedBehaviorText = emailDlgView.findViewById(R.id.expectedBehaviorText);
		actualBehaviorText = emailDlgView.findViewById(R.id.actualBehaviorText);
		featureReasonsText = emailDlgView.findViewById(R.id.featureReasonsText);
		suggestedBehaviorText = emailDlgView.findViewById(R.id.suggestedBehaviorText);
		
		Button submitButton = emailDlgView.findViewById(R.id.button1);
		Button cancelButton = emailDlgView.findViewById(R.id.button2);
		
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View _view) {
				Boolean emptyCheck;
				if (bugsText.getText().toString().isEmpty())
				{
					emptyCheck = true;
				}
				else {
					emptyCheck = false;
				}
				
				if (featureText.getText().toString().isEmpty())
				{
					emptyCheck = true;
				}
				else {
					emptyCheck = false;
				}
				
				if (nameText.getText().toString().isEmpty())
				{
					emptyCheck = true;
				}
				else {
					emptyCheck = false;
				}
				
				if (expectedBehaviorText.getText().toString().isEmpty())
				{
					emptyCheck = true;
				}
				else {
					emptyCheck = false;
				}
				
				if (actualBehaviorText.getText().toString().isEmpty())
				{
					emptyCheck = true;
				}
				else {
					emptyCheck = false;
				}
				
				if (featureReasonsText.getText().toString().isEmpty())
				{
					emptyCheck = true;
				}
				else {
					emptyCheck = false;
				}
				
				if (suggestedBehaviorText.getText().toString().isEmpty())
				{
					emptyCheck = true;
				}
				else {
					emptyCheck = false;
				}
				
				if (emptyCheck == false)
				{
					StringBuilder text = new StringBuilder();
					bugsTextData = bugsText.getText().toString();
					featureTextData = featureText.getText().toString();
					nameTextData = nameText.getText().toString();

					expectedBehaviorTextData = expectedBehaviorText.getText().toString();
					actualBehaviorTextData = actualBehaviorText.getText().toString();
					featureReasonsTextData = featureReasonsText.getText().toString();
					suggestedBehaviorTextData = suggestedBehaviorText.getText().toString();

					text.append("Hello, Jarred Apps").append("\n");
					text.append("\n");

					text.append("This is me, ").append(nameTextData).append(".\n");
					text.append("\n");

					text.append("I hope you're doing well.").append("\n");
					text.append("\n");

					text.append("I’d like to report an issue I encountered in the app.").append("\n");
					text.append("\n");

					text.append("Issue:").append("\n");
					text.append(bugsTextData).append("\n");
					text.append("\n");

					text.append("The Expected Behavior:").append("\n");
					text.append(expectedBehaviorTextData).append("\n");
					text.append("\n");

					text.append("The Actual Behavior:").append("\n");
					text.append(actualBehaviorTextData).append("\n");
					text.append("\n");

					text.append("Issue:").append("\n");
					text.append(bugsTextData).append("\n");
					text.append("\n");

					text.append("Additional Details:").append("\n");
					text.append("Version: ").append("1.0").append("\n");
					String manufacturer = android.os.Build.MANUFACTURER;
					String model = android.os.Build.MODEL;

					String deviceName;

					if (model.startsWith(manufacturer)) {
						deviceName = capitalize(model);
					} else {
						deviceName = capitalize(manufacturer) + " " + model;
					}
					text.append("Device: ").append(deviceName).append("\n");
					text.append("Android Version: ").append(android.os.Build.VERSION.RELEASE).append(" ").append(android.os.Build.VERSION.CODENAME);
					text.append("\n").append("\n");

					text.append("Also, I’d like to suggest a feature that could improve the user experience.").append("\n");
					text.append("\n");

					text.append("Features request:").append("\n");
					text.append(featureTextData).append("\n");
					text.append("\n");

					text.append("Reasons:").append("\n");
					text.append(featureReasonsTextData).append("\n");
					text.append("\n");

					text.append("Suggested Behavior:").append("\n");
					text.append(suggestedBehaviorTextData).append("\n");
					text.append("\n");

					text.append("Thank you for considering this suggestion. I appreciate your work on the app.").append("\n");
					text.append("\n");
					text.append("Best Regards,\n").append(nameTextData);
					
					emailFeedback(text.toString());
				}
				else if (emptyCheck == true)
				{
					Toast toast = Toast.makeText(getApplicationContext(), "You need to enter all of the blank text boxes", Toast.LENGTH_LONG);
					View view = toast.getView();

					view.setBackgroundColor(Color.RED);

					TextView text = view.findViewById(android.R.id.message);
					text.setTextColor(Color.GREEN);
					text.setTextSize(16);

					toast.show();
				}
			}
		});
		
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View _view) {
				emailDlg.dismiss();
			}
		});
		
		emailDlg.setCancelable(true);
		emailDlg.show();
	}
	
	private String capitalize(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		char first = str.charAt(0);
		if (Character.isUpperCase(first)) {
			return str;
		} else {
			return Character.toUpperCase(first) + str.substring(1);
		}
	}
	
	private void emailFeedback(String text) {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:")); // Only email apps handle this
		emailIntent.setType("message/rfc822");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"reyesgavinjarred@gmail.com"});
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback or New Feature Request on MuzicBokx OTG");
		emailIntent.putExtra(Intent.EXTRA_TEXT, text);

		try {
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "No email apps installed.", Toast.LENGTH_SHORT).show();
		}
	}
	
	public String getFileName(Uri uri) {
		String result = null;
		if (uri.getScheme().equals("content")) {
			// Query the content provider for the file name
			try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
				if (cursor != null && cursor.moveToFirst()) {
					int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
					if (nameIndex != -1) {
						result = cursor.getString(nameIndex);
					}
				}
			}
		}

		// Fallback for file:// URIs or if the cursor query fails
		if (result == null) {
			result = uri.getPath();
			int cut = result.lastIndexOf('/');
			if (cut != -1) {
				result = result.substring(cut + 1);
			}
		}
		return result;
	}
	
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);

		switch (_requestCode) {
			case REQ_CD_AUDIOPICKER:
				if (_resultCode == Activity.RESULT_OK) {
					Uri uri;

					if (_data.getClipData() != null) {
						uri = _data.getClipData().getItemAt(0).getUri();
					} else {
						uri = _data.getData();
					}

					if (uri != null) {
						try {
							soundPlayer = new MediaPlayer();
							soundPlayer.setDataSource(this, uri);
							
							audioFileName = getFileName(uri);
							binding.albumName.setText(audioFileName);
							binding.timeCode.setText("00:00");
							
							if (soundPlayer.isPlaying()) {
								soundPlayer.stop();
								soundPlayer.prepareAsync();
								soundPlayer.reset();
								soundPlayer.release();
							}
							else
							{
								soundPlayer.prepareAsync();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				else {

				}
				break;

			case REQ_CD_BGPICKER:
				if (_resultCode == Activity.RESULT_OK) {
					Uri uri;

					if (_data.getClipData() != null) {
						uri = _data.getClipData().getItemAt(0).getUri();
					} else {
						uri = _data.getData();
					}

					if (uri != null) {
						try {
							binding.bg.setVideoURI(uri);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				else {

				}
				break;
			default:
				break;
		}
	}
	
	@Override
	public void onBackPressed() {
		android.app.AlertDialog.Builder backPressed = new android.app.AlertDialog.Builder(this);
		backPressed.setTitle("Are you sure to quit?");
		backPressed.setIcon(R.drawable.icon_question_mark_sharp);
		backPressed.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					if (soundPlayer.isPlaying())
						soundPlayer.stop();
					finish();
				}
			});
		backPressed.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				
			}
		});
		backPressed.show();
	}
} // 6 7!!!

