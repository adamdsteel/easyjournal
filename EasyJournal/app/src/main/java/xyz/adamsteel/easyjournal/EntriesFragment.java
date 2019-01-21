package xyz.adamsteel.easyjournal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EntriesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EntriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntriesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView eRecyclerView;
    private RecyclerView.Adapter eAdapter;
    private RecyclerView.LayoutManager eLayoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //--
    private ArrayList<String> contentList; //The text entries for the journal.
    private TextInputEditText inputEditText; //The text box the user types their entries in.
    private Button sendButton; //The send button.

    public EntriesFragment() {
        // Required empty public constructor
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


        //

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_entries, container, false);

        //Setting up our content:

        //Some test content:
        final int TEST_LENGTH = 100;
        //String[] testContent = new String[TEST_LENGTH];// {"Entry one", "Entry two", "Entry three", "Entry four", "Entry five", "Entry six", };

        contentList = new ArrayList<String>(TEST_LENGTH);

        for(int i = 0; i < TEST_LENGTH; i++){
            //testContent[i] = "Test message number " + Integer.toString(i + 1);
            contentList.add("Test message number " + Integer.toString(i + 1));
        }

        eRecyclerView = (RecyclerView)mView.findViewById(R.id.entries_recycler_view);
        eLayoutManager = new LinearLayoutManager(getContext());
        eRecyclerView.setLayoutManager(eLayoutManager);

        eAdapter = new EasyAdapter(contentList);
        eRecyclerView.setAdapter(eAdapter);

        inputEditText = (TextInputEditText)mView.findViewById(R.id.inputBox);

        //Scroll the message view to the bottom item:
        eRecyclerView.scrollToPosition(contentList.size() - 1);

        //Setting up the send button:
        sendButton = (Button) mView.findViewById(R.id.send_button);
        setButton(); //Grey out the button.
        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("EJLOGS", "Send button tapped - from EntriesFragment");

                //Add the entry to the database and the view:


                String entryText = inputEditText.getText().toString(); //Gets the text from the input text box and converts it to a String.



                contentList.add(entryText);
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
    }
}
