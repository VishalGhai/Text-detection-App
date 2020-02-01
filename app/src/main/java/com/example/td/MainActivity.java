package com.example.td;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView image;
    Button b1,b2;
    private int CAMERA_REQUEST=100;
    FirebaseVisionTextRecognizer detector;
    Bitmap image_rec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image=findViewById(R.id.imageView);
        b1=findViewById(R.id.button);
        b2=findViewById(R.id.button2);

        detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(i,CAMERA_REQUEST);
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectText(image_rec);
            }
        });
    }//Oncreate



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CAMERA_REQUEST){
            Bundle extra=data.getExtras();

            image_rec=(Bitmap)extra.get("data");

            image.setImageBitmap(image_rec);
        }
    }


    public  void detectText(Bitmap b){
        FirebaseVisionImage f_image=FirebaseVisionImage.fromBitmap(b);

        //Task<FirebaseVisionText> t=detector.processImage(f_image);
detector.processImage(f_image).addOnCompleteListener(new OnCompleteListener<FirebaseVisionText>() {
    @Override
    public void onComplete(@NonNull Task<FirebaseVisionText> task) {

        List<FirebaseVisionText.TextBlock> blocks=task.getResult().getTextBlocks();
        String d ="";
        for (int i=0;i<blocks.size();i++){
            List<FirebaseVisionText.Line> lines=blocks.get(i).getLines();

            for (int j=0;j<lines.size();j++){
                String k=lines.get(j).getText();
                d+=k+"\n";
            }d+="\n";
        }
        Toast.makeText(MainActivity.this,""+d,Toast.LENGTH_SHORT).show();
    }
});



    }

}//MainActivity


