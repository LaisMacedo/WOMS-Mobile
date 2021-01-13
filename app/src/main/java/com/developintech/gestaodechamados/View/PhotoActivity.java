package com.developintech.gestaodechamados.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.developintech.gestaodechamados.R;

public class PhotoActivity extends AppCompatActivity {



    FloatingActionButton btnPhoto;
    ImageView photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9;
    int photoCounter = 0;
    private static final int CAM_REQUEST=1313;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        btnPhoto = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        photo1 = (ImageView)findViewById(R.id.imageView1);
        photo2 = (ImageView)findViewById(R.id.imageView2);
        photo3 = (ImageView)findViewById(R.id.imageView3);
        photo4 = (ImageView)findViewById(R.id.imageView4);
        photo5 = (ImageView)findViewById(R.id.imageView5);
        photo6 = (ImageView)findViewById(R.id.imageView6);
        photo7 = (ImageView)findViewById(R.id.imageView7);
        photo8 = (ImageView)findViewById(R.id.imageView8);
        photo9 = (ImageView)findViewById(R.id.imageView9);
        btnPhoto.setOnClickListener(new btnTakePhotoClicker());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAM_REQUEST){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            if (photoCounter == 0)
                photo1.setImageBitmap(bitmap);
            if (photoCounter == 1)
                photo2.setImageBitmap(bitmap);
            if (photoCounter == 2)
                photo3.setImageBitmap(bitmap);
            if (photoCounter == 3)
                photo4.setImageBitmap(bitmap);
            if (photoCounter == 4)
                photo5.setImageBitmap(bitmap);
            if (photoCounter == 5)
                photo6.setImageBitmap(bitmap);
            if (photoCounter == 6)
                photo7.setImageBitmap(bitmap);
            if (photoCounter == 7)
                photo8.setImageBitmap(bitmap);
            if (photoCounter == 8)
                photo9.setImageBitmap(bitmap);
            if (photoCounter == 9)
                photoCounter = 0;

            photoCounter++;
        }
    }

    class btnTakePhotoClicker implements  Button.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,CAM_REQUEST);
        }
    }
}
