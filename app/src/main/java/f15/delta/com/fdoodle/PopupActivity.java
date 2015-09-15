package f15.delta.com.fdoodle;




import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.polites.android.GestureImageView;

public class PopupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        Bundle args = getIntent().getExtras();
        if(args.getInt("num")==1)
            ((GestureImageView)findViewById(R.id.popupimage)).setImageResource(R.drawable.day01);
        if(args.getInt("num")==2)
            ((GestureImageView)findViewById(R.id.popupimage)).setImageResource(R.drawable.day2);
        if(args.getInt("num")==3)
            ((GestureImageView)findViewById(R.id.popupimage)).setImageResource(R.drawable.day3);

    }
}
