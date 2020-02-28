package com.example.speechclassifier;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ListEntity extends ConstraintLayout {

    private ImageView image;
    private TextView entityName;

    public ListEntity(Context context) {
        super(context);
        init(context);
    }

    public ListEntity(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ListEntity(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.list_entity, this, true);

        this.image = findViewById(R.id.image_1);
        this.entityName = findViewById(R.id.text_1);
    }

    public void setImage(Bitmap image){
        this.image.setImageBitmap(image);
    }

    public void setText(String text){
        this.entityName.setText(text);
    }
}
