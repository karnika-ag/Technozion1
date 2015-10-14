package in.technozion.technozion.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import in.technozion.technozion.NavigationDrawerFragment;
import in.technozion.technozion.NavigationItem;
import in.technozion.technozion.R;

public class NavigationDrawerAdapter extends ArrayAdapter<NavigationItem> {
    public List<NavigationItem> mData;
    private int resource;
    private Context context;
    public NavigationDrawerAdapter(Context context,int resource,List<NavigationItem> data) {
        super(context,resource,data);
        this.resource=resource;
        this.context=context;
        this.mData = data;
    }
  /*  public NavigationDrawerAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.context=context;
    }
*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view= (TextView) super.getView(position, convertView, parent);
        Drawable drawable=context.getResources().getDrawable(R.mipmap.ic_launcher);
        drawable.setBounds(0,0,40,40);
        view.setCompoundDrawables(drawable,null,null,null);

        return convertView;
    }
}



