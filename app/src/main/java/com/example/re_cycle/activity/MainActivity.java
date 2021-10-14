package com.example.re_cycle.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.re_cycle.R;
import com.example.re_cycle.Writeinfo;
import com.example.re_cycle.adapter.PostAdaper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BasicActivity
{
    private static final String TAG = "MainActivity";
    private FirebaseUser user;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null)
        {
            GotoActivity(SignUpActivity.class);
        }
        else
            {
            db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        DocumentSnapshot document = task.getResult();
                        if(document != null)
                        {
                            if (document.exists())
                            {
                                Log.e(TAG, "DocumentSnapshot data: " + document.getData());
                            }
                            else
                            {
                                Log.e(TAG, "No such document");
                                GotoActivity(Member_initActivity.class);
                            }
                        }
                    }
                    else
                        {
                        Log.e(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

        recyclerView = findViewById(R.id.recycleView);

        findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }


    protected void onResume()//앱 재실행시
    {
        super.onResume();

        if(user != null)
        {
            CollectionReference collectionReference = db.collection("posts");

            collectionReference.orderBy("createdAt", Query.Direction.DESCENDING)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        ArrayList<Writeinfo> postList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult())
                        {
                            Log.e(TAG, document.getId() + " => " + document.getData());
                            postList.add(new Writeinfo(
                                    document.getData().get("title").toString(),
                                    document.getData().get("contents").toString(),
                                    document.getData().get("publisher").toString(),
                                    new Date(document.getDate("createdAt").getTime()),
                                    document.getData().get("photoUrl").toString()));
                        }

                        RecyclerView.Adapter mAdaper = new PostAdaper(MainActivity.this, postList);
                        recyclerView.setAdapter(mAdaper);
                    }
                    else
                    {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                /*
                case R.id.logoutButton:
                    FirebaseAuth.getInstance().signOut();
                    GotoActivity(SignUpActivity.class);
                    break;
                 */
                case  R.id.floatingActionButton:
                    GotoActivity(WritePostActivity.class);
                    break;
            }
        }
    };



    private void  GotoActivity(Class I)
    {
        Intent intent = new Intent(this,I);
        startActivity(intent);
    }
}