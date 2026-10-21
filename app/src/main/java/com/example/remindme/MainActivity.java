package com.example.remindme;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Your variable declarations will now be recognized
    ListView listViewTasks;
    Button btnAddTodo, btnAddNote;
    ArrayList<String> taskList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewTasks = findViewById(R.id.listViewTasks);
        btnAddTodo = findViewById(R.id.btnAddTodo);
        btnAddNote = findViewById(R.id.btnAddNote);

        taskList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        listViewTasks.setAdapter(adapter);
        listViewTasks.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Pilih Aksi");

            String[] options = {"Edit", "Hapus"};
            builder.setItems(options, (dialog, which) -> {
                if (which == 0) {
                    showEditDialog(position);
                } else if (which == 1) {
                    taskList.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });

            builder.show();
            return true;
        });
        btnAddTodo.setOnClickListener(v -> showInputDialog("To-Do"));
        btnAddNote.setOnClickListener(v -> showInputDialog("Note"));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showInputDialog(String type) {
        // 1. Instantiates an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah " + type);

        // 3. Creates an EditText for user input
        EditText input = new EditText(this);
        input.setHint("Masukkan " + type); // Sets a hint text
        builder.setView(input); // Adds the EditText to the dialog

        // 4. Sets the positive (Save) button
        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String text = input.getText().toString(); // Gets the user's input
            if (!text.isEmpty()) { // Checks if the input is not empty
                // 5. Adds the new task to the list
                taskList.add(type + ": " + text);
                // 6. Notifies the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Batal", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showEditDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Data");

        EditText input = new EditText(this);
        input.setText(taskList.get(position));
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newText = input.getText().toString();
            if (!newText.isEmpty()) {
                taskList.set(position, newText);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
