package xyz.adamsteel.easyjournal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EasyAdapter extends RecyclerView.Adapter<EasyAdapter.EasyViewHolder> {

    private ArrayList<Entry> eDataSet; //The entries

    public static class EasyViewHolder extends RecyclerView.ViewHolder{

        //public TextView eTextView;

        public View eView;
        public EasyViewHolder(View v){
            super(v);
            eView = v;
        }
    }

    public EasyAdapter(ArrayList<Entry> dataSet){

        eDataSet = dataSet;
    }

    public void updateData(ArrayList<Entry> data){
        eDataSet = data;
    }

    public void doSomething(){

    }

    @Override
    public EasyAdapter.EasyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        //Create a new view

        /*
        LinearLayout linLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_text_layout, parent, false);
        TextView eTView = (TextView) linLayout.findViewById(R.id.itemTextView);

        EasyViewHolder evh = new EasyViewHolder(eTView);
        return evh;
        */

        View tv =  LayoutInflater.from(parent.getContext()).inflate(R.layout.list_text_layout, parent, false);

        EasyViewHolder vh = new EasyViewHolder(tv);
        return vh;
    }

    @Override
    public void onBindViewHolder(EasyViewHolder holder, int position){

        TextView txView = holder.eView.findViewById(R.id.item_text_view);
        txView.setText(eDataSet.get(position).text);

        if(BuildConfig.DEBUG) {
            //txView.append(" [id: " + eDataSet.get(position).id + "]"); //Uncomment to show the id number of the entry at the end of it.
        }
    }

    @Override
    public int getItemCount(){
        return eDataSet.size();
    }


}
