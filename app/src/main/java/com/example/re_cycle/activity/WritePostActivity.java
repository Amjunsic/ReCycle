package com.example.re_cycle.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.re_cycle.R;
import com.example.re_cycle.Writeinfo;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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
import java.util.Date;
import java.util.Objects;


public class WritePostActivity extends BasicActivity implements AutoPermissionsListener
{
    private static final String TAG = "WritePostActivity";

    private FirebaseUser user;
    private String imagePath;
    private ImageView imageImageview;
    private Bitmap bmp;
    private RelativeLayout loaderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        loaderLayout = findViewById(R.id.loaderlayout);
        imageImageview = findViewById(R.id.imageButton);

        findViewById(R.id.editbutton).setOnClickListener(onClickListener);
        findViewById(R.id.imageButton).setOnClickListener(onClickListener);
        findViewById(R.id.gallertButton).setOnClickListener(onClickListener);
        findViewById(R.id.cancelText).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.editbutton:
                    postUpdate();
                    break;

                case R.id.imageButton:
                    CardView cardView = findViewById(R.id.buttonsCardView);
                    AutoPermissions.Companion.loadAllPermissions(WritePostActivity.this,101);//권한 설정

                    if (cardView.getVisibility() == View.VISIBLE)//카드뷰가 안보일때
                    {
                        cardView.setVisibility(View.GONE);
                    }
                    else//카드뷰가 보일때때
                    {
                        cardView.setVisibility(View.VISIBLE);
                    }
                    break;

                    case R.id.gallertButton:
                        Intent intent = new Intent();
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 1);
                        break;

                case R.id.cancelText:
                    cardView = findViewById(R.id.buttonsCardView);
                    cardView.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private void postUpdate()
    {
        final String title = ((EditText) findViewById(R.id.title_editTextText)).getText().toString();
        final String contnts = ((EditText) findViewById(R.id.content_editButton)).getText().toString();

        if (title.length() > 0 && contnts.length() > 0 && imagePath != null)
        {
            loaderLayout.setVisibility(View.VISIBLE);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference mountainImagesRef = storageRef.child("post/" + user.getUid() + "/Image."+getMimeType(imagePath));

            if (imagePath == null)
            {
                toast("인증할 사진을 넣어주세요.");
            }
            else
            {
                try
                {
                    InputStream stream = new FileInputStream(new File(imagePath));
                    UploadTask uploadTask = mountainImagesRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                    {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw Objects.requireNonNull(task.getException());
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
                                Writeinfo writeinfo = new Writeinfo(title,contnts, user.getUid(),new Date(),downloadUri.toString());
                                upLoder(writeinfo);
                            }
                            else
                            {
                                toast(" 게시글 작성에 실패하였습니다.");
                                Log.e("로그", "실패");
                            }
                        }
                    });
                }
                catch (FileNotFoundException e)
                {
                    Log.e("로그", "에러: " + e.toString());
                }
            }

        }
        else
        {
            toast("인증 게시글을 작성해주세요(사진 포함).");
        }
    }


    private void upLoder(Writeinfo writeinfo)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").add(writeinfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                {
                    @Override
                    public void onSuccess(DocumentReference documentReference)
                    {
                        loaderLayout.setVisibility(View.GONE);
                        toast("회원정보 등록을 성공하였습니다.");
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
                        toast("회원정보 등록에 실패하였습니다.");
                        Log.e(TAG, "Error writing document", e);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 1://갤러리 선택
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    try
                    {
                        InputStream is = getContentResolver().openInputStream(data.getData());
                        Bitmap bm = BitmapFactory.decodeStream(is);
                        imagePath = getPath(data.getData());
                        is.close();
                        imageImageview.setAdjustViewBounds(true);
                        imageImageview.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageImageview.setImageBitmap(bm);

                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if (resultCode == RESULT_CANCELED)
                {
                    toast("취소");
                }
                break;
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this,requestCode,permissions,this);
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

    public boolean onTouchEvent(MotionEvent event)
    {
        CardView cardView = findViewById(R.id.buttonsCardView);
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //손가락으로 화면을 누르기 시작했을 때 할 일
                cardView.setVisibility(View.GONE);
                break;
            case MotionEvent.ACTION_MOVE:
                //터치 후 손가락을 움직일 때 할 일
                break;
            case MotionEvent.ACTION_UP:
                //손가락을 화면에서 뗄 때 할 일
                break;
            case MotionEvent.ACTION_CANCEL:
                // 터치가 취소될 때 할 일
                break;
            default:
                break;
        }
        return true;
    }

    public String getPath (Uri uri)
    {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    public static String getMimeType(String url)
    {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private void GotoActivity (Class I)
    {
        Intent intent = new Intent(this, I);
        startActivityForResult(intent, 0);
    }

    private void toast (String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDenied(int i, String[] strings)
    {

    }

    @Override
    public void onGranted(int i, String[] strings)
    {

    }
}
