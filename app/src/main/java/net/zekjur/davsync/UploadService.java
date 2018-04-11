package net.zekjur.davsync;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.InputStream;

public class UploadService extends IntentService
{
	public UploadService()
	{
		super("UploadService");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		final Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
		final Notification.Builder mBuilder = new Notification.Builder(this);
		mBuilder.setContentTitle("Uploading to WebDAV server");
		mBuilder.setContentText(filename);
		mBuilder.setSmallIcon(android.R.drawable.ic_menu_upload);
		mBuilder.setOngoing(true);
		mBuilder.setProgress(100, 30, false);
		final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(uri.toString(), 0, mBuilder.build());

//		HttpPut httpPut = new HttpPut(webdavUrl + filename);

		ParcelFileDescriptor fd;
		InputStream stream;
//		try
//		{
//			fd = cr.openFileDescriptor(uri, "r");
//			stream = cr.openInputStream(uri);
//		}
//		catch (FileNotFoundException e1)
//		{
//			e1.printStackTrace();
//			return;
//		}
//
//		CountingInputStreamEntity entity = new CountingInputStreamEntity(stream, fd.getStatSize());
//		entity.setUploadListener(new UploadListener()
//		{
//			@Override
//			public void onChange(int percent)
//			{
//				mBuilder.setProgress(100, percent, false);
//				mNotificationManager.notify(uri.toString(), 0, mBuilder.build());
//			}
//		});
//
//		httpPut.setEntity(entity);
//
//		DefaultHttpClient httpClient = new DefaultHttpClient();
//
//		if (webdavUser != null && webdavPassword != null)
//		{
//			AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT);
//			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(webdavUser, webdavPassword);
//			httpClient.getCredentialsProvider().setCredentials(authScope, credentials);
//
//			try
//			{
//				httpPut.addHeader(new BasicScheme().authenticate(credentials, httpPut));
//			}
//			catch (AuthenticationException e1)
//			{
//				e1.printStackTrace();
//				return;
//			}
//		}
//
//		try
//		{
//			HttpResponse response = httpClient.execute(httpPut);
//			int status = response.getStatusLine().getStatusCode();
//			// 201 means the file was created.
//			// 200 and 204 mean it was stored but already existed.
//			if (status == 201 || status == 200 || status == 204)
//			{
//				// The file was uploaded, so we remove the ongoing notification,
//				// remove it from the queue and that’s it.
//				mNotificationManager.cancel(uri.toString(), 0);
//				DavSyncOpenHelper helper = new DavSyncOpenHelper(this);
//				helper.removeUriFromQueue(uri.toString());
//				return;
//			}
//			Log.d("davsyncs", "" + response.getStatusLine());
//			mBuilder.setContentText(filename + ": " + response.getStatusLine());
//		}
//		catch (ClientProtocolException e)
//		{
//			e.printStackTrace();
//			mBuilder.setContentText(filename + ": " + e.getLocalizedMessage());
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//			mBuilder.setContentText(filename + ": " + e.getLocalizedMessage());
//		}
//
//		// XXX: It would be good to provide an option to try again.
//		// (or try it again automatically?)
//		// XXX: possibly we should re-queue the images in the database
//		mBuilder.setContentTitle("Error uploading to WebDAV server");
//		mBuilder.setProgress(0, 0, false);
//		mBuilder.setOngoing(false);
//		mNotificationManager.notify(uri.toString(), 0, mBuilder.build());
	}
}
