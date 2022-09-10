package in.android.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class PdfDownloadActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "channelId";
    private static final int NOTIFICATION_ID = 100;

    Display mDisplay;
    String path;
    Bitmap bitmap;

    int totalHeight;
    int totalWidth;

    public static final int READ_PHONE = 110;
    String filenameInitials = "MyPdf_";
    String fileName;
    Button btnPrint;
    private WindowManager windowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_download);

        btnPrint = findViewById(R.id.btnPrint);

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mDisplay = windowManager.getDefaultDisplay();

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getView();
            }
        });
    }

    public void getView(){
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyPdf/");

        if(!folder.exists()){
            folder.mkdir();
        }

        path = folder.getAbsolutePath();
        fileName = filenameInitials + System.currentTimeMillis();
        path = path+"/"+fileName+".pdf";

        LinearLayout viewPdf = findViewById(R.id.viewPdf);
        totalHeight = viewPdf.getHeight();
        totalWidth = viewPdf.getWidth();

        bitmap = getBitMapFromView(viewPdf, totalHeight, totalWidth);

        createPdf();
    }

    private void createPdf() {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);

        Bitmap bitmap = Bitmap.createScaledBitmap(this.bitmap, this.bitmap.getWidth(), this.bitmap.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        pdfDocument.finishPage(page);
        File filePath = new File(path);

        try{
            pdfDocument.writeTo(new FileOutputStream(filePath));
            notification(filePath);
            Toast.makeText(this, "Pdf Downloaded", Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "Something Wrong: "+e.toString(), Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();

    }

    private Bitmap getBitMapFromView(View viewPdf, int totalHeight, int totalWidth) {
        Bitmap bitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable drawable = viewPdf.getBackground();

        if(drawable != null){
            drawable.draw(canvas);
        }else{
            canvas.drawColor(Color.WHITE);
        }

        viewPdf.draw(canvas);
        return bitmap;
    }

    public  void notification(File file){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri= FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(fileName +" Downloaded")
                    .setContentText("Click to Open")
                    .setAutoCancel(true)
                    .build();

            mNotificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH));
        }else{
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(fileName +" Downloaded")
                    .setContentText("Click to Open")
                    .setAutoCancel(true)
                    .build();
        }
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }
}