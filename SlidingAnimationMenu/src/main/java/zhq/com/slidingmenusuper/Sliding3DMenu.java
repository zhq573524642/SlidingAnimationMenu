package zhq.com.slidingmenusuper;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Author:  张慧强
 * Version:  1.0
 * Date:    2017/11/22 0022
 * Modify:
 * Description: //TODO
 * Copyright notice:
 */
public class Sliding3DMenu extends AppCompatActivity {
    private ListView mainListView;
    private ListView menuListView;
    private ImageView ivHead;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_3d_menu);
        mainListView = findViewById(R.id.main_listview);
        menuListView = findViewById(R.id.menu_listview);
        ivHead = findViewById(R.id.iv_head);

        //1.填充数据
        mainListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Constant.NAMES));
        menuListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Constant.sCheeseStrings){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextColor(Color.BLACK);
                return view;
            }
        });
    }
}
