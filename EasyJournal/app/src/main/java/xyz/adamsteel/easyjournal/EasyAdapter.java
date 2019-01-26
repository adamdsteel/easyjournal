package xyz.adamsteel.easyjournal;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static xyz.adamsteel.easyjournal.EJLogger.ejLog;
import xyz.adamsteel.easyjournal.EntriesFragment;

public class EasyAdapter extends RecyclerView.Adapter<EasyAdapter.EasyViewHolder> {

    public ArrayList<Entry> eDataSet; //The entries
    private EntriesFragment owningFragment;

    public class EasyViewHolder extends RecyclerView.ViewHolder{

        //public TextView eTextView;

        public View eView;
        public int idNumber; //TODO: Update this to use a getter and setter so it can be read but not written externally.

        public EasyViewHolder(View v){
            super(v);
            eView = v;

            //Setting an onLongClickListener so we can allow deletion of an entry:
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View clickedView) {
                    ejLog("A view has been long tapped: db id number" + idNumber);

                    owningFragment.onViewLongPressed(idNumber);

                    //Another way to call communicate with the fragment would be to get the fragment this way:
                    /*
                    MainActivity act = (MainActivity)clickedView.getContext()
                    Fragment ef = act.eFragment;

                    But for now we'll go with the interface method.
                    */

                    return false;
                }
            });
        }
    }

    //dataSet is the data such as the entry text, etc.
    //We need the fragment so we can do things like call a method when an entry is long pressed.
    public EasyAdapter(ArrayList<Entry> dataSet, EntriesFragment fragment){

        eDataSet = dataSet;
        owningFragment = fragment;
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
        holder.idNumber = eDataSet.get(position).id; //TODO: Check this is valid. Only works if there's one view per holder.
        txView.setText(eDataSet.get(position).text);

        //Setting a listener for long presses


        if(BuildConfig.DEBUG) {
            //txView.append(" [id: " + eDataSet.get(position).id + "]"); //Uncomment to show the id number of the entry at the end of it.
        }
    }

    @Override
    public int getItemCount(){
        return eDataSet.size();
    }


    public interface AdapterInteractor{
        void onViewLongPressed(int adapterNumber);
    }

}
