package com.example.re_cycle.activity;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.re_cycle.Memberinfo;
import com.example.re_cycle.R;
import com.example.re_cycle.activity.CameraActivity;
import com.example.re_cycle.activity.MainActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import io.grpc.Context;


public class Member_initActivity extends BasicActivity implements AutoPermissionsListener
{
    private static final String TAG = "MemberinitActivity";
    private ImageView profile_imageview;
    private String profilePath;
    private FirebaseUser user;
    private Bitmap bmp;
    private RelativeLayout loaderLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);

        loaderLayout = findViewById(R.id.loaderlayout);
        profile_imageview = findViewById(R.id.cameraImageView);//?????? ???????????? id ????????????
        profile_imageview.setOnClickListener(onClickListener);//?????? ?????? id ????????????

        findViewById(R.id.confirmedButton).setOnClickListener(onClickListener);
        findViewById(R.id.pictureButton).setOnClickListener(onClickListener);
        findViewById(R.id.gallertButton).setOnClickListener(onClickListener);

        //ActivityCompat.requestPermissions(Member_initActivity.this ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

    }

    @Override
    public void onBackPressed()
    {
    }


    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation)
    {

        Matrix matrix = new Matrix();
        switch (orientation)
        {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try
        {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this,requestCode,permissions,this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 0://????????? ??????
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    profilePath = data.getStringExtra("profilePath");
                    bmp = BitmapFactory.decodeFile(profilePath);

                    //????????? ?????? ?????? ??????
                    ExifInterface exif = null;
                    try
                    {
                        exif = new ExifInterface(profilePath);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);
                    Bitmap bmRotated = rotateBitmap(bmp, orientation);//????????? ??????

                    profile_imageview.setImageBitmap(bmRotated);

                    Log.e("profilePath", "profilePath:" + profilePath);

                }
                break;
            }
            case 101://????????? ??????
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    try
                    {
                        InputStream is = getContentResolver().openInputStream(data.getData());
                        Bitmap bm = BitmapFactory.decodeStream(is);
                        profilePath = getPath(data.getData());
                        is.close();
                        profile_imageview.setImageBitmap(bm);
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }


                } else if (resultCode == RESULT_CANCELED)
                {
                    toast("??????");
                }
                break;
            }

        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener()//??????ui ????????? ??????
    {
        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.confirmedButton:
                    profileUpdate();
                    break;
                case R.id.cameraImageView:
                    CardView cardView = findViewById(R.id.buttonsCardView);
                    AutoPermissions.Companion.loadAllPermissions(Member_initActivity.this,101);

                    if (cardView.getVisibility() == View.VISIBLE)//???????????? ????????????
                    {
                        cardView.setVisibility(View.GONE);
                    }
                    else//???????????? ????????????
                    {
                        cardView.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.pictureButton:
                    GotoActivity(CameraActivity.class);
                    break;
                case R.id.gallertButton:
                    Intent intent = new Intent();
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 101);
            }
        }
    };

    public boolean onTouchEvent(MotionEvent event)
    {
        CardView cardView = findViewById(R.id.buttonsCardView);
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //??????????????? ????????? ????????? ???????????? ??? ??? ???
                cardView.setVisibility(View.GONE);
                break;
            case MotionEvent.ACTION_MOVE:
                //?????? ??? ???????????? ????????? ??? ??? ???
                break;
            case MotionEvent.ACTION_UP:
                //???????????? ???????????? ??? ??? ??? ???
                break;
            case MotionEvent.ACTION_CANCEL:
                // ????????? ????????? ??? ??? ???
                break;
            default:
                break;
        }
        return true;
    }

    private void profileUpdate()
    {
        final String name = ((EditText) findViewById(R.id.NameEditText)).getText().toString();
        final String phoneNumber = ((EditText) findViewById(R.id.Phonnumber_EditTest)).getText().toString();
        final String birthDay = ((EditText) findViewById(R.id.BirthdayEditText)).getText().toString();

        if (name.length() > 0 && phoneNumber.length() > 9 && birthDay.length() > 5)
        {
            loaderLayout.setVisibility(View.VISIBLE);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference mountainImagesRef = storageRef.child("users/" + user.getUid() + "/profileImage.jpg");

            if (profilePath == null)
            {
                Memberinfo memberInfo = new Memberinfo(name, phoneNumber, birthDay);
                upLoder(memberInfo);
            }
            else
            {
                try
                {
                    InputStream stream = new FileInputStream(new File(profilePath));
                    UploadTask uploadTask = mountainImagesRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                    {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            return mountainImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                Uri downloadUri = task.getResult();
                                Memberinfo memberInfo = new Memberinfo(name, phoneNumber, birthDay, downloadUri.toString());
                                upLoder(memberInfo);
                            } else
                            {
                                toast("???????????? ????????? ?????????????????????.");
                                Log.e("??????", "??????");
                            }
                        }
                    });
                } catch (FileNotFoundException e)
                {
                    Log.e("??????", "??????: " + e.toString());
                }
            }

        } else
        {
            toast("??????????????? ??????????????????.");
        }
    }


    private void upLoder(Memberinfo memberInfo)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).set(memberInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        loaderLayout.setVisibility(View.GONE);
                        toast("???????????? ????????? ?????????????????????.");
                        finish();
                        GotoActivity(MainActivity.class);
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        loaderLayout.setVisibility(View.GONE);
                        toast("???????????? ????????? ?????????????????????.");
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void toast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void GotoActivity(Class I)
    {
        Intent intent = new Intent(this, I);
        startActivityForResult(intent, 0);
    }

    public String getPath(Uri uri)
    {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    @Override
    public void onDenied(int i, String[] strings)
    {
        toast("?????? ?????????");
    }

    @Override
    public void onGranted(int i, String[] strings)
    {
        toast("?????? ?????????");
    }
}
