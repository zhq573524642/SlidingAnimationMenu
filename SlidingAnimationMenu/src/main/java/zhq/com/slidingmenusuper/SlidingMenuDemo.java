package zhq.com.slidingmenusuper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Author:  张慧强
 * Version:  1.0
 * Date:    2017/11/22 0022
 * Modify:
 * Description: //TODO
 * Copyright notice:
 */
public class SlidingMenuDemo extends AppCompatActivity implements View.OnClickListener {

    private Button btnScaleMenu;
    private Button btn3DSlidingMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_menu_demo);
        btnScaleMenu = findViewById(R.id.btn_scale_menu);
        btn3DSlidingMenu = findViewById(R.id.btn_3d_slidingmenu);
        btnScaleMenu.setOnClickListener(this);
        btn3DSlidingMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_scale_menu:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_3d_slidingmenu:
                Intent intent1 = new Intent(this, Sliding3DMenu.class);
                startActivity(intent1);
        }
    }
}
