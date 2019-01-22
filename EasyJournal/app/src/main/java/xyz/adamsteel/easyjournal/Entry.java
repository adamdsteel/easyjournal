package xyz.adamsteel.easyjournal;


//Holds the text of an entry to be displayed, as read from the database or entered in this session by the user.
//Also contains metadata such as entry number.
public class Entry {

    public String text; //The text of the entry.
    public int id; //The count of the entry, should be identical to the _id primary key field in the database.

    public Entry(int idNumber, String entryText){
        text = entryText;
        id = idNumber;
    }

}
