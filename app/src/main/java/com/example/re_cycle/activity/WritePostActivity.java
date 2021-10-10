package com.example.re_cycle.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.re_cycle.Memberinfo;
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
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;


public class WritePostActivity extends BasicActivity
{
    private static final String TAG = "WritePostActivity";

    private FirebaseUser user;
    private String imagePath;
    private ImageView image_imageview;
    private ArrayList<String> pathList = new ArrayList<>();
    private LinearLayout parent;

    private int pathCount, succesCount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        parent = findViewById(R.id.contentview);
        findViewById(R.id.editbutton).setOnClickListener(onClickListener);
        findViewById(R.id.gallayfloatingActionButton).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.editbutton:
                    storageUpdate();
                    break;

                case R.id.gallayfloatingActionButton:
                    Intent intent = new Intent();
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 101);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 101://갤러리 선택
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    try
                    {
                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        InputStream is = getContentResolver().openInputStream(data.getData());
                        Bitmap bm = BitmapFactory.decodeStream(is);
                        imagePath = getPath(data.getData());
                        pathList.add(imagePath);
                        is.close();

                        image_imageview = new ImageView(WritePostActivity.this);
                        image_imageview.setLayoutParams(layoutParams);
                        image_imageview.setImageBitmap(bm);
                        parent.addView(image_imageview);

                        EditText editText = new EditText(WritePostActivity.this);
                        editText.setLayoutParams(layoutParams);
                        editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                        parent.addView(editText);

                    } catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }


                } else if (resultCode == RESULT_CANCELED)
                {
                    toast("취소");
                }
                break;
            }

        }
    }


    private void storageUpdate()
    {
        final String title = ((EditText) findViewById(R.id.title_editTextText)).getText().toString();

        if (title.length() > 0 )
        {
            ArrayList<String> contentList = new ArrayList<>();
            user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();

            for (int i = 0; i < parent.getChildCount(); i++)
            {
                View view = parent.getChildAt(i);
                if (view instanceof EditText)
                {
                    String text = ((EditText) view).getText().toString();
                    if (text.length() > 0)//추가된 텍스트뷰
                    {
                        contentList.add(text);
                    }
                    else//추가된 이미지뷰
                    {
                        contentList.add(pathList.get(pathCount));

                        final StorageReference mountainImagesRef = storageReference.child("users/" + user.getUid() + "/" + pathCount + ".jpg");
                        try
                        {
                            InputStream stream = new FileInputStream(new File(pathList.get(pathCount)));

                            StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + (contentList.size()-1)).build();

                            UploadTask uploadTask = mountainImagesRef.putStream(stream, metadata);
                            uploadTask.addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception exception)
                                {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                            {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));
                                    mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                                    {
                                        @Override
                                        public void onSuccess(Uri uri)
                                        {
                                            Log.e("uri", "uri:" + uri);
                                            contentList.set(index,uri.toString());
                                            succesCount++;
                                            if (pathList.size() == succesCount)
                                            {
                                                //완료
                                                Writeinfo writeinfo = new Writeinfo(title, contentList, user.getUid(),new Date());
                                                storeUploder(writeinfo);
                                                for (int i=0; i <contentList.size();i++)
                                                {
                                                    Log.e("로그","콘텐츠: "+contentList.get(i));
                                                }

                                            }
                                        }
                                    });
                                }
                            });
                        } catch (FileNotFoundException e)
                        {
                            Log.e("로그", "에러: " + e.toString());
                        }
                        pathCount++;
                    }
                }
            }

        }
        else
        {
            toast("회원정보를 입력해주세요.");
        }
    }

        private void storeUploder (Writeinfo writeinfo)
        {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("posts").add(writeinfo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                    {
                        @Override
                        public void onSuccess(DocumentReference documentReference)
                        {
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
                            toast("회원정보 등록에 실패하였습니다.");
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
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

        private void GotoActivity (Class I)
        {
            Intent intent = new Intent(this, I);
            startActivityForResult(intent, 0);
        }

        private void toast (String text)
        {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }


}
