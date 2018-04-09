package net.zekjur.davsync;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import net.zekjur.davsync.configuration.Configuration;
import net.zekjur.davsync.configuration.ConfigurationFactory;

public class NetworkReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.d("davsync", "network connectivity changed");

		if (!ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
		{
			return;
		}

		ConnectivityManager cs = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cs.getActiveNetworkInfo();
		if (info == null || !info.isConnected())
		{
			// Not connected anymore
			return;
		}

		boolean wifiOnly = getConfiguration(context).isWiFiOnly();
		if (wifiOnly && !(ConnectivityManager.TYPE_WIFI == info.getType()))
		{
			Log.d("davsync", "Not on WiFi, not doing anything.");
			return;
		}

		Log.d("davsync", "Checking whether pictures need to be synced");

		DavSyncOpenHelper helper = new DavSyncOpenHelper(context);
		List<String> uris = helper.getQueuedUris();
		for (String uri : uris)
		{
			Intent uploadIntent = new Intent(context, UploadService.class);
			uploadIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri));
			context.startService(uploadIntent);
		}
	}

	private Configuration getConfiguration(Context context)
	{
		return new ConfigurationFactory(context).getConfiguration();
	}
}
