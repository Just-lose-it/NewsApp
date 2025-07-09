package com.java.wangyiding.ui.category;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;

import com.java.wangyiding.databinding.ActivityCategorySelectBinding;

import com.java.wangyiding.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategorySelectActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private final String[] allCategories = {"全部", "科技", "体育", "娱乐", "教育", "军事", "财经", "汽车", "健康", "文化", "社会"};

    private boolean selected[]={false,false,false,false,false,false,false,false,false,false,false};
    private List<String> selectedCategories=new ArrayList<String>();

    private ImageView backButton;
    private Button confirmButton;
    private GridLayout catrgoryTable;

    private boolean isAnimating=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_select);

        catrgoryTable=findViewById(R.id.categoryGrid);
        backButton=findViewById(R.id.BackButton);
        confirmButton=findViewById(R.id.confirmButton);
        catrgoryTable.removeAllViews();
        Intent intent=getIntent();

        List<String>temporaryList = Arrays.asList(intent.getStringArrayExtra("categories"));

        for(int i=0;i<allCategories.length;i++)
        {
            if(temporaryList.contains(allCategories[i]))selected[i]=true;
        }
        for(int i=0;i<allCategories.length;i++)
        {
            Button button=new Button(this);
            button.setTag(i);
            button.setText(allCategories[i]);

            button.setPadding(10, 10, 10, 10);
            button.setWidth(50);
            button.setHeight(30);
            GridLayout.Spec row_spec=GridLayout.spec(i/3);
            GridLayout.Spec col_spec=GridLayout.spec(i%3);
            GridLayout.LayoutParams params=new GridLayout.LayoutParams(row_spec,col_spec);
            button.setBackgroundColor(Color.parseColor(selected[(int) button.getTag()]?"#cc6666":"#cccccc"));
            params.setMargins(10,10,10,10);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isAnimating) return;
                    isAnimating = true;
                    selected[(int) button.getTag()]=!selected[(int) button.getTag()];
                    ObjectAnimator colorAnim = ObjectAnimator.ofArgb(
                            button,
                            "backgroundColor",
                            selected[(int) button.getTag()] ? Color.parseColor("#cccccc") : Color.parseColor("#cc6666"),
                            selected[(int) button.getTag()] ? Color.parseColor("#cc6666") : Color.parseColor("#cccccc")
                    );
                    button.animate()
                            .scaleX(0.9f).scaleY(0.9f).setDuration(100)
                            .withEndAction(() -> {
                                button.animate().scaleX(1f).scaleY(1f).setDuration(100).withEndAction(()->isAnimating=false).start();
                            })
                            .start();

                    colorAnim.setDuration(200); // 200ms 平滑动画
                    colorAnim.start();

                }
            });
            params.setGravity(Gravity.CENTER);
            catrgoryTable.addView(button,params);
            Log.d("c","button"+allCategories[i]);



        }

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                selectedCategories.clear();
                Intent returnIntent=new Intent();
                for(int i=0;i<selected.length;i++)
                {
                    if(selected[i])
                    {
                        selectedCategories.add(allCategories[i]);
                        Log.d("ss",allCategories[i]);
                    }
                }
                if(selectedCategories.isEmpty())
                {
                    Toast.makeText(CategorySelectActivity.this,"至少选一个！",Toast.LENGTH_SHORT).show();
                    return;
                }

                returnIntent.putExtra("categories",selectedCategories.toArray(new String[0]));
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                selectedCategories.clear();
                Intent returnIntent=new Intent();
                for(int i=0;i<selected.length;i++)
                {
                    if(selected[i])selectedCategories.add(allCategories[i]);
                }
                if(selectedCategories.isEmpty())
                {
                    Toast.makeText(CategorySelectActivity.this,"至少选一个！",Toast.LENGTH_SHORT).show();
                    return;
                }
                returnIntent.putExtra("categories",selectedCategories.toArray(new String[0]));
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
    }


}