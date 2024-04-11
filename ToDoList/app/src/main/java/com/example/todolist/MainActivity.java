package com.example.todolist;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final List<Todo> todoList = new ArrayList<>();
    private ListView listView;

    private ViewAdapter adapter;

    public enum CreateType {
        CLICK,
        ENTER,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initTodoDefaultList();

        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ViewAdapter(this, todoList);
        listView.setAdapter(adapter);

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged( ) {
                TextView emptyView = findViewById(R.id.empty_text_view);

                if (todoList.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    emptyView.setVisibility(View.GONE);
                }
            }
        });

        EditText edit_text = (EditText) findViewById(R.id.edit_text);
        edit_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onCreateToDoItem(CreateType.ENTER);
                    return true;
                }
                return false;
            };
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0 ,100);
        toast.show();
    }

    private void initTodoDefaultList() {
        for (int i = 0; i < 2; i++) {
            int order = i + 1;
            Todo item = new Todo("Default TODO " + order, 1);
            todoList.add(item);
        }
    }

    public void onAddButtonClick(View v) {
        onCreateToDoItem(CreateType.CLICK);
    }

    public void onCreateToDoItem(CreateType from) {
        EditText editText = (EditText) findViewById(R.id.edit_text);
        String inputText = editText.getText().toString();
        if (inputText.isEmpty()) {
            if (from.equals(CreateType.CLICK)) {
                showToast("Please type something");
            }
            return;
        }
        editText.setText("");
        Todo todoItem = new Todo(inputText, 1);
        todoList.add(todoItem);
        listView.requestLayout();
        adapter.notifyDataSetChanged();
    }
}
