package
com.touhiDroid.backgroundgpsgetter;

import com.touhiDroid.backgroundgpsgetter.service.GPSSenderService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		this.startService(new Intent(this, GPSSenderService.class));
	}
}