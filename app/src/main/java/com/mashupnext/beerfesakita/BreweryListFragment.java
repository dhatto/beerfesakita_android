package com.mashupnext.beerfesakita;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.SimpleAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by daigoh on 2017/08/05.
 */

public class BreweryListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String LIST_VIEW_SELECTION = "listViewSelection";
    private int _selection = 0;
    public static final int ACTIVITY_REQUEST_CODE_POST_PHOTO = 0;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ListView lv = getListView();
        outState.putInt(LIST_VIEW_SELECTION, lv.getFirstVisiblePosition());

        _selection = lv.getFirstVisiblePosition();
    }

    public static BreweryListFragment newInstance() {
        return new BreweryListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.brewer_list, container, false);

        View rootView = inflater.inflate(R.layout.brewer_list, container, false);
        if(savedInstanceState != null) {
            _selection = savedInstanceState.getInt(LIST_VIEW_SELECTION);
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        setSympleAdapter();
        ListView lv = getListView();
        // 初期フォーカスを移動
        lv.setSelection(_selection);
    }

    private void setSympleAdapter() {

        String titles[] = this.getResources().getStringArray(R.array.brewery_list);
        String tags[] = this.getResources().getStringArray(R.array.brewery_place_list);

        ArrayList<HashMap<String, String>> dataSource = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < titles.length; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("title", titles[i]);
            item.put("tag", tags[i]);
            dataSource.add(item);
        }

        ListView list = getListView();

        list.setAdapter(new SimpleAdapter(getActivity(), dataSource,
                R.layout.list_item_brewery,
                new String[] { "title", "tag" },
                new int[] { R.id.title, R.id.tag }) {

            public View getView(final int pos, View cv,
                                final ViewGroup parent) {

                View view = super.getView(pos, cv, parent);
                Button btn = (Button) view.findViewById(R.id.drinkButton);

                btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListView) parent).performItemClick(v, pos,
                                (long) 0);
                    }
                });
                return view;
            }
        });

        list.setOnItemClickListener(this);
    }

    // サイト名or飲んだボタンをクリック
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = (ListView)parent;

        HashMap<String,String> item = (HashMap<String,String>)listView.getItemAtPosition(position);
        String title = item.get("title");
        String className = view.getClass().getName();

        // 飲んだボタンをクリック
        // ボタンクリックの場合 className = android.support.v7.widget.AppCompatButton
        // セルクリックの場合   className = android.widget.LinearLayout
        //if(view.getClass().getName().equals("android.widget.Button")) {
        if(className.endsWith("Button")) {
            startPostActivity(title);
        // サイト名をクリック
        } else {
            String[] siteList = this.getResources().getStringArray(R.array.brewery_site_list);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(siteList[position]));
            startActivity(intent);
        }
    }

    private void startPostActivity(String title) {
        Intent i = new Intent(getActivity(), PostActivity.class);
        i.putExtra("title", title);
        startActivityForResult(i, ACTIVITY_REQUEST_CODE_POST_PHOTO);
    }

    private ListView getListView() {
        FragmentActivity act = getActivity();
        return (ListView) act.findViewById(R.id.breweryListView);
    }
}

