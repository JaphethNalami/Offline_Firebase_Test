package com.example.offlinefirebasetest;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button saveButton;
    TextInputEditText messageText;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    String message1,status1,id1;
    Dialog dialog;
    ArrayList<MessageClass> messageList;
    MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveButton = findViewById(R.id.button);
        messageText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.recyclerView);

        //dialog to show progress
        dialog = new MaterialAlertDialogBuilder(this)
                .setView(new ProgressBar(this))
                .setTitle("Saving item")
                .setMessage("Please wait")
                .create();


        //initialize firestore
        db = FirebaseFirestore.getInstance();

        //initialize arraylist
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(MainActivity.this, messageList);
        recyclerView.setAdapter(messageAdapter);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //save the message
        saveButton.setOnClickListener(v -> {
            dialog.show();

            message1 = Objects.requireNonNull(messageText.getText()).toString();
            status1 = "unread";

            //validate the message
            if (message1.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            else {
                saveMessage();
            }
        });

        //get messages
        getMessage();


    }


    private void saveMessage() {

        //map to store the message
        Map<String, Object> message = new HashMap<>();
        message.put("message", message1);
        message.put("status", status1);


        db.collection("Messages").add(message)
                .addOnSuccessListener(documentReference -> {
                    // Get the generated unique ID
                    String messageId = documentReference.getId();

                    // Update the item with the generated ID
                    message.put("id", messageId);
                    db.collection("Messages").document(messageId)
                            .set(message) // Update the item with the generated ID
                            .addOnSuccessListener(aVoid -> {

                                Toast.makeText(MainActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                                //clear the edit text
                                messageText.setText("");

                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(MainActivity.this, "Error updating item", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Error adding item", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });

    }

    private void getMessage() {
        db.collection("Messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    return;
                }
                assert value != null;
                //clear the arraylist
                messageList.clear();

                //add all products to the arraylist
                messageList.addAll(value.toObjects(MessageClass.class));


                messageAdapter.notifyDataSetChanged();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }
}