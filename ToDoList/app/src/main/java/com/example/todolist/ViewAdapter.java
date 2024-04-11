package com.example.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewAdapter extends BaseAdapter {
    private final Context context;
    private List<Todo> data = new ArrayList<>();

    static class ViewHolder {
        TextView todoName;

        Button button;

        RadioButton statusTag;
    }

    public ViewAdapter(Context context, List<Todo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return  data.size();
    }

    @Override
    public Todo getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Todo todo = getItem(position);
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.todoName = view.findViewById(R.id.todo_text);
            viewHolder.button = view.findViewById(R.id.todo_button);
            viewHolder.statusTag = view.findViewById(R.id.status_tag);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        // Content
        String text = todo.getContent();
        if (todo.getIsComplete()) {
            SpannableString spannableString = new SpannableString(text);
            StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
            spannableString.setSpan(strikethroughSpan, 0 , text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.todoName.setText(spannableString);
        } else {
            viewHolder.todoName.setText(text);
        }
        viewHolder.statusTag.setChecked(todo.getIsComplete());

        // Delete
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                ViewAdapter.this.notifyDataSetChanged();
            }
        });

        // Complete
        viewHolder.statusTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todo.setIsComplete(!todo.getIsComplete());
                data.set(position, todo);
                ViewAdapter.this.notifyDataSetChanged();
            }
        });

        // Edit
        viewHolder.todoName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle("Please Edit");
                EditText editText = new EditText(view.getContext());
                editText.setText(todo.getContent());
                dialog.setView(editText);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputText = editText.getText().toString();
                        todo.setContent(inputText);
                        ViewAdapter.this.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        return view;
    }

}
