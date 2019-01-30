package xyz.adamsteel.easyjournal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.ArrayList;

import static xyz.adamsteel.easyjournal.EJLogger.ejLog;
import xyz.adamsteel.easyjournal.DeleteDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EntriesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EntriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//EntriesFragment is the fragment that displays the entries display list, and is responsible for keeping the SQL database and in-memory lists in sync.

public class EntriesFragment extends Fragment implements EasyAdapter.AdapterInteractor {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView eRecyclerView;
    private EasyAdapter eAdapter;
    private android.support.v7.widget.LinearLayoutManager eLayoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //--
    private ArrayList<Entry> contentList; //The text entries for the journal.
    private TextInputEditText inputEditText; //The text box the user types their entries in.
    private Button sendButton; //The send button.
    private SQLiteDatabase eDatabase; //The main database for journal entries.
    ESQLiteHelper dbHelper; //The database helper.
    private final int INITIAL_LOAD_LENGTH = 100; //The number of recent messages to initially load.

    private final int ENTRIES_TO_LOAD = INITIAL_LOAD_LENGTH;

    private boolean initialLoaded = false;

    public EntriesFragment() {
        // Required empty public constructor
        ejLog("EntriesFragment constructor");
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EntriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EntriesFragment newInstance(String param1, String param2) {
        EntriesFragment fragment = new EntriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



        //Set up the database
        dbHelper = new ESQLiteHelper(getContext());
        dbHelper.getWritableDatabase();

        ejLog("**EntriesFragment onCreate()**");
        //testSQL();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_entries, container, false);

        //Setting up our content:


        //String[] testContent = new String[TEST_LENGTH];// {"Entry one", "Entry two", "Entry three", "Entry four", "Entry five", "Entry six", };

        //contentList = new ArrayList<String>(100);

        /*
        for(int i = 0; i < TEST_LENGTH; i++){
            //testContent[i] = "Test message number " + Integer.toString(i + 1);
            contentList.add("Test message number " + Integer.toString(i + 1));
        }
        */

        //Load the initial entries. However, if we've already loaded anything, this will cause a bug, so we only want to do this once:
        if(initialLoaded == false) {
            contentList = dbHelper.retrieveMoreEntries(INITIAL_LOAD_LENGTH);
            initialLoaded = true;
        }

        eRecyclerView = (RecyclerView)mView.findViewById(R.id.entries_recycler_view);
        eLayoutManager = new LinearLayoutManager(getContext());
        eRecyclerView.setLayoutManager(eLayoutManager);

        eAdapter = new EasyAdapter(contentList, this);
        eRecyclerView.setAdapter(eAdapter);

        eAdapter.notifyDataSetChanged(); //So it updates with the data loaded from the database aboce.

        inputEditText = (TextInputEditText)mView.findViewById(R.id.inputBox);

        //Scroll the message view to the bottom item:
        eRecyclerView.scrollToPosition(contentList.size() - 1);

        //Scroll the message view to the bottom whenever input box gains focus.
        //Yes, somehow it really is this complicated.
        eRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, final int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom){
                if (bottom < oldBottom) {
                    eRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            eRecyclerView.smoothScrollToPosition(bottom);
                        }
                    }, 100);
                }
            }
        });


        //Setting up what to do when we need to load more data for the RecyclerView
        eRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //ejLog("scroll state changed");


                //int visibleEntryCount = eLayoutManager.getChildCount();
                //int totalEntryCount = eLayoutManager.getItemCount();

                //ejLog( "first visible item pos: " + Integer.toString(eLayoutManager.findFirstVisibleItemPosition()) );

                if(eLayoutManager.findFirstVisibleItemPosition() == 0) { //If we are scrolled to the first item...
                    ejLog("Need to load more");

                    int topEntryId = contentList.get(0).id; //The database id number of the earliest loaded entry.

                    if(topEntryId != 0){ //If we haven't already loaded the earliest entry in the database...
                        ArrayList<Entry> loadedEntries = dbHelper.retrieveMoreEntries(ENTRIES_TO_LOAD);

                        /*
                        loadedEntries.addAll(contentList);
                        contentList = loadedEntries;
                        eAdapter.updateData(contentList);
                        */

                        //We start at the end of loadedEntries and work backwards adding them to the top of contentList
                        //We need to do it this way to get the correct order.
                        for(int i = loadedEntries.size() - 1; i >= 0; i--){
                            contentList.add(0, loadedEntries.get(i));
                        }
                        eAdapter.notifyDataSetChanged(); //TODO: Keep scroll position when loading extra data.
                    }
                }
            }
        });

        //Setting up the send button:
        sendButton = (Button) mView.findViewById(R.id.send_button);
        setButton(); //Grey out the button.
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejLog( "Send button tapped - from EntriesFragment");

                //Add the entry to the database and the view:


                String entryText = inputEditText.getText().toString(); //Gets the text from the input text box and converts it to a String.

                //Update the on-disk database:
                int entryCount = dbHelper.addEntry(entryText);

                //Update the in-memory list:
                contentList.add( new Entry(entryCount, entryText));



                //inputEditText.clearComposingText();
                inputEditText.setText("");

                //Notify the adapter that the data changed so it updates on the screen:
                eAdapter.notifyDataSetChanged();

                //Scroll the message view to the bottom item:
                eRecyclerView.scrollToPosition(contentList.size() - 1);

            }
        });




        //Listener for the text box being changed:
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setButton();
            }
        });

        return mView;


    }

    public void testSQL(){
        dbHelper.getWritableDatabase();
        dbHelper.getReadableDatabase();
        //dbHelper.dumpEntries();
    };

    //Greys out the send button depending on if the text box is empty or not:
    public void setButton()
    {
        sendButton.setEnabled( !inputEditText.getText().toString().equals(""));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void longPress(int id);
    }

    @Override
    public void onViewLongPressed(int dbID) {
        //Accepts the database ID number as an argument, which we'll use on the database to delete the entry.
        ejLog("EF - A view has been long tapped - db id number" + dbID);

        //Setting the entry to be deleted:
        Bundle args = new Bundle();
        args.putInt("toDelete", dbID); //Saving the ID of the entry the user wants to delete.

        //Create the conformation dialog:
        DeleteDialogFragment deleteDialogFrag = new DeleteDialogFragment();
        deleteDialogFrag.setArguments(args);
        deleteDialogFrag.show(getFragmentManager(), "deleteDialogFrag");

    }


    public void deleteConfirmed(int dbID) {
        //Delete the entry from the database:
        dbHelper.deleteEntry(dbID);

        //Delete the entry from the UI section of the database:
        for (int i = 0; i < contentList.size(); i++) {    //TODO: Consider reloading the currently scrolled-to section from the database. This seems likely to be more bug-proof.
            if (contentList.get(i).id == dbID) {      //Iterates thru the list to find the one with the right ID, since the ID won't (always) match the index.
                contentList.remove(i);              //There is probably a way to make this more efficient.
                break;                              //However, other entries may have already been deleted, so you can't just jump (index.id - current.id) places ahead.
            }
        }
        //Update the UI to show the deletion
        eAdapter.notifyDataSetChanged();
    }

}
